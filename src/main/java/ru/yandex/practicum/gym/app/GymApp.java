package ru.yandex.practicum.gym.app;

import ru.yandex.practicum.gym.model.Client;
import ru.yandex.practicum.gym.model.Coach;
import ru.yandex.practicum.gym.model.Group;
import ru.yandex.practicum.gym.model.Age;
import ru.yandex.practicum.gym.model.TimeOfDay;
import ru.yandex.practicum.gym.model.DayOfWeek;
import ru.yandex.practicum.gym.service.Timetable;
import ru.yandex.practicum.gym.service.TrainingSession;
import java.util.*;

public class GymApp {
    public static void main(String[] args) {
        TimeOfDay morning = TimeOfDay.MORNING;
        TimeOfDay afternoon = TimeOfDay.AFTERNOON;
        TimeOfDay evening = TimeOfDay.EVENING;
        TimeOfDay timeNoon = new TimeOfDay(12, 0);
        TimeOfDay timeThree = new TimeOfDay(16, 0);

        Group adultGroup1 = new Group("Фитнес", Age.ADULT, 60);
        Group adultGroup2 = new Group("Джампинг", Age.ADULT, 90);
        Group childGroup1 = new Group("Акробатика", Age.CHILD, 45);
        Group childGroup2 = new Group("Джампинг-кидс", Age.CHILD, 50);

        List<Group> groups = new ArrayList<>();
        groups.add(adultGroup1);
        groups.add(adultGroup2);
        groups.add(childGroup1);
        groups.add(childGroup2);

        Coach coach1 = new Coach("Васильев", "Иван", "Александрович");
        Coach coach2 = new Coach("Николаева", "Марина", "Евгеньевна");
        Coach coach3 = new Coach("Максимов", "Алексей", "Петрович");

        List<Coach> coaches = new ArrayList<>();
        coaches.add(coach1);
        coaches.add(coach2);
        coaches.add(coach3);

        System.out.println("= Группы =");
        for (Group group : groups) {
            String ageLabel = group.getAge() == Age.ADULT ? "Взрослые" : "Дети";
            System.out.printf("Группа: %s | Возраст: %s | Длительность: %d мин%n",
                    group.getTitle(), ageLabel, group.getDuration());
        }

        System.out.println("\n= Тренеры =");
        for (Coach coach : coaches) {
            String fullName = coach.getSurname() + " " + coach.getName() + " " + coach.getMiddleName();
            System.out.println("Тренер: " + fullName);
        }

        List<TrainingSession> allSessions = List.of(
                new TrainingSession(adultGroup1, coach1, DayOfWeek.MONDAY, morning),
                new TrainingSession(adultGroup2, coach2, DayOfWeek.MONDAY, afternoon),
                new TrainingSession(childGroup1, coach3, DayOfWeek.MONDAY, evening),

                new TrainingSession(adultGroup1, coach2, DayOfWeek.TUESDAY, timeNoon),
                new TrainingSession(childGroup2, coach1, DayOfWeek.TUESDAY, timeThree),
                new TrainingSession(childGroup1, coach2, DayOfWeek.TUESDAY, afternoon),

                new TrainingSession(adultGroup2, coach3, DayOfWeek.WEDNESDAY, morning),
                new TrainingSession(childGroup2, coach2, DayOfWeek.WEDNESDAY, afternoon),
                new TrainingSession(adultGroup1, coach3, DayOfWeek.WEDNESDAY, evening),
                new TrainingSession(adultGroup2, coach2, DayOfWeek.WEDNESDAY, afternoon),

                new TrainingSession(adultGroup2, coach2, DayOfWeek.THURSDAY, timeNoon),
                new TrainingSession(childGroup2, coach1, DayOfWeek.THURSDAY, timeThree),
                new TrainingSession(childGroup2, coach3, DayOfWeek.THURSDAY, morning),

                new TrainingSession(childGroup1, coach3, DayOfWeek.FRIDAY, morning),
                new TrainingSession(adultGroup1, coach2, DayOfWeek.FRIDAY, afternoon),
                new TrainingSession(childGroup2, coach1, DayOfWeek.FRIDAY, evening),
                new TrainingSession(adultGroup1, coach3, DayOfWeek.FRIDAY, morning),

                new TrainingSession(childGroup1, coach2, DayOfWeek.SATURDAY, afternoon),
                new TrainingSession(adultGroup2, coach3, DayOfWeek.SATURDAY, evening),
                new TrainingSession(childGroup1, coach1, DayOfWeek.SATURDAY, evening),

                new TrainingSession(childGroup1, coach1, DayOfWeek.SUNDAY, morning)
        );

        Timetable timetable = new Timetable();
        for (TrainingSession s : allSessions) {
            timetable.addNewTrainingSession(s);
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("= Управление расписанием =");
        System.out.println("Введите пожалуйста запрос:");
        System.out.println(" расписание всех занятий — вывести полный список тренировок за неделю");
        System.out.println(" расписание (день) — расписание на конкретный день недели");
        System.out.println(" время (день) (час) (мин) — занятия в конкретное время");
        System.out.println(" тренер (ФИО) — сколько занятий у тренера");
        System.out.println(" добавить нового клиента — добавить клиента и записать на занятие");
        System.out.println(" выход — выйти");

        Map<String, DayOfWeek> dayMap = new HashMap<>();
        dayMap.put("понедельник", DayOfWeek.MONDAY);
        dayMap.put("вторник", DayOfWeek.TUESDAY);
        dayMap.put("среда", DayOfWeek.WEDNESDAY);
        dayMap.put("четверг", DayOfWeek.THURSDAY);
        dayMap.put("пятница", DayOfWeek.FRIDAY);
        dayMap.put("суббота", DayOfWeek.SATURDAY);
        dayMap.put("воскресенье", DayOfWeek.SUNDAY);

        while (true) {
            System.out.print("\n> ");
            if (!scanner.hasNext()) {
                break;
            }
            String command = scanner.next().toLowerCase();

            if ("выход".equals(command)) {
                System.out.println("До свидания!");
                break;
            }

            try {
                if ("расписание".equals(command)) {
                    if (!scanner.hasNext()) {
                        System.out.println("Неверная команда. Используйте: расписание всех занятий или расписание (день).");
                        continue;
                    }
                    String next1 = scanner.next().toLowerCase();
                    if ("всех".equals(next1)) {
                        if (!scanner.hasNext() || !"занятий".equals(scanner.next().toLowerCase())) {
                            System.out.println("Используйте точную формулировку: расписание всех занятий");
                            continue;
                        }
                        System.out.println("\n= СПИСОК ТРЕНИРОВОК (НА НЕДЕЛЮ) =");
                        for (DayOfWeek day : DayOfWeek.values()) {
                            List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(day);
                            if (sessions.isEmpty()) {
                                continue;
                            }
                            System.out.println("\n" + day.getDisplayName() + ":");
                            int index = 1;
                            for (TrainingSession s : sessions) {
                                System.out.printf("%d. %s | Тренер: %s %s %s | Время: %02d:%02d | Возраст: %s (%d мин)%n",
                                        index++,
                                        s.getGroup().getTitle(),
                                        s.getCoach().getSurname(),
                                        s.getCoach().getName(),
                                        s.getCoach().getMiddleName(),
                                        s.getTimeOfDay().getHours(),
                                        s.getTimeOfDay().getMinutes(),
                                        s.getGroup().getAge().getDisplayName(),
                                        s.getGroup().getDuration());
                            }
                        }
                        System.out.println("\n(Этот список можно сразу распечатать и повесить на доску.)");
                        continue;
                    } else {
                        String dayName = next1;
                        DayOfWeek day = dayMap.get(dayName);
                        if (day == null) {
                            System.out.println("Неверное название дня недели. Используйте: понедельник, вторник, ..., воскресенье.");
                            continue;
                        }

                        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(day);
                        System.out.println("\n--- Расписание на " + day.getDisplayName() + " ---");
                        if (sessions.isEmpty()) {
                            System.out.println("Нет тренировок.");
                        } else {
                            for (TrainingSession s : sessions) {
                                System.out.printf("- %s | Тренер: %s %s %s | Время: %02d:%02d | Возраст: %s%n",
                                        s.getGroup().getTitle(),
                                        s.getCoach().getSurname(),
                                        s.getCoach().getName(),
                                        s.getCoach().getMiddleName(),
                                        s.getTimeOfDay().getHours(),
                                        s.getTimeOfDay().getMinutes(),
                                        s.getGroup().getAge().getDisplayName());
                            }
                        }
                        continue;
                    }
                }

                if ("время".equals(command)) {
                    String dayName = scanner.next().toLowerCase();
                    int hours = scanner.nextInt();
                    int minutes = scanner.nextInt();

                    DayOfWeek day = dayMap.get(dayName);
                    if (day == null) {
                        System.out.println("Неверное название дня недели.");
                        continue;
                    }

                    TimeOfDay time = new TimeOfDay(hours, minutes);
                    List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(day, time);
                    System.out.println("\n--- Занятия в " + String.format("%02d:%02d", hours, minutes) + " в " + day.getDisplayName() + " ---");
                    if (sessions.isEmpty()) {
                        System.out.println("Нет занятий в это время.");
                    } else {
                        for (TrainingSession s : sessions) {
                            System.out.printf("- %s | Тренер: %s %s %s%n",
                                    s.getGroup().getTitle(),
                                    s.getCoach().getSurname(),
                                    s.getCoach().getName(),
                                    s.getCoach().getMiddleName());
                        }
                    }
                    continue;
                }

                if ("тренер".equals(command)) {
                    String surname = scanner.next();
                    String namePart = scanner.next();
                    String middlePart = scanner.next();

                    Coach targetCoach = null;
                    for (Coach coach : List.of(coach1, coach2, coach3)) {
                        if (coach.getSurname().equals(surname) && coach.getName().equals(namePart) && coach
                                .getMiddleName().equals(middlePart)) {
                            targetCoach = coach;
                            break;
                        }
                    }

                    if (targetCoach == null) {
                        System.out.println("Тренер с таким ФИО не найден.");
                    } else {
                        int count = timetable.getTrainingCountPerWeek(targetCoach);
                        System.out.println(targetCoach.getSurname() + " " + targetCoach.getName() + " "
                                + targetCoach.getMiddleName() + " ведёт " + count + " занятий в неделю.");
                    }
                    continue;
                }

                if ("добавить".equals(command)) {
                    String next1 = scanner.next().toLowerCase();
                    String next2 = scanner.next().toLowerCase();
                    if (!"нового".equals(next1) || !"клиента".equals(next2)) {
                        System.out.println("Используйте точную формулировку: добавить нового клиента");
                        continue;
                    }

                    System.out.print("Введите имя клиента: ");
                    String name = scanner.next();

                    System.out.print("Введите фамилию клиента: ");
                    String surname = scanner.next();

                    System.out.print("Возрастная категория (детский / взрослый): ");
                    String ageStr = scanner.next().toLowerCase();
                    Age age;
                    if ("детский".equals(ageStr)) {
                        age = Age.CHILD;
                    } else if ("взрослый".equals(ageStr)) {
                        age = Age.ADULT;
                    } else {
                        System.out.println("Неверная возрастная категория. Используйте: детский или взрослый.");
                        continue;
                    }

                    Client client = new Client(name + " " + surname, age);

                    System.out.print("День недели для записи (понедельник..воскресенье): ");
                    String dayStr = scanner.next().toLowerCase();
                    DayOfWeek day = dayMap.get(dayStr);
                    if (day == null) {
                        System.out.println("Неверное название дня недели.");
                        continue;
                    }

                    System.out.print("Время занятия (часы): ");
                    int hours = scanner.nextInt();
                    System.out.print("Время занятия (минуты): ");
                    int minutes = scanner.nextInt();
                    TimeOfDay time = new TimeOfDay(hours, minutes);

                    List<TrainingSession> candidates = timetable.getTrainingSessionsForDayAndTime(day, time);
                    if (candidates.isEmpty()) {
                        System.out.println("В это время в этот день нет тренировок.");
                        continue;
                    }

                    TrainingSession session;
                    if (candidates.size() == 1) {
                        session = candidates.get(0);
                    } else {
                        System.out.println("В это время есть несколько тренировок:");
                        for (int candidateIndex = 0; candidateIndex < candidates.size(); candidateIndex++) {
                            TrainingSession s = candidates.get(candidateIndex);
                            System.out.printf("%d. Группа: %s, Тренер: %s %s %s%n", candidateIndex + 1,
                                    s.getGroup().getTitle(),
                                    s.getCoach().getSurname(),
                                    s.getCoach().getName(),
                                    s.getCoach().getMiddleName());
                        }
                        System.out.print("Выберите номер тренировки: ");
                        int choice = scanner.nextInt();
                        if (choice < 1 || choice > candidates.size()) {
                            System.out.println("Неверный номер.");
                            continue;
                        }
                        session = candidates.get(choice - 1);
                    }

                    boolean success = timetable.registerClient(client, session);
                    if (success) {
                        System.out.println("Клиент " + client.getName() + " успешно записан на тренировку: "
                                + session.getGroup().getTitle() + ", " + session.getDayOfWeek().getDisplayName()
                                + ", " + String.format("%02d:%02d", session.getTimeOfDay().getHours(), session.getTimeOfDay().getMinutes()));
                    } else {
                        System.out.println("Запись не удалась: возраст клиента не соответствует возрастной категории группы.");
                    }
                    continue;
                }

                System.out.println("Неизвестная команда. Попробуйте: расписание всех занятий, расписание (день), время (день) (час) (мин), тренер (ФИО), добавить нового клиента, выход");

            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода: проверьте, что часы и минуты указаны числами.");
                scanner.next();
            } catch (Exception error) {
                System.out.println("Произошла ошибка: " + error.getMessage());
            }
        }
    }
}
