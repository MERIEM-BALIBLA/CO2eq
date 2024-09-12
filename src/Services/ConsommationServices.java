package Services;

import Repository.ConsommationRepository;
import Repository.UserRepository;
import com.entity.Consommation;
import com.entity.Sous_class.Alimentation;
import com.entity.Sous_class.Logement;
import com.entity.Sous_class.Transport;
import com.entity.User;

import java.util.List;
import java.util.Optional;

public class ConsommationServices {

    ConsommationRepository consommationRepository = new ConsommationRepository();
    UserRepository userRepository = new UserRepository();

    public List<Consommation> userConsommation(int id){
        return consommationRepository.consommationList(id);
    }

    public Optional<Consommation> addConsommation(Consommation consommation, int user_id){
        Optional<User> user = userRepository.getUserById(user_id);
        consommation.setUser(user.get());
        return consommationRepository.insertConsummation(consommation);
    }

/*
    public double consomationTotal(int user_id) {
        ConsommationRepository consommationRepository = new ConsommationRepository();
        List<Consommation> consomationList = consommationRepository;
        double totalImpact = 0;
//        double totalImpact = consomationList
//                .stream()
//                .mapToDouble(Consomation::calculerImpact)
//                .sum();
//        System.out.println("Total impact pour l'utilisateur " + user.getId() + ": " + totalImpact);
        return totalImpact;
    }
*/
public double consommerTotalAlimentation(int user_id) {
    List<Consommation> consommationList = consommationRepository.alimentationList(user_id);
    double totalImpact = consommationList
            .stream()
            .filter(c -> c instanceof Alimentation) // Filtrer pour être sûr de traiter uniquement les Alimentation
            .mapToDouble(c -> ((Alimentation) c).calculerImpact())
            .sum();
    System.out.println("Total impact alimentation pour l'utilisateur " + user_id + ": " + totalImpact);
    return totalImpact;
}

    public double consommerTotalLogement(int user_id) {
        List<Consommation> consommationList = consommationRepository.logementList(user_id);
        double totalImpact = consommationList
                .stream()
                .filter(c -> c instanceof Logement) // Filtrer pour être sûr de traiter uniquement les Alimentation
                .mapToDouble(c -> ((Logement) c).calculerImpact())
                .sum();
        System.out.println("Total impact alimentation pour l'utilisateur " + user_id + ": " + totalImpact);
        return totalImpact;
    }

}
