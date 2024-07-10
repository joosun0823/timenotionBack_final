package com.example.geungeunhanjan.controller.lifes;


import com.example.geungeunhanjan.domain.dto.board.*;
import com.example.geungeunhanjan.domain.dto.file.FollowHeartDTO;
import com.example.geungeunhanjan.domain.dto.lifePage.Criteria;
import com.example.geungeunhanjan.domain.dto.lifePage.Page;
import com.example.geungeunhanjan.domain.vo.board.BoardVO;
import com.example.geungeunhanjan.domain.vo.board.LikeVO;
import com.example.geungeunhanjan.domain.vo.file.UserFileVO;
import com.example.geungeunhanjan.domain.vo.lifes.FollowVO;
import com.example.geungeunhanjan.service.MyPageService;
import com.example.geungeunhanjan.service.board.BoardService;
import com.example.geungeunhanjan.service.lifes.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;


// myLife로 가는 컨트롤러
@Controller
@RequestMapping("/myLife")
@AllArgsConstructor
public class MyPageController {

    private final BoardService boardService;
    private final MyPageService myPageService;
    private final LifeUserUpdateDTO lifeUserUpdateDTO;
    private final FollowService followService;
    private final LifeUserInfoDTO lifeUserInfoDTO;
    private final UserFileVO userFileVO;
    BoardVO boardVO;

    // 마이페이지에서 내가 쓴 게시글 리스트 뽑기
    @GetMapping
    public String mypage(Model model, HttpSession session, Criteria criteria) {
        // 로그인 여부 확인
        Long uniId = (Long) session.getAttribute("uniId");
        if (uniId == null) {
            return "redirect:/user/login";
        }
        // 팔로우 : 이거 아직 html에 추가 안 함
//        FollowDTO follow = followService.selectFollowDetail(userId);
//        model.addAttribute("follow", follow);
        // 유저 정보 모두
        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
        userInfo.setUniId(uniId);
        model.addAttribute("userInfo", userInfo);

        // 게시판 정보
//        System.out.println(lifeUserUpdateDTO);
//        List<BoardVO> boards = boardService.selectBoard(uniId);
//        model.addAttribute("boards", boards);
//        System.out.println(boards);

        /* 페이징 윤근님꺼  */
        List<BoardVO> boardPaging = myPageService.selectMypagePaging(criteria, uniId);
        int total = myPageService.myPageTotal(uniId);
        Page page = new Page(criteria, total);
        model.addAttribute("boardPage", boardPaging);
        model.addAttribute("page", page);
        /* 팔로워 팔로잉 수*/
        int followerCnt = myPageService.countFollower(uniId);
        int followingCnt = myPageService.countFollowing(uniId);
        model.addAttribute("followerCnt", followerCnt);
        model.addAttribute("followingCnt", followingCnt);
        UserFileVO userFileVO = myPageService.getProfileBackFile(uniId);
        model.addAttribute("userFileVO", userFileVO);

        return "myLife/mypage";
    }

    @GetMapping("{boardLifeCycle}/{userId}")
    public String myLifeCycle(Model model , HttpSession session, Criteria criteria,
                              @PathVariable String boardLifeCycle, @PathVariable Long userId){

        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(userId);
        userInfo.setUniId(userId);
        model.addAttribute("userInfo", userInfo);

        criteria.setAmount(9);
        List<BoardDTO> boardPaging = boardService.userLifeCyclePaging(boardLifeCycle,userId,criteria);
        model.addAttribute("boardPage", boardPaging);

        /* 팔로워 팔로잉 수*/
        int followerCnt = myPageService.countFollower(userId);
        int followingCnt = myPageService.countFollowing(userId);
        model.addAttribute("followerCnt", followerCnt);
        model.addAttribute("followingCnt", followingCnt);
        UserFileVO userFileVO = myPageService.getProfileBackFile(userId);
        model.addAttribute("userFileVO", userFileVO);


        int total = boardService.MyAgePageMove(boardLifeCycle, userId);
        Page page = new Page(criteria, total);
        model.addAttribute("page", page);

        return "myLife/mypage";
    }



    //나의 일대기 글쓰기 페이지로 이동
    @GetMapping("/detail_writingMode")
    public String detailWritingMode(Model model, HttpSession session) {
        // 로그인 여부 확인
        Long uniId = (Long) session.getAttribute("uniId");
        if (uniId == null) {
            return "redirect:/login";
        }
//        model.addAttribute("boardVO", new BoardVO());
        return "myLife/detail_writingMode";
    }



