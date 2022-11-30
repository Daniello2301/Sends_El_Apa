package co.edu.iudigital.sendelapa.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "transports")
@Data
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Transport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transport", nullable = false)
    Long id;

    @Column( name = "name", nullable = false, unique = true)
    String name;

    @Column( name = "description", nullable = false, length = 255)
    String description;

    @Column
    Boolean enable;

    @Column
    int quantity;

    @Column(name = "date_create")
    LocalDate dateCreate;

    @OneToMany(mappedBy = "transport")
    List<Send> sends;


    @ManyToOne(targetEntity = UserApp.class)
    @JoinColumn( name = "user_id")
    UserApp user;
}
