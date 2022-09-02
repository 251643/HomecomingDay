package com.homecomingday.service;


import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.controller.response.ArticleResponseDto;
import com.homecomingday.controller.response.CommentDto;
import com.homecomingday.domain.*;
import com.homecomingday.repository.FreeCommentRepository;
import com.homecomingday.repository.FreeRepository;
import com.homecomingday.repository.ImageRepository;
import com.homecomingday.repository.MemberRepository;
import com.homecomingday.service.s3.S3Uploader;
import com.homecomingday.util.Time;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class FreeService {

    private final UserDetailsServiceImpl userDetailsService;
    private final FreeCommentRepository freeCommentRepository;
    private final FreeRepository freeRepository;
    private final S3Uploader s3Uploader;
    private final Time time;
    private final ImageRepository imageRepository;





    private Long getTime() {
        Free free = new Free();
        return ChronoUnit.MINUTES.between(free.getCreatedAt(), LocalDateTime.now());

    }
    //게시글  전체조회
    public List<ArticleResponseDto>readAllFree() {
        return freeRepository.findAllByOrderByCreatedAtDesc();
    }



    //게시글 생성
    @Transactional
    public ArticleResponseDto postFree(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto, UserDetailsImpl userDetails) throws IOException {

        List<Image> imageList = new ArrayList<>();
        if (multipartFile != null) {
            //          이미지 업로드
//            imageList.add(s3Uploader.upload(multipartFile));

            Free free = Free.builder()
                    .title(articleRequestDto.getTitle())
                    .content(articleRequestDto.getContent())
                    .member(userDetails.getMember())
                    .build();
            freeRepository.save(free);


            for (MultipartFile uploadedFile : multipartFile) {
                s3Uploader.upload(uploadedFile);

                Image image = Image.builder()
                        .imgUrl((s3Uploader.upload(uploadedFile)))
                        .build();

                imageRepository.save(image);
            }

//            작성시간 조회

            long rightNow = ChronoUnit.MINUTES.between(free.getCreatedAt(), LocalDateTime.now());
            ArticleResponseDto articleResponseDto = new ArticleResponseDto(free, Time.times(rightNow));
            return articleResponseDto;

        } else {
            Free free = Free.builder()
                    .title(articleRequestDto.getTitle())
                    .content(articleRequestDto.getContent())
                    .member(userDetails.getMember())
                    .build();
            freeRepository.save(free);
//            작성시간 조회

            long rightNow = ChronoUnit.MINUTES.between(free.getCreatedAt(), LocalDateTime.now());
            ArticleResponseDto articleResponseDto = new ArticleResponseDto(free, Time.times(rightNow));
            return articleResponseDto;
        }

    }


   // 게시글 상세 조회
    public ArticleResponseDto readFree(Long articleId,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        Free free= freeRepository.findById(articleId)
                .orElseThrow(()->new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        List<FreeComment> freeCommentList = freeCommentRepository.findByFree_Id(articleId);
        List<CommentDto> commentDtoList=new ArrayList<>();

        long articlePresentTime=ChronoUnit.MINUTES.between(free.getCreatedAt(),LocalDateTime.now());

        for(FreeComment comment : freeCommentList){
            commentDtoList.add(
                            CommentDto.builder()
                                    .commentId(comment.getId())
                                    .username(comment.getMember().getUsername())
                                    .content(comment.getContent())
                                    .createdAt(Time.times(ChronoUnit.MINUTES.between(free.getCreatedAt(), LocalDateTime.now())))
                                    .build()
            );
        }


        List<String> data=new ArrayList<>();

        List<Image> target = imageRepository.findAllByBoardName(articleId);
        for(Image image : target){
            data.add(image.getImgUrl());
        }

        ArticleResponseDto articleResponseDto=new ArticleResponseDto(free,data, Time.times(articlePresentTime),commentDtoList);
        return articleResponseDto;
    }
}
