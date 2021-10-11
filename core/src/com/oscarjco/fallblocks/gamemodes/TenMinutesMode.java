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

public class TenMinutesMode extends GameMode {
	private Label scoreLabel;
	private Label timeLabel;
	private boolean finished;
	private boolean cleared;
	private final String MODE_NAME = "ten_minutes";

	public TenMinutesMode(MainGame mainGame) {
		super(mainGame);

		scoreLabel = new Label(String.valueOf(score), skin);
		timeLabel = new Label(i18n.get("modes.time") + ":\n09:59", skin);
		finished = false;
	}

	@Override
	public void show() {
		cleared = false;
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				// 10 minutes
				long currentTime = 600000 - (System.currentTimeMillis() - startTimeMillis);

				if(currentTime <= 0) {
					// Game over
					finished = true;
					cleared = true;
				} else {
					SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.getDefault());
					String time = format.format(currentTime);
					timeLabel.setText(i18n.get("modes.time") + ":\n" + time);
				}
			}
		};
		timer.schedule(timerTask, 1000, 1000);

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

	@Override
	public void render (float delta) {
		if(finished) {
			timer.cancel();
			gameOver(MODE_NAME, score, cleared);
		} else {
			super.render(delta);
		}
	}

	protected void checkFinishedGame() {
		for(Square deadSquare: deadSquares) {
			if (deadSquare.getY() == matrix.getNumberOfRows() - 1
					&& deadSquare.getX() >= matrix.getNumberOfColumns() * 0.4f
					&& deadSquare.getX() <= matrix.getNumberOfColumns() * 0.6f) {
				// Game over
				finished = true;
				break;
			}
		}
	}
}