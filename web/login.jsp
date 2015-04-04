<%--
  Created by IntelliJ IDEA.
  User: vonziper
  Date: 2015-04-02
  Time: 6:15 PM
--%>
<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
  <title>Connexion - Système de gestion de ligue de Baseball</title>
  <jsp:include page="/WEB-INF/includes/html_header.inc.jsp" />
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="/Login">Système de gestion de ligue de Baseball</a>
    </div>
  </div>
</nav>

<!-- Main jumbotron for a primary marketing message or call to action -->
<div class="jumbotron">
  <div class="container">
    <h1>Connexion</h1>
    <p>Gérez votre ligue de baseball efficacement.</p>
  </div>
</div>

<div class="container">

  <div class="row">
    <%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
  </div>

  <div class="row">
    <!-- Example row of columns -->
    <div class="col-md-6 col-md-offset-3">

      <form class="form-horizontal" action="WEB-INF/accueil.jsp">
        <div class="form-group">
          <label for="inputUserId" class="control-label col-xs-3">User Id </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="inputUserId" name="inputUserId" placeholder="User Id">
          </div>
        </div>
        <div class="form-group">
          <label for="inputPassword" class="control-label col-xs-3">Mot de passe </label>
          <div class="col-xs-9">
            <input type="password" class="form-control required" id="inputPassword" name="inputPassword" placeholder="Mot de passe">
          </div>
        </div>
        <div class="form-group">
          <label for="selectServer" class="control-label col-xs-3">Serveur </label>
          <div class="col-xs-9">
            <select class="form-control required" name="server" id="selectServer">
              <option value="postgres">Postgres</option>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="inputAdresseIP" class="control-label col-xs-3">Adresse IP</label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="inputAdresseIP" name="inputAdresseIP" placeholder="Adresse IP">
          </div>
        </div>
        <div class="form-group">
          <label for="inputDatabase" class="control-label col-xs-3">Base de données </label>
          <div class="col-xs-9">
            <input type="text" class="form-control required" id="inputDatabase" name="inputDatabase" placeholder="Base de données">
          </div>
        </div>
        <div class="form-group">
          <div class="col-xs-offset-3 col-xs-9">
            <button type="submit" class="btn btn-success">Connexion</button>
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
</script>

</body>
</html>
