package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;

public class InputHelpScreen extends BaseScreen {
    Texture img;

    public InputHelpScreen (MainGame game) {
        super(game);
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    parent.changeScreen(MainGame.HELP);
                return false;
            }
        });

        img = new Texture(Gdx.files.internal("imgs/input_help_phone.png"));
    }

    @Override
    public void show() {
        super.show();

        TextButton button = new TextButton(i18n.get("buttons.ok"), skin);
        button.pad(40, WIDTH/3, 40, WIDTH/3);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.HELP);
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(new Label(i18n.get("menu.inputs"), skin, "font", Color.GREEN));
        table.row().pad(HEIGHT * 0.8f, 0, 0, 0);
        table.add(button);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(img, WIDTH / 8, HEIGHT / 7,
                WIDTH * 0.75f, HEIGHT * 0.75f);
        batch.end();
    }
}
