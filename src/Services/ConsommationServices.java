package Services;

import Repository.ConsommationRepository;
import Repository.UserRepository;
import com.entity.Consommation;
import com.entity.User;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ConsommationServices {

    ConsommationRepository consommationRepository = new ConsommationRepository();
    UserRepository userRepository = new UserRepository();

    //  Liste des consommations de chaque user
    public List<Consommation> userConsommation(int id) {
        return consommationRepository.UserConsommationList(id);
    }

    //  L'ajout des consommations
    public Optional<Consommation> addConsommation(Consommation consommation, int user_id) {
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

    //  Filtrage des Données Utilisateur
    public double consommationTotal(int user_id) {
        double totalImpact = userConsommations(user_id)
                .stream()
                .mapToDouble(c -> ((Consommation) c).calculerImpact())
                .sum();
//        System.out.println("Total impact alimentation pour l'utilisateur " + user_id + ": " + totalImpact);
        return totalImpact;
    }

    public List<User> filterByConsuption() {
        List<User> userList = new ArrayList<>();
        List<User> allUsers = userRepository.getAllUsers();
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

    //    tri de user selon leurs consommations
    public List<User> sortUsersByConsommation() {
        List<User> allUsers = userRepository.getAllUsers();

        // Créez une liste de pairs (user, consommation totale)
        List<Map.Entry<User, Double>> userConsumptionEntries = allUsers.stream()
                .map(user -> new AbstractMap.SimpleEntry<>(user, consommationTotal(user.getId())))
                .collect(Collectors.toList());

        // Triez les utilisateurs dsc
        userConsumptionEntries.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // liste user tri
        List<User> sortedUsers = userConsumptionEntries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Affichez les utilisateurs triés
        System.out.println("Utilisateurs triés par consommation totale de carbone :");
        for (User user : sortedUsers) {
            System.out.printf("ID : %d, Nom : %s, Consommation Totale : %.2f%n", user.getId(), user.getName(), consommationTotal(user.getId()));
        }

        return sortedUsers;
    }


    //  Calcul de la Consommation Moyenne :
//    génère une liste de toutes les dates entre startDate et endDate dateListRange
//    public static List<LocalDate> dateListRange(LocalDate startDate, LocalDate endDate) {
////        List<LocalDate> dateListRange = new ArrayList<>();
////        for (LocalDate dateTest = startDate; !dateTest.isAfter(endDate); dateTest = dateTest.plusDays(1)) {
////            dateListRange.add(dateTest);
////        }
////        return dateListRange;
////    }

    // DateUtils:


    public static List<LocalDate> dateListRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateListRange = new ArrayList<>();
        for (LocalDate dateTest = startDate; !dateTest.isAfter(endDate); dateTest = dateTest.plusDays(1)) {
            dateListRange.add(dateTest);
//            System.out.println("Dates dans la plage : " + dateListRange);
        }
        return dateListRange;
    }

    public static boolean isDateAvailable(LocalDate startDate, LocalDate endDate, List<LocalDate> dates) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!dates.contains(date)) {
                return false;
            }
        }
        return true;
    }

    public Double averageByPeriod(User user, LocalDate start, LocalDate endDate) {
        if (start.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }

        List<Consommation> consumptions = userConsommations(user.getId());
        List<LocalDate> dates = dateListRange(start, endDate);
        if (dates.isEmpty()) {
            return 0.0;
        }
        double totalImpact = consumptions.stream()
                .filter(e -> {
                    boolean available = isDateAvailable(e.getDateDebut(), e.getDateFin(), dates);
                    //vérifier les dates de consommation
                    System.out.println("Consommation: " + e.getDateDebut() + " - " + e.getDateFin() + " Disponible: " + available);
                    return available;
                })
                .mapToDouble(Consommation::calculerImpact)
                .sum();
        System.out.println("Total des impacts calculés : " + totalImpact);
        return totalImpact / dates.size();
    }

    public List<Consommation> usreInactive( LocalDate start, LocalDate endDate) {
        List<Consommation> consommationsList;
        if (start.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }
        List<LocalDate> dates = dateListRange(start, endDate);
        List<Consommation> consumptions = consommationRepository.consommationsLists();
        consommationsList = consumptions.stream()
                .filter(e -> {
                    boolean available = isDateAvailable(e.getDateDebut(), e.getDateFin(), dates);
                    return available;
                }).collect(Collectors.toList());

        // Affichage des résultats
        System.out.printf("%-20s %-15s %-15s%n", "Utilisateur", "Date Début", "Date Fin");
        System.out.println("-----------------------------------------------");
        for (Consommation consommation : consommationsList) {
            User user = consommation.getUser();
            if (user != null) {
                System.out.printf("%-20s %-15s %-15s%n", user.getName(), consommation.getDateDebut(), consommation.getDateFin());
            } else {
                System.out.printf("%-20s %-15s %-15s%n", "Utilisateur inconnu", consommation.getDateDebut(), consommation.getDateFin());
            }
        }
        return consommationsList;
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



