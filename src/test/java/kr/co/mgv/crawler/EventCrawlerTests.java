package kr.co.mgv.crawler;

import kr.co.mgv.common.file.FileUtils;
import kr.co.mgv.common.vo.MgvFile;
import kr.co.mgv.event.dao.EventDao;
import kr.co.mgv.event.vo.Event;
import kr.co.mgv.event.vo.EventCategory;
import kr.co.mgv.movie.util.DateUtils;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SpringBootTest
public class EventCrawlerTests {

    @Autowired
    private EventDao eventDao;
    @Autowired
    private FileUtils fileUtils;
    private static final Logger log = Logger.getLogger("EventCrawlerTests");
    private static final String BASE_URL = "https://www.megabox.co.kr/event";
    private static final String CHROME_DRIVER_PATH = "C:/Users/sqptm/Downloads/chromedriver-win64/chromedriver.exe";
    private static final String EVENT_LIST_CSS_SELECTOR = ".event-list";
    private static final String EVENT_ELEMENT_CSS_SELECTOR = ".eventBtn";
    private static final String EVENT_DETAIL_CSS_SELECTOR = ".event-detail";
    private static final String EVENT_DATE_CSS_SELECTOR = ".event-detail-date>em";
    private static final String EVENT_IMAGE_CSS_SELECTOR = "img";
    private static final Duration WAIT_DURATION = Duration.ofSeconds(15);

    private static final String EVENT_IMAGE_DIRECTORY = "event";


    @Test
    public void collect() {
        Logger.getLogger("org.asynchttpclient").setLevel(Level.OFF);
        // Chrome WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.setLogLevel(ChromeDriverLogLevel.fromLevel(Level.WARNING));
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        try {
            for (Category category : Category.values()) {
                processCategory(webDriver, category);
            }
        } finally {
            webDriver.quit();
        }
    }

    private void processCategory(WebDriver webDriver, Category category) {
        String categoryPath = category.name().toLowerCase();
        String categoryUrl = String.format("%s/%s", BASE_URL, categoryPath);
        System.out.printf("[%s] 요청: %s%n", category.description, categoryUrl);
        webDriver.get(categoryUrl);

        new WebDriverWait(webDriver, WAIT_DURATION).until((ExpectedCondition<Boolean>) driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        WebElement eventList = webDriver.findElement(By.cssSelector(EVENT_LIST_CSS_SELECTOR));
        List<WebElement> eventElements = eventList.findElements(By.cssSelector(EVENT_ELEMENT_CSS_SELECTOR));

        List<HashMap<String, String>> eventInfos = eventElements.stream().map((eventElement) -> {
            String title = eventElement.findElement(By.cssSelector(".tit")).getText();
            String eventId = eventElement.getAttribute("data-no");
            String imageUrl = eventElement.findElement(By.tagName("img")).getAttribute("src");
            return new HashMap<String, String>() {{
                put("title", title);
                put("eventId", eventId);
                put("imageUrl", imageUrl);
            }};
        }).collect(Collectors.toList());

        eventInfos.stream().map(map -> {
            String eventId = map.get("eventId");
            Event saveEvent = processEvent(webDriver, eventId);
            String imageUrl = map.get("imageUrl");
            saveEvent.setUser(User.builder().id("admin").build());
            saveEvent.setCategory(EventCategory.builder().no(category.value).build());
            saveEvent.setTitle(map.get("title"));
            try {
                byte[] imageBytes = downloadImage(imageUrl);
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                MultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    fileName,
                    "image/jpeg",
                    imageBytes
                );
                MgvFile mainImage = fileUtils.saveFile(EVENT_IMAGE_DIRECTORY, multipartFile);
                saveEvent.setMainImageFile(mainImage);
            } catch (IOException e) {}
            return saveEvent;
        }).forEach(eventDao::insertEvent);
    }

    private Event processEvent(WebDriver webDriver, String eventId) {
        String eventDetailUrl = String.format("%s/%s?eventNo=%s", BASE_URL, "detail", eventId);
        webDriver.get(eventDetailUrl);

        new WebDriverWait(webDriver, WAIT_DURATION).until((ExpectedCondition<Boolean>) driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));

        WebElement eventDetails = webDriver.findElement(By.cssSelector(EVENT_DETAIL_CSS_SELECTOR));
        WebElement eventDateElement = eventDetails.findElement(By.cssSelector(EVENT_DATE_CSS_SELECTOR));
        List<WebElement> imageElements = eventDetails.findElements(By.cssSelector(EVENT_IMAGE_CSS_SELECTOR));

        System.out.printf("[DATE] %s%n", eventDateElement.getText());
        String[] dates = eventDateElement.getText().replace(".", "-").split(" ~ ");

        WebElement imageElement = imageElements.get(0);
        String imageUrl = imageElement.getAttribute("src");
        System.out.printf("[IMAGE-URL] %s%n", imageUrl);
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));

        Event.EventBuilder eventBuilder = Event.builder()
            .mainImage("")
            .detailImage("")
            .startDate(DateUtils.toDate(dates[0]))
            .endDate(DateUtils.toDate((dates[1])));

        try {
            byte[] imageBytes = downloadImage(imageUrl);
            MultipartFile multipartFile = new MockMultipartFile(
                "file",
                fileName,
                "image/jpeg",
                imageBytes
            );
            MgvFile detailImage = fileUtils.saveFile(EVENT_IMAGE_DIRECTORY, multipartFile);
            eventBuilder.detailImageFile(detailImage);
        } catch (IOException e) {}

        return eventBuilder.build();
    }

    @Getter
    @AllArgsConstructor
    private enum Category {
        MEGABOX("메가Pick", 2),
        MOVIE("영화", 3),
        THEATER("극장", 4);
        private final String description;
        private final int value;
    }

    public byte[] downloadImage(String imageUrl) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            byte[] chunk = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
        }

        return outputStream.toByteArray();
    }
}
