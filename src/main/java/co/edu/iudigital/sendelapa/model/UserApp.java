package co.edu.iudigital.sendelapa.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@FieldDefaults( level = AccessLevel.PRIVATE)
public class UserApp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    Long id;

    int document;

    @Column(name = "user_name", unique = true, nullable = false, length = 120)
    String username;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "last_name", length = 120)
    String lastName;

    @Column(nullable = false, unique = true)
    String email;

    @Column
    String password;

    @Column(nullable = false)
    String address;

    @Column(nullable = false)
    String city;

    @Column(name = "birth_day")
    LocalDate birthDay;

    @Column
    Boolean enable;

    @Column
    String image;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "roles_users",
            joinColumns = {@JoinColumn( name = "users_id" )},
            inverseJoinColumns = { @JoinColumn( name = "roles_id") })
    List<Role> roles;
}
