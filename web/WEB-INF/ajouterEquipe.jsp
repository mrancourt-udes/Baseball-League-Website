<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleTerrain" %>
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
    <h1>Ajouter une équipe</h1>
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

        <%
          GestionLigue gestionLigue = new GestionLigue();
        %>

        <div class="form-group">
          <label for="equipe" class="control-label col-xs-3">Équipe </label>
          <div class="col-xs-9">
            <input class="form-control required" name="equipe" id="equipe" placeholder="Nom de l'équipe">
            </input>
          </div>
        </div>


        <%
          List terrains = gestionLigue.getTerrains();
        %>
        <div class="form-group">

          <label for="terrain" class="control-label col-xs-3">
            Terrain
          </label>
          <div class="col-xs-9">
            <div class="input-group">
              <select class="form-control" name="terrain" data-bind="disable: $root.terrains().length > 0, value: $root.terrains().length > 0 ? '' : ''">
                <option value="" disabled="disabled" selected="selected" data-bind="selected: $root.terrains().length > 0">Choisissez</option>
                <%
                  if ( !terrains.isEmpty() ) {
                    ListIterator it = terrains.listIterator();
                    while (it.hasNext())
                    {
                      TupleTerrain tupleTerrain = (TupleTerrain) it.next();
                %>
                <option value="<%= tupleTerrain.terrain %>"><%= tupleTerrain.terrain %></option>
                <%
                    }
                  }
                %>
              </select>
                <span class="input-group-btn">
                    <button data-bind="click: $root.ajouterTerrain, css: $root.terrains().length == 0 ? '' : 'disabled'" class="btn btn-default" type="button">+</button>
                </span>
            </div>
          </div>
        </div>


        <!-- ko if: $root.terrains().length > 0  -->
        <div class="form-group">
          <label for="terrain" class="control-label col-xs-3"></label>
          <div class="col-xs-9">
            <div class="input-group">
              <input class="form-control required" name="terrain" id="terrain" placeholder="Nom du nouveau terrain">
              <span class="input-group-btn">
                <button data-bind="click: $root.retirerTerrain, css: $root.terrains().length > 0 ? '' : 'disabled'" class="btn btn-default" type="button">-</button>
              </span>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label for="adresse" class="control-label col-xs-3">Adresse </label>
          <div class="col-xs-9">
            <textarea class="form-control required" name="equipe" id="adresse" placeholder="Adresse du terrain"></textarea>
          </div>
        </div>
        <!-- /ko -->

        <div class="form-group">
          <div class="col-xs-offset-3 col-xs-9 text-right">
            <button type="submit" name="ajouterEquipe" class="btn btn-success">Soumettre</button>
          </div>
        </div>
      </form>
    </div>

  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script>
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
  function Terrain() {
    var self = this;
    self.nom = ko.observable();
  }

  // viewModel pour la gestion et l'initialisation des inscriptions
  function TerrainsViewModel() {

    // Modeles
    var self = this;

    // Initialisation. Création d'un inscription initial
    self.terrains = ko.observableArray();

    // Ajout d'un inscription
    self.ajouterTerrain = function () {
      self.terrains.push(new Terrain());
    }

    // Suppression d'un inscription
    self.retirerTerrain = function () {
      self.terrains.removeAll();
    }

  }

  // Activation de knockout.js
  ko.applyBindings(new TerrainsViewModel());

</script>

</body>
</html>
