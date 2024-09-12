package com.entity.Sous_class;

import com.entity.Consommation;
import com.entity.User;
import com.entity.enums.AlimentationType;

import java.time.LocalDate;
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
        if (type_aliment.equals(AlimentationType.VIANDE)) {
            return impact = poids * 0.5 * getQuantity();
        } else if (type_aliment == AlimentationType.LEGUME) {
            return impact = poids * 5.0 * getQuantity();
        }
        return impact;
    }
}