    //나의 일대기 게시판 작성하기 ★★★★★
    @PostMapping("/detail_writingMode")
    public String detailWriting(BoardVO boardVO, @SessionAttribute("uniId") Long uniId,
                                @RequestParam("boardFile") List<MultipartFile> files, Model model,
                                RedirectAttributes redirectAttributes) {



        //현재 사용자가 누군지 정보 선언
        boardVO.setUserId(uniId);
        System.out.println("uniId = " + uniId);

        // 데이터베이스에서 사용자의 생일을 가져옴
        LocalDateTime userBirthDateTime = boardService.writerUserBirth(uniId);

        // 사용자의 생일에서 연도를 추출
        int userBirthYear = userBirthDateTime.getYear();
        System.out.println(userBirthYear);

        // 게시물을 작성한 년도
        int boardYear = boardVO.getBoardYear();
        System.out.println(boardYear);

        // 사용자의 나이 계산
        int age = boardYear - userBirthYear;
        System.out.println(age);

        // 사용자의 생년 이전을 입력하면 다시 입력하도록 처리
        if (boardYear < userBirthYear) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시물 작성 년도는 생년보다 이전일 수 없습니다.");
            return "redirect:/myLife/detail_writingMode"; // 사용자가 입력 폼으로 돌아가도록 리다이렉트
        }

        // 사용자의 생애 주기 계산
        String lifeCycle = calculateLifeCycle(age);

        // 게시물의 생애 주기 설정
        boardVO.setBoardLifeCycle(lifeCycle);

        try {
            boardService.registerBoardwithFile(boardVO, files);
        }catch (IOException e){
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("boardId", boardVO.getBoardId());
        return "redirect:/myLife";
    }


    //글쓰기(나의 일대기) 상세페이지로 이동
    @GetMapping("/detail-my")
    public String detailMy(Model model, Long boardId, HttpSession session) {
        Long uniId = (Long) session.getAttribute("uniId");
        if (uniId == null) {
            return "redirect:/user/login";
        }
        BoardVO boards = boardService.selectById(boardId);
        boardService.boardIntViewCnt(boardId);
        String userNickname = boardService.boardUserName(boardId);
        // 유저 정보 모두
        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("boards", boards);
        model.addAttribute("userNickname", userNickname);

        //좋아요 상태 0617 추가
        LikeHeartDTO likeHeartDTO = new LikeHeartDTO();
        likeHeartDTO.setUserId(uniId); //해당 로그인한 유저의 id가지고 오기
        likeHeartDTO.setBoardId(boardId); //해당 보드id 가지고오기
        int likeStatus = myPageService.selectLikeStatus(likeHeartDTO);
        model.addAttribute("likeStatus", likeStatus);


        return "myLife/detail-my";
    }

    //해당 게시물 페이지 좋아요 기능 구현 컨트롤러 - 하트 클릭시
    @PostMapping("/detail-my")
    public String detailMy(HttpServletRequest request, @RequestParam Long boardId, @RequestBody Map<String, Object> requestBody) {
        Long uniId = (Long) request.getSession().getAttribute("uniId"); // session.getAttribute 사용
        if (uniId == null) {
            return "redirect:/user/login";
        }
        System.out.println("Post에 들어옴 ");

        // 좋아요 상태와 게시물 ID 받아오기
        Integer getLikeStatusBoolean = (Integer) requestBody.get("getLikeStatusBoolean"); // Integer로 변경
        Long getBoardId = Long.parseLong((String) requestBody.get("boardId"));
        // 좋아요 로직 LikeVO 설정
        LikeVO likeVO = new LikeVO();
        likeVO.setLikeId(myPageService.getLIkeSeqNext());
        likeVO.setUserId(uniId);
        likeVO.setBoardId(getBoardId);

        // 좋아요 취소 로직 LikeHeartDTO 설정
        LikeHeartDTO likeHeartDTO = new LikeHeartDTO();
        likeHeartDTO.setUserId(uniId);
        likeHeartDTO.setBoardId(getBoardId);

        // 좋아요 상태에 따라 처리
        if (getLikeStatusBoolean == 1) {
            myPageService.insertLike(likeVO);
            boardService.boardViewLike(getBoardId);
        } else {
            myPageService.deleteLike(likeHeartDTO);
        }

        // redirect 시에 boardId를 함께 전달하려면 쿼리 파라미터로 붙여야 함
        return "redirect:/myLife/detail-my?boardId=" + getBoardId;
    }




//    @GetMapping("/detail-my")
//    public String detailMy(Model model, Long boardId, @SessionAttribute("uniId") Long uniId){
//        if (uniId == null) {
//            return "redirect:/user/login";
//        }
//        BoardVO boards = boardService.selectById(boardId);
//        boardService.boardIntViewCnt(boardId);
//        // 유저 정보 모두
//        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
//        model.addAttribute("userInfo", userInfo);
//        model.addAttribute("boards",boards);
//        return "myLife/detail-my";
//    }

