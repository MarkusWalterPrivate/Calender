package de.walter.Calender.task;

import de.walter.Calender.assignee.Assignee;
import de.walter.Calender.assignee.AssigneeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRep;
    @Autowired
    AssigneeRepository assigneeRep;

    protected Task createTask (TaskCreationDTO newTask) throws NoSuchElementException{
        Task task = new Task(newTask);
        List<Assignee> assigneeList = new ArrayList<>();
        for (Long id: newTask.getAssigneeIds()) {
            Optional<Assignee> dbAssignee =assigneeRep.findById(id);
            if (dbAssignee.isPresent()){
                assigneeList.add(dbAssignee.get());
            }else{
                throw new NoSuchElementException();
            }
        }
        task.setAssignees(assigneeList);
        Task savedTask = taskRep.save(task);
        return savedTask;
    }
}
