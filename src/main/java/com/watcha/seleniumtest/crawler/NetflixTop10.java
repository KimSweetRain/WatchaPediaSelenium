package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.Netflix;
import com.watcha.seleniumtest.Entity.TV;
import com.watcha.seleniumtest.Entity.Watcha;
import com.watcha.seleniumtest.Entity.repository.MovieRepository;
import com.watcha.seleniumtest.Entity.repository.NetflixTopRepository;
import com.watcha.seleniumtest.Entity.repository.TVRepository;
import com.watcha.seleniumtest.Entity.repository.WatchaTopRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.*;

@Service
public class NetflixTop10 {

    @Autowired private NetflixTopRepository netflixTopRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private TVRepository tvRepository;

    public void movie() {
        Setting setting = new Setting();
        setting.init("https://www.netflix.com/kr/login");
        setting.driver.get(setting.base_url);

        WebElement id = setting.driver.findElement(By.cssSelector("[name=userLoginId]"));
        WebElement pw = setting.driver.findElement(By.cssSelector("[name=password]"));
        WebElement login = setting.driver.findElement(By.cssSelector(".login-button"));

        String userid = "";
        String userpw = "";

        id.sendKeys(userid);
        pw.sendKeys(userpw);
        login.submit();

        //프로필 클릭
        setting.driver.findElement(By.cssSelector(".profile-link")).click();

        //상단 탭
        List<WebElement> button = setting.driver.findElements(By.cssSelector(".navigation-tab a"));
        for (WebElement btn : button) {
            if (btn.getText().equals("영화")) btn.click();
        }

        // 더보기 버튼 클릭
        try{
            setting.driver.findElement(By.cssSelector("[data-list-context='mostWatched']")).findElement(By.cssSelector(".handleNext")).click();
            Thread.sleep(1000);
        }catch (Exception e){}

        WebElement movieContainer = setting.driver.findElement(By.cssSelector("[data-list-context='mostWatched']"));
        List<WebElement> movLi = movieContainer.findElements(By.cssSelector(".slider-item"));

        HashMap<Integer,String> rankList = new HashMap<Integer,String>();
        for(WebElement mov : movLi){
            String rank = mov.findElement(By.cssSelector("svg[id*='rank-']")).getAttribute("id");
            rank = rank.substring(5,rank.length());
            String title = mov.findElement(By.cssSelector(".slider-refocus")).getAttribute("aria-label");
            rankList.put(Integer.parseInt(rank),title);
            System.out.println("타이틀과 랭크: " + rank + title);
        }
        for(int i=1; i<= rankList.size(); i++){
            System.out.println("담긴 영화" +rankList.get(i));
            Optional<Movie> movTitle = movieRepository.findByMovTitle(rankList.get(i));
            if (!movTitle.isEmpty()) {
                Netflix netflix = Netflix.builder().netRangking((long) i).netContentType("영화").netContentIdx(movTitle.get().getMovIdx()).netContentTitle(rankList.get(i)).build();
                netflixTopRepository.save(netflix);
            } else {
                Netflix netflix = Netflix.builder().netRangking((long) i).netContentType("영화").netContentTitle(rankList.get(i)).build();
                netflixTopRepository.save(netflix);
            }
        }
    }

    public void TV(){
        Setting setting = new Setting();
        setting.init("https://www.netflix.com/kr/login");
        setting.driver.get(setting.base_url);

        WebElement id = setting.driver.findElement(By.cssSelector("[name=userLoginId]"));
        WebElement pw = setting.driver.findElement(By.cssSelector("[name=password]"));
        WebElement login = setting.driver.findElement(By.cssSelector(".login-button"));

        String userid = "";
        String userpw = "";

        id.sendKeys(userid);
        pw.sendKeys(userpw);
        login.submit();

        //프로필 클릭
        setting.driver.findElement(By.cssSelector(".profile-link")).click();

        //상단 탭
        List<WebElement> button = setting.driver.findElements(By.cssSelector(".navigation-tab a"));
        for (WebElement btn : button) {
            if (btn.getText().equals("시리즈")) btn.click();
        }

        WebElement content = setting.driver.findElement(By.cssSelector(".mainView"));
        // 스크롤
        long stTime1 = new Date().getTime(); //현재시간
        while (new Date().getTime() < stTime1 + 5000) { //5초 동안 무한스크롤 지속
            try {
                Thread.sleep(500); //리소스 초과 방지
                ((JavascriptExecutor) setting.driver).executeScript("window.scrollTo(0, document.body.scrollHeight)", content);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        // 더보기 버튼 클릭
        try{
            setting.driver.findElement(By.cssSelector("[data-list-context='mostWatched']")).findElement(By.cssSelector(".handleNext")).click();
            Thread.sleep(1000);
        }catch (Exception e){}

        WebElement movieContainer = setting.driver.findElement(By.cssSelector("[data-list-context='mostWatched']"));
        List<WebElement> TVLi = movieContainer.findElements(By.cssSelector(".slider-item"));

        HashMap<Integer,String> rankList = new HashMap<Integer,String>();
        for(WebElement tv : TVLi){
            String rank = tv.findElement(By.cssSelector("svg[id*='rank-']")).getAttribute("id");
            rank = rank.substring(5,rank.length());
            String title = tv.findElement(By.cssSelector(".slider-refocus")).getAttribute("aria-label");
            rankList.put(Integer.parseInt(rank),title);
            System.out.println("타이틀과 랭크: " + rank + title);
        }
        for(int i=1; i<= rankList.size(); i++){
            System.out.println("담긴 tv프로그램" +rankList.get(i));
            Optional<TV> tv = tvRepository.findByTvTitle(rankList.get(i));
            if (!tv.isEmpty()) {
                Netflix netflix = Netflix.builder().netRangking((long) i).netContentType("TV").netContentIdx(tv.get().getTvIdx()).netContentTitle(rankList.get(i)).build();
                netflixTopRepository.save(netflix);
            } else {
                Netflix netflix = Netflix.builder().netRangking((long) i).netContentType("TV").netContentTitle(rankList.get(i)).build();
                netflixTopRepository.save(netflix);
            }
        }
    }
}
