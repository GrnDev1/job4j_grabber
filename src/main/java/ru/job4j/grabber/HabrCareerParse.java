package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        String tempLink;
        for (int i = 1; i < 6; i++) {
            System.out.printf("Page %s :%n", i);
            tempLink = String.format("%s?page=%s", PAGE_LINK, i);
            Connection connection = Jsoup.connect(tempLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = row.select(".vacancy-card__date").first();
                Element timeElement = dateElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String time = timeElement.attr("dateTime");
                System.out.printf("%s %s %s%n", vacancyName, link, parser.parse(time));
            });
        }
    }
}