package com.oscarjco.fallblocks.figures;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class BarFigure extends Figure {

    private boolean isHorizontal = false;

    public BarFigure(int x, int y, Texture texture) {
        super();
        this.squares = new ArrayList<Square>();
        squares.add(new Square(x, y, texture));
        squares.add(new Square(x, y+1, texture));
        squares.add(new Square(x, y+2, texture));
        squares.add(new Square(x, y+3, texture));
    }

    @Override
    public void rotate() {
        int x = squares.get(0).getX();
        int y = squares.get(0).getY();
        Texture texture = squares.get(0).getTexture();

        squares = new ArrayList();

        if(isHorizontal) {
            squares.add(new Square(x, y, texture));
            squares.add(new Square(x, y+1, texture));
            squares.add(new Square(x, y+2, texture));
            squares.add(new Square(x, y+3, texture));
        } else {
            squares.add(new Square(x, y, texture));
            squares.add(new Square(x+1, y, texture));
            squares.add(new Square(x+2, y, texture));
            squares.add(new Square(x+3, y, texture));
        }
        isHorizontal = !isHorizontal;

    }
}