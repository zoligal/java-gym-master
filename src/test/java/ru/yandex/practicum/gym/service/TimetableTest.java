package ru.yandex.practicum.gym.service;

import ru.yandex.practicum.gym.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.model.Coach;
import ru.yandex.practicum.gym.model.Group;
import ru.yandex.practicum.gym.model.DayOfWeek;
import ru.yandex.practicum.gym.model.TimeOfDay;
import java.util.List;

public class TimetableTest {

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(
                group,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size(), "За понедельник должно быть 1 занятие");
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0), "Занятие за понедельник не совпадает с добавленным");

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty(), "За вторник не должно быть занятий");
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(
                groupAdult,
                coach,
                DayOfWeek.THURSDAY,
                new TimeOfDay(20, 0)
        );

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика детская", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(
                groupChild,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );
        TrainingSession thursdayChildTrainingSession = new TrainingSession(
                groupChild,
                coach,
                DayOfWeek.THURSDAY,
                new TimeOfDay(13, 0)
        );
        TrainingSession saturdayChildTrainingSession = new TrainingSession(
                groupChild,
                coach,
                DayOfWeek.SATURDAY,
                new TimeOfDay(10, 0)
        );

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Assertions.assertEquals(
                1,
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(),
                "За понедельник должно быть 1 занятие"
        );

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size(), "За четверг должно быть 2 занятия");
        Assertions.assertEquals(new TimeOfDay(13, 0), thursdaySessions.get(0).getTime(), "Первое занятие в четверг должно быть в 13:00");
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdaySessions.get(1).getTime(), "Второе занятие в четверг должно быть в 20:00");

        Assertions.assertTrue(
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty(),
                "За вторник не должно быть занятий"
        );
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Фитнес для продвинутых", Age.CHILD, 60);
        Coach coach = new Coach("Курносов", "Александр", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(
                group,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessionsAt13.size(), "В понедельник в 13:00 должно быть 1 занятие");
        Assertions.assertEquals(singleTrainingSession, sessionsAt13.get(0), "Занятие в 13:00 не совпадает с добавленным");

        List<TrainingSession> sessionsAt14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertTrue(sessionsAt14.isEmpty(), "В понедельник в 14:00 не должно быть занятий");
    }
    @Test
    public void testGetCountByCoachesEmpty() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertTrue(counts.isEmpty(), "При пустом расписании список должен быть пуст");
    }

    @Test
    public void testGetCountByCoachesSingleCoachOneSession() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Пример", Age.ADULT, 60);
        TrainingSession session = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(session);

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(1, counts.size());
        CounterOfTrainings counter = counts.get(0);
        Assertions.assertEquals(coach, counter.getCoach());
        Assertions.assertEquals(1, counter.getCount());
    }

    @Test
    public void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Петров", "Пётр", "Петрович");
        Coach coach2 = new Coach("Сидоров", "Сидор", "Сидорович");

        Group group = new Group("Групповое занятие", Age.CHILD, 90);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.TUESDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(17, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(18, 0)));

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();

        Assertions.assertEquals(2, counts.size());

        CounterOfTrainings first = counts.get(0);
        Assertions.assertEquals(coach1, first.getCoach());
        Assertions.assertEquals(2, first.getCount());

        CounterOfTrainings second = counts.get(1);
        Assertions.assertEquals(coach2, second.getCoach());
        Assertions.assertEquals(1, second.getCount());
    }
}
