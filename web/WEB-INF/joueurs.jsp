<%@ page import="ligueBaseball.TupleEquipe" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.List" %>
<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="ligueBaseball.TupleJoueur" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="javax.xml.bind.annotation.adapters.HexBinaryAdapter" %>
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
    <h1>Gestion des joueurs</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
    <p><a class="btn btn-primary btn-lg" href="/Routes?page=ajouterJoueur" role="button">Ajouter un joueur &raquo;</a></p>

  </div>
</div>

<div class="container">

  <div class="row" id="messagesContainer">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <%

    // TODO : Rendre l'affichage non O(n^2)

    GestionLigue gestionLigue = new GestionLigue();
    List equipes = gestionLigue.getEquipes();
    List joueurs = gestionLigue.getJoueurs();
    MessageDigest md = MessageDigest.getInstance("MD5");
    HexBinaryAdapter hexAdapter = new HexBinaryAdapter();

    if ( !equipes.isEmpty() ) {

      ListIterator it = equipes.listIterator();
      while (it.hasNext())
      {
        TupleEquipe tupleEquipe = (TupleEquipe) it.next();
  %>
  <div class="row">
    <h2><%= tupleEquipe.nomEquipe %></h2>
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover statut_parent listeJoueurs">
      <thead>
      <tr>
        <th>N°</th>
        <th>Prénom</th>
        <th>Nom</th>
        <th><span class="glyphicon glyphicon-cog"></span></th>
      </tr>
      </thead>
      <tbody>

      <%
        ListIterator itJ = joueurs.listIterator();
        while (itJ.hasNext()) {
          TupleJoueur tupleJoueur = (TupleJoueur) itJ.next();
          if (tupleEquipe.idEquipe == tupleJoueur.idEquipe) {

            // Generation du token
            String token = (hexAdapter).marshal(md.digest(Integer.toString(tupleJoueur.idJoueur).getBytes()));

      %>
      <tr>
        <td><%= tupleJoueur.idJoueur %></td>
        <td><%= tupleJoueur.nom %></td>
        <td><%= tupleJoueur.prenom %></td>
        <td>
          <a class="nounderline" href="javascript:;"
             data-toggle="modal" data-target="#suppressionModal"
             data-suppressionitem="<%= tupleJoueur.prenom + " " + tupleJoueur.nom %>"
             data-id="<%= tupleJoueur.idJoueur %>" data-token="<%= token %>"
             data-action="supprimerJoueur">
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
  <%
    }
  %>
  <%
    }
  %>

  <!-- Modal -->
  <div class="modal fade" id="suppressionModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="modalLabel">
            <span class="glyphicon glyphicon-warning-sign text-danger"></span>
            Supression du joueur <span class="suppressionItem"></span></h4>
        </div>
        <div class="modal-body">

          <div id="modalUserMessages"></div>

          <div class="checkbox">
            <label>
              <input class="optSuppression" type="checkbox" name="supprimerJoueurParticipe">
              Supprimer les commentaires associés à <span class="suppressionItem"></span>.
            </label>
          </div>
          <div class="checkbox">
            <label>
              <input class="optSuppression" type="checkbox" name="supprimerJoueurFaitPartie">
              Supprimer les liens des équipes dont <span class="suppressionItem"></span> fait parti.
            </label>
          </div>

          <p>
            Souhaitez-vous vraiment supprimer le joueur <span class="suppressionItem"></span>.
            Cette action est irréversible.
          </p>

        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
          <button type="button" class="btn btn-primary" id="confirmSuppression">Supprimer</button>
        </div>
      </div>
    </div>
  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script>

  $('#suppressionModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button qui a triggered le modal
    var suppressionItem = button.data('suppressionitem') // Recuperation des data-* attributs
    var modal = $(this)
    var modalUserMessages = $("#modalUserMessages")

    // Suppression des vieux messages
    modalUserMessages.empty();
    // Reinitialisation des checkboxes
    $("input:checkbox.optSuppression").prop('checked', false);

    modal.data('relatedTarget', button);
    modal.find('.suppressionItem').text(suppressionItem)
  });

  // Bind la fonction pour modifier le statut d'une demande
  $("#suppressionModal").on('click', "#confirmSuppression", function () {

    var modal = $("#suppressionModal");
    var button = modal.data('relatedTarget');
    var modalUserMessages = $("#modalUserMessages")
    var msg;

    // Recuperation des parametres
    var id = button.data("id");
    var token = button.data("token");
    var action = button.data("action");
    var supprimerJoueurParticipe = $( "input[name='supprimerJoueurParticipe']" ).prop('checked');
    var supprimerJoueurFaitPartie = $( "input[name='supprimerJoueurFaitPartie']" ).prop('checked');

    // Suppression des vieux messages
    modalUserMessages.empty();

    $.ajax({
      url: 'Suppression',
      type: "POST",
      dataType: 'json',
      data: ({
        id : id,
        token : token,
        action : action,
        supprimerJoueurParticipe : supprimerJoueurParticipe,
        supprimerJoueurFaitPartie : supprimerJoueurFaitPartie
      }),
      success: function(response) {

        if (response.status == "success") {
          toastr[response.status](response.msg);
          button.closest('tr').fadeOut();
          modal.modal('hide');
        } else {
          afficherMessage(modalUserMessages, response.msg, response.status);
        }

      },
      error: function(XMLHttpRequest, textStatus, errorThrown) {
        msg = "Status: " + textStatus + "<br>Error: " + errorThrown;
        afficherMessage(modalUserMessages, msg, "danger");
      }
    });
  });

  function afficherMessage(parent, msg, type) {
    parent.append($("<div/>", {class : 'alert alert-danger', role : 'alert'})
                    .append($("<span/>", {class : 'glyphicon glyphicon-exclamation-sign', 'aria-hidden' : true}))
                    .append($("<span/>", {html : " " + msg}))
    )
  }

  $(function () {

    $('.listeJoueurs').dataTable({
      "oLanguage": {
        "sUrl": "resources/js/localization/datatable_fr.txt"
      },
      "aoColumnDefs": [
        {"bSortable": false, "aTargets": [3]},
        {"width": "90px", "aTargets": [3]}
      ],
      "iDisplayLength": 10,
      "aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "Tous"]]
    })

  });
</script>

</body>
</html>
