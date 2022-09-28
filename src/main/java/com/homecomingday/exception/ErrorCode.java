package com.homecomingday.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST: 잘못된 요청 */
    /* 404 NOT_FOUND: 리소스를 찾을 수 없음 */
    /* 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출 */
    /* 500 INTERNAL_SERVER_ERROR: 내부 서버 오류 */

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"400", "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "내부 서버 오류입니다."),


    //member
    DUPLE_EMAIL(HttpStatus.BAD_REQUEST,  "M001","중복된 이메일 입니다."),
    INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "M002", "인증코드가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,  "M003","사용자를 찾을 수 없습니다."),
    CONFIRM_EMAIL_PWD(HttpStatus.BAD_REQUEST, "M004", "이메일 또는 비밀번호를 확인해주세요."),
    NOT_LOGIN(HttpStatus.BAD_REQUEST, "M005", "로그인이 필요합니다."),

    //보류
    //INVALID_OLD_PWD(HttpStatus.BAD_REQUEST, "400", "기존 비밀번호가 옳바르지 않습니다."),
    //NOT_EXPIRED_TOKEN_YET(HttpStatus.BAD_REQUEST,"400", "토큰이 만료되지 않았습니다."),
    //INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"400","유효하지 않은 리프레시 토큰입니다."),
    //NEED_EMAIL(HttpStatus.BAD_REQUEST,"400","이메일은 필수로 동의 해주셔야 합니다."),

    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED,"M006", "토큰이 존재하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"M007", "만료된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED,"M008", "유효한 토큰이 아닙니다."),
    UnSupported_Token(HttpStatus.UNAUTHORIZED, "M009", "올바른 JWT 토큰을 입력해주세요"),
    Signature_Exception(HttpStatus.BAD_REQUEST, "M010", "변조된 JWT 토큰입니다"),


    //Article
    ARTICLES_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 게시글입니다."),

    //Join
    PARTICIPANT_NOT_JOIN(HttpStatus.INTERNAL_SERVER_ERROR,"J001","최대 인원에 도달했습니다"),


    //comment, commit
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 댓글입니다."),

    //image
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "존재하지 않는 이미지입니다."),




    // 알림 URL, MESSAGE
//    NOT_VALIDURL(HttpStatus.BAD_REQUEST,"400","유효하지 않는 URL 입니다."),
//    NOT_VALIDMESSAGE(HttpStatus.BAD_REQUEST,"400","유효하지 않는 내용입니다."),
//    NOT_EXIST_NOTIFICATION(HttpStatus.BAD_REQUEST,"400","존재하지 않는 알림입니다. 새로고침 해주세요."),



    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;

}