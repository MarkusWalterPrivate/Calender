package de.unistuttgart.iste.ese.api.assignees;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssigneeRepository extends CrudRepository<Assignee, Long> {
    Assignee findById(long id);
}
