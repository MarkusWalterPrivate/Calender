package de.walter.Calender.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskReturn4AssigneeDTO {
    private long id;
    private String name;
    private String description;
    private String image;
    private long start;
    private long end;
    private long creationDate;
    private boolean finished;
    private long finishDate;

    public TaskReturn4AssigneeDTO (Task task){
        this.id=task.getId();
        this.name=task.getName();
        this.description=task.getDescription();
        this.image=task.getImage();
        this.start= task.getStart();
        this.end=task.getEnd();
        this.creationDate=task.getCreationDate();
        this.finishDate=task.getFinishDate();
        this.finished=task.isFinished();

    }
}
