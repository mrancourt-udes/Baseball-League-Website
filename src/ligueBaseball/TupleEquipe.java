package ligueBaseball;

import java.util.List;

/**
 * Created by vonziper on 2015-04-04.
 */
public class TupleEquipe {
    public int idEquipe;
    public String nomEquipe;
    public List<TupleJoueur> joueurs;


    public TupleEquipe() {

    }

    public TupleEquipe(int idEquipe, String nomEquipe) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
    }

    public TupleEquipe(int idEquipe, String nomEquipe, List<TupleJoueur> joueurs) {
        this.idEquipe = idEquipe;
        this.nomEquipe = nomEquipe;
        this.joueurs = joueurs;
    }

}


