package com.oscarjco.fallblocks.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.oscarjco.fallblocks.figures.BoxFigure;
import com.oscarjco.fallblocks.figures.Figure;
import com.oscarjco.fallblocks.figures.LFigure;
import com.oscarjco.fallblocks.figures.ReversedLFigure;
import com.oscarjco.fallblocks.figures.ReversedZFigure;
import com.oscarjco.fallblocks.figures.TFigure;
import com.oscarjco.fallblocks.figures.ZFigure;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.Matrix;
import com.oscarjco.fallblocks.figures.BarFigure;
import com.oscarjco.fallblocks.figures.Square;
import com.oscarjco.fallblocks.screens.EndScreen;
import com.oscarjco.fallblocks.utils.GameAudio;
import com.oscarjco.fallblocks.utils.GameConfiguration;
import com.oscarjco.fallblocks.utils.GameTimeUpdater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameMode implements Screen {
    protected MainGame parent;
    protected SpriteBatch batch;
    protected Timer timer;
    protected TimerTask timerTask;
    protected Stage stage;
    protected Skin skin;
    protected Matrix matrix;
    protected Figure currentFigure;
    protected GameTimeUpdater gameTimeUpdater;
    protected ArrayList<Square> deadSquares;
    protected long score;
    protected long startTimeMillis;
    protected boolean paused;

    protected I18NBundle i18n;
    private int previousRng;

    public GameMode(MainGame game) {
        parent = game;
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));
        batch = new SpriteBatch();
        matrix = new Matrix();
        startTimeMillis = System.currentTimeMillis();
        score = 0;
        paused = false;
        deadSquares = new ArrayList<Square>();
        i18n = I18NBundle.createBundle(Gdx.files.internal("locale/locale"));

        createFigure();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
        initTouchHandling();

        GameAudio.stop();
        GameAudio.play(GameAudio.getPlayTheme());
    }

    @Override
    public void render(float delta) {
        if(paused) return;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameTimeUpdater.render();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        batch.begin();
        drawFigure();
        drawDeadSquares();
        matrix.draw(batch);
        batch.end();

    }

    protected void initTouchHandling() {
        Gdx.input.setInputProcessor(new InputAdapter(){
            boolean turbo = false;

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float screenWidth = GameConfiguration.getScreenWidth();
                float screenHeight = GameConfiguration.getScreenHeight();

                matrix.showLine(screenX, screenY);

                if(screenX < screenWidth / 5) {
                    tryToMoveLeft();
                } else if(screenX > screenWidth / 5 * 4) {
                    tryToMoveRight();
                } else if(screenY < screenHeight / 8 + screenHeight*0.1f && screenY > screenHeight*0.1f) {
                    tryToFall();
                } else if(screenY > screenHeight / 8 * 7) {
                    if(!turbo) {
                        GameConfiguration.setGameSpeed(GameConfiguration.getGameSpeed() / 2);
                        turbo = true;
                    }

                } else if(screenY > screenHeight/8) {
                    tryToRotate();
                }

                return true;
            }

            @Override
            public boolean touchUp (int screenX, int screenY, int pointer, int button) {
                matrix.hideLines();

                if(turbo) {
                    GameConfiguration.setGameSpeed(GameConfiguration.getGameSpeed() * 2);
                    turbo = false;
                }

                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ) {
                    pause();
                }

                return false;
            }

        });
    }

    protected boolean tryToMoveDown() {
        boolean shouldNotKeepMoving = figureIsTouchingADeadSquareAtBottom() || figureIsTouchingBottom();
        if(!shouldNotKeepMoving) {
            currentFigure.moveDown();
            return true;
        }

        return false;
    }

    protected void tryToRotate() {
        currentFigure.rotate();

        for (Square square : currentFigure.getSquares()) {
            if(square.getX() >= matrix.getNumberOfColumns()) {
                currentFigure.moveLeft();
            }
            if(square.getX() < 0) {
                currentFigure.moveRight();
            }
        }

    }

    protected void tryToMoveLeft() {
        if(canMoveFigureToLeft()) {
            currentFigure.moveLeft();
        }
    }

    protected void tryToMoveRight() {
        if(canMoveFigureToRight()) {
            currentFigure.moveRight();
        }
    }

    protected void tryToFall() {
        boolean canFall;
        do canFall = tryToMoveDown(); while(canFall);
    }

    protected void tryToDeleteCompletedLine() {
        int deletedLines = 0;

        for(int i=0;i<matrix.getNumberOfRows();i++) {
            final ArrayList<Square> selectedSquares = new ArrayList();
            for(int j=0;j<matrix.getNumberOfColumns();j++) {
                for(Square deadSquare: deadSquares) {

                    if(deadSquare.getY() == i && deadSquare.getX() == j) {
                        selectedSquares.add(deadSquare);
                    }
                }
            }

            if(selectedSquares.size() == matrix.getNumberOfColumns()) {
                deadSquares.removeAll(selectedSquares);

                // Move the top ones to bottom
                for(Square moveToSquare: deadSquares) {
                    if(moveToSquare.getY() > i) {
                        moveToSquare.moveDown();
                    }
                }

                score += 100;
                deletedLines++;
                i--;
            }
        }

        if(deletedLines == 4) score += 100;
        // if(deletedLines != 0) GameAudio.playSound(GameAudio.LINE_DESTROY);
    }

    protected boolean canMoveFigureToRight() {
        boolean shouldNotKeepMoving = figureIsTouchingADeadSquareAtRight() || figureIsTouchingRightLimit();
        return !shouldNotKeepMoving;
    }

    protected boolean figureIsTouchingRightLimit() {
        for (Square square : currentFigure.getSquares()) {
            if(square.getX() == matrix.getNumberOfColumns()-1) {
                return true;
            }
        }
        return false;
    }

    protected boolean figureIsTouchingADeadSquareAtRight() {
        for (Square deadSquare : deadSquares) {
            if(currentFigure.isTouchingRight(deadSquare)) {
                return true;
            }
        }
        return false;
    }

    protected boolean canMoveFigureToLeft() {
        boolean shouldNotKeepMoving = figureIsTouchingADeadSquareAtLeft() || figureIsTouchingLeftLimit();
        return !shouldNotKeepMoving;
    }

    protected boolean figureIsTouchingLeftLimit() {
        for (Square square : currentFigure.getSquares()) {
            if(square.getX() == 0) {
                return true;
            }
        }
        return false;
    }

    protected boolean figureIsTouchingADeadSquareAtLeft() {
        for (Square deadSquare : deadSquares) {
            if(currentFigure.isTouchingLeft(deadSquare)) {
                return true;
            }
        }
        return false;
    }

    protected void checkCurrentFigure() {
        boolean shouldNotKeepMoving = figureIsTouchingADeadSquareAtBottom() || figureIsTouchingBottom();
        if(shouldNotKeepMoving) {
            GameAudio.playSound(GameAudio.DROP_BLOCK);
            addDeadSquaresFromCurrentFigure();
            tryToDeleteCompletedLine();
            checkFinishedGame();
            createFigure();
        }
    }

    protected void checkFinishedGame() {

    }

    protected boolean figureIsTouchingBottom() {
        for (Square square : currentFigure.getSquares()) {
            if(square.getY() == 0) {
                return true;
            }
        }
        return false;
    }

    protected void createFigure() {
        int x = matrix.getNumberOfColumns() / 2 -1;
        int y = matrix.getNumberOfRows();
        int n = new Random().nextInt(7);

        if(n != previousRng) {
            previousRng = n;
        } else {
            n = new Random().nextInt(7);
        }


        Texture[] textures = {
                new Texture(Gdx.files.internal("imgs/p1.jpg")),
                new Texture(Gdx.files.internal("imgs/p2.jpg")),
                new Texture(Gdx.files.internal("imgs/p3.jpg")),
                new Texture(Gdx.files.internal("imgs/p4.jpg")),
                new Texture(Gdx.files.internal("imgs/p5.jpg")),
                new Texture(Gdx.files.internal("imgs/p6.jpg")),
                new Texture(Gdx.files.internal("imgs/p7.jpg")),
        };

        currentFigure = Arrays.asList(
                new BarFigure(x, y, textures[0]),
                new BoxFigure(x, y, textures[2]),
                new ReversedZFigure(x, y, textures[1]),
                new TFigure(x, y, textures[6]),
                new ZFigure(x, y, textures[3]),
                new LFigure(x, y, textures[4]),
                new ReversedLFigure(x, y, textures[5])
        ).get(n);

        for(Square square : currentFigure.getSquares()) {
            while(square.getY() > y) {
                currentFigure.moveDown();
            }
        }
    }

    protected boolean figureIsTouchingADeadSquareAtBottom() {
        for (Square deadSquare : deadSquares) {
            if(currentFigure.isTouchingBottom(deadSquare)) {
                return true;
            }
        }
        return false;
    }

    protected void addDeadSquaresFromCurrentFigure() {
        deadSquares.addAll(currentFigure.getSquares());
    }

    protected void drawDeadSquares() {
        for (Square deadSquare : deadSquares) {
            deadSquare.draw(batch);
        }
    }

    protected void drawFigure() {

        if(!figureIsTouchingADeadSquareAtBottom() && !figureIsTouchingBottom() && GameConfiguration.getGhostHelp()) {
            drawGhost();
        }

        currentFigure.draw(batch);
    }

    protected void drawGhost() {
        Figure ghost = new Figure(currentFigure);
        ghost.setColor(new Color(1,1,1,0.3f));
        boolean canMove = true;

        while(canMove) {
            ghost.moveDown();

            for (Square ghostSquare : ghost.getSquares()) {
                if (ghostSquare.getY() == 0) {
                    canMove = false;
                }
            }

            for(Square deadSquare : deadSquares) {
                if(ghost.isTouchingBottom(deadSquare)) {
                    canMove = false;
                }
            }
        }

        ghost.draw(batch);
    }

    protected void gameOver(String gameMode, long score, boolean cleared) {
        EndScreen endScreen = new EndScreen(parent, gameMode, score, cleared);

        parent.setScreen(endScreen);
        dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        paused = true;
        parent.changeScreen(MainGame.PAUSE);
        GameAudio.pause();
    }

    @Override
    public void resume() {
        paused = false;
        GameAudio.play(GameAudio.getCurrentTheme());
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
