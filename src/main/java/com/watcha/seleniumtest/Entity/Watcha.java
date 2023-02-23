package com.watcha.seleniumtest.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "tbWatchaTop10")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Watcha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long watIdx;
    private Long watRangking;
    private String watContentType;
    private Long watContentIdx;
    private String watContentTitle;
}
