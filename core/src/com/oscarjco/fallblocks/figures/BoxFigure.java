package com.oscarjco.fallblocks.figures;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class BoxFigure extends Figure {

    public BoxFigure(int x, int y, Texture texture) {
        super();
        this.squares = new ArrayList<Square>();
        squares.add(new Square(x, y, texture));
        squares.add(new Square(x+1, y, texture));
        squares.add(new Square(x+1, y+1, texture));
        squares.add(new Square(x, y+1, texture));
    }
}
