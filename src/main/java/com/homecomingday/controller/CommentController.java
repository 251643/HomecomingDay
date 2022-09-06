package com.homecomingday.controller;


import com.homecomingday.controller.request.CommentRequestDto;
import com.homecomingday.controller.response.CommentChangeDto;
import com.homecomingday.controller.response.CommentResponseDto;
import com.homecomingday.controller.response.ResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.CommentRepository;
import com.homecomingday.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    //댓글 게시
    @PostMapping("/article/comment/{Id}")
    public CommentResponseDto writeComment(@PathVariable Long Id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.writeComment(Id,commentRequestDto,userDetails);
    }


    //댓글 수정
    @PutMapping("/article/{articleId}/comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long articleId, @PathVariable Long commentId,@RequestBody CommentRequestDto commentRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
       CommentResponseDto commentResponseDto=commentService.updateComment(articleId,commentId,commentRequestDto,userDetails);

        return ResponseDto.success(commentResponseDto);

    }

    //댓글 삭제
    @DeleteMapping("/article/{articleId}/comment/{commentId}")
    public CommentChangeDto deleteComment(@PathVariable Long articleId,@PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(articleId,commentId,userDetails);
    }
}
