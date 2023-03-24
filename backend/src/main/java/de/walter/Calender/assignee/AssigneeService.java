package de.walter.Calender.assignee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssigneeService {
    @Autowired
    AssigneeRepository assigneeRepository;

    protected Assignee createAssignee(Assignee newAssignee){
        return assigneeRepository.save(newAssignee);
    }
}
