package com.example.bakalarkaupdate;

public class User {
    private int age;
    private int available_meters;
    private String average_difficulty;
    private String email;
    private String nick;
    private int total_meters;

    public User() {
    }

    public User(int age, int available_meters, String average_difficulty, String email, String nick, int total_meters) {
        this.age = age;
        this.available_meters = available_meters;
        this.average_difficulty = average_difficulty;
        this.email = email;
        this.nick = nick;
        this.total_meters = total_meters;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAvailable_meters() {
        return available_meters;
    }

    public void setAvailable_meters(int available_meters) {
        this.available_meters = available_meters;
    }

    public String getAverage_difficulty() {
        return average_difficulty;
    }

    public void setAverage_difficulty(String average_difficulty) {
        this.average_difficulty = average_difficulty;
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

    public int getTotal_meters() {
        return total_meters;
    }

    public void setTotal_meters(int total_meters) {
        this.total_meters = total_meters;
    }
}