# 근근한잔 (타임노션)
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/d9c6e0d6-f1af-403a-83a6-8c26445c0c51></a>


## 📁 프로젝트 소개
**근근한잔💪 (타임노션)**은 개인의 생애주기별 경험과 추억을 기록하고, 이를 공유함으로써 공감을 형성할 수 있는 플랫폼입니다. <br> 사용자는 자신의 삶을 일기 형식으로 기록하며, 이를 통해 유대감을 형성하고, 자기 표현의 장을 제공받아 일기 쓰기 습관을 형성할 수 있습니다.

## ⏱ 개발 기간
24.05.03 - 24.05.31

## 주요 기능

1. **시기별 일대기 공유**
   - 사용자는 자신의 일생을 기록할 수 있습니다.<br>  특정 연도를 선택하여 자신의 일대기를 작성하고 게시글을 올리면, 리스트에서 시기별로 볼 수 있습니다.<br>  이를 통해 자신의 유년기, 청년기 등을 모아볼 수 있습니다.

2. **커뮤니티 형성**
   - 공통 키워드를 통해 경험을 공유하며 사용자 간의 친밀함을 생성하고 새로운 만남을 도모할 수 있습니다.

3. **그룹화**
   - 여러 사용자의 일대기를 모아 특정 시기나 집단을 중심으로 묶어냄으로써,<br>  그 시기나 집단의 역사적 기록을 체계적으로 보존하고 다양한 관점에서 재조명할 수 있습니다.<br>  이를 통해 한 개인이 자기계발의 계기를 마련할 수 있습니다.

4. **키워드를 통한 공통사**
   - 사이트의 키워드 기능을 통해 내 관심사를 검색하고 자신과 직종, 학교, 지역 등이 같은 다양한 사람들과 만날 수 있도록 합니다.<br>  댓글을 통해 다양한 경험을 공유하며 소통할 수 있습니다.

