package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) {
        StringBuilder builder = new StringBuilder();
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            Element body = document.select(".section-box").get(1).child(0);
            builder.append(body.child(0).text() + "\n" + body.child(1).text());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();
        String tempLink;
        try {
            for (int i = 1; i < 6; i++) {
                tempLink = String.format("%s?page=%s", link, i);
                Connection connection = Jsoup.connect(tempLink);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element linkElement = titleElement.child(0);
                    Element dateElement = row.select(".vacancy-card__date").first();
                    Element timeElement = dateElement.child(0);
                    String vacancyName = titleElement.text();
                    String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    String time = timeElement.attr("dateTime");
                    Post tempPost = new Post();
                    tempPost.setTitle(vacancyName);
                    tempPost.setCreated(dateTimeParser.parse(time));
                    tempPost.setDescription(this.retrieveDescription(vacancyLink));
                    tempPost.setLink(vacancyLink);
                    list.add(tempPost);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> list = habrCareerParse.list(PAGE_LINK);
        list.forEach(System.out::println);
    }
}