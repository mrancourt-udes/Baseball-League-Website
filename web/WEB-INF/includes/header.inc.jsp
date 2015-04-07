<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="/Routes?page=accueil">Ligue de Baseball</a>
      <ul class="nav navbar-nav">
        <li class="dropdown">
          <a href="/Routes?page=accueil" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
            <span class="glyphicon glyphicon glyphicon-menu-hamburger"></span>
            Menu <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">

            <%--Equipe--%>
            <li><a href="/Routes?page=ajouterEquipe">
              <span>Ajouter une équipe</span>
            </a></li>
            <li><a href="/Routes?page=equipes">
              <span>Liste des équipes</span>
            </a></li>
            <li><a href="/Routes?page=equipes">
              <span>Supprimer une équipe</span>
            </a></li>

            <%--Joueurs--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterJoueur">
              <span>Ajouter un joueur</span>
            </a></li>
            <li><a href="/Routes?page=joueurs">
              <span>Liste de joueurs</span>
            </a></li>
            <li><a href="/Routes?page=joueurs">
              <span>Supprimer un joueur</span>
            </a></li>

            <%--Match--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterMatch">
              <span>Ajouter un match</span>
            </a></li>
            <li><a href="/Routes?page=ajouterResultatMatch">
              <span c>Entrer le résultat d’un match</span>
            </a></li>
            <li><a href="/Routes?page=matchs">
              <span>Résultats des matchs</span>
            </a></li>

            <%--Arbitre--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterArbitre">
              <span>Ajouter un arbitre</span>
            </a></li>
            <li><a href="/Routes?page=arbitres">
              <span>Liste de arbitres</span>
            </a></li>
            <li><a href="/Routes?page=affecterArbitres">
              <span>Affecter des arbitres à un match</span>
            </a></li>

          </ul>

        </li>
        <li><a href="Logout">
          <span class="glyphicon glyphicon-log-out"></span>
          Sortir</a></li>
      </ul>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
      <div class="navbar-form navbar-right" role="form">
        <a class="btn btn-success" href="Routes?page=importer" role="button">
          <span class="glyphicon glyphicon-import" aria-hidden="true"></span> Importer </a>
        <a class="btn btn-success" href="Routes?page=exporter" role="button">
          <span class="glyphicon glyphicon-export" aria-hidden="true"></span> Exporter </a>
      </div>
    </div><!--/.navbar-collapse -->
  </div>
</nav>
<div class="clear margT40"></div>