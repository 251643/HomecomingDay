package com.homecomingday.repository;

import com.homecomingday.domain.Member;
import com.homecomingday.domain.RefreshToken;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
}
