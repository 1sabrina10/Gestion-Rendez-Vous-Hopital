package test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface HospitalReservation extends Remote {
    boolean ajouterPatient(String name, String age, String email, String tel) throws RemoteException;
List<Docteur> trouverDocteursParSpecialite(String specialite) throws RemoteException;

public boolean bookAppointment(int patientId, int doctorId, String date, String appointmentTime) throws RemoteException;
List<String> getReservedSlots(int doctorId) throws RemoteException;
 List<String> getAvailableSlots(int doctorId) throws RemoteException;
int findPatientIdByName(String patientName) throws RemoteException;
    int findDoctorIdByName(String doctorName, String doctorFirstName) throws RemoteException;
}


