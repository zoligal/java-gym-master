package ru.yandex.practicum.gym.model;

import java.util.Objects;

public class TimeOfDay implements Comparable<TimeOfDay> {

    public static final TimeOfDay MORNING = new TimeOfDay(10, 0);
    public static final TimeOfDay AFTERNOON = new TimeOfDay(14, 0);
    public static final TimeOfDay EVENING = new TimeOfDay(18, 0);

    private int hours;
    private int minutes;

    public TimeOfDay(int hours, int minutes) {
        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException("Часы должны быть в диапазоне 0–23, получено: " + hours);
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Минуты должны быть в диапазоне 0–59, получено: " + minutes);
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public int compareTo(TimeOfDay o) {
        if (hours != o.hours) return hours - o.hours;
        return minutes - o.minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeOfDay timeOfDay = (TimeOfDay) o;
        return hours == timeOfDay.hours && minutes == timeOfDay.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }
}
