<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Arbitres - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Gestion des arbitres</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterEquipe" role="button">Ajouter un arbitre &raquo;</a></p>

  </div>
</div>

<div class="container">
  <!-- Example row of columns -->
  <div class="row">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent" id="listeEquipes">
      <thead>
      <tr>
        <th>N°</th>
        <th>Prénom</th>
        <th>Nom</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td>1</td>
        <td>Charles</td>
        <td>Provencher</td>
        <td>
          <a class="supprimer_joueur" href="javascript:;">
            <span style=" font-size: 1.3em;" class="glyphicon glyphicon-trash"></span>
            Supprimer
          </a>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script src="resources/js/main.js"></script>

<script>
  $(function () {

    $('#listeEquipes').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "bProcessing": true,
      "bServerSide": true,
      "iDisplayLength": 50,
      "sAjaxSource": "datatable_load/equipes.datatable.php",
      "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
