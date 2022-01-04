package com.example.opal.Data;

public class Teams {

    private Integer ranking;
    private Double averagePoints;
    private Double average_balls;
    private String name;
    private Integer scoredballs;
    private Integer points;

    public Teams() {

    }
    public Teams(String name, Integer ranking, Double averagePoints, Integer scoredballs, Integer points) {
        this.name = name;
        this.ranking = ranking;
        this.averagePoints = averagePoints;
        this.scoredballs = scoredballs;
        this.points = points;
    }
    public String getName() {
        return name;
    }
    public Integer getRanking() {
        return ranking;
    }
    public Double getAveragePoints() {
        return averagePoints;
    }
    public Double getAverageBalls() {
        return average_balls;
    }
    public Integer getscoredballs() {
        return scoredballs;
    }
    public Integer getPoints() {
        return points;
    }
}