package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.BoxOffice;
import com.watcha.seleniumtest.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Long> {
}
