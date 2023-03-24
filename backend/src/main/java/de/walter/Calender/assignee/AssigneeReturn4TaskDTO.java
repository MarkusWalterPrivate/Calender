package de.walter.Calender.assignee;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeReturn4TaskDTO {
    private long id;
    private String name;
    private String email;

    public AssigneeReturn4TaskDTO(Assignee assignee) {

        this.id = assignee.getId();
        this.name = assignee.getName();
        this.email = assignee.getEmail();
    }
}


