package ligueBaseball;

import java.util.List;

/**
 * Created by vonziper on 2015-04-04.
 */
public class TupleArbitre {
    public int idArbitre;
    public String prenom;
    public String nom;
    public int nbMatchs;

    public TupleArbitre() {

    }

    public TupleArbitre(int idArbitre, String prenom, String nom, int nbMatchs) {
        this.idArbitre = idArbitre;
        this.prenom = prenom;
        this.nom = nom;
        this.nbMatchs = nbMatchs;
    }
}


