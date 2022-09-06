package com.homecomingday.service;

import com.homecomingday.controller.request.CommentRequestDto;
import com.homecomingday.controller.response.CommentChangeDto;
import com.homecomingday.controller.response.CommentResponseDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.Article;
import com.homecomingday.domain.Comment;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.ArticleRepository;
import com.homecomingday.repository.CommentRepository;
import com.homecomingday.util.Time;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;


    @Transactional
    public CommentResponseDto writeComment(Long id, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Article article1 = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .articleFlag(article1.getArticleFlag())
                .article(article1)
                .member(userDetails.getMember())
                .build();

        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .username(userDetails.getMember().getUsername())
                .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                .build();

        return commentResponseDto;
    }


    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long articleId, Long commentId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다"));


            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(commentId)
                    .content(commentRequestDto.getContent())
                    .username(userDetails.getMember().getUsername())
                    .admission(userDetails.getMember().getAdmission().substring(2, 4) + "학번")
                    .createdAt(Time.convertLocaldatetimeToTime(comment.getCreatedAt()))
                    .build();
            //중간에 학번을 변경했을때를 고려해서 comment.getArticle().getMember().getAdmission()에서 userDetails.getMember().getAdmission()로 변경


        if (comment.getMember().getEmail().equals(userDetails.getUsername())) { //이름만 Username일뿐  email값을 비교하는 로직
           comment.updateComment(commentRequestDto);
            return commentResponseDto;
        }
        return null;
    }

    public CommentChangeDto deleteComment(Long articleId, Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다"));
        CommentChangeDto commentChangeDto=new CommentChangeDto(commentId,"삭제가 실패했습니다");
        CommentChangeDto commentChangeDto1=new CommentChangeDto(commentId,"삭제가 성공되었습니다.");


       if(comment.getMember().getEmail().equals(userDetails.getUsername())){
           commentRepository.delete(comment);

           return commentChangeDto1;
       }
    return commentChangeDto;

    }
}