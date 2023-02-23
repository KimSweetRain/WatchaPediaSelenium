package com.watcha.seleniumtest.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "tbBook")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookIdx;
    private String bookThumbnail;
    private String bookTitle;
    private String bookTitleSub;
    private String bookWriter;
    private String bookCategory;
    private String bookAtDate;
    private String bookPage;
    private String bookAge;
    private String bookSummary;
    private String bookPeople;
    private String bookContentIdx;
    private String bookPubSummary;
    private String bookBackImg;
    private String bookBuy;

}
