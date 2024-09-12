package com.entity;

import com.entity.enums.ConsomationType;

import java.sql.Date;
import java.time.LocalDate;
public abstract class Consommation {
    private int id;
//    private int user_id;
    private User user;
    private double quantite;
    private int type_id;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public Consommation( double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin) {

        this.quantite = quantity;
        this.type_id = type_id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Consommation(int id, double quantite, int type_id, LocalDate dateDebut, LocalDate dateFin) {
        this.id = id;
//        this.user = user;
        this.quantite = quantite;
        this.type_id = type_id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Consommation() {
    }

    public int getId() {
        return id;
    }

    public User getUser_id() {
        return user;
    }

    public double getQuantity() {
        return quantite;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public int getType_id() {
        return type_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Consommation ID: " + id + ", Type ID: " + type_id + ", Quantité: " + quantite +
                ", Début: " + dateDebut + ", Fin: " + dateFin;
    }

    public abstract double calculerImpact();
}
