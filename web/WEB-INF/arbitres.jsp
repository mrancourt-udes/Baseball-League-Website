<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleEquipe" %>
<%@ page import="ligueBaseball.TupleArbitre" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="java.security.DigestException" %>
<%@ page import="javax.xml.bind.annotation.adapters.HexBinaryAdapter" %>
<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Arbitres - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Gestion des arbitres</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterArbitre" role="button">Ajouter un arbitre &raquo;</a></p>
  </div>
</div>

<div class="container">

  <div class="row">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <%
    GestionLigue gestionLigue = new GestionLigue();
    List joueurs = gestionLigue.getArbitres();
    MessageDigest md = MessageDigest.getInstance("MD5");
    HexBinaryAdapter hexAdapter = new HexBinaryAdapter();
  %>

  <div class="row">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent" id="listeArbitres">
      <thead>
      <tr>
        <th>N°</th>
        <th>Prénom</th>
        <th>Nom</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <%
        if ( !joueurs.isEmpty() ) {

          ListIterator it = joueurs.listIterator();
          while (it.hasNext())
          {
            TupleArbitre tupleArbitre = (TupleArbitre) it.next();
      %>
      <tr>
        <td><%= tupleArbitre.idArbitre %></td>
        <td><%= tupleArbitre.prenom %></td>
        <td><%= tupleArbitre.nom %></td>
        <td>

          <%
            // Generation du token
            String token = (hexAdapter).marshal(md.digest(Integer.toString(tupleArbitre.idArbitre).getBytes()));
          %>

          <a class="nounderline supprimer" href="javascript:;"
             data-toggle="modal" data-target="#suppressionModal"
             data-suppressionitem="<%= tupleArbitre.prenom + " " + tupleArbitre.nom %>"
             data-id="<%= tupleArbitre.idArbitre %>"
             data-token="<%= token %>"
             data-action="supprimerArbitre">
            <span class="glyphicon glyphicon-trash"></span>
            Supprimer
          </a>
        </td>
      </tr>
      <%
        }
      %>
      <%
        }
      %>
      </tbody>
    </table>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="suppressionModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="modalLabel">
            <span class="glyphicon glyphicon-warning-sign text-danger"></span>
            Supression de l'arbitre <span class="suppressionItem"></span></h4>
        </div>
        <div class="modal-body">
          <p>
            Souhaitez-vous vraiment supprimer l'arbitre <span class="suppressionItem"></span>.
            <br/>Cette action est irréversible.
          </p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
          <button type="button" class="btn btn-primary" id="confirmSuppression">Supprimer</button>
        </div>
      </div>
    </div>
  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script src="/resources/js/suppression.js"></script>

<script>

  $(function () {
    $('#listeArbitres').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "aoColumnDefs": [
        {"bSortable": false, "aTargets": [3]},
        {"width": "90px", "aTargets": [3]}
      ],
      "iDisplayLength": 50,
      "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
