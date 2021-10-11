package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.oscarjco.fallblocks.utils.GameAudio;
import com.oscarjco.fallblocks.MainGame;

public class HelpScreen extends BaseScreen {

    public HelpScreen(MainGame game){
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
        TextButton howToPlay = new TextButton(i18n.get("menu.howToPlay"), skin);
        TextButton input = new TextButton(i18n.get("menu.inputs"), skin);
        TextButton back = new TextButton(i18n.get("buttons.back").toUpperCase(), skin);

        float width = (float) (WIDTH * 0.75);
        float height = HEIGHT / 8;

        //add buttons to table
        table.add(howToPlay).width(width).height(height).uniformX();
        table.row().pad(24, 0, 24, 0);
        table.add(input).width(width).height(height).uniformX();
        table.row();
        table.add(back).width(width).height(height).uniformX();

        // create button listeners
        howToPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                stage.clear();

                Skin uiSkin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
                uiSkin.getFont("font").getData().setScale(3);
                Dialog dialog = new Dialog("", uiSkin) {
                    public void result(Object obj) {
                        parent.changeScreen(MainGame.HELP);
                    }
                };
                dialog.pad(80);
                dialog.getContentTable().padBottom(80);
                dialog.getButtonTable().padTop(80);

                Label text = new Label(i18n.get("help.game"), uiSkin);
                text.setWrap(true);
                dialog.getContentTable().add(text).prefWidth((float) (WIDTH * 0.8));

                TextButton button = new TextButton(i18n.get("buttons.ok"), uiSkin);
                dialog.button(button, true); //sends "true" as the result
                button.setFillParent(true);
                dialog.getButtonTable().left();
                dialog.show(stage);
            }
        });

        input.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.INPUT_HELP);
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

}
