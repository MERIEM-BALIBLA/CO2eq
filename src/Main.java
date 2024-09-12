import Repository.ConsommationRepository;
import Repository.UserRepository;
import Services.ConsommationServices;
import Services.UserServices;
import com.entity.Consommation;
import com.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
            Menu menu = new Menu();

//            menu.ajoutConsommation();

//      menu.userConsommationList();

       menu.alimentation();


        menu.calculSomImpact();

    }


}
