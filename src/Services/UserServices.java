package Services;

import Repository.UserRepository;
import com.entity.User;

import java.util.List;
import java.util.Scanner;
import java.util.Optional;

public class UserServices {
    private final UserRepository userRepository;
    Scanner scanner = new Scanner(System.in);

    public UserServices() {
        this.userRepository = new UserRepository();
    }

    public void ajouterUtilisateur() {
        String name = "";
        int age = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Nom : ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Le nom ne peut pas être vide. Veuillez réessayer.");
            } else if (!name.matches("[a-zA-Z ]+")) {
                System.out.println("Le nom ne peut contenir que des lettres et des espaces. Veuillez réessayer.");
            } else {
                validInput = true;
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.println("Âge : ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                if (age < 10 || age > 99) {
                    System.out.println("L'âge doit être un nombre entre 18 et 99. Veuillez réessayer.");
                } else {
                    validInput = true;
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier pour l'âge.");
                scanner.next(); // Clear the invalid input
            }
        }

        // Création de l'utilisateur
        User user = userRepository.ajouterUtilisateur(name, age);
        System.out.println("Utilisateur ajouté : " + user.getName() + ", Âge : " + user.getAge());
    }

    public void modifierUtilisateur() {
        int id = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Entrez l'ID de l'utilisateur : ");

            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine(); // Consommer la ligne restante après l'entrée de l'entier

                if (id > 0) {
                    validInput = true;
                } else {
                    System.out.println("L'ID doit être un nombre entier positif. Veuillez réessayer.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier pour l'ID.");
                scanner.next(); // Ignorer l'entrée non valide pour éviter une boucle infinie
            }
        }

        Optional<User> existingUser = userRepository.getUserById(id);
        if (existingUser.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé avec l'ID " + id + ".");
            return;
        }

        String name = "";
        validInput = false;

        while (!validInput) {
            System.out.println("Entrez le nom de l'utilisateur : ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Le nom ne peut pas être vide. Veuillez réessayer.");
            } else if (!name.matches("[a-zA-Z ]+")) {
                System.out.println("Le nom ne peut contenir que des lettres et des espaces. Veuillez réessayer.");
            } else {
                validInput = true;
            }
        }

        int age = -1;
        validInput = false;

        while (!validInput) {
            System.out.println("Entrez l'âge de l'utilisateur : ");

            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine();

                if (age >= 10 && age <= 99) {
                    validInput = true;
                } else {
                    System.out.println("L'âge doit être un nombre entre 18 et 99. Veuillez réessayer.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier pour l'âge.");
                scanner.next();
            }
        }

        User user = userRepository.modifierUtilisateur(id, name, age);
        System.out.println("Utilisateur mis à jour : " + user.getId() + " " + user.getName() + " " + user.getAge());
    }

    public void supprimerUtilisateur() {
        int id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Entrez l'ID de l'utilisateur : ");

            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                scanner.nextLine(); // Consommer la ligne restante après l'entrée de l'entier

                if (id > 0) {
                    validInput = true;
                } else {
                    System.out.println("L'ID doit être un nombre entier positif. Veuillez réessayer.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier pour l'ID.");
                scanner.next(); // Ignorer l'entrée non valide pour éviter une boucle infinie
            }
        }
        boolean deletedUser = userRepository.supprimerUtilisateur(id);
        if (deletedUser) {
            System.out.println("L'utilisateur a bien été supprimé");
        } else {
            System.out.println("Aucun utilisateur trouvé avec l'ID " + id + ".");
        }

    }

    public void utilisateurListe() {
        List<User> users = userRepository.utilisateursListe();
        for (User user : users) {
            System.out.printf("ID: %d, Name: %s, Age: %d%n",
                    user.getId(), user.getName(), user.getAge());
        }
    }

    public void utilisateurInformations() {
        System.out.println("found user first :");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<User> userOptional = userRepository.getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.printf("ID: %d, Name: %s, Age: %d\n",
                    user.getId(), user.getName(), user.getAge());
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    public Optional<User> foundUser(int id) {
        return userRepository.getUserById(id);
    }
}
//    public void addUser(){
//        System.out.println("name");
//        String name = scanner.nextLine();
//
//        System.out.println("age");
//        int age = scanner.nextInt();
//        scanner.nextLine();
//
//        User user = userRepository.addUser(name, age);
//        System.out.println(user.getName());
//
//    }