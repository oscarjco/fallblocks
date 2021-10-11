package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;

public class MainMenuScreen extends BaseScreen {

    public MainMenuScreen(MainGame game){
        super(game);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    showExitDialog();
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();

        table.setFillParent(true);
        stage.addActor(table);

        //create buttons
        TextButton play = new TextButton(i18n.get("menu.play"), skin);
        TextButton records = new TextButton(i18n.get("menu.records"), skin);
        TextButton preferences = new TextButton(i18n.get("menu.settings"), skin);
        TextButton help = new TextButton(i18n.get("menu.help"), skin);
        TextButton exit = new TextButton(i18n.get("menu.exit"), skin);

        float width = (float) (WIDTH * 0.75);
        float height = HEIGHT / 8;

        //add buttons to table
        table.add(play).width(width).height(height).uniformX();
        table.row().pad(24, 0, 24, 0);
        table.add(records).width(width).height(height).uniformX();
        table.row();
        table.add(preferences).width(width).height(height).uniformX();
        table.row().pad(24, 0, 24, 0);
        table.add(help).width(width).height(height).uniformX();
        table.row();
        table.add(exit).width(width).height(height).uniformX();

        // create button listeners
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.PLAY);
            }
        });

        records.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.RECORDS);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.PREFERENCES);
            }
        });

        help.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.HELP);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                showExitDialog();
            }
        });
    }

}