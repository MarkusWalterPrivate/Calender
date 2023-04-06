package de.unistuttgart.iste.ese.api.assignees;

import java.util.ArrayList;
import java.util.List;


import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import de.unistuttgart.iste.ese.api.toDos.ToDo;
import de.unistuttgart.iste.ese.api.toDos.ToDoRepository;

@RestController
public class AssigneeController {
		@Autowired
	    private AssigneeRepository assigneeRepository;
		@Autowired
		private ToDoRepository toDoRepository;

		// executed after start-up and dependency injection
	    @PostConstruct
	    public void init() {
	    	
	        // check if DB is empty
	        long numberOfAssignees = assigneeRepository.count();
	        if (numberOfAssignees == 0) {
	            // adding sample data for demonstration purposes
	            Assignee octoassignee = new Assignee("Octoassignee", "bla", "test@uni-stuttgart.de");
	            assigneeRepository.save(octoassignee);

	            Assignee grumpyAssignee = new Assignee("Grumpy Assignee", "blub",
	                    "test2@uni-stuttgart.de");
	            assigneeRepository.save(grumpyAssignee);
	            
	        }
	    }

	    // get all assignee
	    @GetMapping("/assignees")
	    public List<Assignee> getAssignees() {
	        List<Assignee> allAssignees = (List<Assignee>) assigneeRepository.findAll();
	        return allAssignees;
	    }

	    // get a single assignee
	    @GetMapping("/assignees/{id}")
	    public Assignee getAssignee(@PathVariable("id") long id) {

	        Assignee searchedAssignee = assigneeRepository.findById(id);
	        if (searchedAssignee != null) {
	            return searchedAssignee;
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("Assignee with ID %s not found!", id));
	    }

	    // create a assignee
	    @PostMapping("/assignees")
	    @ResponseStatus(HttpStatus.CREATED)
	    public Assignee createAssignee(@Valid @RequestBody Assignee requestBody) {
	        Assignee assignee = new Assignee(requestBody.getPrename(), requestBody.getName(),
	                requestBody.getEmail());
	        Assignee savedAssignee = assigneeRepository.save(assignee);
	        return savedAssignee;
	    }

	    // update a assignee
	    @PutMapping("/assignees/{id}")
	    public Assignee updateAssignee(@PathVariable("id") long id, @Valid @RequestBody Assignee requestBody) {
	        requestBody.setId(id);
	        Assignee assigneeToUpdate = assigneeRepository.findById(id);
	        if (assigneeToUpdate != null) {
	            Assignee savedAssignee = assigneeRepository.save(requestBody);
	            return savedAssignee;
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("Assignee with ID %s not found!", id));
	    }

	    // delete a assignee
	    @DeleteMapping("/assignees/{id}")
	    public void deleteAssignee(@PathVariable("id") long id) {
	    	Assignee assigneeToDelete = assigneeRepository.findById(id);
	    	ArrayList<ToDo> toDos = (ArrayList<ToDo>) toDoRepository.findAll();
	    	for(ToDo todo : toDos) {
	    		if(todo.getAssigneeList().contains(assigneeToDelete)) {
	    			todo.getAssigneeList().remove(assigneeToDelete);
	    			toDoRepository.save(todo);
	    		}
	    	}

	        
	        if (assigneeToDelete != null) {
	            assigneeRepository.deleteById(id);
	            return;
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	                String.format("Assignee with ID %s not found!", id));
	    }
}
