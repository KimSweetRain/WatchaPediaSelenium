package com.watcha.seleniumtest.crawler.Controller;

import com.watcha.seleniumtest.Entity.*;
import com.watcha.seleniumtest.crawler.Setting;
import com.watcha.seleniumtest.service.PersonService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class PersonCrawler {
    @Autowired
    private PersonService personService;

    public Person person(String link) {
        Setting setting = new Setting();
        setting.init(link);
        setting.driver.get(setting.base_url);

        // person 정보 세팅
        String name = setting.driver.findElement(By.cssSelector(".css-n52eyj h1")).getText();
        String role = setting.driver.findElement(By.cssSelector(".css-n52eyj p")).getText();
        String photo = "";
        try {
            String photoUrl = setting.driver.findElement(By.cssSelector("[class*='ProfilePhotoImage']"))
                    .getCssValue("background-image");
            photo = photoUrl = photoUrl.split("url\\(\"")[1]
                    .split("\"\\)")[0]; // remove open bracket
            System.out.println("프로필사진 : " + photo);
        }catch(Exception e){
            System.out.println("** 프로필 사진이 없습니다 **");
        }

        String biography = null;
        try{
            biography = setting.driver.findElement(By.cssSelector(".e1nbfgav1 span")).getText();
        }catch (Exception e){
            System.out.println("** 바이오그래피가 없습니다 **");
        }

        Person person = Person.builder().perName(name).perPhoto(photo.equals("")?null:photo)
                .perRole(role).perBiography(biography).build();

        // 더보기 클릭
        try{
            while(setting.driver.findElement(By.cssSelector("button.css-1gvrt49"))!=null){
                setting.driver.findElement(By.cssSelector("button.css-1gvrt49")).click();
                try {Thread.sleep(500);} catch (InterruptedException e) {;}
            }
        }catch(Exception e){
            System.out.println("** 더보기 버튼이 없습니다 ** ");
        }

        WebElement container = setting.driver.findElement(By.cssSelector(".css-358g0x > .css-1e9niz8"));
        List<WebElement> containerList = container.findElements(By.cssSelector(".css-6fdzfq"));

        WebElement movieContainer = null;
        WebElement tvContainer = null;
        WebElement webtoonContainer = null;
        WebElement bookContainer = null;

        for(WebElement e: containerList){
            String check = e.findElement(By.cssSelector(".css-1w4x7z")).getText();
            if(check.equals("영화")){
                movieContainer = e;
            }else if(check.equals("TV 프로그램")){
                tvContainer = e;
            }else if(check.equals("책")){
                bookContainer = e;
            }else if(check.equals("웹툰")){
                webtoonContainer = e;
            }
        }

        //영화 목록
        List<Movie> movieList = new ArrayList<Movie>();
        if(movieContainer != null){
            List<WebElement> movieDiv = movieContainer.findElements(By.cssSelector(".css-11g9kr1"));
            for(WebElement mov : movieDiv){
                if(!mov.findElement(By.cssSelector(".css-1vjd65c")).getText().equals("—")){
                    if(Calendar.getInstance().get(Calendar.YEAR) >= Integer.parseInt(mov.findElement(By.cssSelector(".css-1vjd65c")).getText())){
                        String title = mov.findElement(By.cssSelector("div.css-1huturz")).getAttribute("innerText");
                        //영화 클릭
                        setting.init(mov.getAttribute("href")+"/overview");
                        setting.driver.get(setting.base_url);
                        String time = null;
                        List<WebElement> infoList = setting.driver.findElements(By.cssSelector(".e1kvv3951"));
                        for(WebElement info : infoList){
                            if(info.findElement(By.className("e1kvv3952")).getText().equals("상영시간")){
                                time = info.findElement(By.className("e1kvv3953")).getText();
                            }
                        }
                        movieList.add(Movie.builder().movTitle(title)
                                .movMakingDate(mov.findElement(By.cssSelector(".css-1vjd65c")).getText())
                                .movTime(time).build());
                        setting.driver.close();
                    }
                }
            }
        }

        //책 목록
        List<Book> bookList = new ArrayList<Book>();
        if(bookContainer != null){
            List<WebElement> bookDiv = bookContainer.findElements(By.cssSelector(".css-11g9kr1"));
            for(WebElement bk : bookDiv){
                if(!bk.findElement(By.cssSelector(".css-1vjd65c")).getText().equals("—")){
                    if(Calendar.getInstance().get(Calendar.YEAR) >= Integer.parseInt(bk.findElement(By.cssSelector(".css-1vjd65c")).getText())){
                        String title = bk.findElement(By.cssSelector("div.css-1huturz")).getAttribute("innerText");
                        //책 클릭
                        setting.init(bk.getAttribute("href")+"/overview");
                        setting.driver.get(setting.base_url);
                        String writer = null;
                        String page = null;
                        List<WebElement> infoList = setting.driver.findElements(By.cssSelector(".e1kvv3951"));
                        for(WebElement info : infoList){
                            if(info.findElement(By.className("e1kvv3952")).getText().equals("작가")){
                                writer = info.findElement(By.className("e1kvv3953")).getText();
                            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("페이지")){
                                page = info.findElement(By.className("e1kvv3953")).getText();
                            }
                        }
                        bookList.add(Book.builder().bookTitle(title).bookWriter(writer).bookPage(page).build());
                        setting.driver.close();
                    }
                }
            }
        }

        //웹툰 목록
        List<Webtoon> webtoonList = new ArrayList<Webtoon>();
        if(webtoonContainer != null){
            List<WebElement> webDiv = webtoonContainer.findElements(By.cssSelector(".css-11g9kr1"));
            for(WebElement web : webDiv){
                if(!web.findElement(By.cssSelector(".css-1vjd65c")).getText().equals("—")){
                    if(Calendar.getInstance().get(Calendar.YEAR) >= Integer.parseInt(web.findElement(By.cssSelector(".css-1vjd65c")).getText())){
                        String title = web.findElement(By.cssSelector("div.css-1huturz")).getAttribute("innerText");
                        //웹툰 클릭
                        setting.init(web.getAttribute("href")+"/overview");
                        setting.driver.get(setting.base_url);
                        String writer = null;
                        String genre = null;
                        List<WebElement> infoList = setting.driver.findElements(By.cssSelector(".e1kvv3951"));
                        for(WebElement info : infoList){
                            if(info.findElement(By.className("e1kvv3952")).getText().equals("작가")){
                                writer = info.findElement(By.className("e1kvv3953")).getText();
                            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("장르")){
                                genre = info.findElement(By.className("e1kvv3953")).getText();
                            }
                        }
                        webtoonList.add(Webtoon.builder().webTitle(title).webWriter(writer).webGenre(genre).build());
                        setting.driver.close();
                    }
                }
            }
        }

        //TV 목록
        List<TV> tvList = new ArrayList<TV>();
        if(tvContainer != null){
            List<WebElement> tvDiv = tvContainer.findElements(By.cssSelector(".css-11g9kr1"));
            for(WebElement tv : tvDiv){
                if(!tv.findElement(By.cssSelector(".css-1vjd65c")).getText().equals("—")){
                    if(Calendar.getInstance().get(Calendar.YEAR) >= Integer.parseInt(tv.findElement(By.cssSelector(".css-1vjd65c")).getText())){
                        String title = tv.findElement(By.cssSelector("div.css-1huturz")).getAttribute("innerText");
                        //TV 클릭
                        setting.init(tv.getAttribute("href")+"/overview");
                        setting.driver.get(setting.base_url);
                        String country = null;
                        String channel = null;
                        List<WebElement> infoList = setting.driver.findElements(By.cssSelector(".e1kvv3951"));
                        for(WebElement info : infoList){
                            if(info.findElement(By.className("e1kvv3952")).getText().equals("국가")){
                                country = info.findElement(By.className("e1kvv3953")).getText();
                            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("채널")){
                                channel = info.findElement(By.className("e1kvv3953")).getText();
                            }
                        }
                        tvList.add(TV.builder().tvTitle(title).tvCountry(country).tvChannel(channel).build());
                        setting.driver.close();
                    }
                }
            }
        }
        person = personService.create(person,movieList,bookList,webtoonList,tvList);
        return person;
    }
}

