package Repository;

import Config.Connexion;
import Services.UserServices;
import com.entity.Consommation;
import com.entity.Sous_class.Alimentation;
import com.entity.Sous_class.Logement;
import com.entity.Sous_class.Transport;
import com.entity.User;
import com.entity.enums.AlimentationType;
import com.entity.enums.LogementType;
import com.entity.enums.TransportType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsommationRepository {
    private final Connexion connection;
UserRepository userRepository = new UserRepository();
    public ConsommationRepository() {
        this.connection = Connexion.getInstance();
    }


//    public void addTransport (Transport transport){
//        String sql = "INSERT INTO transport (user_id, quantity, start_date, end_date, vehicule, distance, type_id) VALUES (?, ?, ?, ?, ?, ?,?)";
//        try(PreparedStatement ps = connection.connectToDB().prepareStatement(sql)){
//            ps.setInt(1,transport.getUser_id());
//            ps.setDouble(2, transport.getQuantity());
//            ps.setDate(3, Date.valueOf(transport.getDateDebut()));
//            ps.setDate(4, Date.valueOf(transport.getDateFin()));
//            ps.setString(5, String.valueOf(transport.getType()));
//            ps.setDouble(6, transport.getDistance_parcourure());
//            ps.setInt(7, transport.getType_id());
//            ps.executeUpdate();
//        }catch (SQLException e){
//            System.out.println(e);
//        }
//    }
//
//    public void addAlimentation(Alimentation alimentation){
//        String sql = "INSERT INTO alimentation (user_id, quantity, start_date, end_date, type_aliment, poids, type_id) VALUES (?, ?, ?, ?, ?, ?,?)";
//        try(PreparedStatement ps = connection.connectToDB().prepareStatement(sql)){
//            ps.setInt(1,alimentation.getUser_id());
//            ps.setDouble(2, alimentation.getQuantity());
//            ps.setDate(3, Date.valueOf(alimentation.getDateDebut()));
//            ps.setDate(4, Date.valueOf(alimentation.getDateFin()));
//            ps.setString(5, String.valueOf(alimentation.getType_aliment()));
//            ps.setDouble(6, alimentation.getPoids());
//            ps.setInt(7, alimentation.getType_id());
//            ps.executeUpdate();
//        }catch (SQLException e){
//            System.out.println(e);
//        }
//    }
//
//    public void addLogement(Logement logement){
//        String sql = "INSERT INTO logement (user_id, quantity, start_date, end_date, type_energie, consommation_energie, type_id) VALUES (?, ?, ?, ?, ?, ?,?)";
//        try(PreparedStatement ps = connection.connectToDB().prepareStatement(sql)){
//            ps.setInt(1,logement.getUser_id());
//            ps.setDouble(2, logement.getQuantity());
//            ps.setDate(3, Date.valueOf(logement.getDateDebut()));
//            ps.setDate(4, Date.valueOf(logement.getDateFin()));
//            ps.setString(5, String.valueOf(logement.getType()));
//            ps.setDouble(6, logement.getConsommation_energie());
//            ps.setInt(7, logement.getType_id());
//            ps.executeUpdate();
//        }catch (SQLException e){
//            System.out.println(e);
//        }
//    }

    public Optional<Consommation> insertConsummation(Consommation consommation) {
        String sql = null;

        if (consommation instanceof Alimentation) {
            sql = "INSERT INTO alimentation (user_id, quantity, start_date, end_date,type_id, type_aliment, poids ) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (consommation instanceof Transport) {
            sql = "INSERT INTO transport (user_id, quantity, start_date, end_date,type_id, vehicule, distance ) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (consommation instanceof Logement) {
            sql = "INSERT INTO logement (user_id, quantity, start_date, end_date,type_id, type_energie, consommation_energie) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        if (sql != null) {
            try (PreparedStatement ps = connection.connectToDB().prepareStatement(sql)) {
                ps.setInt(1, consommation.getUser_id().getId());
                ps.setDouble(2, consommation.getQuantity());
                ps.setDate(3, Date.valueOf(consommation.getDateDebut()));
                ps.setDate(4, Date.valueOf(consommation.getDateFin()));
                ps.setInt(5, consommation.getType_id());

                if (consommation instanceof Alimentation) {
                    Alimentation alimentation = (Alimentation) consommation;
                    ps.setString(6, String.valueOf(alimentation.getType_aliment()));
                    ps.setDouble(7, alimentation.getPoids());
                } else if (consommation instanceof Transport) {
                    Transport transport = (Transport) consommation;
                    ps.setString(6, String.valueOf(transport.getType()));
                    ps.setDouble(7, transport.getDistance_parcourure());
                } else {
                    Logement logement = (Logement) consommation;
                    ps.setString(6, String.valueOf(logement.getType()));
                    ps.setDouble(7, logement.getConsommation_energie());
                }
                ps.executeUpdate();
                return Optional.of(consommation);
            } catch (SQLException e) {
                System.out.println(e);
            }
        }return Optional.empty();
    }

    public List<Consommation> consommationList(int user_id) {
        List<Consommation> consommationList = new ArrayList<>();
        String sql = "SELECT * FROM consummations WHERE user_id = " + user_id + " ORDER BY id ASC";

        try {
            Statement s = connection.connectToDB().createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int type_id = rs.getInt("type_id");
                double quantite = rs.getDouble("quantity");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();

                Consommation consommation = null;

                switch (type_id) {
                    case 1: // Alimentation
                        consommation = new Alimentation(id, quantite, type_id, start, end);
                        break;

                    case 2: // Transport
                        consommation = new Transport(id, quantite, type_id, start, end);
                        break;

                    case 3: // Logement
                        consommation = new Logement(id, quantite, type_id, start, end);
                        break;
                }

                if (consommation != null) {
                    consommationList.add(consommation);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return consommationList;
    }

    public List<Consommation> alimentationList(int user_id) {
        List<Consommation> consommationList = new ArrayList<>();
        String sql = "SELECT * FROM alimentation WHERE user_id = " + user_id;
        try {
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int type_id = rs.getInt("type_id");
                double quantity = rs.getDouble("quantity");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();
                double poids = rs.getDouble("poids");
                String typeAliment = rs.getString("type_aliment");

                AlimentationType TypeAmiment = null;
                if (typeAliment.equals("VIANDE") || typeAliment.equals("LEGUME")) {
                    TypeAmiment = AlimentationType.valueOf(typeAliment);
                }

                Consommation alimentation = new Alimentation(quantity, type_id, start, end, poids, TypeAmiment);
                consommationList.add(alimentation);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return consommationList;
    }

    public List<Consommation> logementList(int user_id) {
        List<Consommation> consommationList = new ArrayList<>();
        String sql = "SELECT * FROM logement WHERE user_id = " + user_id;
        try {
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int type_id = rs.getInt("type_id");
                double quantity = rs.getDouble("quantity");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();
                double energie = rs.getDouble("consommation_energie");
                String type_logement = rs.getString("type_energie");

                LogementType TypeLogement = null;
                if (type_logement.equals("ELECTRICITE") || type_logement.equals("GAZ")) {
                    TypeLogement = LogementType.valueOf(type_logement);
                }

                Consommation logement = new Logement(quantity, type_id, start, end, energie, TypeLogement);
                consommationList.add(logement);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return consommationList;
    }

    public List<Consommation> transportList(int user_id) {
        List<Consommation> consommationList = new ArrayList<>();
        String sql = "SELECT * FROM transport WHERE user_id = " + user_id;
        try {
            PreparedStatement ps = connection.connectToDB().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int type_id = rs.getInt("type_id");
                double quantity = rs.getDouble("quantity");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();
                String vehicule = rs.getString("vehicule");
                double distance = rs.getDouble("distance");

                TransportType Vehicule = null;
                if (vehicule.equals("TRAIN") || vehicule.equals("VOITURE")) {
                    Vehicule = TransportType.valueOf(vehicule);
                }

                Consommation transport = new Transport(quantity, type_id, start, end, distance, Vehicule);
                consommationList.add(transport);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return consommationList;
    }



    private Consommation createConsommation(ResultSet rs) throws SQLException {
        int type_id = rs.getInt("type_id");
        double quantity = rs.getDouble("quantity");
        LocalDate start = rs.getDate("start_date").toLocalDate();
        LocalDate end = rs.getDate("end_date").toLocalDate();

        switch (type_id) {
            case 1: // Transport
                double distance = rs.getDouble("distance");
                TransportType typeTransport = TransportType.valueOf(rs.getString("vehicule").toUpperCase());
                return new Transport(quantity, type_id, start, end, distance, typeTransport);
            case 2: // Alimentation
                double poids = rs.getDouble("poids");
                AlimentationType typeAliment = AlimentationType.valueOf(rs.getString("type_aliment").toUpperCase());
                return new Alimentation(quantity, type_id, start, end, poids, typeAliment);
            case 3: // Logement
                double consommationEnergie = rs.getDouble("consommation_energie");
                LogementType typeLogement = LogementType.valueOf(rs.getString("type_energie").toUpperCase());
                return new Logement(quantity, type_id, start, end, consommationEnergie, typeLogement);

            default:
                throw new SQLException("Type de consommation inconnu : " + type_id);
        }
    }

    public List<Consommation> consommationsList(int user_id){
        List<Consommation> consommationList = new ArrayList<>();
        Consommation consommation = null;
        String sql = null;
        if(consommation instanceof Alimentation){
            sql = "SELECT * FROM alimentation WHERE user_id =" + user_id;
        }else if(consommation instanceof Transport){
            sql = "SELECT * FROM transport WHERE user_id =" + user_id;
        }else if(consommation instanceof Logement){
            sql = "SELECT * FROM logement WHERE user_id =" + user_id;
        }

        try {
            Statement ps = connection.connectToDB().createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                consommation = createConsommation(rs);  // Create the correct instance using your helper method
                consommationList.add(consommation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  consommationList;
    }

}
