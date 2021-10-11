package com.oscarjco.fallblocks.gamemodes;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.oscarjco.fallblocks.MainGame;
import com.oscarjco.fallblocks.figures.Square;
import com.oscarjco.fallblocks.utils.GameConfiguration;
import com.oscarjco.fallblocks.utils.GameTimeUpdater;
import com.oscarjco.fallblocks.utils.GameTimeUpdaterListener;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FortyLinesMode extends GameMode {
	private Label scoreLabel;
	private Label timeLabel;
	private long time;
	private final String MODE_NAME = "forty_lines";

	public FortyLinesMode(MainGame mainGame) {
		super(mainGame);

		scoreLabel = new Label(String.valueOf(score), skin);
		timeLabel = new Label("", skin);
	}

	@Override
	public void show() {
		time = 0;

		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				time = System.currentTimeMillis() - startTimeMillis;

				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
				format.getTimeZone().setRawOffset(0);
				String date = format.format(time);
				timeLabel.setText(i18n.get("modes.time") + ":\n" + date);
			}
		};
		timer.schedule(timerTask, 0, 1000);

		gameTimeUpdater = new GameTimeUpdater(new GameTimeUpdaterListener() {
			@Override
			public void shouldUpdate() {
				scoreLabel.setText(i18n.get("modes.score") + ":\n" + score);

				checkCurrentFigure();
				tryToMoveDown();
			}
		});

		Table table = new Table();

		scoreLabel.setFontScale(4);
		timeLabel.setFontScale(4);
		table.add(scoreLabel).expandX().fillX().uniformX().width(GameConfiguration.getScreenWidth()*0.4f).align(Align.left);
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
				gameOver(MODE_NAME, time, false);
			}
		}
	}

	protected void tryToDeleteCompletedLine() {
		super.tryToDeleteCompletedLine();

		if(score >= 4000) {
			gameOver(MODE_NAME, time, true);
		}
	}
}
