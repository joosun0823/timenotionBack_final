

document.addEventListener('DOMContentLoaded', function () {
    // 단일 사용자 프로필 이미지 로드
    let uniId = document.querySelector('#receiverId2')?.value;
    if (uniId) {
        AjaxOfUserFile(uniId);
    }

    // 채팅 목록의 프로필 이미지 로드
    document.querySelectorAll('.chat-wrapper').forEach(box => {
        let receiverId = box.querySelector('#receiverId2')?.value;
        let senderId = box.querySelector('#senderId')?.value;
        let profileBox = box.querySelector('.profile-list-img-box img');

        if (receiverId) {
            fetchFiles(receiverId, profileBox);
        } else if (senderId) {
            fetchFiles(senderId, profileBox);
        }
    });
});

function AjaxOfUserFile(uniId) {
    fetch(`/v1/mylife/${uniId}/files`, { method: 'GET' })
        .then(res => {
            if (!res.ok) {
                throw new Error('Network response was not ok');
            }
            return res.json();
        })
        .then(data => {
            let profileBox = document.querySelector('.profile-list-img-box img');
            if (data.userFileProfileSource) {
                let profileFileName = encodeURIComponent(`${data.userFileProfileSource}/${data.userFileProfileUuid}_${data.userFileProfileName}`);
                profileBox.src = `/v1/user-files?fileName=${profileFileName}`;
            } else {
                profileBox.src = '/img/main/basic-profile.png';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            let profileBox = document.querySelector('.profile-list-img-box img');
            profileBox.src = '/img/main/basic-profile.png';
        });
}

function fetchFiles(uniId, profileBox) {
    fetch(`/v1/everyLife/${uniId}/files`, { method: 'GET' })
        .then(res => {
            if (!res.ok) {
                throw new Error('Network response was not ok');
            }
            return res.json();
        })
        .then(data => {
            if (data.length > 0 && data[0].userFileProfileSource) {
                let profileFileName = encodeURIComponent(`${data[0].userFileProfileSource}/${data[0].userFileProfileUuid}_${data[0].userFileProfileName}`);
                profileBox.src = `/v1/user-files?fileName=${profileFileName}`;
            } else {
                profileBox.src = '/img/main/basic-profile.png';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            profileBox.src = '/img/main/basic-profile.png';
        });
}
