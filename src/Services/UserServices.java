package Services;
import Repository.UserRepository;
import com.entity.User;

import java.util.List;
import java.util.Scanner;
import java.util.Optional;

public class UserServices {
    private UserRepository userRepository;
    Scanner scanner = new Scanner(System.in);

    public UserServices() {
        this.userRepository = new UserRepository();
    }


    public void displayUserList() {
        List<User> users = userRepository.userList();
        for (User user : users) {
            System.out.printf("ID: %d, Name: %s, Age: %d%n",
                    user.getId(), user.getName(), user.getAge());
        }
    }

    public void addUser(){
        System.out.println("name");
        String name = scanner.nextLine();

        System.out.println("age");
        int age = scanner.nextInt();
        scanner.nextLine();

        User user = userRepository.addUser(name, age);
        System.out.println(user.getName());

    }

    public void deleteUSer(){
        System.out.println("user id :");
        int id = scanner.nextInt();
        scanner.nextLine();
        boolean deletedUser = userRepository.deleteUSer(id);
        if(deletedUser){
            System.out.println("User has been deleted succesfully");
        }else{
            System.out.println("User not found");
        }

    }

//    public boolean foudUser(){
//        System.out.println("user id :");
//        int id = scanner.nextInt();
//        boolean userExists = userRepository.foundUser(id);
//        System.out.println(userExists);
//        return userExists;
//    }

    public void editUser(){

        System.out.println("found user first :");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("set name");
        String name = scanner.nextLine();

        System.out.println("set age");
        int age = scanner.nextInt();

        User user = userRepository.editUser(id , name, age);
        System.out.println(user.getId() + " " + user.getName() + " " + user.getAge());
    }

    public void getUser(){
        System.out.println("found user first :");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<User> userOptional = userRepository.getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.printf("ID: %d, Name: %s, Age: %d",
                    user.getId(), user.getName(), user.getAge());
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    public Optional<User> foundUser(int id){
        return userRepository.getUserById(id);
    }
}
