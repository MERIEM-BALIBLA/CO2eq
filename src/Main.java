import Repository.UserRepository;
import Services.ConsommationServices;
import Services.UserServices;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean status = true;

    public static void main(String[] args) {
        ConsommationMenu menu = new ConsommationMenu();
        ConsommationServices consommationServices = new ConsommationServices();
        UserServices userServices = new UserServices();
        UserRepository userRepository = new UserRepository();

//  ------------------------------- > CRUD USER
//  Ajouter User
//        userServices.addUser();

//  Modifier les informations de user
//        userServices.editUser();

//  Supprimer User
//        userServices.deleteUSer();

//  ------------------------------- > INSERT CONSOMMATION
//            menu.ajoutConsommation();

//  Liste des consommations sans leurs impacts
//      menu.userConsommationList();

//  liste des consommations selon le type de consommation
//       menu.logement();
//       menu.alimentation();

//  Fonction calcule l'impact total
//        menu.calculSomImpact();

//  Fonction calcul l'impact total pour chaque type
//        consommationServices.consommerTotalTransport(5);
//        consommationServices.consommerTotalLogement(5);
//        consommationServices.consommerTotalAlimentation(5);

//  Filtrer users qui ont consommation plus de 31000
//        menu.filterConsommation();

//  Tri des user selon leurs consommations impacts
//        menu.triUserParConsommation();


//        Optional<User> user = userRepository.getUserById(21);
//        LocalDate startDate = LocalDate.of(2024, 10, 1);
//        LocalDate endDate = LocalDate.of(2024, 10, 15);
//        List<Consommation> average = consommationServices.usreInactive(startDate, endDate);
//        System.out.println(average);

        while (status) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            switch (choix) {
//  ------------------------------- > CRUD USER
                case 1:
                    userServices.ajouterUtilisateur();
                    break;
                case 2:
                    userServices.modifierUtilisateur();
                    break;
                case 3:
                    userServices.supprimerUtilisateur();
                    break;
                case 4:
                    userServices.utilisateurListe();
                    break;
                case 5:
                    userServices.utilisateurInformations();
                    break;
//  ------------------------------- > INSERT CONSOMMATION
                case 6:
                    menu.ajouterConsommation();

                    break;
                case 7:
                    menu.userConsommationList();
                    break;
                case 8:
                    menu.calculSomImpact();
                    break;
                case 9:
                    menu.filterConsommation();
                    break;
                case 10:
                    menu.moyenneConsommationRange();
                    break;
                case 11:
                    menu.userActive();
                    break;
                case 12:
                    menu.triUserParConsommation();
                    break;
                case 13:
                    quitter();
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("=== Menu Utilisateur ===");
        System.out.println("1. Ajouter un utilisateur");
        System.out.println("2. Modifier un utilisateur");
        System.out.println("3. Supprimer un utilisateur");
        System.out.println("4. La liste d'utilisateurs");
        System.out.println("5. Voir les informations d'un utilisateur specifié");
        System.out.println("6. Ajouter une consommation pour un utilisateur");
        System.out.println("7. Voir les consommation d'un utilisateur spécifique");
        System.out.println("8. les impacts de consommation d'un utilisateur");
        System.out.println("9. Affichage des utilisateurs avec un impact total > 31000KgCO2eq");
        System.out.println("10. Calculer la consommation moyenne de carbone par utilisateur pour une période donnée.");
        System.out.println("11. Afficher les utilisateurs n'ayant pas enregistré de consommation de carbone pendant une période spécifiée.");
        System.out.println("12. Liste d'utilisateurs en fonction de leur consommation totale de carbone.");
        System.out.println("13. Quitter");
        System.out.print("Choisissez une option : ");

    }

    private static void quitter() {
        System.out.println("Quitter le programme.");
        status = false;
    }

}
