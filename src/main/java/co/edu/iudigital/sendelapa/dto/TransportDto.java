package co.edu.iudigital.sendelapa.dto;

import co.edu.iudigital.sendelapa.model.UserApp;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class TransportDto {

    private Long id;
    private String name;
    private String description;
    private Boolean enable;
    private int quantity;
    private LocalDate dateCreate;
    private Long user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDateCreate() {
        return this.dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getUser_id() {
        return user;
    }

    public void setUser_id(Long user) {
        this.user = user;
    }
}
