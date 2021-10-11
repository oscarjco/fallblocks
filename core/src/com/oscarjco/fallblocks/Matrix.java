package com.oscarjco.fallblocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.oscarjco.fallblocks.utils.GameConfiguration;

public class Matrix {

    private final Texture lineTexture;
    private final Texture areaTexture;

    final float WIDTH = GameConfiguration.getScreenWidth();
    final float HEIGHT = GameConfiguration.getScreenHeight() * 0.9f;
    final float SQUARE_LENGTH = GameConfiguration.getSquareLenght();
    final int NUMBER_OF_ROWS = (int) (HEIGHT / SQUARE_LENGTH);
    final int NUMBER_OF_COLUMNS = GameConfiguration.getVerticalLinesCount();

    private boolean[] showLines;

    public Matrix() {
        showLines = new boolean[] {false, false, false, false};

        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        linePixmap.setColor(Color.GRAY);
        linePixmap.fill();
        lineTexture = new Texture(linePixmap);

        Pixmap inputArea = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        inputArea.setColor(1, 1, 1, 0.25f);
        inputArea.fill();
        areaTexture = new Texture(inputArea);
    }

    public void draw(Batch batch) {
        for (int i = 0; i< NUMBER_OF_COLUMNS; i++) {
            batch.draw(lineTexture , i * SQUARE_LENGTH, 0, 1, HEIGHT);
        }
        for (int i = 0; i< NUMBER_OF_ROWS; i++) {
            batch.draw(lineTexture , 0, i * SQUARE_LENGTH, WIDTH, 1);
        }

        if(showLines[0]) batch.draw(areaTexture, 0, HEIGHT/8*7, WIDTH, HEIGHT/8);
        if(showLines[1]) batch.draw(areaTexture, WIDTH/5*4, 0, WIDTH/5, HEIGHT);
        if(showLines[2]) batch.draw(areaTexture, 0, 0, WIDTH, HEIGHT/8);
        if(showLines[3]) batch.draw(areaTexture, 0, 0, WIDTH/5, HEIGHT);

    }

    public void showLine(int x, int y) {
        if(x < WIDTH / 5) {
            showLines[3] = true;
        } else if(x > WIDTH / 5 * 4) {
            showLines[1] = true;
        } else if(y < HEIGHT / 8 + GameConfiguration.getScreenHeight()*0.1f && y > GameConfiguration.getScreenHeight()*0.1f) {
            showLines[0] = true;
        } else if(y > (GameConfiguration.getScreenHeight()*0.1f + HEIGHT) / 8 * 7) {
            showLines[2] = true;
        }
    }

    public void hideLines() {
        showLines = new boolean[]{false, false, false, false};
    }

    public int getNumberOfColumns() {
        return this.NUMBER_OF_COLUMNS;
    }

    public int getNumberOfRows() {
        return this.NUMBER_OF_ROWS;
    }
}
