package com.homecomingday.repository;

import com.homecomingday.controller.response.DepartmentDto;
import com.homecomingday.controller.response.SchoolDto;
import com.homecomingday.domain.Department;
import com.homecomingday.domain.School;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.homecomingday.domain.QDepartment.*;

public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public DepartmentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<DepartmentDto> readDepartmenList() {

        List<Department> result = queryFactory
                .selectFrom(department)
                .orderBy(department.id.asc())
                .fetch();

        List<DepartmentDto> departmentDtoList = new ArrayList<>();

        for (Department department : result) {
            departmentDtoList.add(
                    DepartmentDto.builder()
                            .seq(Math.toIntExact(department.getId()))
                            .mClass(department.getMClass())
                            .build()
            );
        }

        return departmentDtoList;
    }
}
