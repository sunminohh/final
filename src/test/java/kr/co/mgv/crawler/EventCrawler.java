package kr.co.mgv.crawler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EventCrawler {
    private static final String BASE_URL = "https://www.megabox.co.kr/event";
    private static final String CHROME_DRIVER_PATH = "C:/Users/Administrator/Downloads/chromedriver.exe";
    private static final String EVENT_LIST_CSS_SELECTOR = ".event-list";
    private static final String EVENT_ELEMENT_CSS_SELECTOR = "li>a";
    private static final String EVENT_DETAIL_CSS_SELECTOR = ".event-detail";
    private static final String EVENT_DATE_CSS_SELECTOR = ".event-detail-date>em";
    private static final String EVENT_IMAGE_CSS_SELECTOR = "img";
    private static final Duration WAIT_DURATION = Duration.ofSeconds(10);

    public static void main(String[] args) {
        // Chrome WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.setLogLevel(ChromeDriverLogLevel.fromLevel(Level.WARNING));
        WebDriver webDriver = new ChromeDriver(options);
        try {
            for (EventCategory category : EventCategory.values()) {
                processCategory(webDriver, category);
            }
        } finally {
            webDriver.quit();
        }
    }

    private static void processCategory(WebDriver webDriver, EventCategory category) {
        String categoryPath = category.name().toLowerCase();
        String categoryUrl = String.format("%s/%s", BASE_URL, categoryPath);

        System.out.printf("[%s] 요청: %s%n", category.description, categoryUrl);
        webDriver.get(categoryUrl);

        new WebDriverWait(webDriver, WAIT_DURATION).until((ExpectedCondition<Boolean>) driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        WebElement eventList = webDriver.findElement(By.cssSelector(EVENT_LIST_CSS_SELECTOR));
        List<WebElement> eventElements = eventList.findElements(By.cssSelector(EVENT_ELEMENT_CSS_SELECTOR));
        List<String> eventIds = eventElements.stream()
                .map(event -> event.getAttribute("data-no"))
                .collect(Collectors.toList());

        for (String eventId : eventIds) {
            processEvent(webDriver, eventId);
        }
    }

    private static void processEvent(WebDriver webDriver, String eventId) {
        String eventDetailUrl = String.format("%s/%s?eventNo=%s", BASE_URL, "detail", eventId);
        webDriver.get(eventDetailUrl);

        new WebDriverWait(webDriver, WAIT_DURATION).until((ExpectedCondition<Boolean>) driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        WebElement eventDetails = webDriver.findElement(By.cssSelector(EVENT_DETAIL_CSS_SELECTOR));
        WebElement eventDateElement = eventDetails.findElement(By.cssSelector(EVENT_DATE_CSS_SELECTOR));
        List<WebElement> imageElements = eventDetails.findElements(By.cssSelector(EVENT_IMAGE_CSS_SELECTOR));

        System.out.printf("[DATE] %s%n", eventDateElement.getText());

        for (WebElement imageElement : imageElements) {
            String imageUrl = imageElement.getAttribute("href");
            System.out.printf("[IMAGE-URL] %s%n", imageUrl);
        }
    }

    @Getter
    @AllArgsConstructor
    private enum EventCategory {
        MEGABOX("메가Pick"),
        MOVIE("영화"),
        THEATER("극장"),
        PROMOTION("제휴/할인"),
        CURTAINCALL("시사회/무대인사");
        private final String description;
    }
}
