package com.watcha.seleniumtest.service;

import com.watcha.seleniumtest.Entity.Book;
import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.Person;
import com.watcha.seleniumtest.Entity.repository.BookRepository;
import com.watcha.seleniumtest.Entity.repository.CommentRepository;
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
public class BookService {
    final BookRepository bookRepository;
    final CommentRepository commentRepository;
    final PersonCrawler personCrawler;

    public void bookCreate(String thumbnail, String title, String titleSub, String writer, String category,
               String atDate, String page, String age, String summary, List<String> peopleURL,
                String contentIdx, String pubSummary, String backImg, String buy){
        Optional<Book> overlap = bookRepository.findByBookTitleAndBookWriterAndBookPage(title,writer,page);
        if(overlap.isEmpty()){
            Book bookSave = Book.builder().bookThumbnail(thumbnail).bookTitle(title).bookTitleSub(titleSub)
                            .bookWriter(writer).bookCategory(category).bookAtDate(atDate).bookPage(page)
                            .bookAge(age).bookSummary(summary).bookContentIdx(contentIdx)
                            .bookPubSummary(pubSummary).bookBackImg(backImg).bookBuy(buy).build();
            Book book =  bookRepository.save(bookSave);
            String people = "";
            for(String per : peopleURL){
                String[] personList = per.split(",");
                Person person = personCrawler.person(personList[0]);
                people += person.getPerIdx() + "(" + personList[1] + "),";
            }
            people.substring(0, people.length());
            book.setBookPeople(people);
            bookRepository.save(book);
        }
    }

}
