package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;

public class StartMenuScreen extends BaseScreen {

    public StartMenuScreen(MainGame game){
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

        table.setFillParent(true);
        stage.addActor(table);

        //create buttons
        TextButton marathon = new TextButton(i18n.get("modes.marathon"), skin);
        TextButton tenMinutes = new TextButton(i18n.get("modes.tenMinutes"), skin);
        TextButton fortyLines = new TextButton(i18n.get("modes.fortyLines"), skin);

        Image icon = new Image(new Texture(Gdx.files.internal("imgs/help.png")));
        ImageButton helpMarathon = new ImageButton(icon.getDrawable());
        ImageButton helpTenMin = new ImageButton(icon.getDrawable());
        ImageButton helpFortyLines = new ImageButton(icon.getDrawable());

        TextButton back = new TextButton(i18n.get("buttons.back").toUpperCase(), skin);

        float width = (float) (WIDTH * 0.66);
        float height = HEIGHT / 8;

        //add buttons to table
        table.add(marathon).width(width).height(height).uniformX();
        table.add(helpMarathon).width(180).height(180).padLeft(24);
        table.row().pad(24, 0, 24, 0);
        table.add(tenMinutes).width(width).height(height).uniformX();
        table.add(helpTenMin).width(180).height(180).padLeft(24);
        table.row();
        table.add(fortyLines).width(width).height(height).uniformX();
        table.add(helpFortyLines).width(180).height(180).padLeft(24);
        table.row().pad(48, 0, 0, 0);
        table.add(back).width(WIDTH * 0.85f).height(height).colspan(2).uniformX();

        // create button listeners
        marathon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.MARATHON);
            }
        });

        tenMinutes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.TEN_MINUTES);
            }
        });

        fortyLines.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.FORTY_LINES);
            }
        });

        helpMarathon.addListener(new InputListener() {
            @Override
            public boolean handle (Event e) {
                showDialog(MainGame.MARATHON);
                return false;
            }
        });
        helpTenMin.addListener(new InputListener() {
            @Override
            public boolean handle (Event e) {
                showDialog(MainGame.TEN_MINUTES);
                return false;
            }
        });
        helpFortyLines.addListener(new InputListener() {
            @Override
            public boolean handle (Event e) {
                showDialog(MainGame.FORTY_LINES);
                return false;
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.MENU);
            }
        });
    }

    private void showDialog(int mode) {
        String message = "";
        Label title = new Label("", skin);
        title.setColor(Color.GREEN);

        switch (mode) {
            case MainGame.MARATHON:
                title = new Label(i18n.get("modes.marathon"), skin);
                message = i18n.get("help.marathon");
                break;
            case MainGame.TEN_MINUTES:
                title = new Label(i18n.get("modes.tenMinutes"), skin);
                message = i18n.get("help.tenMinutes");
                break;
            case MainGame.FORTY_LINES:
                title = new Label(i18n.get("modes.fortyLines"), skin);
                message = i18n.get("help.fortyLines");
                break;
        }

        final Dialog dialog = new Dialog("", skin);
        TextButton button = new TextButton(i18n.get("buttons.close"), skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.clear();
                dialog.remove();

                show();
            }
        });

        Label label = new Label(message, skin);
        label.setWrap(true);
        label.setAlignment(Align.center, Align.center);
        dialog.getContentTable().add(title).center();
        dialog.getContentTable().row().padBottom(80);
        dialog.getContentTable().add(label).expandX().fillX().pad(40).width(WIDTH*0.8f);
        dialog.getButtonTable().add(button).width(WIDTH/2).height(160).pad(40);
        dialog.pad(40);

        stage.clear();

        dialog.show(stage);
    }

    @Override
    public boolean keyDown(int keycode) {
        parent.changeScreen(MainGame.MENU);
        return false;
    }

}
