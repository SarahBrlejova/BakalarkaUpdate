package com.example.bakalarkaupdate;

public class User {
    private int age;
    private int availableMeters;
    private String averageDifficulty;
    private String email;
    private String nick;
    private int totalMeters;

    public User() {
    }

    public User(int age, int availableMeters, String averageDifficulty, String email, String nick, int totalMeters) {
        this.age = age;
        this.availableMeters = availableMeters;
        this.averageDifficulty = averageDifficulty;
        this.email = email;
        this.nick = nick;
        this.totalMeters = totalMeters;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAvailableMeters() {
        return availableMeters;
    }

    public void setAvailableMeters(int availableMeters) {
        this.availableMeters = availableMeters;
    }

    public String getAverageDifficulty() {
        return averageDifficulty;
    }

    public void setAverageDifficulty(String averageDifficulty) {
        this.averageDifficulty = averageDifficulty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getTotalMeters() {
        return totalMeters;
    }

    public void setTotalMeters(int totalMeters) {
        this.totalMeters = totalMeters;
    }
}