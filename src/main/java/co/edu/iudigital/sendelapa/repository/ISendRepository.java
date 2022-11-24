package co.edu.iudigital.sendelapa.repository;

import co.edu.iudigital.sendelapa.model.Send;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISendRepository extends CrudRepository<Send, Long> {
}
