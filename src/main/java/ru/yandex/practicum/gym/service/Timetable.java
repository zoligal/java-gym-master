package ru.yandex.practicum.gym.service;

import ru.yandex.practicum.gym.model.Client;
import ru.yandex.practicum.gym.model.Coach;
import ru.yandex.practicum.gym.model.DayOfWeek;
import ru.yandex.practicum.gym.model.TimeOfDay;


import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, List<TrainingSession>> sessionsByDay = new EnumMap<>(DayOfWeek.class);
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> sessionsByDayAndTime = new EnumMap<>(DayOfWeek.class);
    private final Map<TrainingSession, List<Client>> registrations = new HashMap<>();
    private final Set<TrainingSession> existingSessions = new HashSet<>();


    public Timetable() {

    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (existingSessions.contains(trainingSession)) {
            return;
        }

        List<TrainingSession> dayList = sessionsByDay.computeIfAbsent(day, k -> new ArrayList<>());
        dayList.add(trainingSession);

        dayList.sort(Comparator.comparing(TrainingSession::getTimeOfDay));
        TreeMap<TimeOfDay, List<TrainingSession>> timeMap = sessionsByDayAndTime.computeIfAbsent(day, k -> new TreeMap<>());
        timeMap.computeIfAbsent(time, k -> new ArrayList<>()).add(trainingSession);
        existingSessions.add(trainingSession);
    }


        public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> list = sessionsByDay.get(dayOfWeek);
        return (list == null) ? Collections.emptyList() : list;
    }


        public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> timeMap = sessionsByDayAndTime.get(dayOfWeek);
        if (timeMap == null) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessions = timeMap.get(timeOfDay);
        return (sessions == null) ? Collections.emptyList() : sessions;
    }

        public boolean canClientJoin(Client client, TrainingSession session) {
        return client.getAge() == session.getGroup().getAge();
    }

        public boolean registerClient(Client client, TrainingSession session) {
           if (!canClientJoin(client, session)) {
            return false;
        }
        List<Client> clients = registrations.computeIfAbsent(session, k -> new ArrayList<>());
        clients.add(client);
        return true;
    }

    public List<Client> getClientsForSession(TrainingSession session) {
        return registrations.getOrDefault(session, Collections.emptyList());
    }

    public int getTrainingCountPerWeek(Coach coach) {
        int count = 0;
        for (List<TrainingSession> sessions : sessionsByDay.values()) {
            for (TrainingSession session : sessions) {
                if (Objects.equals(session.getCoach(), coach)) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachTrainingCount = new HashMap<>();
        for (List<TrainingSession> sessions : sessionsByDay.values()) {
            for (TrainingSession session : sessions) {
                Coach coach = session.getCoach();
                int currentCount = coachTrainingCount.getOrDefault(coach, 0);
                coachTrainingCount.put(coach, currentCount + 1);
            }
        }

        List<CounterOfTrainings> counters = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachTrainingCount.entrySet()) {
            counters.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(counters, (c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));
        return counters;
    }
}
