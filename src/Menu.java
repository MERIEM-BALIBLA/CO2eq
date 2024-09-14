import Repository.ConsommationRepository;
import Services.ConsommationServices;
import Services.UserServices;
import com.entity.Consommation;
import com.entity.Sous_class.Alimentation;
import com.entity.Sous_class.Logement;
import com.entity.Sous_class.Transport;
import com.entity.User;
import com.entity.enums.AlimentationType;
import com.entity.enums.LogementType;
import com.entity.enums.TransportType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    ConsommationServices consommationServices = new ConsommationServices();
    UserServices userServices = new UserServices();
    ConsommationRepository consommationRepository = new ConsommationRepository();

    public void ajoutConsommation() {
//        l'ajout de consommation
        System.out.println("Enter user id :");
        int user_id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Entrer la quantité ici :");
        double quantite = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("La date de début (format YYYY-MM-DD) :");
        String dateDebut = scanner.nextLine();
        LocalDate DateDebut = LocalDate.parse(dateDebut);

        // Demander à l'utilisateur d'entrer la date de fin
        System.out.println("La date de fin (format YYYY-MM-DD) :");
        String dateFin = scanner.nextLine();  // Saisie directe de la date
        LocalDate DateFin = LocalDate.parse(dateFin);

        System.out.println("Choisir le type de consommation : ");
        System.out.println("1 -------> Transport");
        System.out.println("2 -------> Alimentation");
        System.out.println("3 -------> Logement");
        int type_id = scanner.nextInt();
        scanner.nextLine();

        switch (type_id) {
            case 1:
                System.out.println("Le type de transport : 1 -> VOITURE / 2 -> TRAIN ");
                int element_transport = scanner.nextInt();
                scanner.nextLine();

                System.out.println("La distance :");
                double distance = scanner.nextDouble();

                Transport transport = new Transport(quantite, type_id, DateDebut, DateFin, distance, element_transport == 1 ? TransportType.VOITURE : TransportType.TRAIN);
                consommationServices.addConsommation(transport, user_id);
                break;
            case 2:
                System.out.println("Le type d'alimantation : 1 -> VIANDE / 2 -> LEGUME ");
                int element_aliment = scanner.nextInt();
                scanner.nextLine();

                System.out.println("La poids :");
                double poids = scanner.nextDouble();

                Alimentation alimentation = new Alimentation(quantite, type_id, DateDebut, DateFin, poids, element_aliment == 1 ? AlimentationType.VIANDE : AlimentationType.LEGUME);
                consommationServices.addConsommation(alimentation, user_id);
                break;
            case 3:
                System.out.println("Le type de logement : 1 -> ELECTRICITE / 2 -> GAZ ");
                int element_logement = scanner.nextInt();
                scanner.nextLine();

                System.out.println("L'energies consomé :");
                double energie = scanner.nextDouble();

                Logement logement = new Logement(quantite, type_id, DateDebut, DateFin, energie, element_logement == 1 ? LogementType.ELECTRICITE : LogementType.GAZ);
                consommationServices.addConsommation(logement, user_id);
                break;
        }


    }

    public void userConsommationList() {
        System.out.println("user id");
        int id = scanner.nextInt();

        Optional<User> userOptional = userServices.foundUser(id);
        List<Consommation> consommationList = consommationServices.userConsommation(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
            for (Consommation consommation : consommationList) {
                System.out.printf(
                        "ID: %d \n,Quantity: %.2f, Date début: %s, Date fin: %s\n",
                        consommation.getId(),
                        consommation.getQuantity(),
                        consommation.getDateDebut(),
                        consommation.getDateFin()
//                        consommation.calculerImpact()
                );
            }
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    public void logement() {
        System.out.println("user id");
        int id = scanner.nextInt();

        Optional<User> userOptional = userServices.foundUser(id);
        List<Consommation> consommationList = consommationRepository.logementList(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
            for (Consommation consommation : consommationList) {
                System.out.printf(
                        "\n,Quantity: %.2f, Date début: %s, Date fin: %s, impact: %f \n",
                        consommation.getQuantity(),
                        consommation.getDateDebut(),
                        consommation.getDateFin(),
                        consommation.calculerImpact()
                );
            }
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }
    public void alimentation() {
        System.out.println("user id");
        int id = scanner.nextInt();

        Optional<User> userOptional = userServices.foundUser(id);
        List<Consommation> consommationList = consommationRepository.alimentationList(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("ID: " + user.getId() + ", Name: " + user.getName() + ", Age: " + user.getAge());
            if(!consommationList.isEmpty()){
                for (Consommation consommation : consommationList) {
                    System.out.printf(
                            "\n,Quantity: %.2f, Date début: %s, Date fin: %s, impact: %f \n",
                            consommation.getQuantity(),
                            consommation.getDateDebut(),
                            consommation.getDateFin(),
                            consommation.calculerImpact()
                    );
                }
            }else {
                System.out.println("Pas de consommation de ce type");
            }
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    public void calculSomImpact() {
        System.out.println("user id :");
        int id = scanner.nextInt();
        consommationServices.consommationTotal(id);
    }

    public void filterConsommation() {
        consommationServices.filterByConsuption();
    }

    public void moyenneConsommationRange() {
//        consommationServices.dateListRange(LocalDate.of(2024, 9, 1), LocalDate.of(2024, 9, 30));
    }

    public void triUserParConsommation (){
        consommationServices.sortUsersByConsommation();
    }
}
