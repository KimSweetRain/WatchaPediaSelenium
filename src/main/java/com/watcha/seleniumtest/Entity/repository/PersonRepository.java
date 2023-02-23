package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByPerNameAndPerPhotoAndPerRole(String name, String photo, String role);
}
