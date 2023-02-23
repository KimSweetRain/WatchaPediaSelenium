package com.watcha.seleniumtest.service;

import com.watcha.seleniumtest.Entity.*;
import com.watcha.seleniumtest.Entity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Configurable
@Component
@RequiredArgsConstructor
public class PersonService {
    final PersonRepository personRepository;
    final MovieRepository movieRepository;
    final TVRepository tvRepository;
    final WebtoonRepository webtoonRepository;
    final BookRepository bookRepository;

    public Person create(Person person, List<Movie> movList, List<Book> bookList
            , List<Webtoon> webtoonList, List<TV> TVList){
//        person 중복여부 체크 후 person에 저장
        Optional<Person> overlap = personRepository.findByPerNameAndPerPhotoAndPerRole(person.getPerName(),person.getPerPhoto(),person.getPerRole());
        // 기존에 있던 인물일 경우 가져오기
        if(!overlap.isEmpty()) {
            person = overlap.get();
        }
//        person에 등록돼있지 않던 인물일 경우 등록 진행 후 리턴
        if(overlap.isEmpty()){
            person = Person.builder().perName(person.getPerName()).perPhoto(person.getPerPhoto())
                    .perRole(person.getPerRole()).perBiography(person.getPerBiography()).build();
        }

//        영화목록(movList) 가져와서 검색 후 리스트(movie)에 저장
        String movieStr = "";
        Set<Long> movie = new HashSet<>();
        for(Movie mov : movList){
            Optional<Movie> check = movieRepository.findByMovTitleAndMovTimeAndMovMakingDate(
                    mov.getMovTitle(), mov.getMovTime(), mov.getMovMakingDate()
            );
            if(!check.isEmpty()) {
                movie.add(check.get().getMovIdx());
            }
        }
        if(!movie.isEmpty()){
            for(Long idx : movie){
                movieStr += idx + ",";
            }
        }
//        movie List 해당 person에 세팅
        person.setPerMov(movieStr.equals("")?null:movieStr.substring(0,movieStr.length()-1));

        String bookStr = "";
        Set<Long> book = new HashSet<>();
        for(Book bk : bookList){
            Optional<Book> check = bookRepository.findByBookTitleAndBookWriterAndBookPage(
                    bk.getBookTitle(), bk.getBookWriter(), bk.getBookPage()
            );
            if(!check.isEmpty()) {
                book.add(check.get().getBookIdx());
            }
        }
        if(!book.isEmpty()){
            for(Long idx : book){
                bookStr += idx + ",";
            }
        }
        person.setPerBook(bookStr.equals("")?null:bookStr.substring(0,bookStr.length()-1));


        String webtoonStr = "";
        Set<Long> webtoon = new HashSet<>();
        for(Webtoon web : webtoonList){
            Optional<Webtoon> check = webtoonRepository.findByWebTitleAndWebWriterAndWebGenre(
                    web.getWebTitle(), web.getWebWriter(), web.getWebGenre()
            );
            if(!check.isEmpty()) {
                webtoon.add(check.get().getWebIdx());
            }
        }
        if(!webtoon.isEmpty()){
            for(Long idx : webtoon){
                webtoonStr += idx + ",";
            }
        }
        System.out.println("펄슨서비스의 웹툰: " + webtoonStr);
        person.setPerWebtoon(webtoonStr.equals("")?null:webtoonStr.substring(0,webtoonStr.length()-1));

        String tvStr = "";
        Set<Long> tv = new HashSet<>();
        for(TV tv1 : TVList){
            Optional<TV> check = tvRepository.findByTvTitleAndTvCountryAndTvChannel(
                    tv1.getTvTitle(), tv1.getTvCountry(), tv1.getTvChannel()
            );
            if(!check.isEmpty()) {
                tv.add(check.get().getTvIdx());
            }
        }
        if(!tv.isEmpty()){
            for(Long idx : tv){
                tvStr += idx + ",";
            }
        }
        person.setPerTv(tvStr.equals("")?null:tvStr.substring(0,tvStr.length()-1));

        personRepository.save(person);
        return person;
    }
}
