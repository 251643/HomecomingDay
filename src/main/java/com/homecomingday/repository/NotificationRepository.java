package com.homecomingday.repository;

import com.homecomingday.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select n from Notification n WHERE n.receiver.id = :memberId order by n.receiver.id ")
    List<Notification> findAllByReceiver_MemberId(@Param("memberId")Long memberId);

    @Query(value = "select count(n) from Notification n where n.receiver.id =:memberId and n.readState = false")
    Long countUnReadStateNotification(@Param("memberId") Long memberId);



    void deleteAllByReceiver_Id(Long receiverId);



}
