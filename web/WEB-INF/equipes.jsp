<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page  import="java.util.*,java.text.*,ligueBaseball.*" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="javax.xml.bind.annotation.adapters.HexBinaryAdapter" %>
<html>
<head>
  <title>Équipes - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Gestion des équipes</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterEquipe" role="button">Ajouter un équipe &raquo;</a></p>

  </div>
</div>

<div class="container">

  <div class="row">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <%
    GestionLigue gestionLigue = new GestionLigue();
    List equipes = gestionLigue.getEquipes();
    MessageDigest md = MessageDigest.getInstance("MD5");
    HexBinaryAdapter hexAdapter = new HexBinaryAdapter();
  %>

  <div class="row">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent" id="listeEquipes">
      <thead>
      <tr>
        <th>N°</th>
        <th>Équipe</th>
        <th><span class="glyphicon glyphicon-cog"></span></th>
      </tr>
      </thead>
      <tbody>

      <%
        if ( !equipes.isEmpty() ) {

          ListIterator it = equipes.listIterator();
          while (it.hasNext())
          {
            TupleEquipe tupleEquipe = (TupleEquipe) it.next();
      %>
      <tr>
        <td><%= tupleEquipe.idEquipe %></td>
        <td><%= tupleEquipe.nomEquipe %></td>
        <td>

          <%
            // Generation du token
            String token = (hexAdapter).marshal(md.digest(Integer.toString(tupleEquipe.idEquipe).getBytes()));
          %>

          <a class="nounderline supprimer" href="javascript:;"
             data-id="<%= tupleEquipe.idEquipe %>"
             data-token="<%= token %>"
             data-action="supprimerEquipe">
            <span class="glyphicon glyphicon-trash"></span>
            Supprimer
          </a>
        </td>
      </tr>
      <%
          }

        }
      %>


      </tbody>
    </table>
  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script src="/resources/js/suppression.js"></script>

<script>
  $(function () {
    $('#listeEquipes').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "aoColumnDefs": [
        {"bSortable": false, "aTargets": [2]},
        {"width": "90px", "aTargets": [2]}
      ],
      "iDisplayLength": 50,
      "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
