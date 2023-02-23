package com.watcha.seleniumtest.service;

import com.watcha.seleniumtest.Entity.Book;
import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.Entity.Person;
import com.watcha.seleniumtest.Entity.TV;
import com.watcha.seleniumtest.Entity.repository.CommentRepository;
import com.watcha.seleniumtest.Entity.repository.TVRepository;
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
public class TVService {
    final TVRepository tvRepository;
    final CommentRepository commentRepository;
    final PersonCrawler personCrawler;

    public void tvCreate(String thumbnail, String title, String titleOrg, String makingDate, String channel,
                String genre, String country, String age, List<String> peopleURL, String summary,
                String gallery, String video, String watch, String backgroundImg){
        Optional<TV> overlap = tvRepository.findByTvTitleAndTvCountryAndTvChannel(title,country,channel);
        if(overlap.isEmpty()){
            TV tvSave = TV.builder().tvThumbnail(thumbnail).tvTitle(title).tvTitleOrg(titleOrg)
                    .tvMakingDate(makingDate).tvChannel(channel).tvCountry(country).tvGenre(genre)
                    .tvAge(age).tvSummary(summary).tvGallery(gallery)
                    .tvVideo(video).tvWatch(watch).tvBackImg(backgroundImg).build();
            TV tv =  tvRepository.save(tvSave);
            String people = "";
            for(String per : peopleURL){
                String[] personList = per.split(",");
                Person person = personCrawler.person(personList[0]);
                people += person.getPerIdx() + "(" + personList[1] + "),";
            }
            people.substring(0, people.length());
            tv.setTvPeople(people);
            tvRepository.save(tv);
        }
    }

}
