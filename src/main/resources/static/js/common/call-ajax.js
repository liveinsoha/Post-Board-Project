function callAjax(method, url, data) {
//주의할 점은 다음과 같이 Content-type을 설정하지 않을 경우  `application/x-www-form-urlencoded`로 기본 설정 되어 서버로 전송

    $.ajax({
        method: method,
        url: url,
        data: data,

        success: function (data) {
            console.log(data);
            alert(data.message)
            location.reload();
        },
        error: function (data) {
            if(data.status == 401){
            console.log("401");
                const returnUrl = window.location.href;
                location.href = '/blog/login?returnUrl='+returnUrl;
                return false;
            }
            if(data.status == 403){
            console.log("403");
                const returnUrl = window.location.href;
                location.href = '/blog/login?returnUrl='+returnUrl;
                return false;
            }
            const response = data.responseJSON;
            console.log(response);
            alert(response.message);
            location.reload();
        }
    });
}

function callAjaxNoAlert(method, url, data){

    $.ajax({
        method: method,
        url: url,
        data: data,
        success: function (data) {
            console.log(data);
            location.reload();
        },
        error: function (data) {
            if(data.status == 401){
                const returnUrl = window.location.href;
                location.href = '/account/login?returnUrl='+returnUrl;
                return false;
            }
            if(data.status == 403){
                const returnUrl = window.location.href;
                location.href = '/account/login?returnUrl='+returnUrl;
                return false;
            }
            const response = data.responseJSON;
            console.log(response);
            alert(response.message);
            location.reload();
        }
    });
}

function callAjaxNoAlertWithNoReload(method, url, data){

    $.ajax({
        method: method,
        url: url,
        data: data,
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
            if(data.status == 401){
                const returnUrl = window.location.href;
                location.href = '/account/login?returnUrl='+returnUrl;
                return false;
            }
            if(data.status == 403){
                const returnUrl = window.location.href;
                location.href = '/account/login?returnUrl='+returnUrl;
                return false;
            }
            const response = data.responseJSON;
            console.log(response);
            alert(response.message);
            location.reload();
        }
    });
}

async function getData(method, url, data) {
    try{
        const response = await $.ajax({
            method: method,
            url: url,
            data: JSON.stringify(data),
            contentType: 'application/json',
        });
        return response;
    } catch (error) {
        const response = error.responseJSON;
        alert(response.message);
        return error;
    }
}

function uploadFileUsingAjax(method, url, formData) {
    $.ajax({
        method: method,
        url: url,
        data: formData,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,

        success: function (data) {
            console.log(data);
            alert(data.message)
            location.reload();
        },
        error: function (data) {
            const response = data.responseJSON;
            console.log(response);
            alert(response.message);
            location.reload();
        }
    });
}