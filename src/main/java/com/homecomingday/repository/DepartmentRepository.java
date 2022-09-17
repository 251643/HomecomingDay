package com.homecomingday.repository;

import com.homecomingday.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    boolean existsBymClass(String mClass);
}
