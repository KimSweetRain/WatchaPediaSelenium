package com.watcha.seleniumtest;

import com.watcha.seleniumtest.crawler.*;
import com.watcha.seleniumtest.crawler.Controller.PersonCrawler;
import com.watcha.seleniumtest.crawler.Controller.SearchContent;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeleniumTestApplicationTests {
    @Autowired SearchContent searchContent;
    @Autowired BoxOfficeCrawler boxOfficeCrawler;
    @Autowired PersonCrawler personCrawler;
    @Autowired TVDetailCrawler tvDetailCrawler;
    @Autowired WebtoonDetailCrawler webtoonDetailCrawler;
    @Autowired MovieDetailCrawler movieDetailCrawler;
    @Autowired WatchaTop10 watchaTop10;
    @Autowired NetflixTop10 netflixTop10;

    @Test
    void contextLoads() {
//        searchContent.movie();
//        searchContent.tv();
//        searchContent.book();
//        searchContent.webtoon();
//        boxOfficeCrawler.main();
//        watchaTop10.movie();
//        watchaTop10.TV();
//        netflixTop10.movie();
        netflixTop10.TV();
    }

}
