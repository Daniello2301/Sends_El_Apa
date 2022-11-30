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

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int large;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String ubication;

    @Column(nullable = false, name = "date_create")
    private LocalDate dateCreate;

    @Column(nullable = false, name = "date_update")
    private LocalDate dateUpdate;

    @ManyToOne
    @JoinColumn( name = "user_id")
    private UserApp user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id", nullable = false)
    private Transport transport;
}
