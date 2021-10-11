package com.oscarjco.fallblocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameAudio;
import com.oscarjco.fallblocks.utils.GameConfiguration;

public class BaseScreen implements Screen, InputProcessor {
    MainGame parent;
    Stage stage;
    SpriteBatch batch;
    Texture background;
    Skin skin;
    Table table;
    Preferences prefs;
    I18NBundle i18n;

    final protected float WIDTH = GameConfiguration.getScreenWidth();
    final protected float HEIGHT = GameConfiguration.getScreenHeight();

    public BaseScreen(MainGame game) {
        parent = game;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("imgs/background.jpg"));
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        skin.getFont("font").getData().setScale(4);
        prefs = GameConfiguration.getPrefs();
        i18n = I18NBundle.createBundle(Gdx.files.internal("locale/locale"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

        if(GameAudio.getCurrentTheme() != GameAudio.MAIN_THEME) {

            if(GameAudio.isPlaying()) GameAudio.stop();

            if(!this.getClass().equals(PauseScreen.class)) {
                GameAudio.play(GameAudio.MAIN_THEME);
            }
        }

        if(!GameAudio.isPlaying()) GameAudio.play(GameAudio.MAIN_THEME);

        table = new Table();

        table.setY(GameConfiguration.getScreenHeight());
        table.addAction(Actions.moveTo(table.getX(), 0, 0.75f));
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, WIDTH, HEIGHT);
        batch.end();

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        table.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        GameAudio.pause();
    }

    @Override
    public void resume() {
        if(!GameAudio.isPlaying()) GameAudio.play(GameAudio.MAIN_THEME);
    }

    @Override
    public void hide() {
        stage.clear();
        stage.getActors().clear();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    protected void showExitDialog() {
        final Dialog dialog = new Dialog("", skin);
        Label label = new Label(i18n.get("quit.message"), skin);
        TextButton yes = new TextButton(i18n.get("buttons.yes"), skin);
        TextButton no = new TextButton(i18n.get("buttons.no"), skin);

        final Screen screen = this;

        yes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.clear();
                dialog.remove();

                if(screen.getClass().equals(TitleScreen.class)) {
                    parent.setScreen(new TitleScreen(parent));
                } else {
                    parent.setScreen(screen);
                    batch = new SpriteBatch();
                }

            }
        });

        dialog.pad(40);
        dialog.setWidth(WIDTH * 0.8f);
        dialog.setX(WIDTH * 0.1f);
        dialog.getContentTable().add(label).center().pad(40);
        dialog.getButtonTable().add(yes).width(WIDTH / 4).pad(40);
        dialog.getButtonTable().add(no).width(WIDTH / 4).pad(40);

        stage.clear();

        batch.dispose();

        dialog.show(stage);
    }

}
