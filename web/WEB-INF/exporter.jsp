<%@ page import="ligueBaseball.GestionLigue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="ligueBaseball.TupleEquipe" %>
<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Importation XML - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.inc.jsp" />
<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Exportation XML</h1>
    <p>Exporter les données d'une équipe</p>
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

      <form class="form-horizontal" action="Export" method="post">
        <div class="form-group">
          <label for="equipeId" class="control-label col-xs-3">Équipe </label>
          <div class="col-xs-9">
            <select class="form-control required" name="equipeId" id="equipeId">
              <option value="" disabled="disabled" selected="selected" >Choisissez l'équipe à exporter</option>
              <%
                GestionLigue gestionLigue = new GestionLigue();
                List equipes = gestionLigue.getEquipes();

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
          <div class="col-xs-offset-3 col-xs-9 text-right">
            <button type="submit" name="exporter" class="btn btn-success">
              Exporter <span class="glyphicon glyphicon-export" aria-hidden="true"></span> </button>
          </div>
        </div>
      </form>
    </div>

  </div>

  <jsp:include page="/WEB-INF/includes/footer.inc.jsp" />

</div> <!-- /container -->

<script>

  $(document).on('change', '.btn-file :file', function() {
    var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
  });

  $(document).ready( function() {
    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
      $(".form-control").val(label);
    });
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
</script>

</body>
</html>
