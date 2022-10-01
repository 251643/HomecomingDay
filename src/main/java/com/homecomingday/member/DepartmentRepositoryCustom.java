package com.homecomingday.member;

import com.homecomingday.member.responseDto.DepartmentDto;

import java.util.List;

public interface DepartmentRepositoryCustom {
    List<DepartmentDto> readDepartmentList();
}
