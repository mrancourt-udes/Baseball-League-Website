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
      <a class="navbar-brand" href="">Ligue de Baseball</a>
      <ul class="nav navbar-nav">
        <li class="dropdown">
          <a href="/" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Menu <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">

            <%--Equipe--%>
            <li><a href="/Routes?page=ajouterEquipe">Ajouter une équipe</a></li>
            <li><a href="/Routes?page=equipes">Liste des équipes</a></li>
            <li><a href="/Routes?page=equipes">Supprimer une équipe</a></li>

            <%--Joueurs--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterJoueur">Ajouter un joueur</a></li>
            <li><a href="/Routes?page=joueurs">Liste de joueurs</a></li>
            <li><a href="/Routes?page=joueurs">Supprimer un joueur</a></li>

            <%--Match--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterMatch">Ajouter un match</a></li>
            <li><a href="/Routes?page=ajouterResultatMatch">Entrer le résultat d’un match</a></li>
            <li><a href="/Routes?page=matchs">Résultats des matchs</a></li>

            <%--Arbitre--%>
            <li class="divider"></li>
            <li><a href="/Routes?page=ajouterArbitre">Ajouter un arbitre</a></li>
            <li><a href="/Routes?page=arbitres">Liste de arbitres</a></li>
            <li><a href="/Routes?page=affecterArbitres">Affecter des arbitres à un match</a></li>

          </ul>

        </li>
        <li><a href="Logout">Sortir</a></li>
      </ul>
    </div>
  </div>
</nav>