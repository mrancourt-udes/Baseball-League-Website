package ligueBaseball;

import java.util.List;

/**
 * Created by vonziper on 2015-04-04.
 */
public class TupleEquipe {
    public int idEquipe;
    public String nomEquipe;
    public String terrain;
    public String nbJoueurs;
    public List<TupleJoueur> joueurs;
    public String joueursStr;


    public TupleEquipe() {

    }

    public TupleEquipe(int idEquipe, String nomEquipe, String terrain,
                       String nbJoueurs, String joueursStr) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.terrain = terrain;
        this.nbJoueurs = nbJoueurs;
        this.joueursStr = joueursStr;
    }

    public TupleEquipe(int idEquipe, String nomEquipe, List<TupleJoueur> joueurs) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.joueurs = joueurs;
    }

}


