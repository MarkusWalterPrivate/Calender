package de.walter.Calender.assignee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssigneeService {
    @Autowired
    AssigneeRepository assigneeRepository;

    protected AssigneeReturnDTO createAssignee(Assignee newAssignee) {
        Assignee assignee = assigneeRepository.save(newAssignee);
        return new AssigneeReturnDTO(assignee);
    }
}
