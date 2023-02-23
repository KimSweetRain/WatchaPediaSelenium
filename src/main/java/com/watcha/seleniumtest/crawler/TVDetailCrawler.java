package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Entity.Comment;
import com.watcha.seleniumtest.service.MovieService;
import com.watcha.seleniumtest.service.TVService;
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
public class TVDetailCrawler {
    @Autowired
    private TVService tvService;

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

        //출연/제작자 정보 추출기
        List<WebElement> peopleList = content.findElements(By.cssSelector("li.css-54rr1e"));
        List<String> peopleURL = new ArrayList<>();
        for(WebElement person : peopleList){
            String personURL = person.findElement(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage")).getAttribute("href");
            String role =  person.findElement(By.cssSelector("div.css-1evnpxk-StyledSubtitle")).getText();
            String roleName = "";
            if(role.contains(" | ")){
                String[] roleList = role.split("[|]");
                role = roleList[0].trim();
                if(roleList.length >= 2){
                    roleName += roleList[1].trim();
                }
            }
            if(roleName.equals("")){
                peopleURL.add(personURL + "," + role + "");
            }else{
                peopleURL.add(personURL + "," + role + " | " + roleName + "");
            }
        }

//        동영상 링크
        String video = "";
        try{
            List<WebElement> videoList = content.findElements(By.className("css-1xgzykb-VideoListItem"));
            String videoLast = videoList.get(videoList.size()-1).findElement(By.className("css-18apgv4")).getAttribute("href");
            for(WebElement li : videoList){
                if(li.findElement(By.className("css-18apgv4")).getAttribute("href").equals(videoLast)){
                    video += li.findElement(By.className("css-18apgv4")).getAttribute("href")
                        + "," + li.findElement(By.className("css-1fucs4t-StyledText")).getText();
                }else{
                    video += li.findElement(By.className("css-18apgv4")).getAttribute("href")
                            + "," + li.findElement(By.className("css-1fucs4t-StyledText")).getText() + "|";
                }
            }
        }catch(Exception e){
            System.out.println("** 동영상 링크가 없습니다. **");
        }

//        갤러리 링크
        String gallery = "";
        try{
            List<WebElement> galleryList = content.findElements(By.cssSelector("li.css-1cw0vk0"));
            for(int i=0; i<galleryList.size(); i++){
                ((JavascriptExecutor) setting.driver).executeScript("const li = document.querySelectorAll('.css-1cw0vk0');" +
                        "li.item("+ i +").querySelector('.e1q5rx9q1').click();");
                try {
                    Thread.sleep(1000);
                    System.out.println("이미지 링크: " + setting.driver.findElement(By.cssSelector(".css-1mshedn")));
                    gallery += setting.driver.findElement(By.cssSelector(".css-1mshedn")).getAttribute("src")+ "|";
                    ((JavascriptExecutor) setting.driver).executeScript("document.querySelector('.css-171obip').click();");
                } catch (InterruptedException e) {
                    System.out.println("** 이미지 소스 가져오기 실패 **");
                }
            }
            gallery = gallery.substring(0, gallery.length()-1);
        }catch(Exception e){
            System.out.println("** 갤러리가 없습니다 **");
            e.printStackTrace();
        }

        setting.driver.close();
        //상세정보 링크 연결
        setting.init(link + "/overview");
        setting.driver.get(setting.base_url);

//        영화 내용
        String summary = setting.driver.findElement(By.cssSelector("dd.e1kvv3954")).getText();

        List<WebElement> detailInfo = setting.driver.findElements(By.className("e1kvv3951"));

        String titleOrg = "";
        String makingDate = "";
        String country = "";
        String genre = "";
        String channel = "";
        String ageLimit = "";

        for(WebElement info : detailInfo){
            if(info.findElement(By.className("e1kvv3952")).getText().equals("원제")){
                titleOrg = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("제작 연도")){
                makingDate = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("채널")){
                channel = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("장르")){
                genre = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("국가")){
                country = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연령 등급")){
                ageLimit = info.findElement(By.className("e1kvv3953")).getText();
            }
        }

        setting.driver.close();
        try{
            tvService.tvCreate(thumbnail.equals("")?null:thumbnail
                    ,title,titleOrg,makingDate,channel,genre,country,
                    ageLimit.equals("")?null:ageLimit,
                    peopleURL,
                    summary.equals("-")?null:summary,
                    gallery.equals("")?null:gallery,
                    video.equals("")?null:video,
                    watchList.equals("")?null:watchList,
                    backgroundImg.equals("")?null:backgroundImg);
            setting.driver.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

