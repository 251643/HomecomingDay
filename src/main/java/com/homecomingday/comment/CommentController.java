package com.homecomingday.comment;


import com.homecomingday.comment.requestDto.CommentRequestDto;
import com.homecomingday.comment.responseDto.CommentResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/article/{articleFlag}/comment/{articleId}")
    public CommentResponseDto writeComment(@PathVariable String articleFlag,@PathVariable Long articleId,@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.writeComments(articleFlag,articleId,commentRequestDto,userDetails);
    }


    //댓글 수정
    @PutMapping("/article/{articleFlag}/{articleId}/comment/{commentId}")
    public String updateComment(@PathVariable String articleFlag,@PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//       CommentResponseDto commentResponseDto=commentService.updateComment(articleId,commentId,commentRequestDto,userDetails);
        return commentService.updateComments(articleFlag,articleId,commentId,commentRequestDto,userDetails);
    }

    //댓글 삭제
    @DeleteMapping("/article/{articleFlag}/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable String articleFlag,@PathVariable Long articleId,@PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComments(articleFlag,articleId,commentId,userDetails);
    }
}
