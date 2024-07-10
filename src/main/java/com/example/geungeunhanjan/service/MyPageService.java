package com.example.geungeunhanjan.service;

import com.example.geungeunhanjan.domain.dto.board.*;
import com.example.geungeunhanjan.domain.dto.lifePage.Criteria;
import com.example.geungeunhanjan.domain.vo.board.BoardVO;
import com.example.geungeunhanjan.domain.vo.board.LikeVO;
import com.example.geungeunhanjan.domain.vo.board.ReportVO;
import com.example.geungeunhanjan.domain.vo.file.UserFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiListUI;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MyPageService {
    // 1. 내 좋아요 목록 select
    List<LikeDTO>selectMyLike(Long userId);

    //좋아요 기능 추가하기 0617
    //좋아요의 다음 시퀀스
    Long getLIkeSeqNext();
    //좋아요 상태 확인하기
    int selectLikeStatus(LikeHeartDTO likeHeartDTO);
    //해당 게시글 좋아요 클릭시 ; 좋아요 눌르기
    void insertLike(LikeVO likeVO);
    //해당 게시글 좋아요 클릭시 ; 좋아요 취소하기
    void deleteLike(LikeHeartDTO likeHeartDTO);

    // 페이징
    List<LikeDTO> findPageMyLike(Criteria criteria, Long userId);
    int myLikeTotal(Long userId);
    // 2. 내가 쓴 댓글 목록 select
    List<CommentDTO> selectMyComment(Long userId);
    //페이징
    List<CommentDTO> findPageMyComment(Criteria criteria, Long userId);
    int myCommentTotal(Long userId);

    // 4. 회원정보 수정
    // 4-1. 프사 / 배사 파일 !!
    void registProfileBackFile(UserFileVO userFileVO, MultipartFile profileImage, MultipartFile backgroundImage) throws IOException;
    UserFileVO getProfileBackFile(Long userId);
    void deleteFile(Long userId);
    /* 4-2. 회원 텍스트 정보 수정*/
    /* 업데이트 3번째 06-04 */
    void totalUpdateInfo(LifeUserUpdateDTO lifeUserUpdateDTO) throws IOException;

    /* 5. 회원 정보 셀렉트 : 파일, 유저, 유니, 카카오 */
    LifeUserInfoDTO selectAllInfo (Long userId);
    Map<String, Integer> sliceBirth(Long userId);

    // 6. 마이페이지 페이징 - 윤근님꺼
    List<BoardVO> selectMypagePaging(Criteria criteria, Long userId);
    int myPageTotal(Long userId);

    // 7. 팔로워 / 팔로잉 수
    int countFollower(Long userId);
    int countFollowing(Long userId);

    // 8. 신고
    void insertReport(ReportVO reportVO);

    // 9. 내 알림 목록 + 페이징
    List<ReportListDTO>selectPageMyNotification(Criteria criteria, Long userId);
    int myNotificationTotal(Long userId);
}
