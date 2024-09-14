package com.entity.Sous_class;

import com.entity.Consommation;
import com.entity.User;
import com.entity.enums.AlimentationType;
import com.entity.enums.LogementType;
import com.entity.enums.TransportType;

import java.time.LocalDate;
import java.util.List;

public class Transport extends Consommation {
//    private int tansport_id;
    private double distance_parcourure;
    private TransportType type;

    public Transport( double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin, double distance_parcourure, TransportType type) {
        super( quantity, type_id, dateDebut, dateFin);
        this.distance_parcourure = distance_parcourure;
        this.type = type;
    }

    public Transport(int id, double quantity, int type_id, LocalDate dateDebut, LocalDate dateFin) {
        super(id, quantity, type_id, dateDebut, dateFin);
    }

    //    public Transport( double distance_parcourure, TransportType type) {
//        this.distance_parcourure = distance_parcourure;
//        this.type = type;
//    }

    public Transport() {

    }


    public double getDistance_parcourure() {
        return distance_parcourure;
    }

    public TransportType getType() {
        return type;
    }

    @Override
    public double calculerImpact() {
        if (type == null) {
            throw new IllegalStateException("Le type de transport ne peut pas être null");
        }
        double impact = 0;
        if (type.equals(TransportType.VOITURE)) {
            return distance_parcourure * 0.5 * getQuantity();
        } else if (type == TransportType.TRAIN) {
            return distance_parcourure * 5.0 * getQuantity();
        }
        return impact;
    }
//    public double calculerImpact() {
//        double impact = 0;
//        if(type.equals(TransportType.VOITURE)){
//            return impact=distance_parcourure*0.5*getQuantity();
//        }else if(type == TransportType.TRAIN){
//            return impact=distance_parcourure*5.0*getQuantity();
//        }
//        return impact;
//    }

//    public List<LocalDate> getDaysInRange() {
//        return DateUtils.dateListRange(getDateDebut(), getDateFin());
//    }
}
