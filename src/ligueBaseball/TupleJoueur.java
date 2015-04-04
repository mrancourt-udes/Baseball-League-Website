package ligueBaseball;

/**
 * Created by vonziper on 2015-04-04.
 */
public class TupleJoueur {
    public int idJoueur;
    public int idEquipe;
    public String nom;
    public String prenom;


    public TupleJoueur() {

    }

    public TupleJoueur(int idJoueur, int idEquipe, String nom, String prenom) {
        this.idJoueur = idJoueur;
        this.idEquipe = idEquipe;
        this.nom = nom;
        this.prenom = prenom;
    }
}