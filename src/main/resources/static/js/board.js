let host = 'http://' + window.location.host;
let modalUserId = 0;
// 해당 변수는 openDetailsModal메서드의 boardUserId부분 해결을 위해
let modalBoardId = 0;

let CRUD = [1,2,3,4];
// 해당 변수는 인증된 사용자만이 사용할 수 있는 메서드로 구성되어져있습니다.
// buttonAuthenticated() : 1, addCard() : 2, updateCard() : 3, deleteCard() : 4

$(document).ready(function() {
    const auth = getToken();
    // view 생성시 토큰 생성
    $('#updateButton').on('click', updateModal);
    $('#deleteButton').on('click', deleteModal);
    $('#completeButton').on('click', completeModal);

    $('#updateCloseButton').on('click', function() {
        location.reload();
    });

    $('#deleteCloseButton').on('click', function() {
        location.reload();
    });

    $('#completeCloseButton').on('click', function() {
        location.reload();
    });

    $('#commentsCloseButton').on('click', function() {
        location.reload();
    });
});
// 업데이트 버튼 클릭에 대한 처리

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

function isAuthenticated(userId, crudToken) {
    console.log("userId : "+userId);

    const token = getToken();
    //console.log("isAuthenticated()'s cardUserId : "+cardUserId);
    console.log("token : "+token);
    console.log("crudToken : "+crudToken);
    console.log("boardId : "+modalBoardId);
    //console.log("userBoardId : ",userBoardId);

    // 토큰이 없으면 처리 중단
    if (!token) {
        console.error('Authentication token is missing.');
        return;
    }

    // 서버 엔드포인트 및 요청 데이터 설정 (필요에 따라 수정)
    const endpoint = '/api/user/authToken';  // 실제 엔드포인트에 맞게 수정

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
        if(modalUserId == 0){
            getForUserInfo(token, userId, crudToken);
        }else{
            getForUserInfo(token, modalUserId, crudToken);
        }
        // getForUserInfo
        // 사용자 정보 얻기
    }).fail(error => {
        console.error('Failed to authentication, try again');
        console.log("error : ",error);
        // 요청이 실패한 경우
    });
}
function getForUserInfo(token, cardUserId, crudToken){
    $.ajax({
        type: 'GET',
        url: '/api/user/userInfo',  // 사용자 정보를 제공하는 엔드포인트에 맞게 수정
        headers: {
            'Authorization': token,
        },
    }).done(loginUserId => {
        // console.log('login userId: ', loginUserId);
        // 로그인 사용자를 server에서 잘 가져옴
        methodSelection(loginUserId, cardUserId, crudToken);
        // 의외로 여기가 문제네? 여기서 addCard와 updateCard, deleteCard를 나누는 방법..?
    }).fail(error => {
        console.error('Failed to get user details:', error);
    });
}
function methodSelection(loginUserId, cardUserId, crudToken){
    console.log("methodSelection CardUserId : ", cardUserId);
    console.log("loginUserId : ", loginUserId);
    console.log("boardId : "+modalBoardId);
    console.log("crudToken : ", crudToken);
    // user는 cardUserId는 1,2,3
    if(crudToken == CRUD[0]){
        buttonAuthenticated(loginUserId, cardUserId);
    }else if(crudToken == CRUD[1]){
        addCard(loginUserId, cardUserId);
    }else if(crudToken == CRUD[2]){
        updateCard(loginUserId, cardUserId);
    }else if(crudToken == CRUD[3]){
        deleteCard(loginUserId, cardUserId);
    }else{
        alert("잘못된 요청입니다. 다시 시도해주세요.");
        // 갑자기 여기까지 진전이?
    }
}

function testCheckbox(loginUserId, cardUserId) {
    // 사용자가 인증되지 않았으므로 체크박스를 비활성화
    var checkbox = document.getElementById("flexSwitchCheckDefault");
    if(loginUserId == cardUserId){
        checkbox.disabled = false;
    }else{
        checkbox.disabled = true;
    }
}

function buttonAuthenticated(loginUserId, cardUserId){
    console.log("loginUserId : "+loginUserId);
    console.log("cardUserId : "+cardUserId);
    if(loginUserId == cardUserId){
        console.log("해당 사용자는 완료버튼을 사용하실 수 있습니다.");
        openCompleteModal();
        modalUserId = 0;
    }else{
        console.log("modalUserId : "+modalUserId);
        alert("해당 사용자는 접근 권한이 없습니다.");
        window.location.reload();
    }
}

