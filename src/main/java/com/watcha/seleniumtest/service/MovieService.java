package com.watcha.seleniumtest.service;

import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.Person;
import com.watcha.seleniumtest.Entity.repository.CommentRepository;
import com.watcha.seleniumtest.Entity.repository.MovieRepository;
import com.watcha.seleniumtest.crawler.Controller.PersonCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MovieService {
    final MovieRepository movieRepository;
    final CommentRepository commentRepository;
    final PersonCrawler personCrawler;

    public void movCreate(String thumbnail, String title, String titleOrg, String makingDate, String country,
                String genre, String time, String age, List<String> peopleURL, String summary,
                String gallery, String video, String watch, String backgroundImg){
        Optional<Movie> overlap = movieRepository.findByMovTitleAndMovTimeAndMovMakingDate(title,time,makingDate);
        if(overlap.isEmpty()){
            Movie movie = Movie.builder().movThumbnail(thumbnail).movTitle(title).movTitleOrg(titleOrg)
                    .movMakingDate(makingDate).movCountry(country).movGenre(genre).movTime(time)
                    .movAge(age).movSummary(summary).movGallery(gallery)
                    .movVideo(video).movWatch(watch).movBackImg(backgroundImg).build();
            Movie mov =  movieRepository.save(movie);
            String people = "";
            for(String per : peopleURL){
                String[] personList = per.split(",");
                Person person = personCrawler.person(personList[0]);
                System.out.println("무비서비스의 펄슨" + person.getPerName() + person.getPerRole());
                try{people += person.getPerIdx() + "(" + personList[1] + "),";}catch (Exception e){
                    System.out.println("롤네임 가져오는데 오류남" + person.getPerName());
                };
            }
            people.substring(0, people.length());
            mov.setMovPeople(people);
            movieRepository.save(mov);

        }
    }

//    public void movComment(List<Comment> commentList, String title, String time, String makingDate){
//        for(Comment comm : commentList){
//            Optional<Movie> movie = movieRepository.findByMovTitleAndMovTimeAndMovMakingDate(title,time,makingDate);
//            movie.ifPresent(
//                    selectMovie -> {
//                        Comment comment = Comment.builder().commUserIdx(1L).commName(comm.getCommName())
//                                .commText(comm.getCommText()).commContentType("movie").commContentIdx(selectMovie.getMovIdx())
//                                .commRegDate(new Date()).build();
//                        commentRepository.save(comment);
//                    }
//            );
//        }
//    }
}
