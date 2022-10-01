package com.homecomingday.member;

import com.homecomingday.member.responseDto.DepartmentDto;
import com.homecomingday.domain.Department;
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
    public List<DepartmentDto> readDepartmentList() {
        int a=1;
        List<Department> result = queryFactory
                .selectFrom(department)
                .orderBy(department.id.asc())
                .fetch();

        List<DepartmentDto> departmentDtoList = new ArrayList<>();

        for (Department department : result) {
            departmentDtoList.add(
                    DepartmentDto.builder()
                            .seq(a++)
                            .mClass(department.getMClass())
                            .build()
            );
        }

        return departmentDtoList;
    }
}
