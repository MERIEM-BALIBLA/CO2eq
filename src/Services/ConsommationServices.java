package Services;

import Repository.ConsommationRepository;
import Repository.UserRepository;
import Utils.DateUtil;
import com.entity.Consommation;
import com.entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ConsommationServices {

    ConsommationRepository consommationRepository = new ConsommationRepository();
    UserRepository userRepository = new UserRepository();

    //  Liste des consommations de chaque user
    public List<Consommation> userConsommation(int id) {
        return consommationRepository.UserConsommationList(id);
    }

    //  l'ajout des consommations
    public Optional<Consommation> ajoutConsommation(Consommation consommation, int user_id) {
        Optional<User> user = userRepository.getUserById(user_id);
        consommation.setUser(user.get());
        return consommationRepository.insertConsummation(consommation);
    }

    //  Colecter les consommations de chaque user
    public List<Consommation> userConsommations(int user_id) {
        List<Consommation> consommationList = new ArrayList<>();

        consommationList.addAll(consommationRepository.alimentationList(user_id));
        consommationList.addAll(consommationRepository.transportList(user_id));
        consommationList.addAll(consommationRepository.logementList(user_id));
        return consommationList;
    }

    //  La somme des consommation pour chaque user
    public double consommationTotal(int user_id) {
        double totalImpact = userConsommations(user_id)
                .stream()
                .mapToDouble(c -> ((Consommation) c).calculerImpact())
                .sum();
        return totalImpact;
    }

    //  Filtrage
    public List<User> filtrerParConsommation() {
        List<User> userList = new ArrayList<>();
        List<User> allUsers = userRepository.utilisateursListe();
        List<User> filteredUsers = allUsers.stream()
                .filter(e -> consommationTotal(e.getId()) > 310000)
                .collect(Collectors.toList());
        System.out.println("Utilisateurs avec une consommation totale > 31000 :");
        for (User user : filteredUsers) {
            System.out.println(user.getName());
            userList.add(user);
        }
        return userList;
    }

    //  Calcul de la Consommation Moyenne :
    public Map<User, Double> consommationMoyenneParUtilisateur(LocalDate start, LocalDate endDate) {
        if (start.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }

        List<User> users = userRepository.utilisateursListe(); // Remplace par la méthode appropriée pour obtenir tous les utilisateurs
        Map<User, Double> moyenneParUtilisateur = new HashMap<>();

        for (User user : users) {
            List<Consommation> consumptions = userConsommations(user.getId());
            List<LocalDate> dates = DateUtil.dateListRange(start, endDate);

            if (dates.isEmpty()) {
                moyenneParUtilisateur.put(user, 0.0);
                continue;
            }

            double totalImpact = consumptions.stream()
                    .filter(e -> DateUtil.isDateAvailable(e.getDateDebut(), e.getDateFin(), start, endDate))
                    .mapToDouble(Consommation::calculerImpact)
                    .sum();

            double moyenneConsommation = (dates.size() == 0) ? 0.0 : totalImpact / dates.size();

            if (moyenneConsommation > 0) {
                moyenneParUtilisateur.put(user, moyenneConsommation);
            }
        }

        System.out.printf("%-20s %-15s%n", "Utilisateur", "Moyenne Consommation");
        System.out.println("------------------------------------");
        for (Map.Entry<User, Double> entry : moyenneParUtilisateur.entrySet()) {
            System.out.printf("%-20s %-15.2f%n", entry.getKey().getName(), entry.getValue());
        }

        return moyenneParUtilisateur;

    }

    //   Liste d'utilisateurs actives
    public List<Consommation> utilisateurActive(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }

        // Récupère toutes les consommations
        List<Consommation> consommations = consommationRepository.consommationsListe();
        List<Consommation> filteredConsumptions = consommations.stream()
                .filter(consommation -> DateUtil.isDateAvailable(consommation.getDateDebut(), consommation.getDateFin(), dateDebut, dateFin))
                .collect(Collectors.toList());

        System.out.printf("%-20s %-15s %-15s%n", "Utilisateur", "Date Début", "Date Fin");
        System.out.println("-----------------------------------------------");
        for (Consommation consommation : filteredConsumptions) {
            User user = consommation.getUser();
            if (user != null) {
                System.out.printf("%-20s %-15s %-15s%n", user.getName(), consommation.getDateDebut(), consommation.getDateFin());
            } else {
                System.out.printf("%-20s %-15s %-15s%n", "Utilisateur inconnu", consommation.getDateDebut(), consommation.getDateFin());
            }
        }
        return filteredConsumptions;
    }

    //    tri de user selon leurs consommations
    public List<User> triUtilisateur() {
        List<User> allUsers = userRepository.utilisateursListe();

        // Trie les utilisateurs par consommation totale décroissante
        List<User> sortedUsers = allUsers.stream()
                .sorted((user1, user2) -> Double.compare(consommationTotal(user2.getId()), consommationTotal(user1.getId())))
                .collect(Collectors.toList());

        System.out.println("Utilisateurs triés par consommation totale de carbone :");
        for (User user : sortedUsers) {
            double totalConsumption = consommationTotal(user.getId());
            System.out.printf("ID : %d, Nom : %s, Consommation Totale : %.2f%n", user.getId(), user.getName(), totalConsumption);
        }

        return sortedUsers;
    }

}




/*
    public double consommerTotalLogement(int user_id) {
        List<Consommation> consommationList = consommationRepository.logementList(user_id);
        double totalImpact = consommationList
                .stream()
                .filter(c -> c instanceof Logement) // Filtrer pour être sûr de traiter uniquement les Alimentation
                .mapToDouble(c -> ((Logement) c).calculerImpact())
                .sum();
        System.out.println("Total impact logement pour l'utilisateur " + user_id + ": " + totalImpact);
        return totalImpact;
    }
*/

/*
    public Double consommationMoyenne(User user, LocalDate start, LocalDate endDate) {
        if (start.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }

        List<Consommation> consumptions = userConsommations(user.getId());
        List<LocalDate> dates = DateUtil.dateListRange(start, endDate);
        if (dates.isEmpty()) {
            return 0.0;
        }
        double totalImpact = consumptions.stream()
                .filter(e -> {
                    boolean available = DateUtil.isDateAvailable(e.getDateDebut(), e.getDateFin(), dates);
                    //vérifier les dates de consommation
                    System.out.println("Consommation: " + e.getDateDebut() + " - " + e.getDateFin() + " Disponible: " + available);
                    return available;
                })
                .mapToDouble(Consommation::calculerImpact)
                .sum();
        System.out.println("Total des impacts calculés : " + totalImpact);

        return totalImpact / dates.size();
    }
*/

