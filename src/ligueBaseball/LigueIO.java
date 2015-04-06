package ligueBaseball;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vonziper on 2015-04-06.
 */
public class LigueIO {

    public void importer(String fichier) throws SQLException, LigueException {

        GestionLigue gestionLigue = new GestionLigue();
        List joueurs = new LinkedList<>();
        String nomEquipe = null;

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


        int nbJoueurs;
        String nom, prenom, dateDebut;
        int numero;

        try {
            // Ouvre le doc
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new FileInputStream(fichier);
            doc = db.parse(is);
        } catch (FileNotFoundException e) {
            throw new LigueException("Erreur lors du chargement du fichier : Fichier introuvale");
        } catch (Exception e) {
            e.printStackTrace();
        }


        NodeList listEquipe = doc.getElementsByTagName("equipe");
        if (listEquipe.getLength() > 0) {
            Element element = (Element)listEquipe.item(0);
            nomEquipe = element.getAttribute("nom");
        }

        // Load de la liste des joueurs
        NodeList listJoueurs = doc.getElementsByTagName("joueur");
        nbJoueurs = listJoueurs.getLength();

        for (int i = 0; i < nbJoueurs; i++)
        {
            Element element = (Element)listJoueurs.item(i);

            // Assigne les variables
            nom = element.getAttribute("nom");
            prenom = element.getAttribute("prenom");
            numero = Integer.parseInt(element.getAttribute("numero"));
            dateDebut = element.getAttribute("datedebut");

            joueurs.add(new TupleJoueur(
                    nom,
                    prenom,
                    numero,
                    nomEquipe,
                    dateDebut
            ));
        }

        gestionLigue.insererEquipe(joueurs);

    }

    public void exporter(int equipeId) {

        // Recuperer la liste de joueur

        // Sauvegarder le fichier dans le dossier XML

        // Forcer le download du fichier par le browser


    }

}
