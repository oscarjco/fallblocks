package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;

public class PauseScreen extends BaseScreen {
    Texture img;

    public PauseScreen(MainGame game) {
        super(game);

        img = new Texture(Gdx.files.internal("imgs/pause.png"));
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    parent.resumeGame();
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();

        TextButton resume, menu;

        resume = new TextButton(i18n.get("buttons.resume"), skin);
        menu = new TextButton(i18n.get("buttons.returnToMenu"), skin);

        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.resumeGame();
            }
        });
        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameAudio.playSound(GameAudio.BUTTON_CLICK);
                parent.changeScreen(MainGame.MENU);
            }
        });

        menu.getLabel().setWrap(true);

        table.add(resume).center().width(WIDTH * 0.75f).height(180);
        table.row().pad(24, 0, 0, 0);
        table.add(menu).center().width(WIDTH * 0.75f).height(180);

        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(GameAudio.isPlaying()) GameAudio.pause();

        batch.begin();
        batch.draw(img, WIDTH/4, HEIGHT-img.getHeight()*4, WIDTH/2, HEIGHT/8);
        batch.end();
    }
}
