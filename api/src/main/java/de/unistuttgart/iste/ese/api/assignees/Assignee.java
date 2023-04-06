package de.unistuttgart.iste.ese.api.assignees;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "assignees")
@Getter
@Setter
@NoArgsConstructor
public class Assignee {
		@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
		@Size(min = 1)
	    private String prename;
		@Size(min = 1)
	    private String name;
		@Email()
		//@Pattern(regexp = "^(.+)uni-stuttgart.de$") Requirement by university later excluded.
	    private String email;
        
	    
	    public Assignee(String prename, String name, String email) {
	        this.name = name;
	        this.prename = prename;
	        this.email = email;
	    }

}
