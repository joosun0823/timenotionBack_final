let stompClient = null;
let ws = null;
let currentUser = null; // 현재 사용자 역할

function connect() {
    // SockJS를 통한 STOMP 연결
    let socket = new SockJS('http://localhost:8888/ws-chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('STOMP 연결 성공: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            let message = JSON.parse(messageOutput.body);
            showMessage(message);
        });
    }, function (error) {
        console.error('STOMP 연결 오류: ' + error);
    });

    // 순수 WebSocket 연결
    ws = new WebSocket('ws://localhost:8888/ws-chat-handler');
    ws.onmessage = function(event) {
        console.log('받은 메시지: ' + event.data);
        let message = JSON.parse(event.data);
        showMessage(message);
    };

    ws.onopen = function() {
        console.log('WebSocket 연결 성공');
    };

    ws.onclose = function() {
        console.log('WebSocket 연결 종료');
    };

    ws.onerror = function(error) {
        console.error('WebSocket 오류: ', error);
    };
}


// function loadChatRooms() {
//     fetch('http://localhost:8888/api/chat/rooms')
//         .then(response => response.json())
//         .then(data => {
//             // 채팅방 목록을 받아온 후, 각 채팅방 정보에는 roomId가 포함되어 있을 것으로 가정
//             let roomId = data[0].roomId; // 첫 번째 채팅방의 roomId를 예시로 사용
//             sendMessage(roomId);
//         })
//         .catch(error => console.error('Error loading chat rooms:', error));
// }


function sendMessage() {
    // hidden 태그에서 roomId, loginUser, receiverId, senderId 가져오기
    let roomId = document.getElementById('roomId').value;
    let loginUser = document.getElementById('loginUser').value;
    let receiverId = document.getElementById('receiverId').value;
    let senderId = document.getElementById('senderId').value;

    // console.log(roomId);
    // console.log(loginUser);

    // 입력된 메시지 텍스트 가져오기
    let text = document.getElementById('text').value;

    // 메시지 객체 생성
    let message = {
        'messageContent': text,
        'senderId': loginUser, // 로그인한 사용자가 sender가 됨
        'receiverId': loginUser === senderId ? receiverId : senderId, // 상대방이 receiver가 됨
        'roomId': roomId // 메시지에 roomId 추가
    };

    // WebSocket 헤더 설정
    let headers = {
        'roomId': roomId // WebSocket 헤더에 roomId 추가
    };

    // WebSocket을 통해 메시지 전송
    stompClient.send("/app/message", headers, JSON.stringify(message));

    // 서버로 메시지 저장 요청
    saveMessageToDatabase(message);

    // 입력 필드 비우기
    document.getElementById('text').value = '';
}


document.getElementById("text").addEventListener("keyup", function(event) {
    if (event.key === "Enter") {
        document.getElementById("sendMessage").click();
    }
});



function saveMessageToDatabase(message) {
    fetch('http://localhost:8888/chat/save-message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(message)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                console.log('Message saved successfully.');
            } else {
                console.error('Error saving message:', data.error);
            }
        })
        .catch(error => console.error('Error:', error));
}


// function sendWsMessage() {
//     let text = document.getElementById('text').value;
//     let message = {
//         'text': text,
//         'sender': currentUser,
//         'nickname': '사용자 닉네임',
//         'profileImg': '경로/프로필/이미지.jpg'
//     };
//     ws.send(JSON.stringify(message));
//     saveMessageToDatabase(message); // 서버로 메시지 저장 요청
//     document.getElementById('text').value = ''; // 입력 필드 비우기
// }



function showMessage(message) {
    let loginUser = document.getElementById('loginUser').value;
    let chatBody = document.getElementById('chat-body');

    let newMessageDiv = document.createElement('div');
    newMessageDiv.className = 'chat-message';
    newMessageDiv.classList.add(message.senderId === loginUser ? 'others' : 'user');

    let messageContentDiv = document.createElement('div');
    messageContentDiv.className = 'message-content';
    messageContentDiv.textContent = message.messageContent;

    if (message.senderId !== loginUser) {
        // 받는 사람의 경우: 프로필과 닉네임 표시
        let receiverDiv = document.createElement('div');
        receiverDiv.className = 'receiver';

        let receiverProfileSpan = document.createElement('span');
        receiverProfileSpan.className = 'receiver-profile';
        let receiverProfileImg = document.createElement('img');
        receiverProfileImg.src = message.senderProfileSource; // 발신자의 프로필 이미지
        receiverProfileImg.alt = '';
        receiverProfileSpan.appendChild(receiverProfileImg);
        receiverDiv.appendChild(receiverProfileSpan);

        let receiverNicknameSpan = document.createElement('span');
        receiverNicknameSpan.className = 'receiver-nickname';
        receiverNicknameSpan.textContent = message.senderNickname; // 발신자의 닉네임
        receiverDiv.appendChild(receiverNicknameSpan);

        newMessageDiv.appendChild(receiverDiv);
    }

    newMessageDiv.appendChild(messageContentDiv);
    chatBody.appendChild(newMessageDiv);
    chatBody.scrollTop = chatBody.scrollHeight;
}

connect();

/* 리스트에 마우스 올리면 hover 이벤트 --- */
Hover();
function Hover(){
    // 1. 배경색, 글자색 hover
    $('.chat-wrapper').on('mouseenter', '.chat-li-content', function (e){
        $(this).css('backgroundColor', '#F1F5F6');
        $(this).find('.conversation').css('color', '#5AB6D9');
    }).on('mouseleave', '.chat-li-content', function (){
        $(this).css('backgroundColor', '');
        $(this).find('.conversation').css('color', '');
    });

    // 2. 버튼 호버
    $('.next-wrap').on('mouseenter', function (){
        $(this).css({
            'backgroundColor' : 'white',
            'transition-duration' : '0.3s'
        });
        $(this).find('.submit-default').css('display', 'none');
        $(this).find('.hovers').css('display', 'inline-block');

    }).on('mouseleave', function (){
        $(this).css({
            'backgroundColor' : '',
            'transition-duration' : '0.2s'
        });
        $(this).find('.submit-default').css('display', '');
        $(this).find('.hovers').css('display', 'none');

    })
}




