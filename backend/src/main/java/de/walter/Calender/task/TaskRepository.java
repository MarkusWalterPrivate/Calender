package de.walter.Calender.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    Optional<Task> findById(long id);

}
