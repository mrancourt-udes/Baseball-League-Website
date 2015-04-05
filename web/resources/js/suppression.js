/**
 * Created by vonziper on 2015-04-05.
 */
// Modal de confirmtion de suppression
$('#suppressionModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button qui a triggered le modal
    var suppressionItem = button.data('suppressionitem') // Recuperation des data-* attributs

    var modal = $(this)

    modal.data('relatedTarget', button);
    modal.find('.suppressionItem').text(suppressionItem)
});

// Bind la fonction pour supprimer un item
$("#suppressionModal").on('click', "#confirmSuppression", function () {

    var modal = $("#suppressionModal");
    var button = modal.data('relatedTarget');
    var modalUserMessages = $("#modalUserMessages")

    // Recuperation des parametres
    var id = button.data("id");
    var token = button.data("token");
    var action = button.data("action");

    $.ajax({
        url: 'Suppression',
        type: "POST",
        dataType: 'json',
        data: ({
            id : id,
            token : token,
            action : action
        }),
        success: function(response) {

            toastr[response.status](response.msg);

            if (response.status == "success") {
                button.closest('tr').fadeOut();
                modal.modal('hide');
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            msg = "Status: " + textStatus + "<br>Error: " + errorThrown;
            afficherMessage(modalUserMessages, msg, "danger");
        }
    });
});
