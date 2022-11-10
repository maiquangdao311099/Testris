package mytetrist;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Squares {

    private String shape;
    public int rotating = 1;
    public static final int SQUARE_SIZE = 35;
    Rectangle a, b, c, d;
    Color color;

    public Squares(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String shape) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.shape = shape;

        switch (shape) {
            case "I":
                color = Color.DEEPSKYBLUE;
                break;
            case "J":
                color = Color.ROYALBLUE;
                break;
            case "L":
                color = Color.SANDYBROWN;
                break;
            case "O":
                color = Color.TOMATO;
                break;
            case "S":
                color = Color.DARKTURQUOISE;
                break;
            case "Z":
                color = Color.CRIMSON;
                break;
            case "T":
                color = Color.VIOLET;
                break;

        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }

    public String getShape() {
        return shape;
    }

    public void turnDirection() {
        if (rotating != 4) {
            rotating++;
        } else {
            rotating = 1;
        }
    }
}
