package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.TV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TVRepository extends JpaRepository<TV, Long> {
    Optional<TV> findByTvTitleAndTvCountryAndTvChannel(String title, String country, String channel);
    Optional<TV> findByTvTitle(String title);
}
