package com.homecomingday.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
@Entity
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //GenerationType.IDENTITY : ID값이 서로 영향없이 자기만의 테이블 기준으로 올라간다.
    private Long id;

    @Column(nullable = false)
    private String urlPath;

    @Column(nullable = false)
    private String imgUrl;

    @Column
    private String boardName;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="free_id",nullable = false)
    private Free free;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="Help_id",nullable = false)
    private Help help;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="HoneyTip_id",nullable = false)
    private HoneyTip honeyTip;




    public Image(String urlPath,String imgUrl,Free free){

        this.imgUrl=imgUrl;
        this.free=free;
    }

    public Image(String urlPath,String imgUrl,Help help){

        this.imgUrl=imgUrl;
        this.help=help;
    }

    public Image(String urlPath,String imgUrl,HoneyTip honeyTip){

        this.imgUrl=imgUrl;
        this.honeyTip=honeyTip;
    }

}
