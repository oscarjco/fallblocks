package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;

public class TitleScreen extends BaseScreen {
    private SpriteBatch messageBatch;
    Texture title;
    Texture background;
    Label message;

    public TitleScreen(MainGame game) {
        super(game);

        title = new Texture(Gdx.files.internal("imgs/title.png"));
        background = new Texture(Gdx.files.internal("imgs/image.jpg"));
        message = new Label(i18n.get("title.message"), skin);
        messageBatch = new SpriteBatch();

        message.addAction(Actions.alpha(0));
        message.act(0);
        message.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(1), Actions.fadeOut(1))));
    }

    @Override
    public void show() {
        super.show();
        GameAudio.play(GameAudio.MAIN_THEME);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    showExitDialog();
                return false;
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                parent.changeScreen(MainGame.MENU);
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, WIDTH, HEIGHT);
        batch.draw(title,
                WIDTH / 8, HEIGHT * 0.66f,
                WIDTH * 0.75f, title.getHeight() * 3
        );
        batch.end();

        messageBatch.begin();
        message.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        message.setX(WIDTH / 2 - message.getWidth() / 2);
        message.setY(HEIGHT / 3);
        message.draw(messageBatch, 1);
        messageBatch.end();

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    protected void showExitDialog() {
        super.showExitDialog();
        messageBatch.dispose();
    }
}
