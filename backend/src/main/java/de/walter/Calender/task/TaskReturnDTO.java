package de.walter.Calender.task;

import de.walter.Calender.assignee.Assignee;
import de.walter.Calender.assignee.AssigneeReturn4TaskDTO;
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
public class TaskReturnDTO {
    private long id;
    private String name;
    private String description;
    private String image;
    private long start;
    private long end;
    private long creationDate;
    private boolean finished;
    private long finishDate;
    private List<AssigneeReturn4TaskDTO> assignees;

    public TaskReturnDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.image = task.getImage();
        this.start = task.getStart();
        this.end = task.getEnd();
        this.creationDate = task.getCreationDate();
        this.finishDate = task.getFinishDate();
        this.finished = task.isFinished();
        List<AssigneeReturn4TaskDTO> assignees = new ArrayList<>();
        for (Assignee assignee : task.getAssignees()) {
            assignees.add(new AssigneeReturn4TaskDTO(assignee));
        }
        this.assignees = assignees;
    }
}
