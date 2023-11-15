let host = 'http://' + window.location.host;
let updateUserId;  // 전역 변수로 선언

$(document).ready(function() {
    const auth = getToken();
    //console.log("auth : ",auth);
    $('#updateButton').on('click', updateModal);
    $('#deleteButton').on('click', deleteModal);
    // 해당 버튼 클릭시 모달을 열어줍니다.
});


function getToken() {
    let auth = Cookies.get('Authorization');
    // 나 인증된 사용자인지?
    console.log("Is Authenticated:", auth);
    // BEARER~~~~
    if(auth === undefined) {
        return '';
    }
    return auth;
}

// contents는 null일 수 있게 설계 되었습니다.
function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', { path: '/' });
    window.location.href = host + "/api/user/login-page";
    // 로그 아웃 버튼
}

function isAuthenticated(cardUserId) {
    const token = getToken();
    //console.log("card User Id : "+userId);
    console.log("carUserId : "+cardUserId);
    console.log("token : "+token);
    // 토큰이 없으면 처리 중단
    if (!token) {
        console.error('Authentication token is missing.');
        return;
    }

    // 서버 엔드포인트 및 요청 데이터 설정 (필요에 따라 수정)
    const endpoint = '/api/user/authToken';  // 실제 엔드포인트에 맞게 수정
    /*const requestData = {
        // 필요한 요청 데이터 추가
        // 예: title, contents 등
    };*/

    // 서버로 AJAX 요청 보내기
    $.ajax({
        type: 'POST',
        url: endpoint,
        headers: {
            'Authorization': token,  // 토큰을 헤더에 추가
            'Content-Type': 'application/json',
        },
        //data: JSON.stringify(requestData),  // 필요에 따라 데이터를 JSON 형태로 변환
    }).done(response => {
        console.log('authentication successfully:', response);
        getForUserInfo(token, cardUserId);
        // getForUserInfo
        // 사용자 정보 얻기
    }).fail(error => {
        console.error('Failed to authentication, try again');
        console.log("error : ",error);
        // 요청이 실패한 경우
    });
}
function getForUserInfo(token, cardUserId){
    $.ajax({
        type: 'GET',
        url: '/api/user/userInfo',  // 사용자 정보를 제공하는 엔드포인트에 맞게 수정
        headers: {
            'Authorization': token,
        },
    }).done(loginUserId => {
        // console.log('login userId: ', loginUserId);
        // 로그인 사용자를 server에서 잘 가져옴
        addCard(loginUserId, cardUserId);
    }).fail(error => {
        console.error('Failed to get user details:', error);
    });
}

