package ru.yandex.practicum.gym.model;

public class Client {
    private String name;
    private Age age;

    public Client(String name, Age age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {          
        return name;
    }

    public Age getAge() {
        return age;
    }
}