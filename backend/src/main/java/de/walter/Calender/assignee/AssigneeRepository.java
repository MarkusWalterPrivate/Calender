package de.walter.Calender.assignee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssigneeRepository extends CrudRepository<Assignee, Long> {
    Optional<Assignee> findById(long id);
}
