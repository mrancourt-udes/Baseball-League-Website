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
  <title>Matchs - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Affecter des arbitres à un match</h1>
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
          GestionLigue gestionLigue = new GestionLigue();
          List equipes = gestionLigue.getEquipes();
        %>

        <div class="form-group">
          <label for="equipeLocale" class="control-label col-xs-3">Équipe local </label>
          <div class="col-xs-9">
            <select class="form-control required" name="equipeLocale" id="equipeLocale">
              <option value="" disabled="disabled" selected="selected" >Choisissez</option>
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
              <option value="" disabled="disabled" selected="selected" >Choisissez</option>
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

        <%
          List arbitres = gestionLigue.getArbitres();
        %>
        <span data-bind='foreach: arbitres' style="display:block;">
          <div class="form-group">

            <label for="equipeVisiteur" class="control-label col-xs-3">
              <!-- ko if: $index() == 0  -->
              Arbitres
              <!-- /ko -->
            </label>
            <div class="col-xs-9">
              <div class="input-group">
                <select class="form-control" name="arbitres" data-bind="attr: {css: $index() == 0 ? 'required' : '' }">
                  <option value="" disabled="disabled" selected="selected">Choisissez</option>
                  <%
                    if ( !equipes.isEmpty() ) {
                      ListIterator it = arbitres.listIterator();
                      while (it.hasNext())
                      {
                        TupleArbitre tupleArbitre = (TupleArbitre) it.next();
                  %>
                  <option value="<%= tupleArbitre.idArbitre %>"><%= tupleArbitre.prenom + " " + tupleArbitre.nom %></option>
                  <%
                      }
                    }
                  %>
                </select>
                <span class="input-group-btn">
                    <button data-bind="click: $root.retirerArbitre, css: $parent.arbitres().length == 1 ? 'disabled' : ''" class="btn btn-default" type="button">-</button>
                    <button data-bind="click: $root.ajouterArbitre, css: $parent.arbitres().length >= 4 ? 'disabled' : ''" class="btn btn-default" type="button">+</button>
                </span>
              </div>
            </div>
          </div>
        </span>

        <div class="form-group">
          <div class="col-xs-offset-3 col-xs-9 text-right">
            <button type="submit" name="affecterArbitres" class="btn btn-success">Soumettre</button>
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

  // Class représentant un arbitre
  function Arbitre() {
    var self = this;
    self.arbitreId = ko.observable();
    self.prenom = ko.observable();
    self.nom = ko.observable();
  }

  // viewModel pour la gestion et l'initialisation des inscriptions
  function ArbitresViewModel() {

    // Modeles
    var self = this;

    // Initialisation. Création d'un inscription initial
    self.arbitres = ko.observableArray([new Arbitre()]);

    // Ajout d'un inscription
    self.ajouterArbitre = function () {
      self.arbitres.push(new Arbitre());
    }

    // Suppression d'un inscription
    self.retirerArbitre = function (arbitre) {
      self.arbitres.remove(arbitre);
    }

  }

  // Activation de knockout.js
  ko.applyBindings(new ArbitresViewModel());

</script>

</body>
</html>
