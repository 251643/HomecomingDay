package com.homecomingday.controller;

import com.homecomingday.controller.response.DepartmentDto;
import com.homecomingday.controller.response.SchoolDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;


@RestController("user-data-controller")
public class AccountsDataController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /** Json 데이터 파싱을 위한 매퍼 정의  */
    private static final ObjectMapper MAPPER = new ObjectMapper();


    @RequestMapping(value="/member/signup/schoolList",method={RequestMethod.GET, RequestMethod.POST} )
    public List<SchoolDto> findSchool(
                               HttpServletRequest request, HttpServletResponse response){
        int a = 1;
        ItemList items = new ItemList(Collections.EMPTY_LIST, 0);
        StringBuilder sb = new StringBuilder("http://www.career.go.kr/cnet/openapi/getOpenApi?apiKey=2ba8e2c040276c52e29d91e720b3f21d&svcType=api&svcCode=SCHOOL&contentType=json&gubun=univ_list&perPage=1000");

        List<SchoolDto> list = new ArrayList<>();
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
            }
        } catch (Exception e) {
            logger.error("CAREER API ERROR", e);
        }
        return list;//items;
    }

    @RequestMapping(value="/member/signup/departmentList",method={RequestMethod.GET, RequestMethod.POST} )
    public List<DepartmentDto> findDepartment(
            HttpServletRequest request, HttpServletResponse response){
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
                list.add(new DepartmentDto(
                        //jn3.get("seq").asInt(0),
                        // jn3.get("region").textValue(),
                        a++,
                        jn3.get("mClass").textValue()
                ));
            }
        } catch (Exception e) {
            logger.error("CAREER API ERROR", e);
        }
        return list;//items;
    }

}
