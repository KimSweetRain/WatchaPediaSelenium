package com.watcha.seleniumtest.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "tbPerson")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long perIdx;
    private String perName;
    private String perPhoto;
    private String perRole;

    private String perMov;
    private String perBook;
    private String perWebtoon;
    private String perTv;
    private String perBiography;
}
