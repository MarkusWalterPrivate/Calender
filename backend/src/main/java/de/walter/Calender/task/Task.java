package de.walter.Calender.task;

import de.walter.Calender.assignee.Assignee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String image;
    private long start;
    private long end;
    private long creationDate;
    private boolean finished;
    private long finishDate;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name= "assignees_of_tasks",
    joinColumns = @JoinColumn(name="task_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "assignee_id", referencedColumnName = "id"))
    private List<Assignee> assignees;

    public Task(TaskCreationDTO task){
        this.name= task.getName();
        this.description= task.getDescription();
        this.image = task.getImage();
        this.start= task.getStart();
        this.end= task.getEnd();
        this.creationDate = new Date().getTime();
        this.finishDate= -1;
        this.finished=false;
    }


}
