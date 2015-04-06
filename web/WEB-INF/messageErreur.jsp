<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%

    if(session.getAttribute("listeMessageSucces") != null) {
        ListIterator it = ((List) session.
                getAttribute("listeMessageSucces")).listIterator();
        while (it.hasNext())
        {
        %>
        <div class="alert alert-success" role="alert">
            <%= it.next() %>
        </div>
        <%
        }
            session.removeAttribute("listeMessageSucces");

        } else if(session.getAttribute("listeMessageErreur") != null) {
        ListIterator it = ((List) session.
                getAttribute("listeMessageErreur")).listIterator();
        while (it.hasNext())
        {
        %>
        <div class="alert alert-danger" role="alert">
            <%= it.next() %>
        </div>
        <%
        }
                session.removeAttribute("listeMessageSucces");
            }

    // affichage de la liste des messages d'erreur
    if (request.getAttribute("listeMessageErreur") != null) {
        ListIterator it = ((List) request.
                getAttribute("listeMessageErreur")).listIterator();
        while (it.hasNext())
        {
%>
<div class="alert alert-danger" role="alert">
    <%= it.next() %>
</div>
<%
    }
} else if (request.getAttribute("listeMessageSucces") != null) {
    ListIterator it = ((List) request.
            getAttribute("listeMessageSucces")).listIterator();
    while (it.hasNext())
    {
%>
<div class="alert alert-success" role="alert">
    <%= it.next() %>
</div>
<%
        }
    }
%>