## 📖목차
- [팀 구성](#팀-구성)
- [ERD 구성](#erd-구성)
- [Stacks](#-stacks)

## 🔗팀 구성
**Producer Git-hut Page 장윤근**

| 팀장  | 장윤근 |
|:----:|:-----:|
| 부팀장 | 윤담   |
| 팀원  | 문동주 |
| 팀원  | 고태민 |
| 팀원  | 김주선 |
| 팀원  | 양효준 |


## 💡ERD 구성

<details open>
<summary>ERD</summary>
  
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src='https://github.com/jang1234567/timenotionBack_final/assets/120088675/aab927f4-58cd-4040-945d-d78688755824'></a>

</details>

## 💻 기능 및 화면 소개

**<메인화면>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/73ea0508-944f-436e-b7e4-62b4e3eda60c></a>

- 메인 화면으로 관리자가 뽑는 게시물 베스트 탑3를 보여주고 있습니다.
- 인기 컨텐츠는 게시물 좋아요를 받은 순서대로 탑4를 나타내어 보여주고 있습니다.

**<키워드/배너>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/1efd923a-b6f3-43be-9338-538940175b6f></a>

- 인기키워드는 “모두의 일대기”페이지에서 게시물을 검색할 때 해당 키워드를 검색하면 그 키워드의 검색 횟수가 1씩 누적합 되도록 설정 되어있습니다.
- 키워드 밑에는 배너로 2초가 지나면 자동으로 해당 배너 크기만큼 좌로 이동하여 다음 배너가 보이게 구현 하였습니다.

**<로그인>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img  width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/a28d2e60-f0f5-4918-a3b0-4192572259c0></a>

- 타임 노션이라는 웹사이트는 무조건 로그인을 해야 볼수있는 회원제 게시판입니다.
- Session을 이용하여 로그인을 구현하였고, KAKAO API를 불러와 Oauth2를 통해 카카로 로그인을 구현 하였습니다.


**<회원가입>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img height="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/4b02463d-ed48-4fc5-bf9c-e2d84ba683e3></a>

- 우선 FRONT에서 JS를 가지고 유효성 검사를 진행하였고 이메일, 닉네임 중복검사는 DB에서 값을 가져와 JS의 VALUE값과 비교하여 중복 검사를 진행합니다.
    ⇒ 모든 검사를 만족시킨 후에 다음 페이지로 넘어가게 됩니다.


**<나의 일대기>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/df6e89de-048a-477a-9008-d0d052b5b7c8></a>

- 내가 쓴 게시물을 볼 수 있는 페이지입니다.
- AJAX를 통해 RestController로 구현하여 일대기버튼을 클릭시 일대기별로  게시판을 정렬할 수 있도록 구현해 놓았습니다.
- 이미지사진과 배경사진이 없을 경우에는 default인 기본 배경이미지로 구현하였고 회원정보 수정에가서 파일 수정 할 수 있습니다.

  **<게시글 작성>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/84b29d08-cd1a-4859-8c7e-920af1714ea5></a>


- 기본적인 게시판 작성과정이지만 여기서 특이점으로 사건이 발생했던 년도를 입력받아, <br> 함수를 만들어 자신의 생일과 해당 년도를 계산하여 어떤 일대기로(유아기,청소년기,중년기,노년기) 저장되는지 구현해 보았습니다.
- 게시물을 수정할 때에는 파일은 자동적으로 삭제되게 만들었고 새롭게 추가할 수 있도록 구현하였습니다.


**<모두의 일대기>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/4f3f5cf0-0d8c-4fb1-9a1c-7bd0bbc0b283></a>

- 모든 사람이 쓴 글을 볼 수 있는 페이지 입니다.
- 검색창을 만들어 DB에서 검색란의 문자열 크기를 세글자 정도로 맞추었고 검색어와 제목과 내용이 일치하면 해당 검색어에 대한 게시물만 보일 수 있게 정렬 해 놓았습니다.
- 마찬가지로 일대기별, 최신순, 인기순, 조회순으로도 정렬 할 수 있으며 모두 페이지 이동없는 비동기 방식 AJAX를 사용하여 해당 페이지를 구현 하였습니다.


**<상세페이지>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/ff2644c3-714d-443b-80f3-934b6941dcf6></a>

- 댓글을 작성시에는 해당 input 영역에 댓글을 작성하면 JS로 값을 받아와  RestController를 통해해당영역안에 댓글이 들어가게 구현 하였다.
- 제목을 보면 배경색이 있는데, 게시물의 일대기 값을 받아 JS로 해당 게시물의 일대기별 제목 색상을 정해두었습니다.
- 내가 쓴 게시물에서는 수정과 삭제버튼이 보이고 남이 쓴 게시물을 들어가면 수정, 삭제 버튼이 보이지 않습니다.

**<좋아요>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/0899dfaf-be50-4c21-8137-253aa2aed810></a>

- 게시물에 하트가 있는데 그 하트를 누르면 하트가 빨간색으로 바뀌고 게시물 좋아요 카운트하는 컬럼도 1씩 증가한다.

**<팔로우,언팔로우>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/1db2b380-ae39-40d9-94b9-c3785b5ec89d></a>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/8406faad-6212-49fe-962e-e5f139481740></a>

- 게시물 좋아요 기능과 마찬가지로 다른사람의 페이지!<br> 
에서 하트를 누르면 팔로우가 가능하다.
- 팔로우를 누르는 순간 팔로잉 목록에 해당 회원에 대한 정보가 같이 SELECT 된다.

  **<커뮤니티(공지,문의)>**<br>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/efc10124-1ea0-46e9-b8c7-5679d727a254></a>
<a href='https://ifh.cc/v-H9Fj40' target='_blank'><img width="400" src=https://github.com/jang1234567/timenotionBack_final/assets/120088675/ce615f9d-4c12-4e0c-bdda-70190b312668></a>


- 공지는 관리자만 가능하며 관리자가 아닌사람이 로그인하면 작성하기 버튼이 보이지 않는다.<br>
- 문의는 모든 회원들이 문의 할 수 있고 문의에 대한 답변은 관리자만 할 수 있다.





## ⚙ Stacks

### Environment
<div>
  <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat&logo=visualstudiocode&logoColor=white"/>
  <img src="https://img.shields.io/badge/IntelliJ-FE315D?style=flat&logo=intellijidea&logoColor=white"/>
  <img src="https://img.shields.io/badge/DBeaver-382923?style=flat&logo=dbeaver&logoColor=white"/>
  <img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white"/>
  <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
</div>

### Development
<div>
  <img src="https://img.shields.io/badge/HTML-E34F26?style=flat&logo=html5&logoColor=white"/>
  <img src="https://img.shields.io/badge/CSS-1572B6?style=flat&logo=css3&logoColor=white"/>
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java-5382A1?style=flat"/>
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white"/>
  <img src="https://img.shields.io/badge/Springboot-6DB33F?style=flat&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Oracle-F80000?style=flat&logo=oracle&logoColor=white"/>
  <img src="https://img.shields.io/badge/MyBatis-362929?style=flat"/>
</div>

### API
<div>
  <img src="https://img.shields.io/badge/COOLSMS-0288D1?style=flat&logo=twilio&logoColor=white"/>
  <img src="https://img.shields.io/badge/KakaoLogin-FFCD00?style=flat&logo=kakao&logoColor=white"/>
</div>
