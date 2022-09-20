package com.homecomingday.service;

import com.homecomingday.controller.request.CommentRequestDto;
import com.homecomingday.controller.response.CommitResponseDto;
import com.homecomingday.domain.Comment;
import com.homecomingday.domain.Commit;
import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.repository.CommentRepository;
import com.homecomingday.repository.CommitRepository;
import com.homecomingday.util.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommitService {

    private final CommentRepository commentRepository;
    private final CommitRepository commitRepository;


    // 대댓글 작성
    public CommitResponseDto createCommit(Long commentId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Comment comment  = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다"));


        Commit commit = Commit.builder()
                .content(commentRequestDto.getContent())
                .comment(comment)
                .member(userDetails.getMember())
                .article(comment.getArticle())
                .build();

        commitRepository.save(commit);
        comment.getCommits().add(commit);

        CommitResponseDto commitResponseDto = CommitResponseDto.builder()
                .childCommentId(commit.getId())
                .content(commit.getContent())
                .username(commit.getMember().getUsername())
                .userImage(changeImage(commit.getMember().getUserImage()))
                .admission(commit.getMember().getAdmission())
                .departmentName(commit.getMember().getDepartmentName())
                .createdAt(Time.convertLocaldatetimeToTime(commit.getCreatedAt()))
                .build();

        return commitResponseDto;
    }
    //대댓긇 수정
    public CommitResponseDto updateCommit(Long commitId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        Commit commit  = commitRepository.findById(commitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다"));

        if (commit.getMember().getEmail().equals(userDetails.getUsername())) { //이름만 Username일뿐  email값을 비교하는 로직
            commit.updateCommit(commentRequestDto);
            CommitResponseDto commitResponseDto = CommitResponseDto.builder()
                    .childCommentId(commit.getId())
                    .content(commit.getContent())
                    .username(commit.getMember().getUsername())
                    .userImage(changeImage(commit.getMember().getUserImage()))
                    .admission(commit.getMember().getAdmission())
                    .departmentName(commit.getMember().getDepartmentName())
                    .createdAt(Time.convertLocaldatetimeToTime(commit.getCreatedAt()))
                    .build();

            return commitResponseDto;
        }
        return null;

    }

    public String deleteteCommit(Long commentId,Long commitId, UserDetailsImpl userDetails) {

        Comment comment  = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다"));
        Commit commit  = commitRepository.findById(commitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다"));


        if(userDetails.getUsername().equals(commit.getMember().getEmail())){
            commitRepository.delete(commit);
            comment.getCommits().remove(commit);

            return commitId +"삭제 성공되었습니다.";
        }
        return commitId +"삭제 실패했습니다";
    }

    private String changeImage(String userImage) {

        if(userImage ==null){
            return "https://woochangbk.s3.ap-northeast-2.amazonaws.com/5d44c9fd-1bac-47d3-9b68-17506b43491fKakaoTalk_20220913_175821034.png";

        }else {
            return userImage;
        }

    }
}
