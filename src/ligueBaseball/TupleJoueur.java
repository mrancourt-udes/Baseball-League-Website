package ligueBaseball;

import java.sql.Date;

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

    public TupleJoueur(int idJoueur, int idEquipe, String nom, String prenom) {
        this.idJoueur = idJoueur;
        this.idEquipe = idEquipe;
        this.nom = nom;
        this.prenom = prenom;
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

}