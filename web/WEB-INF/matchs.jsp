<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleMatch" %>
<%@ page import="java.util.List" %>
<%@ page import="ligueBaseball.GestionLigue" %>
<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Matchs - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Gestion des matchs</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterEquipe" role="button">Ajouter un match &raquo;</a></p>

  </div>
</div>

<div class="container">

  <!-- Example row of columns -->
  <div class="row">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent" id="listeMatchs">
      <thead>
      <tr>
        <th>N°</th>
        <th>Local</th>
        <th>visiteur</th>
        <th>Points Local</th>
        <th>Points Visiteur</th>
      </tr>
      </thead>
      <tbody>


      <%
        GestionLigue gestionLigue = new GestionLigue();
        List matchs = gestionLigue.getResultatsDate(null);

        if ( !matchs.isEmpty() ) {

          ListIterator it = matchs.listIterator();
          while (it.hasNext())
          {
            TupleMatch tupleMatch = (TupleMatch) it.next();
      %>
      <tr>
        <td><%= tupleMatch.idMatch %></td>
        <td><%= tupleMatch.equipeLocal %></td>
        <td><%= tupleMatch.equipeVisiteur %></td>
        <td><%= tupleMatch.pointsLocal %></td>
        <td><%= tupleMatch.pointsVisiteur %></td>
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

    $('#listeMatchs').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "iDisplayLength": 50,
      "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
