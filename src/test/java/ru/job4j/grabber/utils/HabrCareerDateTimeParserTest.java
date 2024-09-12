package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HabrCareerDateTimeParserTest {

    @Test
    public void whenDateCorrect() {
        String date = "2024-09-09T11:49:24+03:00";
        DateTimeParser parse = new HabrCareerDateTimeParser();
        LocalDateTime rsl = parse.parse(date);
        LocalDateTime exp = LocalDateTime.of(2024, 9, 9, 11, 49, 24);
        assertThat(rsl).isEqualTo(exp);
    }

    @Test
    public void whenWrongDate() {
        String date = "2024-09-09:11:49:24+03:00";
        DateTimeParser parse = new HabrCareerDateTimeParser();
        assertThrows(DateTimeParseException.class, () -> parse.parse(date));
    }
}