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
    public ResponseEntity<AssigneeReturnDTO> getAssignee(@PathVariable("id") long id) {
        Optional<Assignee> dbAssignee = assigneeRep.findById(id);
        if (dbAssignee.isPresent()) {
            return new ResponseEntity<>(new AssigneeReturnDTO(dbAssignee.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/assignee/")
    public ResponseEntity<List<AssigneeReturnDTO>> getAllAssignees() {
        List<Assignee> assigneesList = (ArrayList<Assignee>) assigneeRep.findAll();
        List<AssigneeReturnDTO> assigneeReturnDTOList = new ArrayList<>();
        for (Assignee assignee : assigneesList) {
            assigneeReturnDTOList.add(new AssigneeReturnDTO(assignee));
        }
        return new ResponseEntity<>(assigneeReturnDTOList, HttpStatus.OK);
    }

    @PostMapping("assignee/")
    public ResponseEntity<AssigneeReturnDTO> createAssignee(@Valid @RequestBody Assignee requestBody) {
        try {
            return new ResponseEntity<>(assigneeService.createAssignee(requestBody), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            log.error("Assignee not found: ", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected Error:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
