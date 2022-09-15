package com.homecomingday.repository;

import com.homecomingday.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository  extends JpaRepository<Commit,Long> {
}
