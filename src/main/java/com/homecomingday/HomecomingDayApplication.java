package com.homecomingday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCaching
@EnableJpaAuditing// 생성시간/수정시간 자동 업데이트
@SpringBootApplication
@EnableScheduling
public class HomecomingDayApplication {


  // 타임스탬프시간 맞추기
  @PostConstruct
  public void started(){
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
  public static void main(String[] args) {
    SpringApplication.run(HomecomingDayApplication.class, args);
  }

}
