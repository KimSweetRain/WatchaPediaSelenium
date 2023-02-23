package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.service.WebtoonService;
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
public class WebtoonDetailCrawler {
    @Autowired
    private WebtoonService webtoonService;

    public void detail(String link) {
        Setting setting = new Setting();
        setting.init(link);
        setting.driver.get(setting.base_url);

        String thumbnailUrl = setting.driver.findElement(By.cssSelector(".css-569z5v")).findElement(By.cssSelector(".css-bhgne5-StyledBackground")).getCssValue("background-image");
        String thumbnail = "";
        try{
            thumbnail = thumbnailUrl.split("url\\(\"")[1]
                    .split("\"\\)")[0]; // remove open bracket
        }catch(Exception e){
            System.out.println("** 썸네일 이미지가 없습니다 **");
        }

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

        //보러가기 링크
        String watchList = "";
        try{
            List<WebElement> watch = content.findElement(By.className("css-wpsvu8")).findElements(By.cssSelector("a.css-1wacncs-InnerPartOfListWithImage"));
            String watchLast = watch.get(watch.size()-1).getAttribute("href");
            for(WebElement wat : watch){
                if(wat.getAttribute("href").equals(watchLast)){
                    watchList += wat.getAttribute("href");
                }else{
                    watchList += wat.getAttribute("href") + ",";
                }
            }
        }catch(Exception e){
            System.out.println("** 보러가기 링크가 없습니다. **");
        };

        //작가 정보 추출기
        List<WebElement> peopleList = content.findElements(By.cssSelector("li.css-54rr1e"));
        List<String> peopleURL = new ArrayList<>();
        for(WebElement person : peopleList){
            String personURL = person.findElement(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage")).getAttribute("href");
            String role =  person.findElement(By.cssSelector("div.css-1evnpxk-StyledSubtitle")).getText();
            String roleName = "";
            if(role.contains(" | ")){
                String[] roleList = role.split("[|]");
                role = roleList[0];
                for(int i=2; i<roleList.length; i++){
                    roleName += roleList[i];
                }
                System.out.println("webtoon크롤러의 인물리스트: " + roleList.toString());
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

//        웹툰 내용
        String summary = setting.driver.findElement(By.cssSelector("dd.e1kvv3954")).getText();

        List<WebElement> detailInfo = setting.driver.findElements(By.className("e1kvv3951"));

        String titleOrg = "";
        String writer = "";
        String genre = "";
        String serDetail = "";
        String serDay = "";
        String serPeriod = "";
        String ageLimit = "";

        for(WebElement info : detailInfo){
            if(info.findElement(By.className("e1kvv3952")).getText().equals("원제")){
                titleOrg = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("작가")){
                writer = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("장르")){
                genre = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연재 정보")){
                serDetail = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연재 요일")){
                serDay = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연재 기간")){
                serPeriod = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연령 등급")){
                ageLimit = info.findElement(By.className("e1kvv3953")).getText();
            }
        }

        setting.driver.close();
        try{
            webtoonService.webtoonCreate(thumbnail.equals("")?null:thumbnail
                    ,title,titleOrg,writer,genre,serDetail,
                    serDay.equals("")?null:serDay
                    ,serPeriod
                    ,ageLimit.equals("")?null:ageLimit
                    ,summary.equals("-")?null:summary
                    ,peopleURL
                    ,watchList.equals("")?null:watchList);

        } catch(Exception e){
            e.printStackTrace();
            System.out.println("** 이미 등록된 컨텐츠입니다. **");
        }
    }
}

