package com.homecomingday.repository;

import com.homecomingday.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository  extends JpaRepository<School,Long>,SchoolRepositoryCustom {
}
