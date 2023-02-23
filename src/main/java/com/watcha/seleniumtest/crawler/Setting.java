package com.watcha.seleniumtest.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Setting {
    public WebDriver driver;
    final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    final String WEB_DRIVER_PATH = "/Users/sweetrain.k/Desktop/AI교육과정/프로젝트/SeleniumTest/chromedriver";
    public String base_url;

    public void init(String input) {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//로드 웹페이지에서 특정 요소를 찾을 때까지 기다리는 시간 설정
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);//페이지로드가 완료 될 때까지 기다리는 시간 설정
        driver.manage().window().maximize();//브라우저 창 최대화

        base_url = input;
    }
}

