package com.watcha.seleniumtest.service;

import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.Person;
import com.watcha.seleniumtest.Entity.Webtoon;
import com.watcha.seleniumtest.Entity.repository.CommentRepository;
import com.watcha.seleniumtest.Entity.repository.WebtoonRepository;
import com.watcha.seleniumtest.crawler.Controller.PersonCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Configurable
@Component
@RequiredArgsConstructor
public class WebtoonService {
    final WebtoonRepository webtoonRepository;
    final CommentRepository commentRepository;
    final PersonCrawler personCrawler;

    public void webtoonCreate(String thumbnail, String title, String titleOrg, String writer, String genre,
                String serDetail, String serDay, String serPeriod, String age, String summary,
                              List<String> peopleURL, String watch){
        Optional<Webtoon> overlap = webtoonRepository.findByWebTitleAndWebWriterAndWebGenre(title,writer,genre);
        if(overlap.isEmpty()){
            Webtoon webtoonSave = Webtoon.builder().webThumbnail(thumbnail).webTitle(title).webTitleOrg(titleOrg)
                            .webWriter(writer).webGenre(genre).webSerDetail(serDetail).webSerDay(serDay)
                            .webSerPeriod(serPeriod).webAge(age).webSummary(summary).webWatch(watch).build();
            Webtoon webtoon =  webtoonRepository.save(webtoonSave);
            String people = "";
            for(String per : peopleURL){
                String[] personList = per.split(",");
                Person person = personCrawler.person(personList[0]);
                people += person.getPerIdx() + "(" + personList[1] + "),";
            }
            people.substring(0, people.length());
            webtoon.setWebPeople(people);
            webtoonRepository.save(webtoon);
        }
    }
}
