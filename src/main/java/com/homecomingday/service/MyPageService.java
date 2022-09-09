package com.homecomingday.service;

import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.controller.response.MyPageResponseDto;
import com.homecomingday.domain.*;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.repository.CommentRepository;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;


    //유저 정보 조회
    public MyPageResponseDto readMyPage(UserDetailsImpl member) {
        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()

                .schoolName(member.getMember().getSchoolname())
                .email(member.getUsername())
                .username(member.getMember().getUsername())
                .departmentName(member.getMember().getDepartmentname())
                .admission(member.getMember().getAdmission().substring(2,4)+"학번")
                .build();
        return myPageResponseDto;
    }

    //# 내가 쓴 게시글 조회
    public List<MyPageDetailResponseDto> readDetailMyPage(UserDetailsImpl member) {

        List<Article> articleList = articleRepository.findAll();
        List<MyPageDetailResponseDto> myPageDetailResponseDtoList = new ArrayList<>();

        for (Article articles : articleList) {

            if (articles.getMember().getUsername().equals(member.getMember().getUsername())) {
                Long sizeCnt=0L;
                List<Comment>commentList=commentRepository.findbyArticle_Id(articles.getId()); //게시물 index 번호에 따라 뽑아옴

                for(Comment datas : commentList ){
                    if(datas.getArticle().getId().equals(articles.getId())){
                        sizeCnt++;
                    }
                }


                myPageDetailResponseDtoList.add(
                        MyPageDetailResponseDto.builder()
                                .articleId(articles.getId())
                                .title(articles.getTitle())
                                .username(member.getMember().getUsername())
                                .departmentName(member.getMember().getDepartmentname())
                                .createdAt(Time.convertLocaldatetimeToTime(articles.getCreatedAt()))
                                .admission(member.getMember().getAdmission().substring(2,4)+"학번")
                                .articleFlag(articles.getArticleFlag())
                                .views(articles.getViews())
                                .commentCnt(sizeCnt) // 0으로 기본세팅
                                .build()
                );
            }
        }
        return myPageDetailResponseDtoList;
    }
}



