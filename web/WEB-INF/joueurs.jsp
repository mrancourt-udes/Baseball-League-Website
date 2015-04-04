<%@ page import="ligueBaseball.TupleEquipe" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.List" %>
<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="ligueBaseball.TupleJoueur" %>
<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Joueurs - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Gestion des joueurs</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterEquipe" role="button">Ajouter un joueur &raquo;</a></p>

  </div>
</div>

<div class="container">

  <%

    // TODO : Rendre l'affichage non O(n^2)

    GestionLigue gestionLigue = new GestionLigue();
    List equipes = gestionLigue.getEquipes();
    List joueurs = gestionLigue.getJoueurs();

    if ( !equipes.isEmpty() ) {

      ListIterator it = equipes.listIterator();
      while (it.hasNext())
      {
        TupleEquipe tupleEquipe = (TupleEquipe) it.next();
  %>
  <div class="row">
    <h2><%= tupleEquipe.nomEquipe %></h2>
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent listeJoueurs">
      <thead>
      <tr>
        <th>N°</th>
        <th>Prénom</th>
        <th>Nom</th>
        <th><span class="glyphicon glyphicon-cog"></span></th>
      </tr>
      </thead>
      <tbody>

      <%
        ListIterator itJ = joueurs.listIterator();
        while (itJ.hasNext()) {
          TupleJoueur tupleJoueur = (TupleJoueur) itJ.next();
          if (tupleEquipe.idEquipe == tupleJoueur.idEquipe) {
          %>
          <tr>
            <td><%= tupleJoueur.idJoueur %></td>
            <td><%= tupleJoueur.nom %></td>
            <td><%= tupleJoueur.prenom %></td>
            <td>
              <a class="nounderline" href="javascript:;">
                <span class="glyphicon glyphicon-trash"></span>
                Supprimer
              </a>
            </td>
          </tr>
          <%
          }
        }
      %>


      </tbody>
    </table>
  </div>
  <%
    }
  %>
  <%
    }
  %>




  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script>
  $(function () {

    $('.listeJoueurs').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "aoColumnDefs": [
        {"bSortable": false, "aTargets": [3]},
        {"width": "90px", "aTargets": [3]}
      ],
      "iDisplayLength": 10,
      "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
