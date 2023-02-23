package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.Watcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchaTopRepository extends JpaRepository<Watcha, Long> {
}
