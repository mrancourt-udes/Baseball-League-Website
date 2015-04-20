package ligueBaseball;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by vonziper on 2015-04-04.
 */
public class TupleJoueur {
    public int idJoueur;
    public int idEquipe;
    public int numero;
    public String nom;
    public String prenom;
    public String nomEquipe;
    public Date dateDebut;
    public Date dateFin;

    public TupleJoueur() {

    }

    public TupleJoueur(String nom, String prenom, int numero,
                       String nomEquipe, String dateDebut)
    throws LigueException{
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.nomEquipe = nomEquipe;

        try {

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            java.util.Date parsed = format.parse(dateDebut);
            Date date = new Date(parsed.getTime());

            this.dateDebut = date;
        } catch (ParseException e) {
            throw new LigueException("Format de date invalide");
        }
    }

    public TupleJoueur(int idJoueur, int idEquipe, int numero,
                       String nom, String prenom, String nomEquipe,
                       Date dateDebut, Date dateFin) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.idJoueur = idJoueur;
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getDateDebut() {
        return dateDebut == null ? "—" : dateDebut.toString();
    }

    public String getDateFin() {
        return dateFin == null ? "—" : dateFin.toString();
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;

    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setDateDebut(Date numero) {
        this.dateDebut = dateDebut;
    }
}