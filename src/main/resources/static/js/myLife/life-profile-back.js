/* --------------------------------------- 프사 / 배사 */
/* userpage에서 씀 , 파생페이지에도 씀 */
console.log("들어왔어요~ ");
let uniId = document.querySelector('#uniId').value; // 유저아이디 가져옴
AjaxOfUserFile();

function AjaxOfUserFile() {
    fetch(`/v1/mylife/${uniId}/files`, { method: 'GET' })
        .then(res => {
            if (!res.ok) {
                throw new Error('Network response was not ok');
            }
            return res.text(); // 먼저 응답을 텍스트로 변환
        })
        .then(text => {
            if (!text) {
                // 응답이 비어 있는 경우 기본 프로필 사진을 표시
                console.log("응답이 비어서 기본 프로필 들어갔습니다.")
                let $profileBox = document.querySelector('.box-profile-img-a');
                $profileBox.innerHTML = `
            <img src="/img/main/basic-profile.png" alt="기본 프로필 사진" class="img-profile-img">
          `;

               let $backBox = document.querySelector('.profile-bg-img-box');
                $backBox.innerHTML = `
                <img src="/img/main/basic-background3.png" alt="기본 배경사진" class="img-back-img">
                `;
                return;
            }
            return JSON.parse(text); // 텍스트가 비어있지 않으면 JSON으로 파싱
        }) // ------------------------------------------------ 여기까지 복붙 0710
        .then(data => { // 변환된 데이터를 data 변수에 저장
            let profileTags = '';
            let backTags = '';

            /* 파일 경로 조합 */
            let profileFileName = encodeURIComponent(data.userFileProfileSource + '/' + data.userFileProfileUuid + '_' + data.userFileProfileName); // ☆★☆★☆★ 파일 경로를 URL 인코딩
            let backFileName = encodeURIComponent(data.userFileBackSource + '/' + data.userFileBackUuid + '_' + data.userFileBackName); // ☆★☆★☆★ 파일 경로를 URL 인코딩
            console.log("프로필사진 파일 이름 : " + profileFileName);
            /* 프로필 사진 태그에 넣기 */
            if (data.userFileProfileSource) {
                console.log("프사 있");
                profileTags = `
                    <img src="/v1/user-files?fileName=${profileFileName}" alt="프로필사진" class="img-profile-img">
                `;
            } else {
                console.log("프사 없");

                profileTags = `
                    <img src="/img/main/basic-profile.png" alt="기본 프로필 사진" class="img-profile-img">
                `;
            }

            /* 배경 사진 태그에 넣기 */
            if (data.userFileBackSource) {
                backTags = `
                    <img src="/v1/user-files?fileName=${backFileName}" alt="배경사진" class="img-back-img">
                `;
            } else {
                backTags = `
                    <img src="/img/main/basic-background3.png" alt="기본 배경사진" class="img-back-img">
                `;
            }

            let $profileBox = document.querySelector('.box-profile-img-a');
            let $backBox = document.querySelector('.profile-bg-img-box');

            /* html에 만든 태그 넣기 */
            $profileBox.innerHTML = profileTags;
            $backBox.innerHTML = backTags;
        });
}