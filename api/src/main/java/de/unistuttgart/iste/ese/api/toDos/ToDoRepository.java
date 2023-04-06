package de.unistuttgart.iste.ese.api.toDos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    ToDo findById(long id);

}
