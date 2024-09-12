package com.entity.Sous_class;

import com.entity.Consommation;
import com.entity.User;
import com.entity.enums.AlimentationType;
import com.entity.enums.LogementType;

import java.time.LocalDate;

public class Logement extends Consommation {
    private double consommation_energie;
    private LogementType type;

    public Logement( double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin, double consommation_energie, LogementType type) {
        super( quantity, type_id, dateDebut, dateFin);
        this.consommation_energie = consommation_energie;
        this.type = type;
    }

    public Logement(int id, double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin) {
        super(id, quantity, type_id, dateDebut, dateFin);
    }

    public double getConsommation_energie() {
        return consommation_energie;
    }

    public LogementType getType() {
        return type;
    }

    @Override
    public double calculerImpact() {
        double impact = 0;
        if(type.equals(LogementType.GAZ)){
            return impact=consommation_energie*0.5*getQuantity();
        }else if(type == LogementType.ELECTRICITE){
            return impact=consommation_energie*5.0*getQuantity();
        }
        return impact;
    }
}
