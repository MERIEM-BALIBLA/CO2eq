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

    public ConsommationRepository() {
        this.connection = Connexion.getInstance();
    }

    //    l'insertion
    public Optional<Consommation> insertConsummation(Consommation consommation) {
        String sql = null;

        if (consommation instanceof Alimentation) {
            sql = "INSERT INTO alimentation (user_id, quantity, start_date, end_date, type_id, type_aliment, poids) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (consommation instanceof Transport) {
            sql = "INSERT INTO transport (user_id, quantity, start_date, end_date, type_id, vehicule, distance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (consommation instanceof Logement) {
            sql = "INSERT INTO logement (user_id, quantity, start_date, end_date, type_id, type_energie, consommation_energie) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        if (sql != null) {
            Connection conn = null;
            try {
                conn = connection.connectToDB();
                conn.setAutoCommit(false); // Start transaction

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
                    conn.commit(); // Commit transaction
                    return Optional.of(consommation);
                } catch (SQLException e) {
                    if (conn != null) {
                        conn.rollback(); // Rollback transaction in case of error
                    }
                    System.out.println("Erreur lors de l'insertion : " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true); // Reset to default
                        conn.close(); // Close connection
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
                    }
                }
            }
        }
        return Optional.empty();
    }

    //    liste des consommation apartir consummations
    public List<Consommation> consommationsListe() {
        List<Consommation> consommationList = new ArrayList<>();
        String sql = "SELECT consummations.*, users.name FROM consummations LEFT JOIN users ON user_id = users.id";

        try {
            Statement s = connection.connectToDB().createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int type_id = rs.getInt("type_id");
                double quantite = rs.getDouble("quantity");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();
                String userName = rs.getString("name");

                User user = new User(); // Crée un nouvel utilisateur pour l'association
                user.setName(userName);

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
                    consommation.setUser(user); // Associe l'utilisateur à la consommation
                    consommationList.add(consommation);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return consommationList;
    }

    //    Liste des consommations pour chaque utilisateur
    public List<Consommation> UserConsommationList(int user_id) {
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

    //    Liste des consommatio de type Alimentation
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

    //    Liste des consommatio de type Logement
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

    //    Liste des consommatio de type Transport
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


}
