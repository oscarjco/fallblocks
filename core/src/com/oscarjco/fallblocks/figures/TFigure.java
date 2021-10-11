package com.oscarjco.fallblocks.figures;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class TFigure extends Figure {

    private int rotationStatus = 0;

    public TFigure(int x, int y, Texture texture) {
        super();
        this.squares = new ArrayList<Square>();
        squares.add(new Square(x, y, texture));
        squares.add(new Square(x+1, y, texture));
        squares.add(new Square(x+1, y+1, texture));
        squares.add(new Square(x+2, y, texture));
    }

    @Override
    public void rotate() {
        int x = squares.get(0).getX();
        int y = squares.get(0).getY();
        Texture texture = squares.get(0).getTexture();
        squares = new ArrayList();

        if(rotationStatus == 0) {
            squares.add(new Square(x, y, texture));
            squares.add(new Square(x, y+1, texture));
            squares.add(new Square(x+1, y+1, texture));
            squares.add(new Square(x, y+2, texture));
        } else if(rotationStatus == 1) {
            squares.add(new Square(x, y+1, texture));
            squares.add(new Square(x+1, y+1, texture));
            squares.add(new Square(x+1, y, texture));
            squares.add(new Square(x+2, y+1, texture));
        } else if(rotationStatus == 2) {
            squares.add(new Square(x+1, y, texture));
            squares.add(new Square(x, y+1, texture));
            squares.add(new Square(x+1, y+1, texture));
            squares.add(new Square(x+1, y+2, texture));
        } else {
            squares.add(new Square(x, y, texture));
            squares.add(new Square(x+1, y, texture));
            squares.add(new Square(x+1, y+1, texture));
            squares.add(new Square(x+2, y, texture));
        }
        rotationStatus = (rotationStatus + 1) % 4;
    }
}
