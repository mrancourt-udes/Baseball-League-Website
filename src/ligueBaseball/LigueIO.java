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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

    public void exporter(int equipeId) throws LigueException, SQLException {
        GestionLigue gestionLigue = new GestionLigue();
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        TupleEquipe equipe;
        List<TupleJoueur> listeJoueurs;

        // Recuperer la liste de joueur
        equipe = gestionLigue.getEquipe(equipeId);
        listeJoueurs = equipe.joueurs;

        // Sauvegarder le fichier dans le dossier XML
            try {
                icBuilder = icFactory.newDocumentBuilder();
                Document doc = icBuilder.newDocument();
                Element mainRootElement = doc.createElementNS("http://crunchify.com/CrunchifyCreateXMLDOM", "Companies");
                doc.appendChild(mainRootElement);

                // append child elements to root element
                mainRootElement.setAttribute("equipe", equipe.nomEquipe + equipe.idEquipe);

                TupleJoueur joueur;
                for(int i = 0; i < listeJoueurs.size(); ++i){
                    joueur = listeJoueurs.get(i);
                    mainRootElement.appendChild(getNodeJoueur(doc, joueur));
                }

                // output DOM XML to console
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult console = new StreamResult(System.out);
                transformer.transform(source, console);

                System.out.println("\nXML DOM Created Successfully..");

            } catch (Exception e) {
                e.printStackTrace();
            }

        // Forcer le download du fichier par le browser



    }

    private static Node getNodeJoueur(Document doc, TupleJoueur joueur) {
        Element nodeJoueur = doc.createElement("joueur");
        nodeJoueur.setAttribute("nom", joueur.nom);
        nodeJoueur.setAttribute("prenom", joueur.prenom);
        nodeJoueur.setAttribute("numero", String.valueOf(joueur.numero));
        nodeJoueur.setAttribute("datedebut", String.valueOf(joueur.dateDebut));
        return nodeJoueur;

    }

}