    //글쓰기(나의 일대기) 업데이트
    @GetMapping("/update_writingMode")
    public String updateWritingMode(Long boardId, Model model) {
        BoardVO boards = boardService.selectById(boardId);
        model.addAttribute("boards",boards);

        return "myLife/update_writingMode";
    }

    //글쓰기(나의 일대기) 업데이트
    @PostMapping("/update_writingMode")
    public String updateWritingMode(BoardVO boardVO,
                                    @RequestParam("boardFile") List<MultipartFile> files,
                                    RedirectAttributes redirectAttributes){
        try{
            boardService.modifyBoard(boardVO, files);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        redirectAttributes.addAttribute("boardId", boardVO.getBoardId());
        return "redirect:/myLife";
    }

    //게시판(나의 일대기) 삭제하기
    @GetMapping("/remove")
    public RedirectView removeBoard(Long boardId){
        boardService.removeBoard(boardId);
        return new RedirectView("/myLife");
    }

    @GetMapping("/mypageCommentList")
    public String mypageCommentList(Model model, HttpSession session, Criteria criteria){
        // 로그인 여부 확인

        Long uniId = (Long) session.getAttribute("uniId");
        if (uniId == null) {
            return "redirect:/login";
        }

        /* 페이징 된 댓글 목록 가져옴 */
        List<CommentDTO> comments = myPageService.findPageMyComment(criteria, uniId);

        /* 1 ) 전체 댓글 수 가져옴 */
        int total = myPageService.myCommentTotal(uniId);
        /* 2 ) page에 criteria랑 전체 댓글 수 전달 */
        Page page = new Page(criteria, total);
        // 회원 정보 모두
        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
        model.addAttribute("userInfo", userInfo);
        // 댓글 / 페이지
        model.addAttribute("comments", comments);
        model.addAttribute("page", page);

        /* 팔로워 팔로잉 수*/
        int followerCnt = myPageService.countFollower(uniId);
        int followingCnt = myPageService.countFollowing(uniId);
        model.addAttribute("followerCnt", followerCnt);
        model.addAttribute("followingCnt", followingCnt);

        return "myLife/mypageCommentList";
    }
    // 내가 쓴 댓글로
    // 좋아요 목록으로
    @GetMapping("/mypageLike")
    public String mypageLike(Model model, HttpSession session, Criteria criteria){
        // 로그인 여부 확인
        Long uniId = (Long) session.getAttribute("uniId");
        if (uniId == null) {
            return "redirect:/user/login";
        }

        List<LikeDTO> likes = myPageService.findPageMyLike(criteria, uniId);
        int total = myPageService.myLikeTotal(uniId);
        Page page = new Page(criteria, total);
        // 팔로우 : 이거 아직 html에 추가 안 함
//        FollowDTO follow = followService.selectFollowDetail(userId);
//        model.addAttribute("follow", follow);
        // 회원 정보 모두
        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
        model.addAttribute("userInfo", userInfo);
        // 좋아요 / 페이지
        model.addAttribute("likes", likes);
        model.addAttribute("page", page);

        /* 팔로워 팔로잉 수*/
        int followerCnt = myPageService.countFollower(uniId);
        int followingCnt = myPageService.countFollowing(uniId);
        model.addAttribute("followerCnt", followerCnt);
        model.addAttribute("followingCnt", followingCnt);

        return "/myLife/mypageLike";
    }
    // 회원정보 수정으로 -------------------------
    @GetMapping("/mypageEditMemberInformation")
    public String mypageEditMemberInformation(HttpSession session, Model model,
                                              @SessionAttribute("uniId") Long uniId){
        // 로그인 여부 확인
        if (uniId == null) {
            return "redirect:/login";
        }
        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
        System.out.println(uniId);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("lifeUserUpdateDTO", lifeUserUpdateDTO);
        System.out.println(lifeUserUpdateDTO);
        return "/myLife/mypageEditMemberInformation";
    }


    // 회원정보 수정으로 post
    @PostMapping("/mypageEditMemberInformation")
    public String mypageEditMemberInformation(UserFileVO userFileVO, RedirectAttributes redirectAttributes,
                                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                              @RequestParam(value = "backgroundImage", required = false) MultipartFile backgroundImage,
                                              @SessionAttribute("uniId") Long uniId,
                                              @RequestParam("year") String year,
                                              @RequestParam("month") String month,
                                              @RequestParam("day") String day,
                                              LifeUserInfoDTO infoDTO, LifeUserUpdateDTO lifeUserUpdateDTO) {
        // 로그인 여부 확인
        if (uniId == null) {
            return "redirect:/login";
        }

        // 누구껀지 아이디
        lifeUserInfoDTO.setUserId(uniId);
        userFileVO.setUserId(uniId);
        lifeUserUpdateDTO.setUserId(uniId);

        /* 입력된 값이 없으면 기존 값으로 사용 */
        if(infoDTO.getNickname() == null || lifeUserUpdateDTO.getNickname().isEmpty()){
            lifeUserUpdateDTO.setNickname(infoDTO.getNickname());
        }
        if(infoDTO.getUniAbout() == null || lifeUserUpdateDTO.getUniAbout().isEmpty()){
            lifeUserUpdateDTO.setUniAbout(infoDTO.getUniAbout());
        }
        String formattedDate = String.format("%s-%02d-%02d", year, Integer.parseInt(month), Integer.parseInt(day));
        LocalDate birthDate = LocalDate.parse(formattedDate);
        infoDTO.setUserBirth(birthDate.atStartOfDay());
        lifeUserUpdateDTO.setUserBirth(LocalDate.from(infoDTO.getUserBirth()));
        System.out.println("birthDate = " + birthDate);
        System.out.println(infoDTO);
        System.out.println(lifeUserUpdateDTO);



        // files가 null인 경우 빈 리스트로 초기화
/*
        if (file == null) {
            file = Collections.emptyList();
        }
*/
// 하.. 프사 배사 나눠서 내일 다시하자...
        try {
            myPageService.registProfileBackFile(userFileVO, profileImage, backgroundImage);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 실패");
            return "redirect:/myLife/mypageEditMemberInformation"; // 파일 업로드 실패 시 리다이렉트
        }
        System.out.println(userFileVO.getUserFileProfileName());

        try {
            myPageService.totalUpdateInfo(lifeUserUpdateDTO);
            System.out.println(lifeUserUpdateDTO);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "회원 정보 업데이트 실패");
            return "redirect:/myLife/mypageEditMemberInformation"; // 업데이트 실패 시 리다이렉트
        }
        redirectAttributes.addFlashAttribute("uniId", userFileVO.getUserId());
        // 폼 제출 후 리다이렉트
        return "redirect:/myLife";
    }



