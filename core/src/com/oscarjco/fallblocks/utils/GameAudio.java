package com.oscarjco.fallblocks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameAudio {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_SOUND_VOL = "sound";
    // private static final String PREF_MUSIC_ENABLED = "music.enabled";
    // private static final String PREF_SOUND_ENABLED = "sound.enabled";

    public final static Music MAIN_THEME = Gdx.audio.newMusic(Gdx.files.internal("audio/main_theme.wav"));
    public final static Music PLAY_THEME_1 = Gdx.audio.newMusic(Gdx.files.internal("audio/play_1.wav"));
    public final static Music PLAY_THEME_2 = Gdx.audio.newMusic(Gdx.files.internal("audio/play_2.wav"));
    public final static Music PLAY_THEME_3 = Gdx.audio.newMusic(Gdx.files.internal("audio/play_3.wav"));
    public static final Music PLAY_THEME_4 = Gdx.audio.newMusic(Gdx.files.internal("audio/play_4.wav"));
    // public final static Music GAME_OVER = Gdx.audio.newMusic(Gdx.files.internal("audio/game_over.wav"));

    public final static Sound BUTTON_CLICK = Gdx.audio.newSound(Gdx.files.internal("audio/button_click.wav"));
    public final static Sound DROP_BLOCK = Gdx.audio.newSound(Gdx.files.internal("audio/drop_block.wav"));
    // public final static Sound LINE_DESTROY = Gdx.audio.newSound(Gdx.files.internal("audio/line_destroy.wav"));
    public final static Sound NEW_RECORD = Gdx.audio.newSound(Gdx.files.internal("audio/new_record.wav"));
    public static final Sound CLEAR = Gdx.audio.newSound(Gdx.files.internal("audio/clear.wav"));

    private static Music currentTheme;
    private static Music playTheme;

    public static void play(Music music) {
        music.setLooping(true);
        music.setVolume(getMusicVolume(GameConfiguration.getPrefs()));
        music.play();

        currentTheme = music;
    }

    public static void pause() {
        if(currentTheme.isPlaying()) currentTheme.pause();
    }

    public static void stop() {
        if(currentTheme.isPlaying()) currentTheme.stop();
    }

    public static void playSound(Sound sound) {
        sound.setVolume(sound.play(), getSoundVolume(GameConfiguration.getPrefs()));
        //sound.play();
    }

    public static void setPlayTheme(Music theme) {
        playTheme = theme;
    }

    public static Music getPlayTheme() {
        if(playTheme == null) playTheme = PLAY_THEME_1;
        return playTheme;
    }

    public static Music getCurrentTheme() {
        return currentTheme;
    }

    /*
    public static boolean isSoundEffectsEnabled(Preferences prefs) {
        return prefs.getBoolean(PREF_SOUND_ENABLED, true);
    }

    public static void setSoundEffectsEnabled(Preferences prefs, boolean soundEffectsEnabled) {
        prefs.putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
    }

    public static boolean isMusicEnabled(Preferences prefs) {
        return prefs.getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public static void setMusicEnabled(Preferences prefs, boolean musicEnabled) {
        prefs.putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
    }
    */

    public static float getMusicVolume(Preferences prefs) {
        return prefs.getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public static void setMusicVolume(Preferences prefs, float volume) {
        prefs.putFloat(PREF_MUSIC_VOLUME, volume);
    }

    public static float getSoundVolume(Preferences prefs) {
        return prefs.getFloat(PREF_SOUND_VOL, 0.5f);
    }

    public static void setSoundVolume(Preferences prefs, float volume) {
        prefs.putFloat(PREF_SOUND_VOL, volume);
    }

    public static void testSoundVolume() {
        // Reproducir el sonido para el test
        playSound(BUTTON_CLICK);
    }

    public static boolean isPlaying() {
        if(currentTheme != null) {
            return currentTheme.isPlaying();
        }

        return false;

    }
}
