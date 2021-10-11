package com.oscarjco.fallblocks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameConfiguration {
    private static final String PREFS_NAME = "fall_blocks";
    private static float gameSpeed = 0.5f;

    public static float getScreenWidth() {
        return Gdx.app.getGraphics().getWidth();
    }

    public static float getScreenHeight() {
        return Gdx.app.getGraphics().getHeight();
    }

    public static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static int getVerticalLinesCount() {
        return 15;
    }

    public static float getSquareLenght() {
        return getScreenWidth()/ getVerticalLinesCount();
    }

    public static void setGameSpeed(float speed) {
        gameSpeed = speed;
    }

    public static float getGameSpeed() {
        return gameSpeed;
    }

    public static boolean getGhostHelp() {
        return getPrefs().getBoolean("ghost");
    }

    public static void setGhostHelp(Preferences prefs, boolean checked) {
        prefs.putBoolean("ghost", checked);
    }
}
