// ---- 삭제 문구 ---- //
// function del() {
//   if (confirm('정말 삭제하시겠습니까?')) list_ok.submit();
// }

// ---- 좋아요 하트 구현 ---- //

// 클릭한 이미지의 부모 요소인 p 태그를 선택합니다.
const heartImage = document.getElementById('heartImage');

// 클릭한 이미지의 부모 요소인 p 태그를 선택합니다.
const getLikeUser = document.getElementById("get-user-like-id").textContent.trim();
console.log("getLikeUser: ", getLikeUser);

let getLikeStatusBoolean = parseInt(document.getElementById("get-user-like-status").textContent.trim());
console.log("getLikeStatusBoolean:", getLikeStatusBoolean);

const getboardId = document.getElementById('boardId').value;
console.log('boardId:', getboardId);

// heart-image를 클릭했을 때 실행할 함수를 정의합니다.
heartImage.addEventListener("click", async function(event) {
  event.preventDefault(); // 기본 클릭 이벤트 방지

  if (heartImage.classList.contains('red')) {
    heartImage.classList.remove('red');
    getLikeStatusBoolean = 0; // 좋아요 취소 상태로 변경
    alert("확인용 : 좋아요를 취소했습니다.");
  } else {
    heartImage.classList.add('red');
    getLikeStatusBoolean = 1; // 좋아요 상태로 변경
    alert("확인용 : 해당 게시글을 좋아합니다.");
  }

  try {
    const response = await fetch(`/myLife/detail-my?boardId=${getboardId}`, { // boardId를 쿼리 파라미터로 전송
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        getLikeStatusBoolean: getLikeStatusBoolean,
        boardId : getboardId
      })
    });

    if (!response.ok) {
      throw new Error("getLikeStatusBoolean 전송 실패");
    }

    // 페이지 리로드 대신 좋아요 상태에 따라 UI 업데이트를 원한다면 여기에서 처리
  } catch (error) {
    console.error(error.message);
  }


  // 페이지를 새로고침합니다.
  location.reload();
});


// 페이지 로딩시 로컬 스토리지에서 팔로우 상태를 가져와서 이미지 색상 설정
window.addEventListener("DOMContentLoaded", function (){
  if(getLikeStatusBoolean === 1){
    heartImage.classList.add('red');
  } else {
    heartImage.classList.remove('red');
  }


});





//페이지 로딩시 로컬 스토리지에서 팔로우 상태를 가져와서 이미지 색상 설정
window.addEventListener("DOMContentLoaded", function (){
  if(getLikeStatusBoolean == 1){
    heartImage.classList.add('red');
  }else{
    heartImage.classList.remove('red');
  }

});











// ---- 댓글 구현 ---- //

function toggleReplyInput(comment) {
  const replyInput = comment.parentElement.querySelector('.box-re-comment-btn');

  replyInput.style.display = replyInput.style.display === 'none' ? 'flex' : 'none';
}

/* 1. 신고 모달  택스트 비활성화--------------------------------------------*/

$(document).ready(function () {
  // 텍스트 입력창을 기본적으로 비활성화
  $('.report-input').prop('disabled', true);

  $('input[name="report-reason"]').change(function () {
    if ($('#g').is(':checked')) {
      $('.report-input').prop('disabled', false); // 활성화
    } else {
      $('.report-input').prop('disabled', true); // 비활성화
    }
  });
});

/* 2. '점점점' 누르면 버튼 나오게 */
$('.dotdotdot').on('click', function (e) {
  console.log("점점점 클릭됨");
  // console.log($(this).next);
  // console.log(e.currentTarget.next);
  $('.box-mini-report').css('display', 'none');
  $(e.target).next().css('display', 'flex'); /* 누른거만 나오게  */
});

/* 3. '신고하기' 누르면 '신고모달' 뜸 */
$('.box-mini-report').on('click', function () {
  $('.box-mini-report').css('display', 'none');
  $('.report').css('display', 'flex');

  $('.dotdotdot').on('click', function (e) {
    $('.report').css('display', 'none');
  });
});
/* 4. '닫기' 누르면 닫히게 */
$('.report-btn-close').on('click', function () {
  $('.report').css('display', 'none');
});
$('.report-btn-report').on('click', function () {
  $('.report').css('display', 'none');
});

