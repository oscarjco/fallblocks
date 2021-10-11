package com.oscarjco.fallblocks.utils;

public class GameTimeUpdater {
    private long currentTimeMillis;
    GameTimeUpdaterListener listener;

    public GameTimeUpdater(GameTimeUpdaterListener listener) {
        this.listener = listener;
    }

    public void render() {
        long newTimeMillis = System.currentTimeMillis();
        float frameTimeSeconds = (newTimeMillis - currentTimeMillis) / 1000f;
        if(frameTimeSeconds >= GameConfiguration.getGameSpeed()) {
            currentTimeMillis = newTimeMillis;
            listener.shouldUpdate();
        }
    }
}
