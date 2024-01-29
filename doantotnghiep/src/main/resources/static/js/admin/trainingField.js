$(document).ready(() => {


    let deleteTrainingFieldId = -1;

    //validate
    const validator = $('#trainingField-form').validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            'fieldName': {
                required: true,
                maxlength: 100
            },
            'description': {
                maxlength: 255
            }
        },
        messages: {
            'fieldName': {
                required: "Tên lĩnh vực bắt buộc",
                maxlength: "Tên lĩnh vực dài tối đa 100 ký tự"
            },
            'description': {
                maxlength: "Mô tả tối đa 255 ký tự"
            }
        }
    });

    //open modal to create a new trainingField
    $('.create-trainingField-btn').click(function () {
        $('#trainingField-modal #save-trainingField-btn').attr("action-type", "CREATE");
        $('#trainingField-modal').modal('show');
    });

    //open modal to update a trainingField
    $('.update-trainingField-btn').click(async function (event) {
        //call api lên java để lấy dữ liệu
        const updateTrainingFieldId = parseInt($(event.currentTarget).attr("trainingField-id"));
        let trainingField = null;
        await $.ajax({
            url: "/api/v1/training-fields/" + updateTrainingFieldId,
            type: "GET",
            success: function (data) {
                trainingField = data;
            },
            error: function (err) {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    showHideTransition: 'fade',
                    position: 'top-right',
                    loader: false,
                    bgColor: '#FF0000'
                });
            }
        });

        if (!trainingField) {
            $.toast({
                heading: 'Lỗi',
                text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                icon: 'error',
                showHideTransition: 'fade',
                position: 'top-right',
                loader: false,
                bgColor: '#FF0000'
            });
            return;
        }
        //đổ dữ liệu vào form
        $('#trainingField-form #fieldName').val(trainingField.fieldName);
        $('#trainingField-form #description').val(trainingField.description);

        $('#trainingField-modal #save-trainingField-btn').attr('action-type', "UPDATE");
        $('#trainingField-modal #save-trainingField-btn').attr("trainingField-id", updateTrainingFieldId);
        $('#trainingField-modal').modal("show");
    });

    //create or update a trainingField
    $('#save-trainingField-btn').click(function (event) {
        //validate
        const isValidForm = $('#trainingField-form').valid();
        if (!isValidForm) {
            return;
        }
        const actionType = $(event.currentTarget).attr("action-type");
        const trainingFieldId = $(event.currentTarget).attr("trainingField-id");
        //lấy dữ liệu từ form
        const formTrainingFieldData = $('#trainingField-form').serializeArray();
        if (!formTrainingFieldData || formTrainingFieldData.length === 0) {
            return;
        }
        //chuyển dữ liệu từ object sang json
        const trainingFieldRequestBody = {};
        for (let i = 0; i < formTrainingFieldData.length; i++) {
            trainingFieldRequestBody[formTrainingFieldData[i].name] = formTrainingFieldData[i].value;
        }
        const method = actionType === "CREATE" ? "POST" : "PUT";
        if (method === "PUT") {
            trainingFieldRequestBody["id"] = trainingFieldId;
        }
        //call api lên backend
        $.ajax({
            url: "/api/v1/training-fields",
            type: method,
            data: JSON.stringify(trainingFieldRequestBody),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $.toast({
                    text: (method === "POST" ? "Tạo mới " : "Cập nhật ") + "thành công lĩnh vực đào tạo!",
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                });
                setTimeout(() => {
                    location.reload();
                }, 3000);
            },
            error: function (error) {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                });
            }
        });
        $("#trainingField-modal #save-trainingField-btn").attr("action-type", "");
        $('#trainingField-modal #save-trainingField-btn').attr("trainingField-id", "");
    });

    //show modal to delete a trainingField
    $('.delete-trainingField-btn').click(function (event) {
        deleteTrainingFieldId = parseInt($(event.currentTarget).attr("trainingField-id"));
        $('#trainingField-delete-modal').modal('show');
    });
    //delete a trainingField
    $('#delete-trainingField-btn').click(function () {
        $.ajax({
            url: "/api/v1/training-fields/" + deleteTrainingFieldId,
            type: "DELETE",
            success: function (data) {
                $.toast({
                    text: "Xóa lĩnh vực đào tạo thành công!",
                    icon: 'success',
                    position: 'top-right',
                    bgColor: '#4CAF50'
                });
                setTimeout(() => {
                    location.reload();
                }, 3000);
            },
            error: function (err) {
                $.toast({
                    text: "Đã có lỗi xảy ra, vui lòng thử lại sau!",
                    icon: 'error',
                    position: 'top-right',
                    bgColor: '#FF0000'
                });
            }
        });
    });

    //reset form
    $('#trainingField-modal').on('hidden.bs.modal', function () {
        $("#trainingField-modal #save-trainingField-btn").attr("action-type", "");
        $('#trainingField-modal #save-trainingField-btn').attr("trainingField-id", "");
        $('#trainingField-form').trigger("reset");
        $('#trainingField-form input').removeClass("error");
        validator.resetForm();
    });



});