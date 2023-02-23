package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByMovTitleAndMovTimeAndMovMakingDate(String title, String time, String makingDate);
    Optional<Movie> findByMovTitleAndMovCountryAndMovMakingDate(String title, String country, String makingDate);
    Optional<Movie> findByMovTitle(String title);
}
