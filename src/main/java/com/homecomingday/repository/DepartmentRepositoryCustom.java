package com.homecomingday.repository;

import com.homecomingday.controller.response.DepartmentDto;
import com.homecomingday.controller.response.SchoolDto;

import java.util.List;

public interface DepartmentRepositoryCustom {
    List<DepartmentDto> readDepartmenList();
}
