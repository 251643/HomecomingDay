package com.homecomingday.util;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleChange {

    public static String changeImage(String userImage) {

        if(userImage ==null){
            return "https://woochangbk.s3.ap-northeast-2.amazonaws.com/52a5f48d-7452-48a6-b167-1497931c7cf7%ED%94%84%EB%A1%9C%ED%95%84%20%EC%9D%B4%EB%AF%B8%EC%A7%80%20%EC%A0%80%EC%9E%A5%EC%9A%A9.png";

        }else {
            return userImage;
        }

    }

    public static String changearticleFlag(String articleFlag) {
        if (articleFlag.equals("help")) {
            return "도움요청";
        } else if (articleFlag.equals("freeTalk")) {
            return "자유토크";
        } else if (articleFlag.equals("information")) {
            return "정보공유";
        } else if (articleFlag.equals("calendar")) {
            return "만남일정";
        }
        return null;
    }

    public static String changeCalendarDate(String calendarDate){

        if(calendarDate.substring(14).equals("Monday")){
            return calendarDate.substring(0, 14) + "월요일";
        }else if(calendarDate.substring(14).equals("Tuesday")) {
            return calendarDate.substring(0, 14) + "화요일";
        }else if(calendarDate.substring(14).equals("Wednesday")) {
            return calendarDate.substring(0, 14) + "수요일";
        }else if(calendarDate.substring(14).equals("Thursday")) {
            return calendarDate.substring(0, 14) + "목요일";
        }else if(calendarDate.substring(14).equals("Friday")) {
            return calendarDate.substring(0, 14) + "금요일";
        }else if(calendarDate.substring(14).equals("Saturday")) {
            return calendarDate.substring(0, 14) + "토요일";
        }else if(calendarDate.substring(14).equals("Sunday")) {
            return calendarDate.substring(0, 14) + "일요일";
        }else
            return null;
    }


    public static String changeNoticeType(String noticeType) {
        if (noticeType.equals("comment")) {
            return "댓글";
        } else if(noticeType.equals("heart")){
            return "좋아요";
        }else if(noticeType.equals("childcomment")){
            return "대댓글";
        }
        return null;
    }
}