    // 알림으로
    @GetMapping("/mypageNotification")
    public String mypageNotification(Model model, HttpSession session, Criteria criteria,   @SessionAttribute("uniId") Long uniId){
        // 로그인 여부 확인
        if (uniId == null) {
            return "redirect:/login";
        }

        // 리스트 및 페이징
        List<ReportListDTO> reports = myPageService.selectPageMyNotification(criteria, uniId);
        int total = myPageService.myNotificationTotal(uniId);
        Page page = new Page(criteria, total);

        // 회원 정보 모두
        LifeUserInfoDTO userInfo = myPageService.selectAllInfo(uniId);
        model.addAttribute("userInfo", userInfo);
        // 알림 리스트 / 페이지
        model.addAttribute("reports", reports);
        model.addAttribute("page", page);

        int followerCnt = myPageService.countFollower(uniId);
        int followingCnt = myPageService.countFollowing(uniId);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("followerCnt", followerCnt);
        model.addAttribute("followingCnt", followingCnt);
        return "/myLife/myPageNotification";
    }

    // 비밀번호 변경으로
    @GetMapping("/mypagePasswordChange")
    public String changepassword(){
        return "/myLife/mypagePasswordChange";
    }

    //나이에 따른 LifeCycle 지정해주는 메소드
    // 나이에 따른 LifeCycle 지정해주는 메소드
    private String calculateLifeCycle(int age) {
        if (age >= 65) {
            return "노년기";
        } else if (age >= 35) {
            return "중년기";
        } else if (age >= 20) {
            return "청년기";
        } else if (age >= 13) {
            return "청소년기";
        } else if (age >= 9) {
            return "아동기";
        } else if (age >= 6) {
            return "유년기";
        } else if (age >= 0) {
            return "유아기";
        } else {
            return "유아기 미만";
        }
    }

}

