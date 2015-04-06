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
  <title>Joueurs - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Ajouter un joueur</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" onclick="history.back()" role="button">&laquo; Retour </a></p>
  </div>
</div>

<div class="container">

  <div class="row">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <div class="row">
    <div class="col-md-6 col-md-offset-3">

      <form class="form-horizontal" action="FormHandler" method="post">
        <div class="form-group">
          <label for="prenom" class="control-label col-xs-3">Prénom </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="prenom" name="prenom" placeholder="Prénom">
          </div>
        </div>

        <div class="form-group">
          <label for="nom" class="control-label col-xs-3">Nom </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="nom" name="nom" placeholder="Nom">
          </div>
        </div>

        <div class="form-group">
          <label for="equipe" class="control-label col-xs-3">Équipe </label>
          <div class="col-xs-9">
            <select class="form-control required" name="equipe" id="equipe">
              <option value="" disabled="disabled" selected="selected" >Choisissez</option>
              <%
                GestionLigue gestionLigue = new GestionLigue();
                List equipes = gestionLigue.getEquipes();

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
          <label for="numero" class="control-label col-xs-3">Numéro </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required number" id="numero" name="numero" placeholder="Numéro de joueur">
          </div>
        </div>

        <div class="form-group">
          <label for="dateDebut" class="control-label col-xs-3">Date début </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="dateDebut" name="dateDebut" placeholder="Date de début">
          </div>
        </div>

        <div class="form-group">
          <div class="col-xs-offset-3 col-xs-9 text-right">
            <button type="submit" name="ajouterJoueur" class="btn btn-success">Soumettre</button>
          </div>
        </div>
      </form>
    </div>

  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script>

  $('#dateDebut').datepicker({
    todayHighlight: true,
    weekStart: 0,
    language : 'fr',
    startView : 'decade',
    format : 'dd-mm-yyyy'
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
