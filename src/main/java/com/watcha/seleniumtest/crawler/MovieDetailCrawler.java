package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.repository.MovieRepository;
import com.watcha.seleniumtest.service.MovieService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieDetailCrawler {
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;

    public void detail(String link) {
        Setting setting = new Setting();
        setting.init(link);
        setting.driver.get(setting.base_url);

        String thumbnail = setting.driver.findElement(By.cssSelector(".css-qhzw1o-StyledImg")).getAttribute("src");

        WebElement content = setting.driver.findElement(By.cssSelector("div.css-10ofaaw")); //컨텐츠 추출
//        long stTime1 = new Date().getTime(); //현재시간
//        while (new Date().getTime() < stTime1 + 3000) { //3초 동안 무한스크롤 지속
//            try {
//                Thread.sleep(500); //리소스 초과 방지
//                //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
//                ((JavascriptExecutor) setting.driver).executeScript("window.scrollTo(0, document.body.scrollHeight)", content);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//        // 비슷한 작품 더보기 클릭
//        try{
//        setting.driver.findElement(By.cssSelector("button.css-1d4r906-StylelessButton")).click();
//        try {Thread.sleep(1000);} catch (InterruptedException e) {;}}catch(Exception e){
//            System.out.println("** 비슷한 작품 더보기 버튼이 없습니다 ** ");
//        };

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
//        String people = "";
        List<String> peopleURL = new ArrayList<>();
        for(WebElement person : peopleList){
            String personURL = person.findElement(By.cssSelector(".css-1aaqvgs-InnerPartOfListWithImage")).getAttribute("href");
            String role =  person.findElement(By.cssSelector("div.css-1evnpxk-StyledSubtitle")).getText();
            String roleName = "";
            if(role.contains("|")){
                String[] roleList = role.split("[|]");
                role = roleList[0].trim();
                if(roleList.length >= 2){
                    roleName += roleList[1].trim();
                }
            }
//            String name = person.findElement(By.cssSelector(".css-17vuhtq")).getText();
            if(roleName.equals("")){
                peopleURL.add(personURL + "," + role + "");
//                people += name + "(" + role + "),";
            }else{
                peopleURL.add(personURL + "," + role + " | " + roleName + "");
//                people += name + "(" + role + "-" + roleName + "),";
            }
        }
//        people.substring(0,people.length()-1);

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
        String time = "";
        String ageLimit = "";

        for(WebElement info : detailInfo){
            if(info.findElement(By.className("e1kvv3952")).getText().equals("원제")){
                titleOrg = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("제작 연도")){
                makingDate = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("국가")){
                country = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("장르")){
                genre = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("상영시간")){
                time = info.findElement(By.className("e1kvv3953")).getText();
            }else if(info.findElement(By.className("e1kvv3952")).getText().equals("연령 등급")){
                ageLimit = info.findElement(By.className("e1kvv3953")).getText();
            }
        }
        Optional<Movie> mov = movieRepository.findByMovTitleAndMovTimeAndMovMakingDate(title,time,makingDate);
        if(!mov.isEmpty()) {
            System.out.println("** 이미 등록되어있는 영화입니다. **");
            setting.driver.close();
            return;
        }

        setting.driver.close();
        //CGV 검색 연결
        setting.init("http://www.cgv.co.kr/search/?query=" + title);
        setting.driver.get(setting.base_url);
        try {
            WebElement reservation =  setting.driver.findElement(By.cssSelector(".preOrderMovie_list"));
            String resLink = reservation.findElement(By.cssSelector(".btn_style1")).getAttribute("href");
            System.out.println("cgv if 전");
            if(!resLink.equals("")){
                System.out.println("cgv if 후");
                if(!watchList.equals("")){
                    watchList += "|" + resLink;
                }else{
                    watchList += resLink;
                }
            }
        }catch(Exception e){
            System.out.println("** 예매 가능한 영화가 아닙니다. **");
        };

        setting.driver.close();
        //메가박스 검색 연결
        setting.init("https://www.megabox.co.kr/movie?searchText=" + title);
        setting.driver.get(setting.base_url);
        try {
            WebElement reservation = setting.driver.findElement(By.cssSelector(".movieStat3"));
            if(reservation.getAttribute("style")==null){
                System.out.println("스타일값" + reservation.getAttribute("style"));
                System.out.println("** 예매 가능한 영화가 아닙니다. **");
            }else {
                if(!watchList.equals("")){
                    watchList += ",https://www.megabox.co.kr/booking";
                }else{
                    watchList += "https://www.megabox.co.kr/booking";
                }
            }
        }catch(Exception e){
            System.out.println("** 예매 가능한 영화가 아닙니다. **");
        }
        setting.driver.close();

        try{
//            System.out.println("***************null값 체크********************");
//            System.out.println("타이틀: " + title);
//            System.out.println("원제: " + titleOrg);
//            System.out.println("제작연도: " + makingDate);
//            System.out.println("국가: " + country);
//            System.out.println("장르: " + genre);
//            System.out.println("상영시간: " + time);
//            System.out.println("연령 등급: " + ageLimit);
//            System.out.println("인물: " + people);
//            System.out.println("내용: " + summary);
//            System.out.println("갤러리링크: " + gallery);
//            System.out.println("비디오링크: " + video);
//            System.out.println("감상가능한곳: " + watchList);
//            System.out.println("백그라운드 이미지: " + backgroundImg);
//
            movieService.movCreate(thumbnail.equals("")?null:thumbnail
                    ,title,titleOrg,makingDate,country,genre,time,
                    ageLimit.equals("")?null:ageLimit,peopleURL,
                    summary.equals("-")?null:summary,
                    gallery.equals("")?null:gallery,
                    video.equals("")?null:video,
                    watchList.equals("")?null:watchList,
                    backgroundImg.equals("")?null:backgroundImg);

            setting.driver.close();
//            //댓글 링크 연결
//            setting.init(link + "/comments");
//            setting.driver.get(setting.base_url);
//
//            WebElement commentContainer = setting.driver.findElement(By.cssSelector(".e1ntr3260"));
//            long stTime2 = new Date().getTime(); //현재시간
//            while (new Date().getTime() < stTime2 + 5000) { //5초 동안 무한스크롤 지속
//                try {
//                    Thread.sleep(500); //리소스 초과 방지
//                    //executeScript: 해당 페이지에 JavaScript 명령을 보내는 거
//                    ((JavascriptExecutor) setting.driver).executeScript("window.scrollTo(0, document.body.scrollHeight)", commentContainer);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//            try{
//                List<WebElement> spo = commentContainer.findElements(By.cssSelector("button[aria-label='Accept Spoiler']"));
//                for(WebElement idx: spo){
//                    System.out.println(idx.getText());
//                    System.out.println("클릭전");
//                    idx.sendKeys(Keys.ENTER);
//                    System.out.println("클릭후");
//                    try {Thread.sleep(1000);} catch (InterruptedException e) {;}
//                }
//            }catch(Exception e){};

//            // 댓글 div 찾기
//            List<WebElement> comment = commentContainer.findElements(By.cssSelector("div.css-bawlbm"));
//            List<Comment> commentList = new ArrayList<>();
//            int i = 1;
//            for(WebElement idx : comment){
//                String username = idx.findElement(By.cssSelector(".css-1agoci2")).getText();
//                String text = idx.findElement(By.className("css-4tkoly")).findElement(By.cssSelector("span")).getText();
//                Comment com = Comment.builder().commName(username).commText(text).build();
//                commentList.add(com);
//                i++;
//            }
//            movieService.movComment(commentList,title,time,makingDate);

//            // DB 연결
//            Connection conn = Dbconn.getConnection();
//            StringBuilder sql = new StringBuilder();
//            sql.append("insert into tb_movie (mov_thumbnail, mov_title, mov_title_org, mov_making_date, mov_country, mov_genre,")
//                    .append("mov_time, mov_age_limit, mov_people, mov_summary, mov_gallery, mov_video, mov_ott)")
//                    .append("values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
//            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
//            pstmt.setString(1, thumbnail);
//            pstmt.setString(2, title);
//            pstmt.setString(3, titleOrg);
//            pstmt.setString(4, makingDate);
//            pstmt.setString(5, country);
//            pstmt.setString(6, genre);
//            pstmt.setString(7, time);
//            pstmt.setString(8, ageLimit);
//            pstmt.setString(9, people);
//            pstmt.setString(10, summary);
//            pstmt.setString(11, gallery);
//            pstmt.setString(12, video);
//            pstmt.setString(13, watchList);
//
//            int result = pstmt.executeUpdate();
//            if(result >= 1) System.out.println("🎉 등록 성공 🎉");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

