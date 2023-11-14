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

function submitForm(key) {
    var auth = getToken();
    console.log("submitForm first time!");
    // 게시글 작성에 대한 js
    // console.log("auth : ",auth);
    // auth를 getToken()에 가져가더라도 여기에 못쓰네? 지역변수라서>??

    var titleElement = document.getElementById("addTitle_" + /*[[${entry.key}]]*/ '');
    console.log("titleElement : ",titleElement);
    if (!titleElement) {
        console.error("ID가 'addTitle" + key + "'인 요소를 찾을 수 없습니다.");
        return;
    }

    var title = titleElement.value;
    //var contents = $('#contents').val();

    if(!title){
        alert("Title을 입력해주세요.");
        return;
    }
    // contents는 생략 가능하게 만듦.
    var data = {
        title: title,
        /*username: username,
        password: password,
        contents: contents*/
    };

    $.ajax({
        type: 'POST',
        url: '/api/user',
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).done(res => {
        console.log(title);
        //console.log(contents);
        alert("게시글 등록에 성공하셨습니다.");
        window.location.reload();
        //$('#postModal').modal('hide');
    }).fail(err=> {
        console.log("게시글 등록 성공했다는데 왜..?");
        console.log(err);
    });
}
function openDetailsModal(userId) {
    // 해당 게시글에 대한 상세보기 js
    console.log("openDetailsModal method start!");

    console.log("userId : ", userId);
    $('#detailsModal').modal('show');
    // AJAX를 사용하여 서버에서 데이터 가져오기
    $.ajax({
        type: 'GET',
        url: `/api/user/${userId}`,
        contentType: 'application/json'
    }).done(function(user) {
        // 성공 시 처리
        $('#modalTitle').text(user.title);
        $('#modalUsername').text(user.username);
        $('#modalContents').text(user.contents);

        // createdDate를 형식화하여 표시
        var createdDate = new Date(user.createdDate);
        var formattedDate = createdDate.toLocaleString(); // 더 적절한 형식으로 변환할 수 있습니다.
        $('#modalCreateDate').text(formattedDate);
    }).fail(function(xhr, status, error) {
        // 오류 시 처리
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