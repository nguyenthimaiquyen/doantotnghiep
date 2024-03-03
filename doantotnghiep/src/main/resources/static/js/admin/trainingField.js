$(document).ready(() => {
    let chosenFile = [];
    const defaultImg = "/images/course/default.png";

    let deleteTrainingFieldId = -1;

    //validate
    const validator = $('#training-field-form').validate({
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
    $('.create-training-field-btn').click(function () {
        $('#training-field-img-show').attr('src', defaultImg);
        $('#training-field-modal #save-training-field-btn').attr("action-type", "CREATE");
        $('#training-field-modal').modal('show');
    });
    
    //open modal to update a trainingField
    $('.update-training-field-btn').click(async function (event) {
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
        $('#training-field-form #fieldName').val(trainingField.fieldName);
        $('#training-field-form #description').val(trainingField.description);

        $('#training-field-modal #save-training-field-btn').attr('action-type', "UPDATE");
        $('#training-field-modal #save-training-field-btn').attr("trainingField-id", updateTrainingFieldId);
        $('#training-field-img-show').attr('src', '/api/v1/files/' + trainingField.imageUrl);
        $('#training-field-modal').modal("show");
    });

    $('#training-field-img-show').click(() => {
        $('#training-field-image').click();
    });

    $('#training-field-image').change(function (event) {
        const tempFiles = event.target.files;
        if (!tempFiles || tempFiles.length === 0) {
            return;
        }
        chosenFile = tempFiles[0];
        const imageBlob = new Blob([chosenFile], {type: chosenFile.type});
        const imageUrl = URL.createObjectURL(imageBlob);
        $('#training-field-img-show').attr("src", imageUrl);
    });

    //create or update a trainingField
    $('#save-training-field-btn').click(function (event) {
        //validate
        const isValidForm = $('#training-field-form').valid();
        if (!isValidForm) {
            return;
        }
        const actionType = $(event.currentTarget).attr("action-type");
        const trainingFieldId = $(event.currentTarget).attr("trainingField-id");
        //lấy dữ liệu từ form
        const formTrainingFieldData = $('#training-field-form').serializeArray();
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
        //tạo 1 blob từ dữ liệu JSONs
        const jsonBlob = new Blob([JSON.stringify(trainingFieldRequestBody)], {
            type: "application/json; charset=utf-8"
        });
        const formData = new FormData();
        formData.append("image", chosenFile, chosenFile.name);
        formData.append("trainingFieldRequest", jsonBlob);
        //call api lên backend
        $.ajax({
            url: "/api/v1/training-fields",
            type: method,
            data: formData,
            // contentType: "application/json; charset=utf-8",
            contentType: false, //NEEDED, DON'T OMIT THIS
            processData: false, //NEEDED, DON'T OMIT THIS
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
        $("#training-field-modal #save-training-field-btn").attr("action-type", "");
        $('#training-field-modal #save-training-field-btn').attr("trainingField-id", "");
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
    $('#training-field-modal').on('hidden.bs.modal', function () {
        $("#training-field-modal #save-training-field-btn").attr("action-type", "");
        $('#training-field-modal #save-training-field-btn').attr("trainingField-id", "");
        $('#training-field-form').trigger("reset");
        $('#training-field-form input').removeClass("error");
        validator.resetForm();
    });



});