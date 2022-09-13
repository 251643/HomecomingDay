package com.homecomingday.controller;

import com.homecomingday.controller.request.CommentRequestDto;
import com.homecomingday.controller.response.CommitResponseDto;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.service.CommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommitController {

    private final CommitService commitService;

    //대댓글 생성
    @PostMapping("/article/{articleFlag}/{articleId}/comment/{commentId}")
    public CommitResponseDto createCommit(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commitService.createCommit(commentId, commentRequestDto,userDetails);
    }

    //대댓글 수정 patch로 내용만 수정
    @PatchMapping("/article/{articleFlag}/{articleId}/comment/{commentId}/{commitId}")
    public String updateCommit(@PathVariable Long commitId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commitService.updateCommit(commitId, commentRequestDto,userDetails);
    }

    //대댓글 삭제
    @DeleteMapping("/article/{articleFlag}/{articleId}/comment/{commentId}/{commitId}")
    public String deleteteCommit(@PathVariable Long commitId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commitService.deleteteCommit(commitId,userDetails);
    }

}
