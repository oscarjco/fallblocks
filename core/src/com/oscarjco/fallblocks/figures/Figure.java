package com.oscarjco.fallblocks.figures;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class Figure {
    ArrayList<Square> squares;

    public Figure() {}

    public Figure(Figure figure) {
        this.squares = new ArrayList<Square>();

        for(Square square: figure.getSquares()) {
            this.squares.add(new Square(square.getX(), square.getY(), square.getTexture()));
        }
    }

    public void draw(Batch batch) {
        for (Square square : squares) {
            square.draw(batch);
        }
    }

    public void moveDown() {
        for (Square square : squares) {
            square.moveDown();
        }
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public boolean isTouchingBottom(Square square) {
        for (Square square1 : squares) {
            if(square1.isTouchingBottom(square)) {
                return true;
            }
        }
        return false;
    }

    public void moveLeft() {
        for (Square square : squares) {
            square.moveLeft();
        }
    }

    public void moveRight() {
        for (Square square : squares) {
            square.moveRight();
        }
    }

    public boolean isTouchingRight(Square deadSquare) {
        for (Square square1 : squares) {
            if(square1.isTouchingRight(deadSquare)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouchingLeft(Square deadSquare) {
        for (Square square1 : squares) {
            if(square1.isTouchingLeft(deadSquare)) {
                return true;
            }
        }
        return false;
    }

    public void rotate() {}

    public void setColor(Color color) {
        Pixmap grayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        grayPixmap.setColor(color.r, color.g, color.b, color.a);
        grayPixmap.fill();

        for(Square square: this.squares) {
            square.setTexture(new Texture(grayPixmap));
        }
    }
}
