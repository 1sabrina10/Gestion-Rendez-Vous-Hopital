package test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
   public static void main(String[] args) {
    try {

        HospitalReservationImpl obj = new HospitalReservationImpl();


        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("HospitalService", obj);

        System.out.println("Service RMI démarré.");
    } catch (Exception e) {
        System.err.println("Erreur lors du démarrage du serveur RMI : " + e.getMessage());
        e.printStackTrace();
    }
}

}
