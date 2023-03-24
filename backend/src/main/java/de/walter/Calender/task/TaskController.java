package de.walter.Calender.task;

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
public class TaskController {

    @Autowired
    TaskRepository taskRep;
    @Autowired
    TaskService taskService;

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskReturnDTO> getTask(@PathVariable("id") long id) {
        Optional<Task> dbTask = taskRep.findById(id);
        if (dbTask.isPresent()) {
            return new ResponseEntity<>(new TaskReturnDTO(dbTask.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/task/")
    public ResponseEntity<List<TaskReturnDTO>> getAllTasks() {
        List<Task> taskList = (ArrayList<Task>) taskRep.findAll();
        List<TaskReturnDTO> taskReturnDTOList = new ArrayList<>();
        for (Task task : taskList) {
            taskReturnDTOList.add(new TaskReturnDTO(task));
        }
        return new ResponseEntity<>(taskReturnDTOList, HttpStatus.OK);
    }

    @PostMapping("task/")
    public ResponseEntity<TaskReturnDTO> createTask(@Valid @RequestBody TaskCreationDTO newTask) {
        try {
            return new ResponseEntity<>(taskService.createTask(newTask), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            log.error("Assignee not found: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected Error:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
