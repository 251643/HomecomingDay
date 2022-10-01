package com.homecomingday.member;

import com.homecomingday.member.responseDto.SchoolDto;

import java.util.List;

public interface SchoolRepositoryCustom {
    List<SchoolDto> readSchoolList();
}
