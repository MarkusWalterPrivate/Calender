package de.walter.Calender.assignee;


import de.walter.Calender.task.Task;
import de.walter.Calender.task.TaskReturn4AssigneeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeReturnDTO {

    private long id;

    private String name;

    private String email;

    private List<TaskReturn4AssigneeDTO> tasks;

    public AssigneeReturnDTO(Assignee assignee) {
        this.id = assignee.getId();
        this.name = assignee.getName();
        this.email = assignee.getEmail();
        List<TaskReturn4AssigneeDTO> tasks = new ArrayList<>();
        for (Task task : assignee.getTasks()) {
            tasks.add(new TaskReturn4AssigneeDTO(task));
        }
        this.tasks = tasks;
    }
}
