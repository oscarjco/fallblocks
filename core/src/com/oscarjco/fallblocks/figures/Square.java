package com.oscarjco.fallblocks.figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.oscarjco.fallblocks.utils.GameConfiguration;

public class Square {

    private final Texture blackTexture;
    private final float margin;
    private int x;
    private int y;
    private Texture texture;
    private float squareLength;
    private boolean destroyed;

    public Square(int x, int y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;

        Pixmap blackPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        blackPixmap.setColor(Color.BLACK);
        blackPixmap.fill();
        this.blackTexture = new Texture(blackPixmap);
        this.margin = 5.0f;
        this.squareLength = GameConfiguration.getScreenWidth()/ GameConfiguration.getVerticalLinesCount();
        this.destroyed = false;
    }

    public void draw(Batch batch) {
        if(destroyed) {
            Pixmap grayPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            grayPixmap.setColor(Color.GRAY);
            grayPixmap.fill();
            this.texture = new Texture(grayPixmap);

            squareLength--;
        }

        batch.draw(blackTexture, x*squareLength, y*squareLength, squareLength, squareLength);
        batch.draw(texture, x*squareLength + margin, y*squareLength + margin, squareLength - margin * 2, squareLength - margin * 2);
    }

    public void moveDown() {
        y-=1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isTouchingBottom(Square square) {
        return square.getY() == y - 1 && square.getX() == x;
    }

    public void moveRight() {
        x+=1;
    }

    public void moveLeft() {
        x-=1;
    }

    public boolean isTouchingRight(Square square) {
        return square.getX() == x + 1 && square.getY() == y;
    }

    public boolean isTouchingLeft(Square square) {
        return square.getX() == x - 1 && square.getY() == y;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public float getSquareLength() {
        return this.squareLength;
    }
}
