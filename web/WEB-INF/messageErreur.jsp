<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
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