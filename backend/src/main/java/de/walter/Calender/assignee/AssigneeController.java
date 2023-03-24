package de.walter.Calender.assignee;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@Slf4j
public class AssigneeController {

    @Autowired
    AssigneeRepository assigneeRep;
    @Autowired
    AssigneeService assigneeService;

    @GetMapping("/assignee/{id}")
    public ResponseEntity<Assignee> getAssignee(@PathVariable("id") long id){
        Optional<Assignee> dbAssignee =assigneeRep.findById(id);
        if (dbAssignee.isEmpty()){
            return new ResponseEntity<>(dbAssignee.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/assignee")
    public ResponseEntity<List<Assignee>> getAllTasks(){
        List<Assignee> assigneesList = (ArrayList<Assignee>) assigneeRep.findAll();
        return new ResponseEntity<>(assigneesList, HttpStatus.OK);
    }

    @PostMapping("task/create")
    public ResponseEntity<Assignee> createAssignee(@Valid @RequestBody Assignee requestBody){
        try {
            return new ResponseEntity<>(assigneeService.createAssignee(requestBody), HttpStatus.CREATED);
        }catch (NoSuchElementException e){
            log.error("Assignee not found: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            log.error("Unexpected Error:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
