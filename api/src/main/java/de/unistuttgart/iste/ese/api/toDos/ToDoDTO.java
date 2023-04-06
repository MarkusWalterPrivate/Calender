package de.unistuttgart.iste.ese.api.toDos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import javax.validation.constraints.Size;
@Getter
@Setter
@NoArgsConstructor
public class ToDoDTO {

			@Size(min=1)
		    private String title;
			@Size(min=1)
		    private String description;
			private boolean finished;
		    private ArrayList<Long> assigneeIdList;
			private long dueDate;
			private long finishedDate;
            
		    
		    public ToDoDTO(String title, String description, boolean finished, ArrayList<Long> assigneeIdList, long dueDate) {
		        this.description = description;
		        this.title = title;
		        this.finished = finished;
		        this.assigneeIdList=assigneeIdList;
		        this.dueDate=dueDate;
		    }
	}

