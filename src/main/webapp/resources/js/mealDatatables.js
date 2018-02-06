var datatableApi;
var ajaxUrl = "ajax/meals/"
$(function () {
    datatableApi = $("#meal-datatable").DataTable(
        {
            "paging": true,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            // "order": [
            //     [
            //         0,
            //         "asc"
            //     ]
            // ]
        });
    makeEditable();
});

$(document).ready(function () {
    $('#filterForm').submit(function () {
        filter();
        return false;
    });
});
function filter() {
    $.ajax({
        url: ajaxUrl+"filter",
        type: "POST",
        data: $("#filterForm").serialize(),
        success: function (data) {
            datatableApi.clear();
            $.each(data, function (key, item) {
                datatableApi.row.add(item);
            });
            datatableApi.draw();
        }
    });
}
function save() {
    var form = $('#newDataForm');
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $('#modal').modal('hide');
            filter();
            successNoty('Saved');
        }
    });
}