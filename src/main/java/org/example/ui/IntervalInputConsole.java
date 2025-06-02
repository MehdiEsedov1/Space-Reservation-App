package org.example.ui;

import org.example.entity.Interval;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.example.ui.util.ConsoleReader.readLine;

public class IntervalInputConsole {

    private static final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT);

    public Interval getInterval() {
        String dateStr = readLine("Enter date (yyyy-MM-dd): ");
        String startTimeStr = readLine("Enter start time (HH:mm): ");
        String endTimeStr = readLine("Enter end time (HH:mm): ");

        if (dateStr.isBlank() || startTimeStr.isBlank() || endTimeStr.isBlank()) {
            System.out.println("Date and time inputs cannot be empty!");
            return null;
        }

        try {
            Date startTime = dateTimeFormat.parse(dateStr + " " + startTimeStr);
            Date endTime = dateTimeFormat.parse(dateStr + " " + endTimeStr);

            if (startTime.after(endTime)) {
                System.out.println("Start time must be before end time!");
                return null;
            }

            return new Interval.IntervalBuilder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();

        } catch (ParseException e) {
            System.out.println("Invalid date or time format! Use yyyy-MM-dd and HH:mm");
            return null;
        }
    }
}
