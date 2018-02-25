var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}


function showDateTimePicker(dateTimeInput) {
    $(dateTimeInput).datetimepicker({
        format: 'Y-m-d H:i:s',
        lang: 'ru'
    });
    $(dateTimeInput).datetimepicker("show");

}

function datePicker(dateInput) {
    $(dateInput).datetimepicker({
        format: 'Y-m-d',
        timepicker: false,
        lang: 'ru'
    });
    $(dateInput).datetimepicker("show");

}

function timePicker(timeInput) {
    $(timeInput).datetimepicker({
        format: 'H:i',
        datepicker: false,
        lang: 'ru'
    });
    $(timeInput).datetimepicker("show");

}

$(function () {

    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (date, type, row) {
                    if (type === "display") {
                        return date.substr(0, 16).replace("T", " ");
                    }
                    return date;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "Delete",
                "orderable": false,
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            if (data.exceed) {
                $(row).addClass("exceeded");
            } else {
                $(row).addClass("normal");
            }
        },
        "initComplete": makeEditable
    });
    makeEditable();
});