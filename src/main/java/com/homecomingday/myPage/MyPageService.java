package com.homecomingday.myPage;

import com.homecomingday.myPage.responseDto.MyPageDetailResponseDto;
import com.homecomingday.myPage.responseDto.MyPageResponseDto;
import com.homecomingday.domain.*;
import com.homecomingday.article.ArticleRepository;
import com.homecomingday.member.MemberRepository;
import com.homecomingday.shared.s3.S3Uploader;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;


    //유저 정보 조회
    public MyPageResponseDto readMyPage(UserDetailsImpl member) {

        int articleCnt = articleRepository.countByMember(member.getMember());

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .schoolName(member.getMember().getSchoolName())
                .userId(member.getMember().getId())
                .email(member.getUsername())
                .username(member.getMember().getUsername())
                .userImage(changeImage(member.getMember().getUserImage()))
                .departmentName(member.getMember().getDepartmentName())
                .admission(member.getMember().getAdmission().substring(2,4)+"학번")
                .articleCnt(articleCnt)
                .build();
        return myPageResponseDto;
    }


    //이미지 수정시 마이페이지 입력값 다 수정

    private String changeImage(String userImage) {

        if(userImage ==null){
            return "https://woochangbk.s3.ap-northeast-2.amazonaws.com/52a5f48d-7452-48a6-b167-1497931c7cf7%ED%94%84%EB%A1%9C%ED%95%84%20%EC%9D%B4%EB%AF%B8%EC%A7%80%20%EC%A0%80%EC%9E%A5%EC%9A%A9.png";

        }else {
            return userImage;
        }

    }


    //유저 정보 이미지 생성
    @Transactional
    public MyPageResponseDto updateMyPage(UserDetailsImpl member, MultipartFile multipartFile) throws IOException {
        System.out.println("프사 수정 시도");
        System.out.println("들어온 사진 : " + multipartFile);

        Member signupMember = memberRepository.findByEmail(member.getUsername()).orElse(null);

        String s3Dto = s3Uploader.upload1(multipartFile);

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .schoolName(member.getMember().getSchoolName())
                .email(member.getUsername())
                .username(member.getMember().getUsername())
                .userImage(s3Dto)
                .departmentName(member.getMember().getDepartmentName())
                .admission(member.getMember().getAdmission().substring(2,4)+"학번")
                .build();
        signupMember.updateMyPage(myPageResponseDto);
        return myPageResponseDto;




    }


    //# 내가 쓴 게시글 조회
    public List<MyPageDetailResponseDto> readDetailMyPage(UserDetailsImpl member) {

        List<Article> articleList = articleRepository.findAll();
        List<MyPageDetailResponseDto> myPageDetailResponseDtoList = new ArrayList<>();

        for (Article articles : articleList) {

            if (articles.getMember().getUsername().equals(member.getMember().getUsername())) {


                myPageDetailResponseDtoList.add(
                        MyPageDetailResponseDto.builder()
                                .articleId(articles.getId())
                                .title(articles.getTitle())
                                .username(articles.getMember().getUsername())
                                .departmentName(articles.getMember().getDepartmentName())
                                .createdAt(Time.convertLocaldatetimeToTime(articles.getCreatedAt()))
                                .admission(articles.getMember().getAdmission().substring(2,4)+"학번")
                                .articleFlag(changearticleFlag(articles.getArticleFlag()))
                                .views(articles.getViews())
                                .heartCnt( articles.getHeartCnt())
                                .commentCnt((long) articles.getComments().size()) // 0으로 기본세팅
                                .build()
                );
            }
        }
        return myPageDetailResponseDtoList;
    }
    public String changearticleFlag(String articleFlag) {
        if (articleFlag.equals("help")) {
            return "도움요청";
        } else if(articleFlag.equals("freeTalk")){
            return "자유토크";
        }else if(articleFlag.equals("information")){
            return "정보공유";
        }else if(articleFlag.equals("calendar")){
            return "만남일정";
        }
        return null;
    }


}



