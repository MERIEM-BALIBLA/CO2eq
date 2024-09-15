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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ConsommationMenu {
    Scanner scanner = new Scanner(System.in);
    ConsommationServices consommationServices = new ConsommationServices();
    UserServices userServices = new UserServices();
    ConsommationRepository consommationRepository = new ConsommationRepository();

    public void ajouterConsommation() {
//        int user_id = -1;
        double quantite = -1;
        LocalDate dateDebut = null;
        LocalDate dateFin = null;
//        int type_id = -1;
        boolean validInput = false;


        System.out.println("Enter user id :");
        int user_id = scanner.nextInt();
        scanner.nextLine();

        validInput = false;
        while (!validInput) {
            System.out.println("Entrer la quantité ici :");
            if (scanner.hasNextDouble()) {
                quantite = scanner.nextDouble();
                scanner.nextLine(); // Consommer la ligne restante après l'entrée de la quantité

                if (quantite > 0) {
                    validInput = true;
                } else {
                    System.out.println("La quantité doit être un nombre positif. Veuillez réessayer.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre valide pour la quantité.");
                scanner.next(); // Consommer l'entrée non valide
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.println("La date de début (format YYYY-MM-DD) :");
            String dateDebutStr = scanner.nextLine();
            try {
                dateDebut = LocalDate.parse(dateDebutStr);
                if (dateDebut.isBefore(LocalDate.now())) {
                    System.out.println("La date de début ne peut pas être avant aujourd'hui. Veuillez entrer une date valide.");
                } else {
                    validInput = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez entrer la date au format YYYY-MM-DD.");
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.println("La date de fin (format YYYY-MM-DD) :");
            String dateFinStr = scanner.nextLine();
            try {
                dateFin = LocalDate.parse(dateFinStr);
                if (dateFin.isBefore(dateDebut)) {
                    System.out.println("La date de fin ne peut pas être avant la date de début. Veuillez réessayer.");
                } else {
                    validInput = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez entrer la date au format YYYY-MM-DD.");
            }
        }


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

                Transport transport = new Transport(quantite, type_id, dateDebut, dateFin, distance, element_transport == 1 ? TransportType.VOITURE : TransportType.TRAIN);
                consommationServices.ajoutConsommation(transport, user_id);
                break;
            case 2:
                System.out.println("Le type d'alimantation : 1 -> VIANDE / 2 -> LEGUME ");
                int element_aliment = scanner.nextInt();
                scanner.nextLine();

                System.out.println("La poids :");
                double poids = scanner.nextDouble();

                Alimentation alimentation = new Alimentation(quantite, type_id, dateDebut, dateFin, poids, element_aliment == 1 ? AlimentationType.VIANDE : AlimentationType.LEGUME);
                consommationServices.ajoutConsommation(alimentation, user_id);
                break;
            case 3:
                System.out.println("Le type de logement : 1 -> ELECTRICITE / 2 -> GAZ ");
                int element_logement = scanner.nextInt();
                scanner.nextLine();

                System.out.println("L'energies consomé :");
                double energie = scanner.nextDouble();

                Logement logement = new Logement(quantite, type_id, dateDebut, dateFin, energie, element_logement == 1 ? LogementType.ELECTRICITE : LogementType.GAZ);
                consommationServices.ajoutConsommation(logement, user_id);
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
            if (!consommationList.isEmpty()) {
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
        consommationServices.filtrerParConsommation();
    }

    public void moyenneConsommationRange() {
        System.out.println("start");
        String start = scanner.nextLine();
        LocalDate dateDebut = LocalDate.parse(start);
        System.out.println("fin");
        String fin = scanner.nextLine();
        LocalDate datefin = LocalDate.parse(fin);

        Map<User, Double> consommationMoyenneMap = consommationServices.consommationMoyenneParUtilisateur(dateDebut, datefin);
    }

    public void userActive() {
        System.out.println("start");
        String start = scanner.nextLine();
        LocalDate startdate = LocalDate.parse(start);
        System.out.println("fin");
        String fin = scanner.nextLine();
        LocalDate findate = LocalDate.parse(fin);

        consommationServices.utilisateurActive(startdate, findate);
    }

    public void triUserParConsommation() {
        consommationServices.triUtilisateur();
    }
}
