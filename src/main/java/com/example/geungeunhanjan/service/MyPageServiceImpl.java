package com.example.geungeunhanjan.service;

import com.example.geungeunhanjan.domain.dto.board.*;
import com.example.geungeunhanjan.domain.dto.lifePage.Criteria;
import com.example.geungeunhanjan.domain.vo.board.BoardVO;
import com.example.geungeunhanjan.domain.vo.board.LikeVO;
import com.example.geungeunhanjan.domain.vo.board.ReportVO;
import com.example.geungeunhanjan.domain.vo.file.UserFileVO;
import com.example.geungeunhanjan.domain.vo.user.UserVO;
import com.example.geungeunhanjan.mapper.lifes.MyPageMapper;
import com.example.geungeunhanjan.mapper.lifes.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    // 마이페이지
    private final MyPageMapper myPageMapper;
    private final ReportMapper reportMapper;
    private final UserFileVO userFileVO;
    private final UserVO userVO;

    //1. 내가 좋아요 한 목록 ~!
    @Override
    public List<LikeDTO> selectMyLike(Long userId) {
        return myPageMapper.selectMyLike(userId);
    }

    //좋아요 기능 추가하기 0617
    //1. 좋아요 다음 시퀀스
    @Override
    public Long getLIkeSeqNext() {
        return myPageMapper.getLIkeSeqNext();
    }
    //2.좋아요 상태 확인하기
    @Override
    public int selectLikeStatus(LikeHeartDTO likeHeartDTO) {
        return myPageMapper.selectLikeStatus(likeHeartDTO);
    }
    //3.좋아요 누르기
    @Override
    public void insertLike(LikeVO likeVO) {
        myPageMapper.insertLike(likeVO);
    }
    //4.좋아요 취소하기
    @Override
    public void deleteLike(LikeHeartDTO likeHeartDTO) {
        myPageMapper.deleteLike(likeHeartDTO);
    }

    //페이징
    @Override
    public List<LikeDTO> findPageMyLike(Criteria criteria, Long userId) {
        return myPageMapper.selectPageMyLike(criteria, userId);
    }

    @Override
    public int myLikeTotal(Long userId) {
        return myPageMapper.myLikeTotal(userId);
    }

    //2. 내가 쓴 댓글 select
    @Override
    public List<CommentDTO> selectMyComment(Long userId) {
        return myPageMapper.selectMyComment(userId);
    }

    //페이징
    @Override
    public List<CommentDTO> findPageMyComment(Criteria criteria, Long userId) {
        return myPageMapper.selectPageMyComment(criteria, userId);
    }

    @Override
    public int myCommentTotal(Long userId) {
        return myPageMapper.myCommentTotal(userId);
    }

    // 4. 회원정보 수정 ----------------------------------------------------------------------------------------------------
    // 4-1. 프사 / 배사 파일 !!
    @Value("C:/upload/")//  프로퍼티에 저장된 파일 프로퍼티값을 필드에 넣어줌
    private String fileDir;

    @Override
    public void registProfileBackFile(UserFileVO userFileVO, MultipartFile profileImage, MultipartFile backgroundImage) throws IOException {  // 프사 / 배사 등록
        /* 파일 이름 경로 재설정한 객체를 받아서 저장함
         *  유저 아이디 넣어줌
         *  파일 정보를 DB에 저장 */

        // 기존 파일 삭제
        myPageMapper.deleteFile(userFileVO.getUserId());

        // 프사
        if (profileImage != null && !profileImage.isEmpty()) {
            UserFileVO profile = renameResourceFile(profileImage); // ☆★☆★☆★ 프로필 이미지 파일 재설정 및 저장
            profile.setUserId(userFileVO.getUserId()); // 사용자 ID 설정
            userFileVO.setUserFileProfileName(profileImage.getOriginalFilename()); // ☆★☆★☆★ 프로필 이미지 이름 설정
            userFileVO.setUserFileProfileSource(profile.getUserFileProfileSource()); // ☆★☆★☆★ 프로필 이미지 경로 설정
            userFileVO.setUserFileProfileUuid(profile.getUserFileProfileUuid()); // ☆★☆★☆★ 프로필 이미지 UUID 설정
        }

        // 배경 이미지 처리
        if (backgroundImage != null && !backgroundImage.isEmpty()) {
            UserFileVO back = renameResourceFile(backgroundImage); // ☆★☆★☆★ 배경 이미지 파일 재설정 및 저장
            back.setUserId(userFileVO.getUserId()); // 사용자 ID 설정
            userFileVO.setUserFileBackName(backgroundImage.getOriginalFilename()); // ☆★☆★☆★ 배경 이미지 이름 설정
            userFileVO.setUserFileBackSource(back.getUserFileBackSource()); // ☆★☆★☆★ 배경 이미지 경로 설정
            userFileVO.setUserFileBackUuid(back.getUserFileBackUuid()); // ☆★☆★☆★ 배경 이미지 UUID 설정
        }
        myPageMapper.insertFileById(userFileVO); // 디비에 저장
    }

    private String getUploadPath() {
        /* 밑에 renameResourceFile에서 쓸 경로 뒤에 붙여질 형식임 */
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    public UserFileVO renameResourceFile(MultipartFile files) throws IOException {
        /* 1. 사용자가 올린 파일 이름을 가져와 UUID 붙여주고 경로를 합침
         *  2. 경로가 없다면 경로가 필요한 곳에 경로 생성해줌
         *  3. 파일이름 + 전체경로 연결, 객체가 가진 파일을 우리가 만든 이름으로 저장해서 FileVO의 객체를 반환*/

        // files : 업로드된 파일 객체

        // 파일이름 생성 : 오리지널 _ uuid
        String originalFilename = files.getOriginalFilename(); // getOriginalFilename() : 객체에 저장된 원본 파일이름을 반환함!
        UUID uuid = UUID.randomUUID();
        String nameAddUuid = uuid.toString() + "_" + originalFilename;


        // 경로지정
        File uploadPath = new File(fileDir, getUploadPath());
        /*fileDir, getUploadPath 합쳐서 File객체를 생성함, 기본디렉터리 / 추가경로 <--- 이 구성임
         * fileDir는 "C:/upload/"이고  getUploadPath()는 yyyy/MM/dd */

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        } // 경로 없으면 갖다 만들어라~


        // 경로 + 파일이름 : C:/upload/yyyy/MM/dd/오리지널 _ uuid
        File uploadFile = new File(uploadPath, nameAddUuid); // C:/upload/yyyy/MM/dd/파일이름.jpg


        // 매개변수로 받은 Multipart객체가 가진 파일을 우리가 만든 경로랑 이름으로 재설정~☆☆☆☆
        files.transferTo(uploadFile); // C:/upload/yyyy/MM/dd/파일이름.jpg <-- 이 이름으로 MultipartFile의 file이름을 대체함

        // 새 객체를 만들어서 우리가 만든 이름을 넣고 걔를 반환할 것
        UserFileVO finalUserFileVO = new UserFileVO();


        finalUserFileVO.setUserFileProfileUuid(uuid.toString());
        finalUserFileVO.setUserFileProfileName(originalFilename);
        finalUserFileVO.setUserFileProfileSource(getUploadPath()); // /yyyy/MM/dd 이걸 넣어줌
        finalUserFileVO.setUserFileBackUuid(uuid.toString());
        finalUserFileVO.setUserFileBackName(originalFilename);
        finalUserFileVO.setUserFileBackSource(getUploadPath()); // /yyyy/MM/dd 이걸 넣어줌

        return finalUserFileVO;

    }


    @Override
    public UserFileVO getProfileBackFile(Long userId) {   // 프사 / 배사 셀렉
        return myPageMapper.selectFileById(userId);
    }

    @Override
    public void deleteFile(Long userId) {}

    /* 4. 업데이트 3번째 06-04 */
    /* 여기서 회원 정보 업데이트*/
    @Override
    public void totalUpdateInfo(LifeUserUpdateDTO lifeUserUpdateDTO) throws IOException {
        myPageMapper.updateTwo(lifeUserUpdateDTO);
        Long userId = lifeUserUpdateDTO.getUserId();


        // 유저 정보 업데이트
        myPageMapper.mergeToUser(userId);
        myPageMapper.mergeToKakao(userId);
    }


    // 5. 회원정보 전체 불러오기
    @Override
    public LifeUserInfoDTO selectAllInfo(Long userId) {
        return myPageMapper.selectAllInfo(userId);
    }

    /* 생일 쪼개서 가져오기 */
    @Override
    public Map<String, Integer> sliceBirth(Long userId) {
        LifeUserInfoDTO userInfo = selectAllInfo(userId);
        LocalDateTime birthDate = userInfo.getUserBirth();

        Map<String, Integer> birthDateMap = new HashMap<>();
        birthDateMap.put("year", birthDate.getYear());
        birthDateMap.put("month", birthDate.getMonthValue());
        birthDateMap.put("day", birthDate.getDayOfMonth());
        return birthDateMap;
    }

    // 6. 마이페이지 페이징 - 윤근님꺼
    @Override
    public List<BoardVO> selectMypagePaging(Criteria criteria, Long userId) {
        return myPageMapper.selectMypagePaging(criteria, userId);
    }

    @Override
    public int myPageTotal(Long userId) {
        return myPageMapper.selectMypageTotalCount(userId);
    }

    // 7. 팔로워 / 팔로잉 수
    @Override
    public int countFollower(Long userId) {
        return myPageMapper.countFollower(userId);
    }

    @Override
    public int countFollowing(Long userId) {
        return myPageMapper.countFollowing(userId);
    }

    // 8. 신고
    @Override
    public void insertReport(ReportVO reportVO) {
        ReportVO isExistReport = reportMapper.selectReportByCommentId(reportVO.getCommentId());
        if (isExistReport != null ) {
            // 신고가 존재 : count, reason만 업데이트
            isExistReport.setReportReason(reportVO.getReportReason());
            reportMapper.incReportCnt(reportVO);
            System.out.println("신고가 존재했고 신고 업데이트 처리했습니다. ");
        }else{
            // 신고 존재 x : 새로 insert
            reportMapper.insertReport(reportVO);
            System.out.println("신고가 존재하지 않았고 신고문을 새로 인서트했습니다.");
        }
    }

    // 9. 내 알림 목록 + 페이징
    @Override
    public List<ReportListDTO> selectPageMyNotification(Criteria criteria, Long userId) {
        return myPageMapper.selectPageMyNotification(criteria, userId);
    }

    @Override
    public int myNotificationTotal(Long userId) {
        return myPageMapper.myNotificationTotal(userId);
    }


}