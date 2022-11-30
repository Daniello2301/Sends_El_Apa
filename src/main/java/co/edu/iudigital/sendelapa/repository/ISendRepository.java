package co.edu.iudigital.sendelapa.repository;

import co.edu.iudigital.sendelapa.dto.SendDto;
import co.edu.iudigital.sendelapa.model.Send;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISendRepository extends JpaRepository<Send, Long> {

    @Query("SELECT s FROM Send s WHERE s.user.id = ?1")
    public List<Send> findByUserAppId(Long id);
}
