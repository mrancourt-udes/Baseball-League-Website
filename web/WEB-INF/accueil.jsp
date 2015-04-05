<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
  <title>Accueil - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>

<jsp:include page="/WEB-INF/includes/header.inc.jsp" />

<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Accueil</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p>&nbsp;</p>
  </div>
</div>


<div class="container">

  <div class="row">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <div class="row">

    <h2>Équipes</h2>

    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="/Routes?page=ajouterEquipe">Ajouter une équipe</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="Equipes">Liste des équipes</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="Equipes">Supprimer une équipe</a>
    </p>
  </div>

  <div class="row">

    <h2>Joueurs</h2>

    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="/Routes?page=ajouterJoueur">Ajouter un joueur</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="Joueurs">Liste des joueurs</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="Joueurs">Supprimer un joueur</a>
    </p>
  </div>

  <div class="row">

    <h2>Matchs</h2>

    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="/Routes?page=ajouterMatch">Ajouter un match</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="/Routes?page=ajouterResultatMatch">Entrer le résultat d’un match</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="Matchs">Résultats des matchs</a>
    </p>
  </div>

  <div class="row">

    <h2>Arbitres</h2>

    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="/Routes?page=ajouterArbitre">Ajouter un arbitre</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="Arbitres">Liste de arbitres</a>
    </p>
    <p class="col-md-4">
      <a class="btn btn-primary btn-lg btn-block" href="/Routes?page=affecterArbitres">Affecter des arbitres à un match</a>
    </p>
  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

</body>
</html>
