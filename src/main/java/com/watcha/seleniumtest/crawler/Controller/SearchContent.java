package com.watcha.seleniumtest.crawler.Controller;

import com.watcha.seleniumtest.crawler.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class SearchContent {
    @Autowired
    MovieDetailCrawler movieDetailCrawler;
    @Autowired
    TVDetailCrawler tvDetailCrawler;
    @Autowired
    BookDetailCrawler bookDetailCrawler;
    @Autowired
    WebtoonDetailCrawler webtoonDetailCrawler;

    static final List<String> word = Arrays.asList(new String[]{"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"});
    static final List<String> Engword = Arrays.asList(new String[]{"a", "b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"});
    public void movie (){
        Setting setting = new Setting();
        int i = 0;
        for(String idx : word){
            setting.init("https://pedia.watcha.com/ko-KR/searches/movies?query=" + word.get(i));
            i++;
            setting.driver.get(setting.base_url);
//            // 더보기 클릭
            try{
                for(int a=1; a<=1; a++){
                    setting.driver.findElement(By.cssSelector("button.css-1d4r906-StylelessButton")).click();
                    try {Thread.sleep(500);} catch (InterruptedException e) {;}
                }
            }catch(Exception e){
                System.out.println("** 비슷한 작품 더보기 버튼이 없습니다 ** ");
            }

            List<WebElement> linkList = setting.driver.findElements(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage"));
            for(WebElement li : linkList){
                String link = li.getAttribute("href");
                movieDetailCrawler.detail(link);
            }
            setting.driver.close();
        }
    }

    public void tv (){
        Setting setting = new Setting();
        int i = 0;
        for(String idx : word){
            setting.init("https://pedia.watcha.com/ko-KR/searches/tv_seasons?query=" + word.get(i));
            i++;
            setting.driver.get(setting.base_url);
//            // 더보기 클릭
            try{
                for(int a=1; a<=2; a++){
                    setting.driver.findElement(By.cssSelector("button.css-1d4r906-StylelessButton")).click();
                    try {Thread.sleep(500);} catch (InterruptedException e) {;}
                }
            }catch(Exception e){
                System.out.println("** 비슷한 작품 더보기 버튼이 없습니다 ** ");
            }

            List<WebElement> linkList = setting.driver.findElements(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage"));
            for(WebElement li : linkList){
                String link = li.getAttribute("href");
                tvDetailCrawler.detail(link);
            }
            setting.driver.close();
        }
    }

    public void book (){
        Setting setting = new Setting();
        int i = 0;
        for(String idx : word){
            setting.init("https://pedia.watcha.com/ko-KR/searches/books?query=" + word.get(i));
            i++;
            setting.driver.get(setting.base_url);
//            // 더보기 클릭
            try{
                for(int a=1; a<=2; a++){
                    setting.driver.findElement(By.cssSelector("button.css-1d4r906-StylelessButton")).click();
                    try {Thread.sleep(500);} catch (InterruptedException e) {;}
                }
            }catch(Exception e){
                System.out.println("** 비슷한 작품 더보기 버튼이 없습니다 ** ");
            }

            List<WebElement> linkList = setting.driver.findElements(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage"));
            for(WebElement li : linkList){
                String link = li.getAttribute("href");
                bookDetailCrawler.detail(link);
            }
            setting.driver.close();
        }
    }

    public void webtoon (){
        Setting setting = new Setting();
        int i = 0;
        for(String idx : word){
            setting.init("https://pedia.watcha.com/ko-KR/searches/webtoons?query=" + word.get(i));
            i++;
            setting.driver.get(setting.base_url);
//            // 더보기 클릭
            try{
                for(int a=1; a<=2; a++){
                    setting.driver.findElement(By.cssSelector("button.css-1d4r906-StylelessButton")).click();
                    try {Thread.sleep(500);} catch (InterruptedException e) {;}
                }
            }catch(Exception e){
                System.out.println("** 비슷한 작품 더보기 버튼이 없습니다 ** ");
            }

            List<WebElement> linkList = setting.driver.findElements(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage"));
            for(WebElement li : linkList){
                String link = li.getAttribute("href");
                webtoonDetailCrawler.detail(link);
            }
            setting.driver.close();
        }
    }
}
