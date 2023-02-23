package com.watcha.seleniumtest.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "tbNetflixTop10")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Netflix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long netIdx;
    private Long netRangking;
    private String netContentType;
    private Long netContentIdx;
    private String netContentTitle;
}
