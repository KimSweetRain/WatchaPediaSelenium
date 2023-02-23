package com.watcha.seleniumtest.crawler;

import com.watcha.seleniumtest.Dbconn;
import com.watcha.seleniumtest.Entity.BoxOffice;
import com.watcha.seleniumtest.service.BoxOfficeService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BoxOfficeCrawler {
    @Autowired
    private BoxOfficeService boxOfficeService;
    public void main() {
        Setting setting = new Setting();
        setting.init("https://www.kobis.or.kr/kobis/business/stat/boxs/findRealTicketList.do");
        setting.driver.get(setting.base_url);

        WebElement table = setting.driver.findElement(By.cssSelector("table.tbl_comm")); //순위 테이블 추출

//        List<BoxOffice> boxOfficeList = new ArrayList<>();
        for(int i=1; i<=30; i++) {
            WebElement tr = table.findElement(By.cssSelector("tbody tr:nth-child(" + i + ")"));

            String rangking = tr.findElement(By.cssSelector("td:nth-child(1)")).getText();
            String movTitle = tr.findElement(By.cssSelector(".per90:nth-child(1)")).getText();
            String[] bookingList = tr.findElement(By.cssSelector("td:nth-child(4)")).getText().split("%");
            String[] spectatorsList = tr.findElement(By.cssSelector("td:nth-child(8)")).getText().split(",");
            String spectators = "";
            for(String idx : spectatorsList){
                spectators += idx;
            }


            // 영화 클릭
            try{
                tr.findElement(By.cssSelector(".per90 a")).click();
                try {Thread.sleep(500);} catch (InterruptedException e) {;}}catch(Exception e){
                System.out.println("** 영화 클릭 실패 ** ");
            };
            WebElement info = setting.driver.findElement(By.cssSelector(".basic")).findElement(By.cssSelector("dl.cont"));
            String[] countryList = info.findElement(By.cssSelector("dd:nth-child(8)")).getText().trim().split(" | ");
            String country = countryList[countryList.length-1];
            String[] years = info.findElement(By.cssSelector("dd:nth-child(14)")).getText().trim().split("년");


            System.out.println("***************null값 체크********************");
            System.out.println("순위: " + rangking);
            System.out.println("영화제목: " + movTitle);
            System.out.println("예매율: " + Double.parseDouble(bookingList[0]));
            System.out.println("누적관객수: " + spectators);
            System.out.println("제작연도: " + years[0]);
            System.out.println("나라: " + country);


            BoxOffice boxOffice = BoxOffice.builder().boxRangking(Long.valueOf(rangking)).boxMovTitle(movTitle).boxBooking(Double.parseDouble(bookingList[0]))
                            .boxSpectators(Long.valueOf(spectators)).build();
            boxOfficeService.create(boxOffice, country, years[0]);
            try{
                setting.driver.findElement(By.cssSelector(".close")).click();
                try {Thread.sleep(500);} catch (InterruptedException e) {;}}catch(Exception e){
                System.out.println("** 영화 클릭 실패 ** ");
            };
//            boxOfficeList.add(boxOffice);
        }
        setting.driver.close();
    }
}

