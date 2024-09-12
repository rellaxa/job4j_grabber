package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class HabrCareerParse implements Parse {

    private static final int MAX_PAGE = 5;
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            Element element = document.select(".vacancy-description__text").first();
            return element.text();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> list(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        List<Post> posts = new ArrayList<>();

        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {

            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();
            String date = row.select(".vacancy-card__date")
                    .first()
                    .child(0)
                    .attr("datetime");
            LocalDateTime dateCreated = dateTimeParser.parse(date);
            String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            String description = retrieveDescription(vacancyLink);

            Post post = new Post();
            post.setTitle(vacancyName);
            post.setLink(vacancyLink);
            post.setDescription(description);
            post.setCreated(dateCreated);
            posts.add(post);
        });
        return posts;
    }

    public static void main(String[] args) throws IOException {
        DateTimeParser parser = new HabrCareerDateTimeParser();
        HabrCareerParse habrCareerParse = new HabrCareerParse(parser);

        List<Post> posts = new ArrayList<>();
        for (int pageNumber = 1; pageNumber <= HabrCareerParse.MAX_PAGE; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            posts.addAll(habrCareerParse.list(fullLink));
        }
        posts.forEach(System.out::println);
    }
}
