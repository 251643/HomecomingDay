package com.homecomingday.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecomingday.controller.request.ArticleRequestDto;
import com.homecomingday.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Article extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length=400)
    private String content;

    @Column(nullable = false)
    private Long views;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String articleFlag;

    @Column
    private String calendarDate;

    @Column
    private String calendarTime;

    @Column
    private String calendarLocation;

    @Column
    private Integer maxPeople;

    @Column
    private String schoolName;

    @JsonIgnore
    private long heartCnt;

    //  @Transient //사진은 보여지기만 하면 되므로 불필요하게 관게를 맺기보단 일시적으로 체류만 시켜주면됨
    @JsonManagedReference
    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List <Image>imageList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List <Comment>comments= new ArrayList<>();


    public void updateArticle(ArticleRequestDto articleRequestDto) {
        this.title=articleRequestDto.getTitle();
        this.content=articleRequestDto.getContent();
        this.calendarDate=articleRequestDto.getCalendarDate();
        this.calendarTime=articleRequestDto.getCalendarTime();
        this.calendarLocation=articleRequestDto.getCalendarLocation();
        this.maxPeople=articleRequestDto.getMaxPeople();
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Heart> heartList = new ArrayList<>();


    public void addHeart(Heart heart) {
        this.heartList.add(heart);
    }
    public void removeHeart(Heart heart) {
        this.heartList.remove(heart);
    }

    public void setHeartCnt(int heartListSize) {
        this.heartCnt = heartListSize;
    }

}
