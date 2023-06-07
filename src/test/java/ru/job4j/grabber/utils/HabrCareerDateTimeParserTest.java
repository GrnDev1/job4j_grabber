package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    public void whenLocalDateTimeIsSuccessful() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String time = "2023-06-07T18:48:28+03:00";
        LocalDateTime expected = LocalDateTime.parse("2023-06-07T18:48:28", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThat(parser.parse(time)).isEqualTo(expected);
    }
}