package test;


import java.util.Date;


public class RendezVous {


    private Long id;
    private String nomPatient;
    private String nomDocteur;
    private String prenomDocteur;
    private Date date;
    private String heure;

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getNomDocteur() {
        return nomDocteur;
    }

    public void setNomDocteur(String nomDocteur) {
        this.nomDocteur = nomDocteur;
    }

    public String getPrenomDocteur() {
        return prenomDocteur;
    }

    public void setPrenomDocteur(String prenomDocteur) {
        this.prenomDocteur = prenomDocteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
     @Override
    public String toString() {
        return "RendezVous [nomPatient=" + nomPatient + ", nomDocteur=" + nomDocteur + ", prenomDocteur="
                + prenomDocteur + ", date=" + date + ", heure=" + heure + "]";
    }
}

