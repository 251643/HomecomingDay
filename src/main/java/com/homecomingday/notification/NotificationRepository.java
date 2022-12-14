package com.homecomingday.notification;

import com.homecomingday.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

//    @Query(value = "select n from Notification n WHERE n.receiver.id = :memberId order by n.receiver.id ")
//    List<Notification> findAllByReceiver_MemberId(@Param("memberId")Long memberId);


    @Query(value = "select n from Notification n where n.member.id = :memberId order by n.id desc")
    List<Notification> findAllByReceiver_MemberId(@Param("memberId")Long memberId);

    @Query(value = "select count(n) from Notification n where n.member.id =:memberId and n.readState = false")
    Long countUnReadStateNotification(@Param("memberId") Long memberId);


    void deleteAllByMember_Id(Long receiverId);

//    void deleteAllByReceiver_MemberId(Long receiverId);
}
