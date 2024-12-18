package test;

import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static test.DatabaseConnection.getConnection;

public class HospitalReservationImpl extends UnicastRemoteObject implements HospitalReservation {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public HospitalReservationImpl() throws RemoteException {
        super();
    }


    @Override
    public boolean ajouterPatient(String name, String age, String email, String tel) throws RemoteException {
        String query = "INSERT INTO patient (name, age, email, tel) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setString(3, email);
            stmt.setString(4, tel);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient ajouté avec succès !");
                return true;  // Ajout réussi
            } else {
                System.out.println("Aucun patient ajouté.");
                return false;  // Aucun ajout effectué
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du patient : " + e.getMessage());
            e.printStackTrace();
            return false;  // En cas d'exception
        }
    }
@Override
public List<Docteur> trouverDocteursParSpecialite(String specialite) throws RemoteException {
    List<Docteur> docteurs = new ArrayList<>();
    String requete = "SELECT prenom, nom, specialite, disponibilite, salle_consultation FROM docteurs WHERE specialite = ?";

    try (Connection connexion = getConnection();
         PreparedStatement stmt = connexion.prepareStatement(requete)) {

        stmt.setString(1, specialite);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Docteur docteur = new Docteur(
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("specialite"),
                        rs.getString("disponibilite"),
                        rs.getString("salle_consultation")
                );
                docteurs.add(docteur);  // Ajouter le docteur à la liste
            }

            if (docteurs.isEmpty()) {
                System.out.println("Aucun médecin trouvé pour la spécialité : " + specialite);
            }
        }
    } catch (SQLException e) {
        throw new RemoteException("Erreur lors de la recherche des médecins.", e);
    }

    return docteurs;
}
@Override
public boolean bookAppointment(int patientId, int doctorId, String date, String appointmentTime) throws RemoteException {
    String query = "INSERT INTO rendezvous (patient_id, docteur_id, date, heure) VALUES (?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, patientId);
        stmt.setInt(2, doctorId);
        stmt.setString(3, date);
        stmt.setString(4, appointmentTime);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        throw new RemoteException("Erreur lors de la réservation du rendez-vous.", e);
    }
}
@Override
public List<String> getReservedSlots(int doctorId) throws RemoteException {
    List<String> reservedSlots = new ArrayList<>();
    String query = "SELECT CONCAT(date, ' ', heure) AS slot FROM rendezvous WHERE docteur_id = ?";
    try (Connection connection = getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, doctorId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            reservedSlots.add(rs.getString("slot"));
        }
    } catch (SQLException e) {
        throw new RemoteException("Erreur lors de la récupération des créneaux réservés.", e);
    }
    return reservedSlots;
}
@Override
public List<String> getAvailableSlots(int doctorId) throws RemoteException {
    List<String> availableSlots = new ArrayList<>();
    String[] possibleSlots = {
        "2024-12-17 09:00:00",
        "2024-12-23 10:00:00",
        "2024-12-24 11:00:00",
        "2024-12-25 14:00:00",
            "2024-12-25 14:00:00"
    };

    for (String slot : possibleSlots) {
        String query = "SELECT COUNT(*) AS count FROM rendezvous WHERE docteur_id = ? AND CONCAT(date, ' ', heure) = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, doctorId);
            stmt.setString(2, slot);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("count") == 0) {
                availableSlots.add(slot);
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des créneaux disponibles.", e);
        }
    }
    return availableSlots;
}

public int findPatientIdByName(String patientName) {
        String sql = "SELECT id FROM patient WHERE name = ?"; // Requête SQL pour trouver l'ID du patient
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patientName);  // Lier le nom du patient à la requête
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Retourner l'ID du patient
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Retourner -1 si le patient n'est pas trouvé
    }

    // Recherche d'un médecin par son nom et prénom
    public int findDoctorIdByName(String doctorName, String doctorFirstName) {
        String sql = "SELECT id FROM docteurs WHERE prenom = ? AND nom = ?"; // Requête SQL pour trouver l'ID du médecin
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctorName);  // Lier le nom du médecin
            stmt.setString(2, doctorFirstName);  // Lier le prénom du médecin
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Retourner l'ID du médecin
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Retourner -1 si le médecin n'est pas trouvé
    }




}