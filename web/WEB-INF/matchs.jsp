<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleMatch" %>
<%@ page import="java.util.List" %>
<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterMatch" role="button">Ajouter un match &raquo;</a></p>

  </div>
</div>

<%
  GestionLigue gestionLigue = (GestionLigue) session.getAttribute("ligue");
%>

<div class="container">

  <div class="row">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <%
    Date aPartirDe = null;
    String aPartirDeStr = null;

    if (session.getAttribute("aPartirDe") != null) {
      aPartirDe = (Date) session.getAttribute("aPartirDe");
      aPartirDeStr = new SimpleDateFormat("dd-MM-yyyy").format(aPartirDe).toString();
      session.removeAttribute("aPartirDe");
    }
  %>

  <div class="row">
    <form class="navbar-form navbar-right match-filter" role="form" action="FormHandler" method="post">
      <%
        if (aPartirDeStr != null) {
      %>
      <a href="Routes?page=matchs" class="btn btn-default glyphicon glyphicon-ban-circle"></a>
      <%
        }
      %>
      <div class="form-group">
        <input name="aPartirDe" id="aPartirDe" type="text" placeholder="À partir du"
          <%= aPartirDeStr != null ? "value='"+aPartirDeStr+"'" : "" %> class="form-control required">
      </div>
      <button type="submit" name="filtrerMatchDate" class="btn btn-success">Filtrer</button>
    </form>
  </div>

  <!-- Example row of columns -->
  <div class="row">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent" id="listeMatchs">
      <thead>
      <tr>
        <th>N°</th>
        <th>Local</th>
        <th>Visiteur</th>
        <th>Terrain</th>
        <th>Date</th>
        <th>Heure</th>
        <th>Score</th>
        <th>Arbitres</th>
        <th><span class="glyphicon glyphicon-cog"></span></th>
      </tr>
      </thead>
      <tbody>

      <%
        List matchs = gestionLigue.getResultatsDate(aPartirDe);

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
        <td><%= tupleMatch.terrain %></td>
        <td><%= tupleMatch.date %></td>
        <td><%= tupleMatch.heure %></td>
        <td><%= tupleMatch.pointsLocal %> - <%= tupleMatch.pointsVisiteur %></td>
        <td><%= tupleMatch.arbitres %></td>
        <td>
          <a class="nounderline" href="Routes?page=ajouterResultatMatch">
            <span class="glyphicon glyphicon-stats"></span>
            Score
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

  $('#aPartirDe').datepicker({
    todayHighlight: true,
    weekStart: 0,
    language : 'fr',
    startView : 'decade',
    format : 'dd-mm-yyyy'
  });

  $(function () {

    $('#listeMatchs').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "aoColumnDefs": [
        {"bSortable": false, "aTargets": [8]},
      ],
      "iDisplayLength": 5,
      "aLengthMenu": [[5, 10, 25, 50, -1], [5, 10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
