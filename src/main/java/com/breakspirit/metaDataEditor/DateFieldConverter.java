package com.breakspirit.metaDataEditor;

import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFieldConverter extends StringConverter<LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    @Override
    public String toString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        } else {
            return dateTimeFormatter.format(localDateTime);
        }
    }

    @Override
    public LocalDateTime fromString(String localDateTimeString) {
        try {
            return LocalDateTime.parse(localDateTimeString, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
