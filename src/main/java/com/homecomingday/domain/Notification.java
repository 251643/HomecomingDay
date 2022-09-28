package com.homecomingday.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecomingday.util.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    //알림 메세지
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean readState;

    @Column
    private String title;


    @Column(nullable = false)
    private Long url;


    @ManyToOne
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_member_id")
    private Member member;

    @ManyToOne
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_comment_id")
    private Comment comment;


    @Builder
    public Notification(NoticeType noticeType, String message, Boolean readState,
                        Long articlesId, Member receiver, String title,Comment comment) {
        this.noticeType = noticeType;
        this.message = message;
        this.readState = readState;
        this.url = articlesId;
        this.member = receiver;
        this.title = title;
        this. comment = comment;
    }

    public void changeState() {
        readState = true;
    }



}