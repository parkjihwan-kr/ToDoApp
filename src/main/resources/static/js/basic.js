let host = 'http://' + window.location.host;

$(document).ready(function () {
    const auth = getToken();
    //console.log(auth);
    if(auth === '') {
        // $('#login-true').show();
        // $('#login-false').hide();
        window.location.href = host + "/api/user/login-page";
    } else {
        $('#login-true').show();
        $('#login-false').hide();
    }
})

function logout() {
    console.log("logout");
    // 토큰 삭제
    Cookies.remove('Authorization', { path: '/' });
    window.location.href = host + "/api/user/login-page";
}

function getToken() {
    let auth = Cookies.get('Authorization');
    console.log("getToken");
    if(auth === undefined) {
        return '';
    }
    return auth;
}