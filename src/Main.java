import Repository.UserRepository;
import Services.ConsommationServices;
import Services.UserServices;
import com.entity.Consommation;
import com.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean status = true;
    public static void main(String[] args) {
        Menu menu = new Menu();
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
                case 1:
                    userServices.ajouterUtilisateur();
                    break;
                case 2:
                    userServices.modifierUtilisateur();
                    break;
                case 3:
                    userServices.deleteUSer();
                    break;
                case 4:
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
        System.out.println("4. Quitter");
        System.out.print("Choisissez une option : ");
    }
    private static void quitter() {
        System.out.println("Quitter le programme.");
        status = false;
    }

}
