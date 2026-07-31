package ru.yandex.practicum.gym.model;

public enum Age {
    CHILD("детский"),
    ADULT("взрослый");

    private final String displayName;

    Age(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}