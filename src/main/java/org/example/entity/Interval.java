package org.example.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Interval {
    private final Date startTime;
    private final Date endTime;

    public Interval(IntervalBuilder builder) {
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static boolean isOverlap(Interval i1, Interval i2) {
        return i1.startTime.before(i2.endTime) && i2.startTime.before(i1.endTime);
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT);
        return "[" + sdf.format(startTime) + " to " + sdf.format(endTime) + "]";
    }

    public static class IntervalBuilder {
        private Date startTime;
        private Date endTime;

        public IntervalBuilder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public IntervalBuilder endTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public Interval build() {
            if (startTime == null || endTime == null) {
                throw new IllegalStateException("Start and end time must not be null.");
            }
            if (!startTime.before(endTime)) {
                throw new IllegalArgumentException("Start time must be before end time.");
            }
            return new Interval(this);
        }
    }
}
