package mytetrist;


import javafx.scene.shape.Rectangle;
import java.util.Random;

import static mytetrist.TetrisGame.MAP_MATRIX;
import static mytetrist.TetrisGame.GAME_WIDTH;
import static mytetrist.TetrisGame.GAME_HEIGHT;
import static mytetrist.Squares.SQUARE_SIZE;

public class ShapeGenerator {
        public static int shapeType;
        public static Random shapeRandom;
        public static int currentRepeat = 0;
        public static int lastRepeat = 0;

	public static Squares shaping() {
                shapeRandom = new Random();
                shapeType = shapeRandom.nextInt(7) + 1;
                while(shapeType == currentRepeat || shapeType == lastRepeat){
                    shapeType = shapeRandom.nextInt(7) + 1;
                }
		String squaresShape;
		Rectangle   a = new Rectangle(SQUARE_SIZE, SQUARE_SIZE), 
                            b = new Rectangle(SQUARE_SIZE, SQUARE_SIZE), 
                            c = new Rectangle(SQUARE_SIZE, SQUARE_SIZE),
                            d = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                switch(shapeType){
                    case 1:
                        a.setX(GAME_WIDTH / 2 - SQUARE_SIZE - SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			c.setX(GAME_WIDTH / 2);
			d.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			squaresShape = "I";
                        lastRepeat = currentRepeat;
                        currentRepeat = 1;
                        break;
                    case 2:
                        a.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			b.setY(SQUARE_SIZE);
			c.setX(GAME_WIDTH / 2);
			c.setY(SQUARE_SIZE);
			d.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			d.setY(SQUARE_SIZE);
			squaresShape = "J";
                        lastRepeat = currentRepeat;
                        currentRepeat = 2;
                        break;
                    case 3:
                        a.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			b.setY(SQUARE_SIZE);
			c.setX(GAME_WIDTH / 2);
			c.setY(SQUARE_SIZE);
			d.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			d.setY(SQUARE_SIZE);
			squaresShape = "L";
                        lastRepeat = currentRepeat;
                        currentRepeat = 3;
                        break;
                    case 4:
                        a.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2);
			c.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			c.setY(SQUARE_SIZE);
			d.setX(GAME_WIDTH / 2);
			d.setY(SQUARE_SIZE);
			squaresShape = "O";
                        lastRepeat = currentRepeat;
                        currentRepeat = 4;
                        break;
                    case 5:
                        a.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2);
			c.setX(GAME_WIDTH / 2);
			c.setY(SQUARE_SIZE);
			d.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			d.setY(SQUARE_SIZE);
			squaresShape = "S";
                        lastRepeat = currentRepeat;
                        currentRepeat = 5;
                        break;
                    case 6:
                        a.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2);
			c.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			c.setY(SQUARE_SIZE);
			d.setX(GAME_WIDTH / 2 + SQUARE_SIZE + SQUARE_SIZE);
			d.setY(SQUARE_SIZE);
			squaresShape = "Z";
                        lastRepeat = currentRepeat;
                        currentRepeat = 6;
                        break;
                    default:
                        a.setX(GAME_WIDTH / 2 - SQUARE_SIZE);
			b.setX(GAME_WIDTH / 2);
			c.setX(GAME_WIDTH / 2);
			c.setY(SQUARE_SIZE);
			d.setX(GAME_WIDTH / 2 + SQUARE_SIZE);
			squaresShape = "T";
                        lastRepeat = currentRepeat;
                        currentRepeat = 7;
                        break;       
                }

		return new Squares(a, b, c, d, squaresShape);
	}
}