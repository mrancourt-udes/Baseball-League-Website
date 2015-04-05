/**
 * Created by vonziper on 2015-04-05.
 */
// Suppression
function supprimer(id, token, action, caller) {
    $.ajax({
        url: 'Suppression',
        type: "POST",
        dataType: 'json',
        data: ({id : id, token : token, action : action}),
        success: function(response) {
            toastr[response.status](response.msg);

            if (response.status == "success") {
                caller.closest('tr').fadeOut();
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            toastr["error"]("Status: " + textStatus + "<br>Error: " + errorThrown);
        }
    });
}

// Bind la fonction pour modifier le statut d'une demande
$(".table").on('click', ".supprimer", function () {
    var id = $(this).data("id");
    var token = $(this).data("token");
    var action = $(this).data("action");
    supprimer(id, token, action, $(this));
});