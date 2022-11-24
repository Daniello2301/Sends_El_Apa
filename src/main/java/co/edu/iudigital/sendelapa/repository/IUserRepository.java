package co.edu.iudigital.sendelapa.repository;

import co.edu.iudigital.sendelapa.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserApp, Long> {

    public UserApp findByUsername(String username);
}
