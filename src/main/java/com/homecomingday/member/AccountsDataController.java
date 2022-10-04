package com.homecomingday.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homecomingday.member.responseDto.AdmissionDto;
import com.homecomingday.member.responseDto.DepartmentDto;
import com.homecomingday.member.responseDto.SchoolDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class AccountsDataController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /** Json 데이터 파싱을 위한 매퍼 정의  */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final SchoolRepositoryImpl schoolRepositoryImpl;
    private final DepartmentRepositoryImpl departmentRepositoryImpl;

    @RequestMapping(value="/schoolSearchs",method={RequestMethod.GET, RequestMethod.POST} )
    public List<SchoolDto> findSchool(){

        return schoolRepositoryImpl.readSchoolList();//items;
    }

    @RequestMapping(value="/departmentSearchs",method={RequestMethod.GET, RequestMethod.POST} )
    public List<DepartmentDto> findDepartment(){
        return departmentRepositoryImpl.readDepartmentList();//items;
    }

    @RequestMapping(value="/admissions",method={RequestMethod.GET, RequestMethod.POST} )
    public List<AdmissionDto> findAdmission(){
        int a = 1;
        int year = LocalDate.now().getYear();
        List<AdmissionDto> list = new ArrayList<>();
        for(int i = year ; i >= 1975 ; i-- ){
            list.add(new AdmissionDto(a++,String.valueOf(i)));
        }
        return list;//items;
    }

}

