package com.homecomingday.repository;

import com.homecomingday.domain.Notification;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
@Repository
public interface EmitterRepository {

    //Emitter 저장
   SseEmitter save(String id, SseEmitter sseEmitter);
    //Emitter 삭제
    void deleteById(String id);

    //해당 회원과 관련된 모든 이벤트를 찾는다.
    Map<String, Object> findAllEventCacheStartWithId(String userId);

    //이벤트를 저장
    void saveEventCache(String eventCacheId, Object event);

    //해당 회원과 관련된 모든 Emitter를 찾는다.
    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);

    //이벤트를 저장한다.
    void saveEventCache(String key, Notification notification);
}
