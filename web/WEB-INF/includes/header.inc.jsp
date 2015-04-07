<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand hidden-lg hidden-md hidden-sm" href="javascript:;">Gestionnaire de ligues</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">

        <li>
          <a href="Routes?home">
            <span class="glyphicon glyphicon-home"></span>
            Accueil</a>
        </li>

        <li class="dropdown">
          <a href="/Routes?page=accueil" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
            <span class="glyphicon glyphicon glyphicon-menu-hamburger"></span>
            Menu <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/Routes?page=ajouterEquipe">
              <span>Ajouter une équipe</span>
            </a></li>
            <li><a href="/Routes?page=equipes">
              <span>Liste des équipes</span>
            </a></li>
            <li><a href="/Routes?page=equipes">
              <span>Supprimer une équipe</span>
            </a></li>

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

        <li class="visible-xs">
          <a href="Routes?page=importer">
            <span class="glyphicon glyphicon-import" aria-hidden="true"></span> Importer
          </a>
        </li>

        <li class="visible-xs">
          <a href="Routes?page=exporter">
            <span class="glyphicon glyphicon-export" aria-hidden="true"></span> Exporter
          </a>
        </li>

        <li>
         <a href="Logout">
          <span class="glyphicon glyphicon-log-out"></span> Sortir
          </a>
        </li>

      </ul>

      <div class="navbar-form navbar-right hidden-xs" role="form">
        <a class="btn btn-success" href="Routes?page=importer" role="button">
          <span class="glyphicon glyphicon-import" aria-hidden="true"></span> Importer
        </a>
        <a class="btn btn-success" href="Routes?page=exporter" role="button">
          <span class="glyphicon glyphicon-export" aria-hidden="true"></span> Exporter
        </a>
      </div>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="clear margT40"></div>