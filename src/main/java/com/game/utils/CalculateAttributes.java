package com.game.utils;

public class CalculateAttributes {

    public static Integer getCurrentLevel(Integer experience) {
        return (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
    }

    public static Integer getExperienceToNextLevel(Integer currentLevel, Integer experience) {
        return 50 * (currentLevel + 1) * (currentLevel + 2) - experience;
    }
}
