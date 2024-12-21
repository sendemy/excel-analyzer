package org.example;

import java.util.HashMap;

public class ScheduleData {
    // Структура: группа -> день недели -> пара -> предмет
    //HashMap<String/*Группа*/, HashMap<String/*День недели*/, HashMap<String/*Пара*/,String/*Предмет*/>>> Groups;
    private final HashMap<String, HashMap<String, HashMap<String, String>>> groups;
    public ScheduleData() {
        this.groups = new HashMap<>();
    }

    public void addSubject(String group, String day, String pair, String subject) {
        groups
                .computeIfAbsent(group, g -> new HashMap<>())
                .computeIfAbsent(day, d -> new HashMap<>())
                .put(pair, subject);
    }

    public HashMap<String, HashMap<String, HashMap<String, String>>> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        groups.forEach((group, days) -> {
            sb.append("Группа: ").append(group).append("\n");
            days.forEach((day, pairs) -> {
                sb.append("  ").append(day).append(":\n");
                pairs.forEach((pair, subject) ->
                        sb.append("    ").append(pair).append(" пара: ").append(subject).append("\n")
                );
            });
        });
        return sb.toString();
    }
    public void merge(ScheduleData other) {
        other.getGroups().forEach((group, days) ->
                days.forEach((day, pairs) ->
                        pairs.forEach((pair, subject) ->
                                this.addSubject(group, day, pair, subject)
                        )
                )
        );
    }
}
