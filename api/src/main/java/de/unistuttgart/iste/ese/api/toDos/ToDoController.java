package de.unistuttgart.iste.ese.api.toDos;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import de.unistuttgart.iste.ese.api.assignees.Assignee;
import de.unistuttgart.iste.ese.api.assignees.AssigneeRepository;



@RestController
public class ToDoController {
	 	@Autowired
	    private ToDoRepository toDoRepository;
	 	@Autowired
	 	private AssigneeRepository assigneeRepository;
    	@Autowired
    	private ToDoService toDoService;
	    // executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	

	    }
	    @GetMapping("/csv-downloads/todos")
	    public ResponseEntity<Resource> getFile() {
	      String filename = "tutorials.csv";
	      InputStreamResource file = new InputStreamResource(toDoService.loadCSV());
	      return ResponseEntity.ok()
	          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	          .contentType(MediaType.parseMediaType("application/csv"))
	          .body(file);
	    }
	    
	    
	    // get all toDo
	    @GetMapping("/todos")
	    public List<ToDo> getToDos() {
	        List<ToDo> allToDos = (List<ToDo>) toDoRepository.findAll();
	        return allToDos;
	    }

	    // get a single toDo
	    @GetMapping("/todos/{id}")
	    public ToDo getToDo(@PathVariable("id") long id) {

	        ToDo searchedToDo = toDoRepository.findById(id);
	        if (searchedToDo != null) {
	            return searchedToDo;
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("ToDo with ID %s not found!", id));
	    }

	    // create a toDo 
	    @PostMapping("/todos")
	    @ResponseStatus(HttpStatus.CREATED)
	    public ToDo createToDo(@Valid @RequestBody ToDoDTO requestBody) {

	    	
	    	//Eliminate doublicated and compare size
	    	Set<Long> assigneeIds = new HashSet(requestBody.getAssigneeIdList());
	    	if (assigneeIds.size()<requestBody.getAssigneeIdList().size()) {
	    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
    	                String.format("Douplicate Assignees are not allowed"));
	    	}
	    	Set <Assignee> assignees =new HashSet<Assignee>();
	    	for(Long assigneeID : assigneeIds) {
	    		if (assigneeRepository.findById(assigneeID.intValue())!=null) {
	    			//Add assignee to toDo
	    			assignees.add(assigneeRepository.findById(assigneeID.intValue()));
	    			//send Mail with to each assignee
	    			String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(requestBody.getDueDate()));
	    			toDoService.sendMail(assigneeRepository.findById(assigneeID.intValue()).getEmail(), requestBody.getTitle()+": \r\n"+requestBody.getDescription()+ "\r\n Due Date:" + date);
	    		}else {
	    			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	    	                String.format("Assignee with ID %s not found!", assigneeID));
	    		}
	    			
	    	}
	    	
	        ToDo toDo = new ToDo(requestBody.getTitle(), requestBody.getDescription(),
	                requestBody.isFinished(), assignees, requestBody.getDueDate());
	        ToDo savedToDo = toDoRepository.save(toDo);
	        return savedToDo;
	    }

	    // update a toDo
	    @PutMapping("/todos/{id}")
	    public ToDo updateToDo(@PathVariable("id") long id, @Valid @RequestBody ToDoDTO requestBody) {
	 
	        ToDo toDoToUpdate = toDoRepository.findById(id);
	        if (toDoToUpdate != null) {
	        	/*
		    	 * Maybe refactor this into ToDoService
		    	 */
	        	toDoToUpdate.setDescription(requestBody.getDescription());
	        	toDoToUpdate.setDueDate(new Date(requestBody.getDueDate()));
	        	toDoToUpdate.setTitle(requestBody.getTitle());
	        	//set finished date if it was just finished
	        	if(requestBody.isFinished()&&!toDoToUpdate.isFinished()) { 
	        		toDoToUpdate.setFinished(true);
	        		Date now= new Date();
	        		toDoToUpdate.setFinishedDate(now);
	        	}
	        	//"un"finish todo 
	        	if(!requestBody.isFinished()&&toDoToUpdate.isFinished()) { 
	        		toDoToUpdate.setFinished(false);
	        		toDoToUpdate.setFinishedDate(new Date(0));
	        	}
	        	//Eliminate  duplicates of assignees and compare size
	        	Set<Long> assigneeIds =new HashSet( requestBody.getAssigneeIdList());
	        	if (assigneeIds.size()<requestBody.getAssigneeIdList().size()) {
		    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	    	                String.format("Douplicate Assignees are not allowed"));
		    	}
		    	Set <Assignee> assignees =new HashSet<Assignee>();
		    	for(Long assigneeID : assigneeIds) {
		    		if (assigneeRepository.findById(assigneeID.intValue())!=null) {
		    			assignees.add(assigneeRepository.findById(assigneeID.intValue()));
		    		}else {
		    			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
		    	                String.format("Assignee with ID %s not found!", assigneeID));
		    		}
		    			
		    	}
		    	//cross-checking all old ones with the new ones
		    	for(Assignee oldAssignee:toDoToUpdate.getAssigneeList()) {
		    		if(assigneeIds.contains(oldAssignee.getId())) {
		    			//if assignee was already part of previous team, remove them from the list
		    			assigneeIds.remove(oldAssignee.getId());
		    		}
		    	}
		    	toDoToUpdate.setAssigneeList(assignees);
		    	//send mail to only the new assignees
		    	for(Long assigneeID : assigneeIds) {
		    		if (assigneeRepository.findById(assigneeID.intValue())!=null) {
		    			//send Mail with to each new assignee
		    			String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(requestBody.getDueDate());
		    			toDoService.sendMail(assigneeRepository.findById(assigneeID.intValue()).getEmail(), requestBody.getTitle()+": \r\n"+requestBody.getDescription()+ "\r\n Due Date:" + date);
		    		}else {
		    			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
		    	                String.format("Assignee with ID %s not found!", assigneeID));
		    		}
		    			
		    	}
	            ToDo savedToDo = toDoRepository.save(toDoToUpdate);
	            return savedToDo;
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("ToDo with ID %s not found!", id));
	    }

	    // delete a toDo
	    @DeleteMapping("/todos/{id}")
	    public void deleteToDo(@PathVariable("id") long id) {

	        ToDo toDoToDelete = toDoRepository.findById(id);
	        if (toDoToDelete != null) {
	            toDoRepository.deleteById(id);
	            return;
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("ToDo with ID %s not found!", id));
	    }
}
