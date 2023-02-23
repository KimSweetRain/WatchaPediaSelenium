package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Entity.Movie;
import com.watcha.seleniumtest.Entity.TV;
import com.watcha.seleniumtest.Entity.Watcha;
import com.watcha.seleniumtest.Entity.repository.MovieRepository;
import com.watcha.seleniumtest.Entity.repository.TVRepository;
import com.watcha.seleniumtest.Entity.repository.WatchaTopRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchaTop10 {

    @Autowired private WatchaTopRepository watchaTopRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private TVRepository tvRepository;

    public void movie(){
        Setting setting = new Setting();
        setting.init("https://watcha.com/sign_in");
        setting.driver.get(setting.base_url);

        WebElement id = setting.driver.findElement(By.cssSelector("[name=email]"));
        WebElement pw = setting.driver.findElement(By.cssSelector("[name=password]"));
        WebElement login = setting.driver.findElement(By.cssSelector(".css-11a3zmg"));

        String userid = "";
        String userpw = "";

        id.sendKeys(userid);
        pw.sendKeys(userpw);
        login.submit();

        // 프로필 클릭
        setting.driver.findElement(By.cssSelector(".css-1n8cdfd-ProfileName")).click();
        // 영화, tv 탭 선택
        List<WebElement> button = setting.driver.findElements(By.cssSelector(".css-doffhr-StyledTab"));
        for(WebElement btn : button){
            if(btn.getText().equals("영화")) btn.click();
        }

        List<WebElement> container = setting.driver.findElements(By.cssSelector(".css-jtawwa"));
        long i = 1;
        for(WebElement con : container) {
            if(con.findElement(By.cssSelector(".css-ugw8eb")).getText().equals("왓챠 영화 TOP 10")){
                List<WebElement> liList = con.findElements(By.cssSelector(".w_exposed_cell"));
                for(WebElement li : liList){
                    String title = li.findElement(By.cssSelector(".css-1i82ydo")).getAttribute("aria-label");
                    Optional<Movie> movTitle = movieRepository.findByMovTitle(title);
                    if(!movTitle.isEmpty()){
                        Watcha watcha = Watcha.builder().watRangking(i).watContentType("영화").watContentIdx(movTitle.get().getMovIdx()).watContentTitle(title).build();
                        watchaTopRepository.save(watcha);
                    }else{
                        Watcha watcha = Watcha.builder().watRangking(i).watContentType("영화").watContentTitle(title).build();
                        watchaTopRepository.save(watcha);
                    }
                    i++;
                }
            }
        }
    }

    public void TV(){
        Setting setting = new Setting();
        setting.init("https://watcha.com/sign_in");
        setting.driver.get(setting.base_url);

        WebElement id = setting.driver.findElement(By.cssSelector("[name=email]"));
        WebElement pw = setting.driver.findElement(By.cssSelector("[name=password]"));
        WebElement login = setting.driver.findElement(By.cssSelector(".css-11a3zmg"));

        String userid = "";
        String userpw = "";

        id.sendKeys(userid);
        pw.sendKeys(userpw);
        login.submit();

        setting.driver.findElement(By.cssSelector(".css-1n8cdfd-ProfileName")).click();
        List<WebElement> button = setting.driver.findElements(By.cssSelector(".css-doffhr-StyledTab"));
        for(WebElement btn : button){
            if(btn.getText().equals("TV 프로그램")) btn.click();
        }

        List<WebElement> container = setting.driver.findElements(By.cssSelector(".css-jtawwa"));
        int i = 1;
        for(WebElement con : container) {
            if(con.findElement(By.cssSelector(".css-ugw8eb")).getText().equals("왓챠 TV 프로그램 TOP 10")){
                List<WebElement> liList = con.findElements(By.cssSelector(".w_exposed_cell"));
                for(WebElement li : liList){
                    String title = li.findElement(By.cssSelector(".css-1i82ydo")).getAttribute("aria-label");
                    Optional<TV> TVTitle = tvRepository.findByTvTitle(title);
                    if(!TVTitle.isEmpty()){
                        Watcha watcha = Watcha.builder().watRangking((long) i).watContentType("TV").watContentIdx(TVTitle.get().getTvIdx()).watContentTitle(title).build();
                        watchaTopRepository.save(watcha);
                    }else{
                        Watcha watcha = Watcha.builder().watRangking((long) i).watContentType("TV").watContentTitle(title).build();
                        watchaTopRepository.save(watcha);
                    }
                    i++;
                }
            }
        }
    }
}
