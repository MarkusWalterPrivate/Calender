package de.unistuttgart.iste.ese.api.toDos;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import de.unistuttgart.iste.ese.api.assignees.Assignee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
public class ToDo {
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
		@Size(min=1)
	    private String title;
	    private String description;
		private boolean finished;
	    @ManyToMany(targetEntity=de.unistuttgart.iste.ese.api.assignees.Assignee.class)
	    private Set<Assignee> assigneeList;
		private Date createdDate =new Date();
		private Date dueDate=new Date(0);
		private Date finishedDate = new Date (0);
	    
	    public ToDo(String title, String description, boolean finished, Set<Assignee> assigneeList, long dueDate) {
	        this.description = description;
	        this.title = title;
	        this.finished = finished;
	        this.assigneeList=assigneeList;
	        this.dueDate.setTime(dueDate);
	    }

		@Override
		public String toString() {
			String answer=id+","+title+","+description+","+finished+",";
			int i=1;
			for(Assignee assignee:assigneeList) {
				answer.concat(assignee.getPrename()+" "+ assignee.getName());
				if(i<assigneeList.size()) {
					answer.concat("+");
				}
				i++;
			}
			String stringCreatedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(createdDate);
			answer.concat(","+stringCreatedDate+",");
			if(dueDate!= new Date(0)) {
				String stringDueDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dueDate);
				answer.concat(stringDueDate);
			}
			answer.concat(",");
			if(finished) {
				String stringFinishedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(finishedDate);
				answer.concat(stringFinishedDate);
			}
			return answer;
		}
}
