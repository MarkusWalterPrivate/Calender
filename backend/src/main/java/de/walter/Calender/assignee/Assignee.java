package de.walter.Calender.assignee;

import de.walter.Calender.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="assignees")
public class Assignee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @ManyToMany(mappedBy = "assignees")
    private List<Task> tasks;
}
