package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.service.BookService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookDetailCrawler {
    @Autowired
    private BookService bookService;

    public void detail(String link) {
        Setting setting = new Setting();
        setting.init(link);
        setting.driver.get(setting.base_url);

        String thumbnail = setting.driver.findElement(By.cssSelector(".css-qhzw1o-StyledImg")).getAttribute("src");

        WebElement content = setting.driver.findElement(By.cssSelector("div.css-10ofaaw")); //컨텐츠 추출

        // 백그라운드 이미지
        String backgroundImg = "";
        try {
            String backgroundImgUrl = "background 이미지: " + content.findElement(By.cssSelector(".e1svyhwg4")).getCssValue("background-image");
            backgroundImg = backgroundImgUrl.split("url\\(\"")[1]
                    .split("\"\\)")[0]; // remove open bracket
        }catch(Exception e){
            System.out.println("** 백그라운드 이미지가 없습니다. **");
        }
        String title = content.findElement(By.cssSelector("h1.css-171k8ad-Title")).getText(); // 제목 추출

        //구매 가능한 곳 링크
        String buyList = "";
        try{
            List<WebElement> buys = content.findElement(By.className("css-wpsvu8")).findElements(By.cssSelector("a.css-1wacncs-InnerPartOfListWithImage"));
            String watchLast = buys.get(buys.size()-1).getAttribute("href");
            for(WebElement buy : buys){
                if(buy.getAttribute("href").equals(watchLast)){
                    buyList += buy.getAttribute("href");
                }else{
                    buyList += buy.getAttribute("href") + ",";
                }
            }
        }catch(Exception e){
            System.out.println("** 구매 링크가 없습니다. **");
        }

        // 저자/역자 정보 추출기
        List<WebElement> peopleList = content.findElements(By.cssSelector("li.css-54rr1e"));
        List<String> peopleURL = new ArrayList<>();
        for(WebElement person : peopleList){
            String personURL = person.findElement(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage")).getAttribute("href");
            String role =  person.findElement(By.cssSelector("div.css-1evnpxk-StyledSubtitle")).getText();
            String roleName = "";
            if(role.contains(" | ")){
                String[] roleList = role.split(" | ");
                role = roleList[0];
                for(int i=2; i<roleList.length; i++){
                    roleName += roleList[i];
                }
            }
            if(roleName.equals("")){
                peopleURL.add(personURL + "," + role + "");
            }else{
                peopleURL.add(personURL + "," + role + " | " + roleName + "");
            }
        }

        setting.driver.close();
        //상세정보 링크 연결
        setting.init(link + "/overview");
        setting.driver.get(setting.base_url);

//        책 내용
        String summary = setting.driver.findElement(By.cssSelector("dd.e1kvv3954")).getText();

        List<WebElement> detailInfo = setting.driver.findElements(By.className("e1kvv3951"));

        String titleSub = "";
        String writer = "";
        String category = "";
        String atDate = "";
        String page = "";
        String ageLimit = "";

        for(WebElement info : detailInfo){
            if(info.findElement(By.className("e1kvv3952")).getText().equals("부제")){
                titleSub = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("작가")){
                writer = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("카테고리")){
                category = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("출간 연도")){
                atDate = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("페이지")){
                page = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연령 등급")){
                ageLimit = info.findElement(By.className("e1kvv3953")).getText();
            }
        }

        setting.driver.close();
        // 출판사 책 소개 링크 연결
        setting.init(link + "/book_description");
        setting.driver.get(setting.base_url);

        String pubSummary = setting.driver.findElement(By.cssSelector(".euu3om70")).getText().equals("")?null:setting.driver.findElement(By.cssSelector(".euu3om70")).getText();

        setting.driver.close();
        // 목차 링크 연결
        setting.init(link + "/book_contents");
        setting.driver.get(setting.base_url);

        String contentIdx = setting.driver.findElement(By.cssSelector(".e1h7kv560")).getText();
        if(contentIdx.equals("")){
            contentIdx = null;
        }else if(contentIdx.equals("목차 없는 도서입니다.")){
            contentIdx = null;
        }
        setting.driver.close();

        try{
            bookService.bookCreate(thumbnail.equals("")?null:thumbnail
                    ,title,titleSub,writer
                    ,category.equals("")?null:category
                    ,atDate,page
                    ,ageLimit.equals("")?null:ageLimit
                    ,summary.equals("-")?null:summary
                    ,peopleURL,contentIdx,pubSummary
                    ,backgroundImg.equals("")?null:backgroundImg
                    ,buyList.equals("")?null:buyList);

            setting.driver.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

