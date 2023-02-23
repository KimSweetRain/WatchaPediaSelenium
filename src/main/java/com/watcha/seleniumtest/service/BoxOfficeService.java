package com.watcha.seleniumtest.service;

import com.watcha.seleniumtest.Entity.BoxOffice;
import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.repository.BoxOfficeRepository;
import com.watcha.seleniumtest.Entity.repository.MovieRepository;
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
public class BoxOfficeService {
    final BoxOfficeRepository boxOfficeRepository;
    final MovieRepository movieRepository;

    public void create(BoxOffice boxOffice, String country, String year){
        movieRepository.deleteAll();
        Optional<Movie> movie = movieRepository.findByMovTitleAndMovCountryAndMovMakingDate(boxOffice.getBoxMovTitle(),country,year);
        movie.ifPresent(
                selectMovie -> {
                    BoxOffice boxOfficeSave = BoxOffice.builder().boxRangking(boxOffice.getBoxRangking()).movie(selectMovie)
                            .boxMovTitle(boxOffice.getBoxMovTitle()).boxBooking(boxOffice.getBoxBooking()).boxSpectators(boxOffice.getBoxSpectators()).build();
                    boxOfficeRepository.save(boxOfficeSave);
                }
        );
        if(movie.isEmpty()){
            BoxOffice boxOfficeSave = BoxOffice.builder().boxRangking(boxOffice.getBoxRangking())
                    .boxMovTitle(boxOffice.getBoxMovTitle()).boxBooking(boxOffice.getBoxBooking())
                    .boxSpectators(boxOffice.getBoxSpectators()).build();
            boxOfficeRepository.save(boxOfficeSave);
        }
    }
}
