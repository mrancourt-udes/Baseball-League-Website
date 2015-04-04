<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleEquipe" %>
<%@ page import="ligueBaseball.TupleArbitre" %>
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
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterEquipe" role="button">Ajouter un arbitre &raquo;</a></p>

  </div>
</div>

<div class="container">
  <!-- Example row of columns -->
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
        GestionLigue gestionLigue = new GestionLigue();
        List joueurs = gestionLigue.getArbitres();

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
          <a class="nounderline" href="javascript:;">
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

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

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
