package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;
import com.oscarjco.fallblocks.utils.GameConfiguration;

public class PreferencesScreen extends BaseScreen {

    public PreferencesScreen(MainGame game){
        super(game);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    parent.changeScreen(MainGame.MENU);
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        table.removeAction(table.getActions().first());
        table.setPosition(WIDTH*0.01f, HEIGHT*0.01f);

        Label titleLabel = new Label(i18n.get("menu.settings"), skin);
        Label playThemeLabel = new Label(i18n.get("settings.theme"), skin);
        Label volumeMusicLabel = new Label(i18n.get("settings.musicVol"), skin);
        Label volumeSoundLabel = new Label(i18n.get("settings.soundVol"), skin);

        titleLabel.setColor(Color.GREEN);

        titleLabel.setFontScale(4);
        playThemeLabel.setFontScale(4);
        volumeMusicLabel.setFontScale(4);
        volumeSoundLabel.setFontScale(4);

        final SelectBox<String> musicBox = new SelectBox<String>(skin);
        musicBox.setItems("Tema 1", "Tema 2", "Tema 3", "Tema 4");

        Music playTheme = GameAudio.getPlayTheme();

        if(playTheme == GameAudio.PLAY_THEME_2) {
            musicBox.setSelectedIndex(1);
        } else if(playTheme == GameAudio.PLAY_THEME_3) {
            musicBox.setSelectedIndex(2);
        } else if(playTheme == GameAudio.PLAY_THEME_4) {
            musicBox.setSelectedIndex(3);
        } else {
            musicBox.setSelectedIndex(0);
        }

        musicBox.getStyle().listStyle.font.getData().setScale(3);
        musicBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                changePlayTheme(musicBox.getSelectedIndex());
                return false;
            }
        });

        Image icon = new Image(new Texture(Gdx.files.internal("imgs/volume.png")));
        icon.setSize(30, 30);
        final ImageButton testButton = new ImageButton(icon.getDrawable());
        testButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameAudio.pause();
                GameAudio.play(GameAudio.getPlayTheme());
                return false;
            }
        });

        final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        int knobSize = 24;
        volumeMusicSlider.getStyle().knob.setMinWidth(knobSize);
        volumeMusicSlider.getStyle().knob.setMinHeight(knobSize);
        volumeMusicSlider.getStyle().knobDown.setMinWidth(knobSize);
        volumeMusicSlider.getStyle().knobDown.setMinHeight(knobSize);
        volumeMusicSlider.setValue(GameAudio.getMusicVolume(prefs));
        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameAudio.setMusicVolume(prefs, volumeMusicSlider.getValue());
                prefs.flush();

                GameAudio.pause();
                GameAudio.play(GameAudio.getCurrentTheme());
                return false;
            }
        });

        final Slider volumeSoundSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeSoundSlider.getStyle().knob.setMinWidth(knobSize);
        volumeSoundSlider.getStyle().knob.setMinHeight(knobSize);
        volumeSoundSlider.getStyle().knobDown.setMinWidth(knobSize);
        volumeSoundSlider.getStyle().knobDown.setMinHeight(knobSize);
        volumeSoundSlider.setValue(GameAudio.getSoundVolume(prefs));
        volumeSoundSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                GameAudio.setSoundVolume(prefs, volumeSoundSlider.getValue());
                prefs.flush();

                GameAudio.testSoundVolume();
                return false;
            }
        });

        Label ghostLabel = new Label(i18n.get("settings.ghost"), skin);
        ghostLabel.setFontScale(4);

        final CheckBox ghostCheck = new CheckBox("", skin);
        ghostCheck.getImage().setScaling(Scaling.fill);
        ghostCheck.getImageCell().size(WIDTH/6);
        ghostCheck.setChecked(GameConfiguration.getGhostHelp());
        ghostCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameConfiguration.setGhostHelp(prefs, ghostCheck.isChecked());
                prefs.flush();
            }
        });

        table.row().pad(8, 8, 8, 8);
        table.add(playThemeLabel).uniformX().align(Align.left);
        table.row().pad(8, 8, 8, 8);
        table.add(musicBox).fillX().pad(0, 24, 0, 24);
        table.add(testButton);
        table.row().pad(80, 8, 8, 8);
        table.add(volumeMusicLabel).uniformX().align(Align.left).padRight(80);
        table.row().pad(8, 8, 80, 8);
        table.add(volumeMusicSlider).uniformX().fillX();
        table.row().pad(8, 8, 8, 8);
        table.add(volumeSoundLabel).uniformX().align(Align.left).padRight(80);
        table.row().pad(8, 8, 80, 8);
        table.add(volumeSoundSlider).uniformX().fillX();
        table.row().pad(8, 8, 8, 8);
        table.add(ghostLabel).uniformX().align(Align.left);
        table.add(ghostCheck);

        final TextButton backButton = new TextButton(i18n.get("buttons.confirm"), skin);
        backButton.getLabel().setFontScale(4);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                prefs.flush();
                parent.changeScreen(MainGame.MENU);
            }
        });

        table.setFillParent(true);

        stage.addActor(titleLabel);
        stage.addActor(table);
        stage.addActor(backButton);

        titleLabel.setPosition(
                WIDTH / 2 - titleLabel.getWidth() / 2,
                HEIGHT - titleLabel.getHeight()*4);
        backButton.setBounds(
                WIDTH / 4, backButton.getHeight()*1.5f,
                WIDTH / 2, 160);
    }

    private void changePlayTheme(int index) {
        switch (index) {
            case 0:
                GameAudio.setPlayTheme(GameAudio.PLAY_THEME_1);
                break;
            case 1:
                GameAudio.setPlayTheme(GameAudio.PLAY_THEME_2);
                break;
            case 2:
                GameAudio.setPlayTheme(GameAudio.PLAY_THEME_3);
                break;
            case 3:
                GameAudio.setPlayTheme(GameAudio.PLAY_THEME_4);
                break;
        }
    }

}
