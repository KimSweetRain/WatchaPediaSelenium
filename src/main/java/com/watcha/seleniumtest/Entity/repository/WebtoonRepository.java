package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    Optional<Webtoon> findByWebTitleAndWebWriterAndWebGenre(String title, String writer, String genre);
}
