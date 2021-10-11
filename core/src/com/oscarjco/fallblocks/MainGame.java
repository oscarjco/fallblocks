package com.oscarjco.fallblocks;

import com.badlogic.gdx.Game;
import com.oscarjco.fallblocks.gamemodes.MarathonMode;
import com.oscarjco.fallblocks.gamemodes.FortyLinesMode;
import com.oscarjco.fallblocks.gamemodes.TenMinutesMode;
import com.oscarjco.fallblocks.screens.HelpScreen;
import com.oscarjco.fallblocks.screens.InputHelpScreen;
import com.oscarjco.fallblocks.screens.PauseScreen;
import com.oscarjco.fallblocks.screens.RecordsScreen;
import com.oscarjco.fallblocks.screens.StartMenuScreen;
import com.oscarjco.fallblocks.screens.TitleScreen;
import com.oscarjco.fallblocks.screens.MainMenuScreen;
import com.oscarjco.fallblocks.screens.PreferencesScreen;

public class MainGame extends Game {

    public final static int MENU = 0;
    public final static int PLAY = 10;
    public final static int MARATHON = 11;
    public final static int TEN_MINUTES = 12;
    public final static int FORTY_LINES = 13;
    public final static int RECORDS = 20;
    public final static int PREFERENCES = 30;
    public final static int HELP = 40;
    public final static int INPUT_HELP = 41;
    public final static int PAUSE = 50;
    public static int currentMode;

    private TitleScreen titleScreen;
    private PreferencesScreen preferencesScreen;
    private MainMenuScreen menuScreen;
    private StartMenuScreen startMenuScreen;
    private HelpScreen helpScreen;
    private MarathonMode marathonMode;
    private TenMinutesMode tenMinutesMode;
    private FortyLinesMode fortyLinesMode;
    private RecordsScreen recordsScreen;
    private InputHelpScreen inputHelpScreen;
    private PauseScreen pauseScreen;

    @Override
    public void create () {
        titleScreen = new TitleScreen(this);
        setScreen(titleScreen);
    }

    public void changeScreen(int screen){
        switch(screen){
            case MENU:
                if(menuScreen == null) menuScreen = new MainMenuScreen(this); // added (this)
                this.setScreen(menuScreen);
                break;
            case PLAY:
                if(startMenuScreen == null) startMenuScreen = new StartMenuScreen(this); // added (this)
                this.setScreen(startMenuScreen);
                break;
            case MARATHON:
                marathonMode = new MarathonMode(this); // added (this)
                this.setScreen(marathonMode);
                currentMode = MARATHON;
                break;
            case TEN_MINUTES:
                tenMinutesMode = new TenMinutesMode(this); // added (this)
                this.setScreen(tenMinutesMode);
                currentMode = TEN_MINUTES;
                break;
            case FORTY_LINES:
                fortyLinesMode = new FortyLinesMode(this); // added (this)
                this.setScreen(fortyLinesMode);
                currentMode = FORTY_LINES;
                break;
            case RECORDS:
                if(recordsScreen == null) recordsScreen = new RecordsScreen(this); // added (this)
                this.setScreen(recordsScreen);
                break;
            case PREFERENCES:
                if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this); // added (this)
                this.setScreen(preferencesScreen);
                break;
            case HELP:
                if(helpScreen == null) helpScreen = new HelpScreen(this); //added (this)
                this.setScreen(helpScreen);
                break;
            case INPUT_HELP:
                if(inputHelpScreen == null) inputHelpScreen = new InputHelpScreen(this); //added (this)
                this.setScreen(inputHelpScreen);
                break;
            case PAUSE:
                pauseScreen = new PauseScreen(this); //added (this)
                this.setScreen(pauseScreen);
                break;
        }

    }

    public void resumeGame() {
        switch (currentMode) {
            case MARATHON:
                marathonMode.resume();
                setScreen(marathonMode);
                break;
            case TEN_MINUTES:
                tenMinutesMode.resume();
                setScreen(tenMinutesMode);
                break;
            case FORTY_LINES:
                fortyLinesMode.resume();
                setScreen(fortyLinesMode);
                break;
        }

    }

}
