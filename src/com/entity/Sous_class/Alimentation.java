package com.entity.Sous_class;

import com.entity.Consommation;
import com.entity.User;
import com.entity.enums.AlimentationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Alimentation extends Consommation {
    private double poids;
    private AlimentationType type_aliment;

    public Alimentation(double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin, double poids, AlimentationType type_aliment) {
        super(quantity, type_id, dateDebut, dateFin);
        this.poids = poids;
        this.type_aliment = type_aliment;
    }

    public Alimentation(int id, double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin) {
        super(id, quantity, type_id, dateDebut, dateFin);
    }

    public Alimentation() {
    }

//    public Alimentation(int id, Optional<User> userById, double quantite, int typeId, LocalDate start, LocalDate end) {
//    }

    public double getPoids() {
        return poids;
    }

    public String getType_aliment() {
        return type_aliment.name();
    }

    @Override
    public double calculerImpact() {
        double impact = 0;
        System.out.println("Type aliment: " + type_aliment + ", Poids: " + poids + ", Quantité: " + getQuantity());

        if (type_aliment.equals(AlimentationType.VIANDE)) {
            impact = poids * 5.0 * getQuantity();  // Exemple d'augmentation du coefficient
        } else if (type_aliment == AlimentationType.LEGUME) {
            impact = poids * 50.0 * getQuantity();  // Exemple d'augmentation du coefficient
        }

        System.out.println("Impact calculé pour " + type_aliment + ": " + impact);
        return impact;
    }


//    public List<LocalDate> getDaysInRange() {
//        return DateUtils.dateListRange(getDateDebut(), getDateFin());
//    }
}
