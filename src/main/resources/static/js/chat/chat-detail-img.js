document.addEventListener('DOMContentLoaded', function () {
    let sessionUniId = document.querySelector('#loginUser').value;
    let senderId = document.querySelector('#senderId').value;
    let receiverId = document.querySelector('#receiverId').value;

    if (sessionUniId === senderId) {
        fetchFiles(receiverId, document.querySelector('.profile-img-box'));
    } else {
        fetchFiles(senderId, document.querySelector('.profile-img-box'));
    }

    document.querySelectorAll('.chat-wrapper').forEach(box => {
        let boxSenderId = box.querySelector('#senderId').value;
        let boxReceiverId = box.querySelector('#receiverId').value;
        let profileBox = box.querySelector('.profile-list-img-box img');

        if (sessionUniId === boxSenderId) {
            fetchFiles(boxReceiverId, profileBox);
        } else {
            fetchFiles(boxSenderId, profileBox);
        }
    });
});

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
                profileBox.innerHTML = `<img src="/v1/user-files?fileName=${profileFileName}" alt="프로필 이미지">`;
            } else {
                profileBox.innerHTML = `<img src="/img/main/basic-profile.png" alt="기본 프로필 이미지">`;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            profileBox.innerHTML = `<img src="/img/main/basic-profile.png" alt="기본 프로필 이미지">`;
        });
}
