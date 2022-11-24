package co.edu.iudigital.sendelapa.repository;

import co.edu.iudigital.sendelapa.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransportRepository extends JpaRepository<Transport, Long> {

    /// Query editable
    /// @Query("UPDATE Transport t SET t.enable = ?1 WHERE t.id = ?2")
    /// public Boolean setEnable(Boolean enable, Long id);

}
