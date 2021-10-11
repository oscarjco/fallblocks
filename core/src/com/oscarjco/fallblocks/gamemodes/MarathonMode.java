package com.oscarjco.fallblocks.gamemodes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.oscarjco.fallblocks.figures.Square;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.utils.GameConfiguration;
import com.oscarjco.fallblocks.utils.GameTimeUpdater;
import com.oscarjco.fallblocks.utils.GameTimeUpdaterListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MarathonMode extends GameMode {
	private Label levelLabel;
	private Label scoreLabel;
	private Label timeLabel;
	private int level;
	private int time;
	private float scoreMultiplier;
	private final String MODE_NAME = "marathon";

	public MarathonMode(MainGame mainGame) {
		super(mainGame);

		levelLabel = new Label(String.valueOf(level), skin);
		scoreLabel = new Label(String.valueOf(score), skin);
		timeLabel = new Label("", skin);
	}

	@Override
	public void show() {
		level = 1;
		time = 0;
		scoreMultiplier = 1;

		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
				format.getTimeZone().setRawOffset(0);
				String time = format.format(System.currentTimeMillis() - startTimeMillis);
				timeLabel.setText(i18n.get("modes.time") + ":\n" + time);

			}
		};
		timer.schedule(timerTask, time, 1000);

		gameTimeUpdater = new GameTimeUpdater(new GameTimeUpdaterListener() {
			@Override
			public void shouldUpdate() {
				levelLabel.setText(i18n.get("modes.level") + level);
				scoreLabel.setText(i18n.get("modes.score") + ":\n" + score);

				checkCurrentFigure();
				tryToMoveDown();
			}
		});

		Table table = new Table();

		scoreLabel.setFontScale(4);
		levelLabel.setFontScale(6);
		levelLabel.setColor(Color.YELLOW);
		timeLabel.setFontScale(4);
		table.add(scoreLabel).expandX().fillX().uniformX().width(GameConfiguration.getScreenWidth()*0.3f).align(Align.left);
		table.add(levelLabel).expandX().fillX().uniformX().width(GameConfiguration.getScreenWidth()*0.2f).align(Align.center);
		table.add(timeLabel).expandX().fillX().uniformX().width(GameConfiguration.getScreenWidth()*0.2f).align(Align.right);
		table.setPosition(GameConfiguration.getScreenWidth()/2, GameConfiguration.getScreenHeight()*0.95f);
		stage.addActor(table);

		super.show();
	}

	protected void checkFinishedGame() {
		for(Square deadSquare: deadSquares) {
			if(deadSquare.getY() == matrix.getNumberOfRows()-1
					&& deadSquare.getX() >= matrix.getNumberOfColumns() * 0.4f
					&& deadSquare.getX() <= matrix.getNumberOfColumns() * 0.6f) {
				// Game over
				timer.cancel();

				// Le resta el tiempo a la puntuacion
				time = (int) ((System.currentTimeMillis() - startTimeMillis) / 1000);
				score -= time;

				if(score < 0) score = 0;

				gameOver(MODE_NAME, score, true);
			}
		}
	}

	@Override
	protected void tryToDeleteCompletedLine() {
		int deletedLines = 0;

		for(int i=0;i<matrix.getNumberOfRows();i++) {
			ArrayList<Square> selectedSquares = new ArrayList();
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

				score += (100 * scoreMultiplier);
				deletedLines++;
				i--;
			}
		}

		if(deletedLines == 4) score += (100 * scoreMultiplier);

		checkLevelUp();
	}

	private void checkLevelUp() {
		switch (level) {
			case 1:
				if(score >= 1000) {
					level = 2;
					GameConfiguration.setGameSpeed(0.5f/1.25f);
					scoreMultiplier = 1.2f;
				}
				break;
			case 2:
				if(score >= 2500) {
					level = 3;
					GameConfiguration.setGameSpeed(0.5f/1.5f);
					scoreMultiplier = 1.4f;
				}
				break;
			case 3:
				if(score >= 5000) {
					level = 4;
					GameConfiguration.setGameSpeed(0.5f/1.75f);
					scoreMultiplier = 1.6f;
				}
				break;
			case 4:
				if(score >= 7500) {
					level = 5;
					GameConfiguration.setGameSpeed(0.5f/2f);
					scoreMultiplier = 1.8f;
				}
				break;
			case 5:
				if(score >= 10000) {
					level = 6;
					GameConfiguration.setGameSpeed(0.5f/2.5f);
					scoreMultiplier = 2f;
				}
				break;
			case 6:
				if(score >= 12500) {
					level = 7;
					GameConfiguration.setGameSpeed(0.5f/3f);
					scoreMultiplier = 2.25f;
				}
				break;
			case 7:
				if(score >= 15000) {
					level = 8;
					GameConfiguration.setGameSpeed(0.5f/3.5f);
					scoreMultiplier = 2.5f;
				}
				break;
			case 8:
				if(score >= 20000) {
					level = 9;
					GameConfiguration.setGameSpeed(0.5f/4f);
					scoreMultiplier = 3f;
				}
				break;
		}
	}
}