/* 5. 바깥 클릭 -> 모달 숨기기 */
$(document).on('click', function (e) {
  if (!$(e.target).closest('.dotdotdot, .box-mini-report, .report').length) {
    $('.box-mini-report').css('display', 'none');
    $('.report').css('display', 'none');
  }
}); // 클릭한 html 요소가 . 얘네랑 일치하지 . length(객체에 포함된 요소 수를 반환함, 하나라도 일치하면 length가 적어도 1이 되는 것)

{   // 버튼 처리
  let $modifyBtn = document.querySelector('.btn-modify');
  let $adminModify = document.querySelector('.admin-modify');
  let $removeBtn = document.querySelector('.btn-remove');
  let $adminRemove = document.querySelector('.admin-remove');
  let $adminListRemove = document.querySelector('.adminList-remove');
  let $backBtn = document.querySelector('.btn-back');

  //삭제버튼 처림
  $removeBtn?.addEventListener("click", function () {
    let boardId = this.dataset.id; //클릭된 요소의 data-id 속성값을 가져와 boardId 변수에 저장
    //this : 이벤트 핸들러 안에서 이벤트가 발생한 요소(클릭한 요소)
    //dataset : dataset 객체는 요소의 모든 data-* 속성을 포함
    //  ex) dataset.id ="123"
    //id : data-id 속성의 값을 가져온다
    location.href = `/myLife/remove?boardId=${boardId}`;
  });

  //삭제버튼 처림
  $adminRemove?.addEventListener("click", function () {
    let boardId = this.dataset.id; //클릭된 요소의 data-id 속성값을 가져와 boardId 변수에 저장
    //this : 이벤트 핸들러 안에서 이벤트가 발생한 요소(클릭한 요소)
    //dataset : dataset 객체는 요소의 모든 data-* 속성을 포함
    //  ex) dataset.id ="123"
    //id : data-id 속성의 값을 가져온다
    location.href = `/admin/remove?boardId=${boardId}`;
  });

  //삭제버튼 처림
  $adminListRemove?.addEventListener("click", function () {
    let boardId = this.dataset.id; //클릭된 요소의 data-id 속성값을 가져와 boardId 변수에 저장
    //this : 이벤트 핸들러 안에서 이벤트가 발생한 요소(클릭한 요소)
    //dataset : dataset 객체는 요소의 모든 data-* 속성을 포함
    //  ex) dataset.id ="123"
    //id : data-id 속성의 값을 가져온다
    location.href = `/admin/remove?boardId=${boardId}`;
  });

  //수정 버튼 처리
  $modifyBtn?.addEventListener("click", function () {
    let boardId = this.dataset.id; //클릭된 요소의 data-id 속성값을 가져와 변수에 저장
    console.log(boardId)
    location.href = `/myLife/update_writingMode?boardId=${boardId}`;
  });

  //관리자 게시글 수정 버튼 처리
  $adminModify?.addEventListener("click", function () {
    let boardId = this.dataset.id; //클릭된 요소의 data-id 속성값을 가져와 변수에 저장
    console.log(boardId)
    location.href = `/admin/update?boardId=${boardId}`;
  });

  //뒤로가기 버튼
  $backBtn?.addEventListener("click", function () {
    window.history.back() //브라우저의 이전 페이지로 이동
  });
}

//상세 페이지에서 이미지 띄우기
let boardId = document.querySelector('#boardId').value;
displayImgAjax(); //이미지 표시 함수 호출
//Ajax : Asynchronous JavaScript and XML
// JS와 XML을 활용하여 비동기 통신으로 데이터를 교환하는 기법
// Ajax라는 기술을 활용하는 방법은 여러가지가 있지만 우리는 fetchAPI를 사용한다.
function displayImgAjax() {
  // fetch()함수는 js에 내장된 함수이므로 바로 사용하면된다.
  /*
  fetch('api주소', {설정객체})
      .then(함수)       // api에서 보낸 응답을 then으로 받는다.
      .then(함수);      // 위의 then에서 반환하는 값을 여기서 받는다.

   */
  // fetch(`/v1/boards/${boardId}/files`, {method : 'GET'})
  //     .then(res => res.json()) // 응답을 받아서 데이터를 변환하고 다음 then으로 넘겨준다.
  //     .then(data => console.log(data))  // 위에서 넘겨준 데이터를 올바르게 처리한다.


  fetch(`/v1/boards/${boardId}/files`, {method: 'GET'})
      //서버에 GET요청을 보내 파일 목록을 가져옴
      .then(res => res.json())//응답을 JSON으로 변환
      .then(list => { //변환된 데이터를 list 변수에 저장
        let tags = ''; //HTML 태그를 저장할 변수 초기화

        console.log(list);
        for (let i = 0; i < list.length; i++) {
          let fileName = list[i].boardFileSourceName + '/' + list[i].boardFileUuid + '_' + list[i].boardFileName;
          //파일 경로 조합

          tags += ` <img src="/v1/files?fileName=${fileName}" data-id="${list[i].boardFileId}" data-name="${fileName}"/> `;
        }

        let $postImgs = document.querySelector('.post-images'); //이미지가 삽입될 요소

        $postImgs.innerHTML = tags; //생성된 html 태그를 삽입
      });
}



