package com.homecomingday.repository;

import com.homecomingday.controller.response.MyPageDetailResponseDto;
import com.homecomingday.controller.response.SchoolDto;
import com.homecomingday.domain.Article;
import com.homecomingday.domain.School;
import com.homecomingday.util.Time;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.homecomingday.domain.QSchool.*;

public class SchoolRepositoryImpl implements SchoolRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public SchoolRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SchoolDto> readSchoolList() {

        List<School> result = queryFactory
                .selectFrom(school)
                .orderBy(school.id.asc())
                .fetch();

        List<SchoolDto> schoolDtoList = new ArrayList<>();

        for (School school : result) {
            schoolDtoList.add(
                    SchoolDto.builder()
                            .seq(Math.toIntExact(school.getId()))
                            .schoolName(school.getSchoolName())
                            .address(school.getAddress())
                            .build()
            );
        }

        return schoolDtoList;
    }
}
