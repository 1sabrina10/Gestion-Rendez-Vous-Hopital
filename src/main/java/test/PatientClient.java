package test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class PatientClient {

    public static void main(String[] args) {
        // Lancer l'interface graphique Swing
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Acceuil().setVisible(true);
            }
        });
    }

    // Méthode pour envoyer les données au serveur RMI pour ajouter un patient

    public static boolean envoyerPatient(String name, String age, String email, String tel) {
        try {
            System.out.println("Tentative d'ajout du patient : " + name + ", " + age + ", " + email + ", " + tel);

            // Connexion au registre RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HospitalReservation service = (HospitalReservation) registry.lookup("HospitalService");

            // Appel de la méthode ajouterPatient
            boolean result = service.ajouterPatient(name, age, email, tel);

            if (result) {
                System.out.println("Patient ajouté avec succès !");
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Échec de l'ajout du patient. Veuillez réessayer.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'appel RMI.");
            e.printStackTrace();
            return false;
        }
    }



    // Méthode pour afficher les docteurs par spécialité
    public static void afficherDocteursParSpecialite(String specialite) {
        try {
            // Connexion au registre RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HospitalReservation service = (HospitalReservation) registry.lookup("HospitalService");

            // Appel de la méthode chercherDocteursParSpecialite
            List<Docteur> docteurs = service.trouverDocteursParSpecialite(specialite);

            // Vérifier si la liste des docteurs est vide
            if (docteurs.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Aucun médecin trouvé pour la spécialité : " + specialite,
                        "Résultats de recherche",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Afficher la liste des docteurs
                StringBuilder message = new StringBuilder();
                for (Docteur docteur : docteurs) {
                    message.append("Nom : ").append(docteur.getNom()).append(" - ")
                           .append("Prénom : ").append(docteur.getPrenom()).append("\n")
                           .append("Spécialité : ").append(docteur.getSpecialite()).append("\n")
                           .append("Disponibilité : ").append(docteur.getDisponibilite()).append("\n")
                           .append("Salle de consultation : ").append(docteur.getSalleConsultation()).append("\n\n");
                }
                JOptionPane.showMessageDialog(null, message.toString(), "Médecins trouvés", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des docteurs.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération des docteurs. Veuillez réessayer.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void gererRendezVous() {
         try {
            // Connexion au registre RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HospitalReservation service = (HospitalReservation) registry.lookup("HospitalService");

            // Menu principal
            String[] options = {"Réserver un rendez-vous", "Afficher créneaux réservés", "Afficher créneaux disponibles", "Quitter"};
            while (true) {
                String choix = (String) JOptionPane.showInputDialog(null,
                        "Choisissez une option :",
                        "Gestion des rendez-vous",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choix == null || choix.equals("Quitter")) {
                    break; // Quitter le menu
                }

                switch (choix) {
                    case "Réserver un rendez-vous":
                        // Réserver un rendez-vous
                        String patientName = JOptionPane.showInputDialog(null, "Entrez le nom du patient :", "Réserver un rendez-vous", JOptionPane.PLAIN_MESSAGE);
                        if (patientName == null || patientName.trim().isEmpty()) continue;

                        int patientId = service.findPatientIdByName(patientName);  // Recherche de l'ID du patient
                        if (patientId == -1) {
                            JOptionPane.showMessageDialog(null, "Patient non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        String doctorName = JOptionPane.showInputDialog(null, "Entrez le nom du médecin :", "Réserver un rendez-vous", JOptionPane.PLAIN_MESSAGE);
                        String doctorFirstName = JOptionPane.showInputDialog(null, "Entrez le prénom du médecin :", "Réserver un rendez-vous", JOptionPane.PLAIN_MESSAGE);
                        if (doctorName == null || doctorName.trim().isEmpty() || doctorFirstName == null || doctorFirstName.trim().isEmpty()) continue;

                        int doctorId = service.findDoctorIdByName(doctorName, doctorFirstName);  // Recherche de l'ID du médecin
                        if (doctorId == -1) {
                            JOptionPane.showMessageDialog(null, "Médecin non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        String date = JOptionPane.showInputDialog(null, "Entrez la date (yyyy-MM-dd) :", "Réserver un rendez-vous", JOptionPane.PLAIN_MESSAGE);
                        if (date == null || date.trim().isEmpty()) continue;

                        String time = JOptionPane.showInputDialog(null, "Entrez l'heure (HH:mm:ss) :", "Réserver un rendez-vous", JOptionPane.PLAIN_MESSAGE);
                        if (time == null || time.trim().isEmpty()) continue;

                        boolean success = service.bookAppointment(patientId, doctorId, date, time);
                        JOptionPane.showMessageDialog(null,
                                success ? "Rendez-vous réservé avec succès !" : "Échec de la réservation du rendez-vous.",
                                "Résultat",
                                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                        break;

                    case "Afficher créneaux réservés":
                        // Afficher les créneaux réservés
                        String doctorNameForSlots = JOptionPane.showInputDialog(null, "Entrez le nom du médecin :", "Afficher créneaux réservés", JOptionPane.PLAIN_MESSAGE);
                        String doctorFirstNameForSlots = JOptionPane.showInputDialog(null, "Entrez le prénom du médecin :", "Afficher créneaux réservés", JOptionPane.PLAIN_MESSAGE);
                        if (doctorNameForSlots == null || doctorNameForSlots.trim().isEmpty() || doctorFirstNameForSlots == null || doctorFirstNameForSlots.trim().isEmpty()) continue;

                        int doctorIdForSlots = service.findDoctorIdByName(doctorNameForSlots, doctorFirstNameForSlots);  // Recherche de l'ID du médecin
                        if (doctorIdForSlots == -1) {
                            JOptionPane.showMessageDialog(null, "Médecin non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        java.util.List<String> reservedSlots = service.getReservedSlots(doctorIdForSlots);
                        if (reservedSlots.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Aucun créneau réservé pour ce médecin.", "Informations", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Créneaux réservés : \n" + String.join("\n", reservedSlots), "Créneaux réservés", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case "Afficher créneaux disponibles":
                        // Afficher les créneaux disponibles
                        String doctorNameForAvailableSlots = JOptionPane.showInputDialog(null, "Entrez le nom du médecin :", "Afficher créneaux disponibles", JOptionPane.PLAIN_MESSAGE);
                        String doctorFirstNameForAvailableSlots = JOptionPane.showInputDialog(null, "Entrez le prénom du médecin :", "Afficher créneaux disponibles", JOptionPane.PLAIN_MESSAGE);
                        if (doctorNameForAvailableSlots == null || doctorNameForAvailableSlots.trim().isEmpty() || doctorFirstNameForAvailableSlots == null || doctorFirstNameForAvailableSlots.trim().isEmpty()) continue;

                        int doctorIdForAvailableSlots = service.findDoctorIdByName(doctorNameForAvailableSlots, doctorFirstNameForAvailableSlots);  // Recherche de l'ID du médecin
                        if (doctorIdForAvailableSlots == -1) {
                            JOptionPane.showMessageDialog(null, "Médecin non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }

                        java.util.List<String> availableSlots = service.getAvailableSlots(doctorIdForAvailableSlots);
                        if (availableSlots.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Aucun créneau disponible pour ce médecin.", "Informations", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Créneaux disponibles : \n" + String.join("\n", availableSlots), "Créneaux disponibles", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Option non reconnue.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    }