// ※※※※※※※※※※※※※※※※※※※※※※※※글의 생애주기에 맞춰 제목 배경 바꾸기 ※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
const checkLifeCycle = document.getElementById("user-lifecycle-check").textContent.trim();
//배경 범위 변수 wrapper-title
let lifeCycleBG = document.getElementsByClassName("wrapper-title");

window.addEventListener("DOMContentLoaded", function() {
  console.log("checkLifeCycle"+ checkLifeCycle);



  switch(checkLifeCycle) {
    case '유아기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#E99A9A";
      }
      break;
    case '유년기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#F8CB9F";
      }
      break;
    case '아동기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#FEE49E";
      }
      break;
    case '청소년기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#B7D6AA";
      }
      break;
    case '청년기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#A5C3F2";
      }
      break;
    case '중년기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#B4A8D5";
      }
      break;
    case '노년기':
      // 모든 요소에 대해 배경색을 흰색으로 설정
      for (let i = 0; i < lifeCycleBG.length; i++) {
        lifeCycleBG[i].style.backgroundColor = "#D4A7BD";
      }
      break;
    default:
      break;
  }
});


/* 프로필사진 이미지 처리 ---------------------------------------------------------------------------------------------------------- */
/* 프로필사진 이미지 처리 ---------------------------------------------------------------------------------------------------------- */


let uniId = document.querySelector('#uniIdForFile').value; // 유저아이디 가져옴
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
          return;
        }
        return JSON.parse(text); // 텍스트가 비어있지 않으면 JSON으로 파싱
      }) // 응답을 JSON으로 변환 ------------------------------------------------ 여기까지 수정함 24.07.08
      .then(data => { // 변환된 데이터를 data 변수에 저장
        let profileTags = '';

        if(data){ // data 존재여부 확인
          let profileFileName = encodeURIComponent(data.userFileProfileSource + '/' + data.userFileProfileUuid + '_' + data.userFileProfileName); // ☆★☆★☆★ 파일 경로를 URL 인코딩
          console.log("파일이름 : " + profileFileName);
          if (data.userFileProfileSource) {
            profileTags = `
                    <img src="/v1/user-files?fileName=${profileFileName}" alt="프로필사진" class="img-profile-img">
                `;
          }else{
            profileTags = `
                    <img src="/img/main/basic-profile.png" alt="기본 프로필 사진" class="img-profile-img">
                `;
          }
        }else{
          profileTags = `
                    <img src="/img/main/basic-profile.png" alt="기본 프로필 사진" class="img-profile-img">
                `;
        }
        /* 파일 경로 조합 */
        /*
        let profileFileName = encodeURIComponent(data.userFileProfileSource + '/' + data.userFileProfileUuid + '_' + data.userFileProfileName); // ☆★☆★☆★ 파일 경로를 URL 인코딩
        if (data.userFileProfileSource) {
          profileTags = `
                    <img src="/v1/user-files?fileName=${profileFileName}" alt="프로필사진" class="img-profile-img">
                `;
        } else {
          profileTags = `
                    <img src="/img/main/basic-profile.png" alt="기본 프로필 사진" class="img-profile-img">
                `;
        }
*/
        let $profileBox = document.querySelector('.box-profile-img-a');

        /* html에 만든 태그 넣기 */
        $profileBox.innerHTML = profileTags;
      });
}

