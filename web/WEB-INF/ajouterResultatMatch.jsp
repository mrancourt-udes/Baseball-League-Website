<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleEquipe" %>
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
    <h1>Entrer le résultat d'un match</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" onclick="history.back()" role="button">&laquo; Retour </a></p>
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

  <div class="row">
    <div class="col-md-6 col-md-offset-3">
      <form class="form-horizontal" action="FormHandler" method="post">

        <div class="form-group">
          <label for="matchDate" class="control-label col-xs-3">Date </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="matchDate" name="matchDate" placeholder="Date du match">
          </div>
        </div>

        <div class="form-group">
          <label for="matchHeure" class="control-label col-xs-3">Heure </label>
          <div class="col-xs-9 input-group bootstrap-timepicker">
            <input id="matchHeure" name="matchHeure" type="text" class="form-control required">
            <span class="input-group-addon"><i class="glyphicon glyphicon-time"></i></span>
          </div>
        </div>

        <%
          List equipes = gestionLigue.getEquipes();
        %>

        <div class="form-group">
          <label for="equipeLocale" class="control-label col-xs-3">Équipe local </label>
          <div class="col-xs-9">
            <select class="form-control required" name="equipeLocale" id="equipeLocale">
              <option value="" disabled="disabled" selected="selected" >Choisissez l'équipe locale</option>
              <%
                if ( !equipes.isEmpty() ) {
                  ListIterator it = equipes.listIterator();
                  while (it.hasNext())
                  {
                    TupleEquipe tupleEquipe = (TupleEquipe) it.next();
              %>
              <option value="<%= tupleEquipe.nomEquipe %>"><%= tupleEquipe.nomEquipe %></option>
              <%
                  }
                }
              %>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label for="equipeVisiteur" class="control-label col-xs-3">Équipe visiteur </label>
          <div class="col-xs-9">
            <select class="form-control required" name="equipeVisiteur" id="equipeVisiteur">
              <option value="" disabled="disabled" selected="selected" >Choisissez l'équipe visiteur</option>
              <%
                if ( !equipes.isEmpty() ) {
                  ListIterator it = equipes.listIterator();
                  while (it.hasNext())
                  {
                    TupleEquipe tupleEquipe = (TupleEquipe) it.next();
              %>
              <option value="<%= tupleEquipe.nomEquipe %>"><%= tupleEquipe.nomEquipe %></option>
              <%
                  }
                }
              %>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label for="pointsLocal" class="control-label col-xs-3">Points local </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required number" id="pointsLocal" name="pointsLocal" placeholder="Score de l'équipe locale">
          </div>
        </div>

        <div class="form-group">
          <label for="pointsVisiteur" class="control-label col-xs-3">Points visiteur </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required number" id="pointsVisiteur" name="pointsVisiteur" placeholder="Score de l'équipe visiteur">
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-offset-3 col-xs-9 text-right">
            <button type="submit" name="ajouterResultatMatch" class="btn btn-success">Soumettre</button>
          </div>
        </div>
      </form>
    </div>

  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script>

  $('#matchDate').datepicker({
    todayHighlight: true,
    weekStart: 0,
    language : 'fr',
    startView : 'decade',
    format : 'dd-mm-yyyy'
  });

  $('#matchHeure').timepicker({
    showMeridian : false
  });

  $('form').validate({
    highlight: function(element) {
      $(element).closest('.form-group').addClass('has-error');
    },
    unhighlight: function(element) {
      $(element).closest('.form-group').removeClass('has-error');
    },
    errorElement: 'span',
    errorClass: 'help-block',
    errorPlacement: function(error, element) {
      if(element.parent('.input-group').length) {
        error.insertAfter(element.parent());
      } else {
        error.insertAfter(element);
      }
    }
  });
</script>

</body>
</html>
