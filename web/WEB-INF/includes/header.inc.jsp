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
          <a href="/Routes?page=accueil" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Menu <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">

            <%--Equipe--%>
            <li><a href="/Routes?page=ajouterEquipe">
              <span class="text-warning">Ajouter une équipe</span>
            </a></li>
            <li><a href="Equipes">
              <span class="text-warning">Liste des équipes</span>
            </a></li>
            <li><a href="Equipes">
              <span class="text-success">Supprimer une équipe</span>
            </a></li>

            <%--Joueurs--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterJoueur">
              <span class="text-warning">Ajouter un joueur</span>
            </a></li>
            <li><a href="Joueurs">
              <span class="text-warning">Liste de joueurs</span>
            </a></li>
            <li><a href="Joueurs">
              <span class="text-success
              ">Supprimer un joueur</span>
            </a></li>

            <%--Match--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterMatch">
              <span class="text-success">Ajouter un match</span>
            </a></li>
            <li><a href="/Routes?page=ajouterResultatMatch">
              <span class="text-warning">Entrer le résultat d’un match</span>
            </a></li>
            <li><a href="Matchs">
              <span class="text-warning">Résultats des matchs</span>
            </a></li>

            <%--Arbitre--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterArbitre">
              <span class="text-success">Ajouter un arbitre</span>
            </a></li>
            <li><a href="Arbitres">
              <span class="text-success">Liste de arbitres</span>
            </a></li>
            <li><a href="/Routes?page=affecterArbitres">
              <span class="text-warning">Affecter des arbitres à un match</span>
            </a></li>

          </ul>

        </li>
        <li><a href="Logout">Sortir</a></li>
      </ul>
    </div>
  </div>
</nav>
<div style="clear: both; height: 40px;"></div>