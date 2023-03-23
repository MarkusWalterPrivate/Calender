package de.walter.Calender.task;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@Slf4j
public class TaskController {

    @Autowired
    TaskRepository taskRep;
    @Autowired
    TaskService taskService;

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") long id){
        Optional<Task> dbTask =taskRep.findById(id);
        if (dbTask.isEmpty()){
            return new ResponseEntity<>(dbTask.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("task/create")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskCreationDTO newTask){
        try {
            return new ResponseEntity<>(taskService.createTask(newTask), HttpStatus.CREATED);
        }catch (NoSuchElementException e){
            log.error("Assignee not found: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