function addCard(loginUserId, cardUserId){
    console.log("loginUserId : ",loginUserId);
    console.log("cardUserId : ",cardUserId);
    if(loginUserId != cardUserId){
        alert("해당 사용자는 접근 불가능한 카드입니다.");
        return;
    }
    $('#postModal').modal('show');
    //postModal이 보이고 submitForm으로
}
function submitForm(key) {
    var auth = getToken();
    console.log("submitForm first time!");

    /*var titleElement = document.getElementById("addTitle_" + [[${entry.key}]] '');*/
    // 여기 없대

    /*if (!titleElement) {
        console.error("ID가 'addTitle" + key + "'인 요소를 찾을 수 없습니다.");
        return;
    }*/

    var title = $('#title').val();
    var contents = $('#contents').val();

    if(!title){
        alert("Title을 입력해주세요.");
        return;
    }
    // contents는 생략 가능하게 만듦.
    var data = {
        title: title,
        contents: contents
    };

    $.ajax({
        type: 'POST',
        url: '/api/user',
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).done(res => {
        console.log("title : ",title);
        console.log("contents : ", contents);
        alert("게시글 등록에 성공하셨습니다.");
        window.location.reload();
        //$('#postModal').modal('hide');
    }).fail(err=> {
        console.log(err);
    });
}
function openDetailsModal(button) {
    console.log("openDetailsModal method start!");

    var boardId = button.getAttribute('data-board-id');
    var username = button.getAttribute('data-username');

    console.log("boardId: ", boardId);

    $('#detailsModal').modal('show');

    $.ajax({
        type: 'GET',
        url: `/api/user/${boardId}`,
        contentType: 'application/json'
    }).done(function(board) {
        console.log(board);

        $('#modalUsername').text(username);
        $('#modalTitle').text(board.title);
        $('#modalContents').text(board.contents);

        var createdAt = new Date(board.createdAt);
        console.log(createdAt);
        // 작동
        var formattedDate = createdAt.getFullYear() + '-' +
            ('0' + (createdAt.getMonth() + 1)).slice(-2) + '-' +
            ('0' + createdAt.getDate()).slice(-2) + ' ' +
            ('0' + createdAt.getHours()).slice(-2) + ':' +
            ('0' + createdAt.getMinutes()).slice(-2) + ':' +
            ('0' + createdAt.getSeconds()).slice(-2);
        // 날짜 포멧팅

        $('#modalCreatedAt').text(formattedDate);
    }).fail(function(xhr, status, error) {
        console.log(err);
    });
}


function openUpdateModal(userId) {
    // 해당 게시글의 수정 모달 열기 js
    updateUserId = userId; // userId를 변수에 저장
    console.log("updateUserId : ", updateUserId);
    $('#updateModal').modal('show');
}
function updateModal() {
    // 해당 게시글의 수정 js
    console.log("userId: ", updateUserId);

    let updateTitleValue = $('#updateTitle').val();
    let updateUsernameValue = $('#updateUsername').val();
    let updatePasswordValue = $('#updatePassword').val();
    let updateContentsValue = $('#updateContents').val();
    /*
    console.log("updateTitle: ", updateTitleValue);
    console.log("updateUsername: ", updateUsernameValue);
    console.log("updatePassword: ",updatePasswordValue);
    console.log("updateContents: ", updateContentsValue);
*/
    if(!updateUsernameValue){
        alert("Username을 입력해주세요");
        return;
    }
    if(!updatePasswordValue){
        alert("Password를 입력해주세요");
        return;
    }
    if(!updateTitleValue){
        alert("Title를 입력해주세요");
        return;
    }
    // 현재 시간을 생성
    let currentTime = new Date();
    // test
    let data = {
        updateUsername: updateUsernameValue,
        updatePassword : updatePasswordValue,
        updateTitle: updateTitleValue,
        updateContents: updateContentsValue,
        createdDate: currentTime.toISOString() // 현재 시간을 ISO 문자열로 변환
    };

    console.log(JSON.stringify(data));
    $.ajax({
        type: 'PUT',
        url: `/api/user/${updateUserId}`,
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).done(res => {
        $('#updateModal').modal('hide');
        window.location.reload();
    }).fail(err => {
        alert("회원 수정에 실패하셨습니다. 비밀번호를 확인해주세요!");
    });
}

function openDeleteModal(userId){
    // 해당 게시글의 삭제 모달 열기 js
    deleteUserId = userId; // userId를 변수에 저장
    console.log("deleteUserId : ",deleteUserId);
    $('#deleteModal').modal('show');
}
function deleteModal(){
    // 해당 게시글의 삭제 js
    console.log("deleteId?: ", deleteUserId);

    let deletePassword = $('#deletePassword').val();

    console.log("deletePassword: ", deletePassword);
    if(!deletePassword){
        alert("Password를 입력해주세요!");
        return;
    }

    let data = {
        deletePassword: deletePassword
    };
    console.log(JSON.stringify(data));
    $.ajax({
        type: 'DELETE',
        url: `/api/user/${deleteUserId}`, // 저장한 userId 사용
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).done(res => {
        $('#deleteModal').modal('hide');
        console.log(res);
        window.location.reload();
    }).fail(err => {
        alert("게시글 삭제에 실패하셨습니다. 비밀번호를 확인해주세요!");
       });
}