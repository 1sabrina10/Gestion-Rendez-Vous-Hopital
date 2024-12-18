package test;
import java.io.Serializable;

public class Docteur implements Serializable {
    private String nom;
    private String prenom;
    private String specialite;
    private String disponibilite;
    private String salleConsultation;

    public Docteur(String nom, String prenom, String specialite, String disponibilite, String salleConsultation) {
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.disponibilite = disponibilite;
        this.salleConsultation = salleConsultation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getSalleConsultation() {
        return salleConsultation;
    }

    public void setSalleConsultation(String salleConsultation) {
        this.salleConsultation = salleConsultation;
    }

    public String[] toArray() {
        return new String[]{nom, prenom, specialite, disponibilite, salleConsultation};
    }

}
