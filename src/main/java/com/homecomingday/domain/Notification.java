package com.homecomingday.domain;

import com.homecomingday.util.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * 알림 Entity
 */

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

    /**
     * 알림 message
     */
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean readState;

    /**
     * 클릭 시 이동할 수 있는 link 필요
     */

    @Column(nullable = false)
    private Long url;

    /**
     * 멤버 변수이름 변경
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_member_id")
    private Member receiver;

    @Column
    private String title;


    @Builder
    public Notification(NoticeType noticeType, String message, Boolean readState,
                        Long articlesId, Member receiver, String title) {
        this.noticeType = noticeType;
        this.message = message;
        this.readState = readState;
        this.url = articlesId;
        this.receiver = receiver;
        this.title = title;
    }

    public void changeState() {
        readState = true;
    }




}