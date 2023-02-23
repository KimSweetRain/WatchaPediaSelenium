package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.Netflix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetflixTopRepository extends JpaRepository<Netflix, Long> {
}
