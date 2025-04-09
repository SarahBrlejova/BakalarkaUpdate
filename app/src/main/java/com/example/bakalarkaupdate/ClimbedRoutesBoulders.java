package com.example.bakalarkaupdate;

public class ClimbedRoutesBoulders {
    private String id;
    private String name;
    private String difficulty;
    private int difficultyValue;
    private int timesClimbed;

    public ClimbedRoutesBoulders(String id, String name, String difficulty, int difficultyValue, int timesClimbed) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.difficultyValue = difficultyValue;
        this.timesClimbed = timesClimbed;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDifficulty() { return difficulty; }
    public int getDifficultyValue() { return difficultyValue; }
    public int getTimesClimbed() { return timesClimbed; }
}

