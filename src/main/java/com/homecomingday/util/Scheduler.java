package com.homecomingday.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homecomingday.member.responseDto.DepartmentDto;
import com.homecomingday.member.responseDto.SchoolDto;
import com.homecomingday.domain.Department;
import com.homecomingday.domain.School;
import com.homecomingday.member.DepartmentRepository;
import com.homecomingday.member.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /** Json 데이터 파싱을 위한 매퍼 정의  */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SchoolRepository schoolRepository;
    private final DepartmentRepository departmentRepository;


    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 0 1 * *")
    public void updateSchoolList() throws InterruptedException {
        System.out.println("학교목록 업데이트");

        int a = 1;
        ItemList items = new ItemList(Collections.EMPTY_LIST, 0);
        StringBuilder sb = new StringBuilder("http://www.career.go.kr/cnet/openapi/getOpenApi?apiKey=2ba8e2c040276c52e29d91e720b3f21d&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&perPage=1000");

        List<SchoolDto> list = new ArrayList<>();
        List<SchoolDto> listDistinct = new ArrayList<>();
        try {
            URL url = new URL(sb.toString());
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            JsonNode jn = MAPPER.readTree(is);
            JsonNode jn2 = jn.get("dataSearch").get("content");
            Iterator<JsonNode> iter = jn2.elements();
            while(iter.hasNext()){
                JsonNode jn3 = iter.next();
                if( items.getTotalCount() == 0 )
                    items.setTotalCount(jn3.get("totalCount").asInt());

                list.add(new SchoolDto(
                        //jn3.get("seq").asInt(0),
                        // jn3.get("region").textValue(),
                        a++,
                        jn3.get("schoolName").textValue(),
                        jn3.get("adres").textValue()
                ));
                listDistinct = DeduplicationUtils.deduplication(list, SchoolDto::getSchoolName);

            }

            for(SchoolDto schoolDto : listDistinct){
                School school = School.builder()
                        .schoolName(schoolDto.getSchoolName())
                        .address(schoolDto.getAddress())
                        .build();

                if(!schoolRepository.existsBySchoolName(schoolDto.getSchoolName())){
                    schoolRepository.save(school);
                }
            }

        } catch (Exception e) {
            logger.error("CAREER API ERROR", e);
        }
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void updateDepartmentList() throws InterruptedException {
        System.out.println("학과목록 업데이트");
        int a = 1;
        ItemList items = new ItemList(Collections.EMPTY_LIST, 0);
        StringBuilder sb = new StringBuilder("http://www.career.go.kr/cnet/openapi/getOpenApi?apiKey=2ba8e2c040276c52e29d91e720b3f21d&svcType=api&svcCode=MAJOR&contentType=json&gubun=univ_list&perPage=1000");

        List<DepartmentDto> list = new ArrayList<>();
        try {
            URL url = new URL(sb.toString());
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            JsonNode jn = MAPPER.readTree(is);
            JsonNode jn2 = jn.get("dataSearch").get("content");
            Iterator<JsonNode> iter = jn2.elements();

            while(iter.hasNext()){
                JsonNode jn3 = iter.next();
                if( items.getTotalCount() == 0 )
                    items.setTotalCount(jn3.get("totalCount").asInt());
                Department department = Department.builder()
                        .mClass(jn3.get("mClass").textValue())
                        .build();

                if(!departmentRepository.existsBymClass(jn3.get("mClass").textValue())){
                    departmentRepository.save(department);
                }
            }
        } catch (Exception e) {
            logger.error("CAREER API ERROR", e);
        }
    }
}