function openCompleteModal() {
    $('#completeModal').modal('show');
}
function completeModal(loginUserId, cardUserId){
    console.log("handleCheckboxChange() start!");
    // 이거까지 되는데

    var checkbox = document.getElementById("flexSwitchCheckDefault");
    var isChecked = checkbox.checked;

    console.log(isChecked);

    var ajaxType = isChecked ? 'POST' : 'DELETE';

    $.ajax({
        type: ajaxType,
        url: `/api/user/${modalBoardId}/checks`,
        contentType: 'application/json'
    }).done(res => {
        console.log(res);
        if (isChecked) {
            checkbox.checked = true;
        } else {
            checkbox.checked = false;
        }
        $('#completeModal').modal('hide');
        //window.location.reload();
    }).fail(err =>{
        console.log(err);
        alert("투두 완료에 실패했습니다. 다시 시도해주세요.");
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
    var userId = button.getAttribute("data-user-id"); // 동적 값 검색
    // html에서 무조건 가져와야할 userId

    console.log("openDetailsModal -> boardId : ", boardId);
    console.log("openDetailsModal -> username : ", username);
    console.log("openDetailsModal -> userId : ", userId);

    // var updateBtn = document.getElementById("updateBtn");
    // updateBtn.setAttribute("data-user-id", userId);
    // 이것만 있어도되는지 확인

    $('#detailsModal').modal('show');

    $.ajax({
        type: 'GET',
        url: `/api/user/${boardId}`,
        contentType: 'application/json'
    }).done(function(board) {
        console.log(board);

        fetchComments(boardId);

        $('#modalUsername').text(username);
        $('#modalTitle').text(board.title);
        $('#modalContents').text(board.contents);
        $('#modalUserId').text(userId);

        // error 1 : 여기서 값을 불러오고 isAuthenticated라인에서 modalId를 불러온다
        modalUserId = userId;
        // 내가 buttonAuthenticated(), addCard 등등에 쓸 내용들
        modalBoardId = boardId;
        console.log("openDetailsModal's modalUserId : "+modalUserId);
        console.log("openDetailsModal's modalBoardId : "+modalBoardId);
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

function updateCard(loginUserId, cardUserId){
    console.log("loginUserId : ",loginUserId);
    console.log("cardUserId : ",cardUserId);
    if(loginUserId == cardUserId){
        console.log("해당 사용자는 update, delete 버튼을 사용하실 수 있습니다.");
        openUpdateModal();
        modalUserId = 0;
    }else{
        console.log("modalUserId : "+modalUserId);
        alert("해당 사용자는 접근 권한이 없습니다.");
        window.location.reload();
    }
}

function openUpdateModal(userId) {
    // 해당 게시글의 수정 모달 열기 js
    $('#updateModal').modal('show');
}
function updateModal() {
    let isFirstTime = true;
    // 해당 게시글의 수정 js

    let updateTitleValue = $('#updateTitle').val();
    let updateContentsValue = $('#updateContents').val();
    console.log("modalBoardId : "+ modalBoardId);
    if(!isFirstTime){
        if(!updateTitleValue){
            alert("Title를 입력해주세요");
            return;
        }
    }
    let currentTime = new Date();
    // test
    let data = {
        updateTitle: updateTitleValue,
        updateContents: updateContentsValue,
        createdDate: currentTime.toISOString() // 현재 시간을 ISO 문자열로 변환
    };

    console.log(JSON.stringify(data));
    // 여기는 boardId가 필요해
    $.ajax({
        type: 'PUT',
        url: `/api/user/${modalBoardId}`,
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).done(res => {
        $('#updateModal').modal('hide');
        window.location.reload();
    }).fail(err => {
        alert("회원 수정에 실패하셨습니다!");
        window.location.reload();
        isFirstTime = true;
    });
    // 현재 시간을 생성
}

function deleteCard(loginUserId, cardUserId){
    console.log("loginUserId : ",loginUserId);
    console.log("cardUserId : ",cardUserId);
    if(loginUserId == cardUserId){
        console.log("해당 사용자는 update, delete 버튼을 사용하실 수 있습니다.");
        openDeleteModal();
        modalUserId = 0;
    }else{
        alert("해당 사용자는 접근 권한이 없습니다.");
        window.location.reload();
    }
}

function openDeleteModal(){
    // 해당 게시글의 삭제 모달 열기 js
    $('#deleteModal').modal('show');
}
function deleteModal(){
    console.log("modalBoardId : "+modalBoardId);

    $.ajax({
        type: 'DELETE',
        url: `/api/user/${modalBoardId}`, // 저장한 userId 사용
        contentType: 'application/json',
        //data: JSON.stringify(data)
    }).done(res => {
        modalUserId = 0;
        $('#deleteModal').modal('hide');
        console.log(res);
        window.location.reload();
    }).fail(err => {
        console.log(err);
        alert("게시글 삭제에 실패하셨습니다. 다시 시도해주세요!");
        window.location.reload();
    });
}

function openCommentsModal() {
    console.log("openCommentsModal() : ");
    console.log("modalBoardId : "+modalBoardId);
    // 해당 게시글의 수정 모달 열기 js
    $('#commentsModal').modal('show');
}

function submitCommentsForm(){
    console.log("submitCommentsForm first time!");
    var content = $('#newComment').val();

    console.log("comments : "+content);
    if(!content){
        alert("Comments을 입력해주세요.");
        return;
    }
    // contents는 생략 가능하게 만듦.
    var data = {
        content: content
    };

    $.ajax({
        type: 'POST',
        url: `/api/user/comments/${modalBoardId}`,
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).done(res => {
        console.log("comments : ", content);
        alert("댓글 작성에 성공하셨습니다.");
        updateCommentList(content);
        window.location.reload();
    }).fail(err=> {
        console.log(err);
    });
}

function updateCommentList(comments) {
    console.log(comments);
    // 가져옴
    var commentsList = $('#commentList');
    commentsList.empty(); // 기존 댓글을 비우고 새로운 댓글을 추가

    // 새로운 댓글 추가
    var commentHtml = '<li>' + comments + '</li>';
    commentsList.append(commentHtml);
}

function initializeCommentList(comments) {
    // 서버에서 받은 초기 댓글 목록을 업데이트
    var commentList = $('#commentList');
    console.log("comment : "+comments);
    // 받은 댓글 목록을 반복하여 추가
    for (var i = 0; i < comments.length; i++) {
        if (comments[i].content) {
            var commentHtml = '<li>' + comments[i].content + '</li>';
            commentList.append(commentHtml);
        }
    }
}


function fetchComments(boardId) {
    console.log("boardId : " +boardId);
    $.ajax({
        type: 'GET',
        url: `/api/user/comments/${boardId}`,
        contentType: 'application/json'
    }).done(function (comments) {
        initializeCommentList(comments);
    }).fail(function (xhr, status, error) {
        console.error("Failed to fetch comments:", error);
    });
}