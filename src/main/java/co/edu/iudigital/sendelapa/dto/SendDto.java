package co.edu.iudigital.sendelapa.dto;

import co.edu.iudigital.sendelapa.model.Transport;
import co.edu.iudigital.sendelapa.model.UserApp;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class SendDto {
    private Long id;

    private String description;

    private int large;

    private int height;

    private int width;

    private int weight;

    private String status;

    private String ubication;

    private LocalDate dateCreate;

    private LocalDate dateUpdate;


    @JsonProperty("user")
    private Long user;

    @JsonProperty("transport")
    private Long transport;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getTransport() {
        return transport;
    }

    public void setTransport(Long transport) {
        this.transport = transport;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUbication() {
        return ubication;
    }

    public void setUbication(String ubication) {
        this.ubication = ubication;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

}
