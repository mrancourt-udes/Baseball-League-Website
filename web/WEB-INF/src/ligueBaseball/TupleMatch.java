package ligueBaseball;

import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by vonziper on 2015-04-04.
 */
public class TupleMatch {
    public int idMatch;
    public String equipeLocal;
    public String equipeVisiteur;
    public String pointsLocal;
    public String pointsVisiteur;
    public String terrain;
    public Date date;
    public String heure;
    public String arbitres;

    public TupleMatch() {

    }

    public TupleMatch(int idMatch, String equipeLocal, String equipeVisiteur,
                      String pointsLocal, String pointsVisiteur, Date date,
                      String heure, String terrain, String arbitres) {
        this.idMatch = idMatch;
        this.equipeLocal = equipeLocal;
        this.equipeVisiteur = equipeVisiteur;
        this.pointsLocal = pointsLocal;
        this.pointsVisiteur = pointsVisiteur;
        this.arbitres = arbitres;
        this.date = date;
        this.heure = heure;
        this.terrain = terrain;
    }

}