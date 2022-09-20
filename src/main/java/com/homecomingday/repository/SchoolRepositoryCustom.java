package com.homecomingday.repository;

import com.homecomingday.controller.response.SchoolDto;
import com.homecomingday.domain.School;

import java.util.List;

public interface SchoolRepositoryCustom {
    List<SchoolDto> readSchoolList();
}
