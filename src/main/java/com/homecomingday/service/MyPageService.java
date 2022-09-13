package com.homecomingday.service;

import com.homecomingday.controller.S3Dto;
import com.homecomingday.controller.response.ImagePostDto;
import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.controller.response.MyPageResponseDto;
import com.homecomingday.domain.*;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.repository.CommentRepository;
import com.homecomingday.repository.ImageRepository;
import com.homecomingday.repository.MemberRepository;
import com.homecomingday.service.s3.S3Uploader;
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

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()

                .schoolName(member.getMember().getSchoolname())
                .email(member.getUsername())
                .username(member.getMember().getUsername())
                .userImage(changeImage(member.getMember().getUserImage()))
                .departmentName(member.getMember().getDepartmentname())
                .admission(member.getMember().getAdmission().substring(2,4)+"학번")
                .build();
        return myPageResponseDto;
    }

    private String changeImage(String userImage) {

        if(userImage ==null){
            return "https://woochangbk.s3.ap-northeast-2.amazonaws.com/e778fd8b-8761-444f-8f0a-8f7cb56c3854%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF.png";

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
                .schoolName(member.getMember().getSchoolname())
                .email(member.getUsername())
                .username(member.getMember().getUsername())
                .userImage(s3Dto)
                .departmentName(member.getMember().getDepartmentname())
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
                                .departmentName(articles.getMember().getDepartmentname())
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



