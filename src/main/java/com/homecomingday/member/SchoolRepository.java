package com.homecomingday.member;

import com.homecomingday.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository  extends JpaRepository<School,Long>,SchoolRepositoryCustom {
    boolean existsBySchoolName(String schoolName);
}
