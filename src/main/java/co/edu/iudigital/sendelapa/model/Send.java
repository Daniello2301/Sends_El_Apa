package co.edu.iudigital.sendelapa.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "sends")
@Data
public class Send implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_send", nullable = false)
    private Long id;

    private String description;

    private int large;

    private int height;

    private int width;

    private int weight;

    private String status;

    private String ubication;

    @Column(nullable = false, name = "date_create")
    private LocalDate dateCreate;

    @Column(nullable = false, name = "date_update")
    private LocalDate dateUpdate;

    @ManyToOne
    @JoinColumn( name = "usuario_id")
    private UserApp user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id", nullable = false)
    private Transport transport;
}
