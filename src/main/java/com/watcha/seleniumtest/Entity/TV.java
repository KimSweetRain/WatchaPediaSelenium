package com.watcha.seleniumtest.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "tbTv")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tvIdx;
    private String tvThumbnail;
    private String tvTitle;
    private String tvTitleOrg;
    private String tvMakingDate;
    private String tvChannel;
    private String tvGenre;
    private String tvCountry;
    private String tvAge;
    private String tvPeople;
    private String tvSummary;
    private String tvWatch;
    private String tvGallery;
    private String tvVideo;
    private String tvBackImg;

}
