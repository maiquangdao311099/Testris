package mytetrist;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static mytetrist.Squares.SQUARE_SIZE;

public class TetrisGame extends Application {
        
        private static Pane gameContent = new Pane();
	private static Squares currentShape;
        private static Squares nextShape;
        public static int GAME_WIDTH = SQUARE_SIZE * 16;
	public static int GAME_HEIGHT = SQUARE_SIZE * 24;
	public static int[][] MAP_MATRIX = new int[GAME_WIDTH / SQUARE_SIZE][GAME_HEIGHT / SQUARE_SIZE];
	private static Scene gameSpace = new Scene(gameContent, GAME_WIDTH, GAME_HEIGHT, Color.LIGHTSKYBLUE);
	public static int points = 0;
	private static int timeCount = 0;
	private static boolean isPlaying = true;
        ArrayList<Node> currentBlocks = new ArrayList<Node>();
        ArrayList<Node> updatedBlocks = new ArrayList<Node>();
        ArrayList<Integer> filledBlockRows = new ArrayList<Integer>();
        int blocksOnOneLine = 0;
        
        public void start(Stage stage) throws Exception {
            
                nextShape = ShapeGenerator.shaping();
		Squares shape = nextShape;
		gameContent.getChildren().addAll(shape.a, shape.b, shape.c, shape.d);
		userController(shape);
		currentShape = shape;
		nextShape = ShapeGenerator.shaping();

		Text pointText = new Text("0");
                pointText.setFill(Color.WHITE);
		pointText.setStyle("-fx-font: 140 arial;");
                pointText.setX(GAME_WIDTH/2 - SQUARE_SIZE);
		pointText.setY(300);

		gameContent.getChildren().addAll(pointText);

		stage.setScene(gameSpace);
		stage.setTitle("Group 2's Tetris");
		stage.show();

		Timer fallingPeriod = new Timer();
		TimerTask autoActivity = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (currentShape.a.getY() == 0 || currentShape.b.getY() == 0 || 
                                                    currentShape.c.getY() == 0 || currentShape.d.getY() == 0)
							timeCount++;
						else
							timeCount = 0;

						if (timeCount == 2) {
							//When the bricks cannot fall anymore
							Text gameOver = new Text("GAME OVER");
							gameOver.setFill(Color.WHITE);
							gameOver.setStyle("-fx-font: 70 arial;");
							gameOver.setY(350);
							gameOver.setX(70);
                                                        gameContent.getChildren().add(gameOver);
                                                        
                                                        Text gameResult = new Text("Your Score: " + Integer.toString(points));
							gameResult.setFill(Color.WHITE);
							gameResult.setStyle("-fx-font: 35 arial;");
							gameResult.setY(450);
							gameResult.setX(70);
                                                        
							gameContent.getChildren().add(gameResult);
							isPlaying = false;
						}

						if (isPlaying) {
                                                    if (isSquaresLanded(currentShape) || isSquaresConnected(currentShape)) {
                                                    MAP_MATRIX[(int) currentShape.a.getX() / SQUARE_SIZE][(int) currentShape.a.getY() / SQUARE_SIZE] = 1;
                                                    MAP_MATRIX[(int) currentShape.b.getX() / SQUARE_SIZE][(int) currentShape.b.getY() / SQUARE_SIZE] = 1;
                                                    MAP_MATRIX[(int) currentShape.c.getX() / SQUARE_SIZE][(int) currentShape.c.getY() / SQUARE_SIZE] = 1;
                                                    MAP_MATRIX[(int) currentShape.d.getX() / SQUARE_SIZE][(int) currentShape.d.getY() / SQUARE_SIZE] = 1;
                                                    BlocksController(gameContent);

                                                    Squares shape = nextShape;
                                                    nextShape = ShapeGenerator.shaping();
                                                    currentShape = shape;
                                                    gameContent.getChildren().addAll(shape.a, shape.b, shape.c, shape.d);
                                                    userController(shape);
                                                    }
							MoveShapeDown(currentShape);
                                                        
                                                        if(points >= 10 && points <= 99){
                                                            pointText.setX(GAME_WIDTH/2 - 2*SQUARE_SIZE);
                                                        }else if(points >= 100 && points <= 999){
                                                            pointText.setX(GAME_WIDTH/2 - 3*SQUARE_SIZE);
                                                        }else if(points >= 1000 && points <= 9999){
                                                            pointText.setX(GAME_WIDTH/2 - 4*SQUARE_SIZE);
                                                        }else if(points >= 10000 && points <= 99999){
                                                            pointText.setX(GAME_WIDTH/2 - 5*SQUARE_SIZE);
                                                        }else if(points >= 100000 && points <= 999999){
                                                            pointText.setX(GAME_WIDTH/2 - 6*SQUARE_SIZE);
                                                        }
							pointText.setText(Integer.toString(points));
						}
                                                
					}
				});
			}
		};
		fallingPeriod.schedule(autoActivity, 0, 300); 
	}
        
        private boolean isSquaresLanded(Squares shape){
            boolean touchFloor = (shape.a.getY() == GAME_HEIGHT - SQUARE_SIZE) 
                              || (shape.b.getY() == GAME_HEIGHT - SQUARE_SIZE) 
                              || (shape.c.getY() == GAME_HEIGHT - SQUARE_SIZE)
                              || (shape.d.getY() == GAME_HEIGHT - SQUARE_SIZE);
            return touchFloor;
        }
        
        private boolean isSquaresConnected(Squares shape){
            return isAConnected(shape) || isBConnected(shape) || isCConnected(shape) || isDConnected(shape);
        }
        
	private boolean isAConnected(Squares shape) {
		return (MAP_MATRIX[(int) shape.a.getX() / SQUARE_SIZE][((int) shape.a.getY() / SQUARE_SIZE) + 1] == 1);
	}

	private boolean isBConnected(Squares shape) {
		return (MAP_MATRIX[(int) shape.b.getX() / SQUARE_SIZE][((int) shape.b.getY() / SQUARE_SIZE) + 1] == 1);
	}

	private boolean isCConnected(Squares shape) {
		return (MAP_MATRIX[(int) shape.c.getX() / SQUARE_SIZE][((int) shape.c.getY() / SQUARE_SIZE) + 1] == 1);
	}

	private boolean isDConnected(Squares shape) {
		return (MAP_MATRIX[(int) shape.d.getX() / SQUARE_SIZE][((int) shape.d.getY() / SQUARE_SIZE) + 1] == 1);
	}
	

	private void userController(Squares shape) {
		gameSpace.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case D:
					MoveShapeRight(shape);
					break;
                                case A:
					MoveShapeLeft(shape);
					break;
                                case W:
					RotateShape(shape);
					break;
				case S:
					MoveShapeDown(shape);
					points++;
					break;
				}
			}
		});
	}
        
        private void MoveShapeRight(Squares shape) {
		if (canShapeMove(shape, 1, 0)) {
				shape.a.setX(shape.a.getX() + SQUARE_SIZE);
				shape.b.setX(shape.b.getX() + SQUARE_SIZE);
				shape.c.setX(shape.c.getX() + SQUARE_SIZE);
				shape.d.setX(shape.d.getX() + SQUARE_SIZE);
			
		}
	}

	private void MoveShapeLeft(Squares shape) {
		if (canShapeMove(shape, -1, 0)) {
				shape.a.setX(shape.a.getX() - SQUARE_SIZE);
				shape.b.setX(shape.b.getX() - SQUARE_SIZE);
				shape.c.setX(shape.c.getX() - SQUARE_SIZE);
				shape.d.setX(shape.d.getX() - SQUARE_SIZE);
			
		}
	}
        
        private void RotateShape(Squares shape) {
		switch (shape.getShape()) {
                case "I":
			RotateIShape(shape);
			break;
		case "J":
			RotateJShape(shape);
			break;
		case "L":
                        RotateLShape(shape);
			break;
		case "O":
                        RotateOShape(shape);
			break;
		case "S":
			RotateSShape(shape);
			break;
                case "Z":
                        RotateZShape(shape);
			break;
		case "T":
                        RotateTShape(shape);
			break;
		}
	}
        
        private void RotateIShape(Squares shape){
                    if (shape.rotating == 1 && canSquareMove(shape.a, 2, 2) && canSquareMove(shape.b, 1, 1) && canSquareMove(shape.d, -1, -1)) {
				MoveSquareUp(shape.a);
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareUp(shape.b);
				MoveSquareRight(shape.b);
				MoveSquareDown(shape.d);
				MoveSquareLeft(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 2 && canSquareMove(shape.a, -2, -2) && canSquareMove(shape.b, -1, -1) && canSquareMove(shape.d, 1, 1)) {
				MoveSquareDown(shape.a);
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareDown(shape.b);
				MoveSquareLeft(shape.b);
				MoveSquareUp(shape.d);
				MoveSquareRight(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 3 && canSquareMove(shape.a, 2, 2) && canSquareMove(shape.b, 1, 1) && canSquareMove(shape.d, -1, -1)) {
				MoveSquareUp(shape.a);
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareUp(shape.b);
				MoveSquareRight(shape.b);
				MoveSquareDown(shape.d);
				MoveSquareLeft(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 4 && canSquareMove(shape.a, -2, -2) && canSquareMove(shape.b, -1, -1) && canSquareMove(shape.d, 1, 1)) {
				MoveSquareDown(shape.a);
                                MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareDown(shape.b);
				MoveSquareLeft(shape.b);
				MoveSquareUp(shape.d);
				MoveSquareRight(shape.d);
                                shape.turnDirection();
			}
        }
        
        private void RotateJShape(Squares shape){
                    if (shape.rotating == 1 && canSquareMove(shape.a, 1, -1) && canSquareMove(shape.c, -1, -1) && canSquareMove(shape.d, -2, -2)) {
				MoveSquareRight(shape.a);
				MoveSquareDown(shape.a);
				MoveSquareDown(shape.c);
				MoveSquareLeft(shape.c);
				MoveSquareDown(shape.d);
				MoveSquareDown(shape.d);
				MoveSquareLeft(shape.d);
				MoveSquareLeft(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 2 && canSquareMove(shape.a, -1, -1) && canSquareMove(shape.c, -1, 1) && canSquareMove(shape.d, -2, 2)) {
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
				MoveSquareLeft(shape.d);
				MoveSquareLeft(shape.d);
				MoveSquareUp(shape.d);
				MoveSquareUp(shape.d);
                                shape.turnDirection();
			}else if (shape.rotating == 3 && canSquareMove(shape.a, -1, 1) && canSquareMove(shape.c, 1, 1) && canSquareMove(shape.d, 2, 2)) {
				MoveSquareLeft(shape.a);
				MoveSquareUp(shape.a);
				MoveSquareUp(shape.c);
				MoveSquareRight(shape.c);
				MoveSquareUp(shape.d);
				MoveSquareUp(shape.d);
				MoveSquareRight(shape.d);
				MoveSquareRight(shape.d);
                                shape.turnDirection();
			}else if (shape.rotating == 4 && canSquareMove(shape.a, 1, 1) && canSquareMove(shape.c, 1, -1) && canSquareMove(shape.d, 2, -2)) {
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
				MoveSquareRight(shape.d);
				MoveSquareRight(shape.d);
				MoveSquareDown(shape.d);
				MoveSquareDown(shape.d);
                                shape.turnDirection();
			}
        }
        
        private void RotateLShape(Squares shape){
                	if (shape.rotating == 1 && canSquareMove(shape.a, 1, -1) && canSquareMove(shape.c, 1, 1) && canSquareMove(shape.b, 2, 2)) {
				MoveSquareRight(shape.a);
				MoveSquareDown(shape.a);
				MoveSquareUp(shape.c);
				MoveSquareRight(shape.c);
				MoveSquareUp(shape.b);
				MoveSquareUp(shape.b);
				MoveSquareRight(shape.b);
				MoveSquareRight(shape.b);
                                shape.turnDirection();
			}else if (shape.rotating == 2 && canSquareMove(shape.a, -1, -1) && canSquareMove(shape.b, 2, -2) && canSquareMove(shape.c, 1, -1)) {
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareRight(shape.b);
				MoveSquareRight(shape.b);
				MoveSquareDown(shape.b);
				MoveSquareDown(shape.b);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
                                shape.turnDirection();
			} else if (shape.rotating == 3 && canSquareMove(shape.a, -1, 1) && canSquareMove(shape.c, -1, -1) && canSquareMove(shape.b, -2, -2)) {
				MoveSquareLeft(shape.a);
				MoveSquareUp(shape.a);
				MoveSquareDown(shape.c);
				MoveSquareLeft(shape.c);
				MoveSquareDown(shape.b);
				MoveSquareDown(shape.b);
				MoveSquareLeft(shape.b);
				MoveSquareLeft(shape.b);
                                shape.turnDirection();
			} else if (shape.rotating == 4 && canSquareMove(shape.a, 1, 1) && canSquareMove(shape.b, -2, 2) && canSquareMove(shape.c, -1, 1)) {
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareLeft(shape.b);
				MoveSquareLeft(shape.b);
				MoveSquareUp(shape.b);
				MoveSquareUp(shape.b);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
                                shape.turnDirection();
			}
        }
        
        private void RotateOShape(Squares shape){
            shape.turnDirection();
        }
        
        private void RotateSShape(Squares shape){
                if (shape.rotating == 1 && canSquareMove(shape.a, -1, -1) && canSquareMove(shape.c, -1, 1) && canSquareMove(shape.d, 0, 2)) {
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
				MoveSquareUp(shape.d);
				MoveSquareUp(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 2 && canSquareMove(shape.a, 1, 1) && canSquareMove(shape.c, 1, -1) && canSquareMove(shape.d, 0, -2)) {
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
				MoveSquareDown(shape.d);
				MoveSquareDown(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 3 && canSquareMove(shape.a, -1, -1) && canSquareMove(shape.c, -1, 1) && canSquareMove(shape.d, 0, 2)) {
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
				MoveSquareUp(shape.d);
				MoveSquareUp(shape.d);
                                shape.turnDirection();
			} else if (shape.rotating == 4 && canSquareMove(shape.a, 1, 1) && canSquareMove(shape.c, 1, -1) && canSquareMove(shape.d, 0, -2)) {
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
				MoveSquareDown(shape.d);
				MoveSquareDown(shape.d);
                                shape.turnDirection();
			}
        }
        
        private void RotateZShape(Squares shape){
                        if (shape.rotating == 1 && canSquareMove(shape.b, 1, 1) && canSquareMove(shape.c, -1, 1) && canSquareMove(shape.d, -2, 0)) {
				MoveSquareUp(shape.b);
				MoveSquareRight(shape.b);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
				MoveSquareLeft(shape.d);
				MoveSquareLeft(shape.d);
                                shape.turnDirection();
			}
                        else if (shape.rotating == 2 && canSquareMove(shape.b, -1, -1) && canSquareMove(shape.c, 1, -1) && canSquareMove(shape.d, 2, 0)) {
				MoveSquareDown(shape.b);
				MoveSquareLeft(shape.b);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
				MoveSquareRight(shape.d);
				MoveSquareRight(shape.d);
                                shape.turnDirection();
			}
                        else if (shape.rotating == 3 && canSquareMove(shape.b, 1, 1) && canSquareMove(shape.c, -1, 1) && canSquareMove(shape.d, -2, 0)) {
				MoveSquareUp(shape.b);
				MoveSquareRight(shape.b);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
				MoveSquareLeft(shape.d);
				MoveSquareLeft(shape.d);
                                shape.turnDirection();
			}
                        else if (shape.rotating == 4 && canSquareMove(shape.b, -1, -1) && canSquareMove(shape.c, 1, -1) && canSquareMove(shape.d, 2, 0)) {
				MoveSquareDown(shape.b);
				MoveSquareLeft(shape.b);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
				MoveSquareRight(shape.d);
				MoveSquareRight(shape.d);
                                shape.turnDirection();
			}
        }
        
        private void RotateTShape(Squares shape){
            		if (shape.rotating == 1 && canSquareMove(shape.a, 1, 1) && canSquareMove(shape.d, -1, -1) && canSquareMove(shape.c, -1, 1)) {
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.a);
				MoveSquareDown(shape.d);
				MoveSquareLeft(shape.d);
				MoveSquareLeft(shape.c);
				MoveSquareUp(shape.c);
                                shape.turnDirection();
			} else if (shape.rotating == 2 && canSquareMove(shape.a, 1, -1) && canSquareMove(shape.d, -1, 1) && canSquareMove(shape.c, 1, 1)) {
				MoveSquareRight(shape.a);
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.d);
				MoveSquareUp(shape.d);
				MoveSquareUp(shape.c);
				MoveSquareRight(shape.c);
                                shape.turnDirection();
			} else if (shape.rotating == 3 && canSquareMove(shape.a, -1, -1) && canSquareMove(shape.d, 1, 1) && canSquareMove(shape.c, 1, -1)) {
				MoveSquareDown(shape.a);
				MoveSquareLeft(shape.a);
				MoveSquareUp(shape.d);
				MoveSquareRight(shape.d);
				MoveSquareRight(shape.c);
				MoveSquareDown(shape.c);
                                shape.turnDirection();
			} else if (shape.rotating == 4 && canSquareMove(shape.a, -1, 1) && canSquareMove(shape.d, 1, -1) && canSquareMove(shape.c, -1, -1)) {
				MoveSquareLeft(shape.a);
				MoveSquareUp(shape.a);
				MoveSquareRight(shape.d);
				MoveSquareDown(shape.d);
				MoveSquareDown(shape.c);
				MoveSquareLeft(shape.c);
                                shape.turnDirection();
			}
        }
        
        private void MoveShapeDown(Squares shape) {

		if (canShapeMove(shape, 0, -1)) {
				shape.a.setY(shape.a.getY() + SQUARE_SIZE);
				shape.b.setY(shape.b.getY() + SQUARE_SIZE);
				shape.c.setY(shape.c.getY() + SQUARE_SIZE);
				shape.d.setY(shape.d.getY() + SQUARE_SIZE);
			
		}
	}

	private void BlocksController(Pane game) {
                //check filled rows
		for (int i = 0; i < MAP_MATRIX[0].length; i++) {
			for (int j = 0; j < MAP_MATRIX.length; j++) {
				if (MAP_MATRIX[j][i] == 1)
					blocksOnOneLine++;
			}
			if (blocksOnOneLine == MAP_MATRIX.length)
			filledBlockRows.add(i);
			blocksOnOneLine = 0;
		}
                
                        while (filledBlockRows.size() > 0){
				for (Node node : game.getChildren()) {
					if (node instanceof Rectangle)
						currentBlocks.add(node);
				}
				points += 100;

				for (Node node : currentBlocks) {
					Rectangle block = (Rectangle) node;
                                        //check if the y coordinate of that block is the same as the bricks line in the array
					if (block.getY() == filledBlockRows.get(0) * SQUARE_SIZE) {
						MAP_MATRIX[(int) block.getX() / SQUARE_SIZE][(int) block.getY() / SQUARE_SIZE] = 0;
						game.getChildren().remove(node); //if so, remove that block, the loop will remove every filled row
					} else
						updatedBlocks.add(node);//if not, update the bricks
				}

				for (Node node : updatedBlocks) {
					Rectangle block = (Rectangle) node;
                                        //check every row above the removed row
					if (block.getY() < filledBlockRows.get(0) * SQUARE_SIZE) {
						MAP_MATRIX[(int) block.getX() / SQUARE_SIZE][(int) block.getY() / SQUARE_SIZE] = 0;//update the matrix
						block.setY(block.getY() + SQUARE_SIZE);//move the rows above down
					}
				}
				filledBlockRows.remove(0);
				currentBlocks.clear();
				updatedBlocks.clear();
                                
                                //now we traverse the blocks again to update the matrix
				for (Node node : game.getChildren()) {
					if (node instanceof Rectangle)
						currentBlocks.add(node);
				}
				for (Node node : currentBlocks) {
					Rectangle block = (Rectangle) node;
						MAP_MATRIX[(int) block.getX() / SQUARE_SIZE][(int) block.getY() / SQUARE_SIZE] = 1;
					
				}
				currentBlocks.clear();
			}
	}

	private void MoveSquareDown(Rectangle rect) {
		if (rect.getY() + SQUARE_SIZE < GAME_HEIGHT)
			rect.setY(rect.getY() + SQUARE_SIZE);
	}

	private void MoveSquareRight(Rectangle rect) {
		if (rect.getX() + SQUARE_SIZE <= GAME_WIDTH - SQUARE_SIZE)
			rect.setX(rect.getX() + SQUARE_SIZE);
	}

	private void MoveSquareLeft(Rectangle rect) {
		if (rect.getX() - SQUARE_SIZE >= 0)
			rect.setX(rect.getX() - SQUARE_SIZE);
	}

	private void MoveSquareUp(Rectangle rect) {
		if (rect.getY() - SQUARE_SIZE > 0)
			rect.setY(rect.getY() - SQUARE_SIZE);
	}
        
        private boolean canMoveRight(Squares shape){
            boolean touchRightBorder = (shape.a.getX() + SQUARE_SIZE <= GAME_WIDTH - SQUARE_SIZE) 
                                    && (shape.b.getX() + SQUARE_SIZE <= GAME_WIDTH - SQUARE_SIZE)
                                    && (shape.c.getX() + SQUARE_SIZE <= GAME_WIDTH - SQUARE_SIZE) 
                                    && (shape.d.getX() + SQUARE_SIZE <= GAME_WIDTH - SQUARE_SIZE);
            boolean touchOtherBlocks = (MAP_MATRIX[((int) shape.a.getX() / SQUARE_SIZE) + 1][((int) shape.a.getY() / SQUARE_SIZE)] == 0)
                                    && (MAP_MATRIX[((int) shape.b.getX() / SQUARE_SIZE) + 1][((int) shape.b.getY() / SQUARE_SIZE)] == 0)
                                    && (MAP_MATRIX[((int) shape.c.getX() / SQUARE_SIZE) + 1][((int) shape.c.getY() / SQUARE_SIZE)] == 0)
                                    && (MAP_MATRIX[((int) shape.d.getX() / SQUARE_SIZE) + 1][((int) shape.d.getY() / SQUARE_SIZE)] == 0);
            return ((touchRightBorder) && (touchOtherBlocks));
        }
        
        private boolean canMoveLeft(Squares shape){
            boolean touchLeftBorder = (shape.a.getX() - SQUARE_SIZE >= 0) 
                                   && (shape.b.getX() - SQUARE_SIZE >= 0)
                                   && (shape.c.getX() - SQUARE_SIZE >= 0) 
                                   && (shape.d.getX() - SQUARE_SIZE >= 0);
            boolean touchOtherBlocks = (MAP_MATRIX[((int) shape.a.getX() / SQUARE_SIZE) - 1][((int) shape.a.getY() / SQUARE_SIZE)] == 0)
                                    && (MAP_MATRIX[((int) shape.b.getX() / SQUARE_SIZE) - 1][((int) shape.b.getY() / SQUARE_SIZE)] == 0)
                                    && (MAP_MATRIX[((int) shape.c.getX() / SQUARE_SIZE) - 1][((int) shape.c.getY() / SQUARE_SIZE)] == 0)
                                    && (MAP_MATRIX[((int) shape.d.getX() / SQUARE_SIZE) - 1][((int) shape.d.getY() / SQUARE_SIZE)] == 0);
            return ((touchLeftBorder) && (touchOtherBlocks));
        }
        
        private boolean canShapeMove(Squares shape, int xMove, int yMove){
            return canSquareMove(shape.a, xMove, yMove)
                    && canSquareMove(shape.b, xMove, yMove)
                    && canSquareMove(shape.c, xMove, yMove)
                    && canSquareMove(shape.d, xMove, yMove);
        }
        
        private boolean canSquareMove(Rectangle square, int xMove, int yMove) {
		boolean horizontalValid = false;
		boolean verticalValid = false;
                boolean isPositionEmpty = false;
                //check if the brick touches the left or right bound
		if (xMove >= 0)
			horizontalValid = square.getX() + xMove * SQUARE_SIZE <= GAME_WIDTH - SQUARE_SIZE;
		if (xMove < 0)
			horizontalValid = square.getX() + xMove * SQUARE_SIZE >= 0;
                //check if the brick touches the ceil or floor
		if (yMove >= 0)//go down
			verticalValid = square.getY() - yMove * SQUARE_SIZE > 0;
		if (yMove < 0)//go up
			verticalValid = square.getY() - yMove * SQUARE_SIZE < GAME_HEIGHT;
                //check if there is enough space for the shape to roll
                isPositionEmpty = MAP_MATRIX[((int) square.getX() / SQUARE_SIZE) + xMove][((int) square.getY() / SQUARE_SIZE) - yMove] == 0;
                
		return horizontalValid && verticalValid && isPositionEmpty;
	}
        
        private boolean canMoveDown(Squares shape){
            boolean touchFloor = (shape.a.getY() + SQUARE_SIZE < GAME_HEIGHT) 
                              && (shape.b.getY() + SQUARE_SIZE < GAME_HEIGHT) 
                              && (shape.c.getY() + SQUARE_SIZE < GAME_HEIGHT)
                              && (shape.d.getY() + SQUARE_SIZE < GAME_HEIGHT);
            boolean touchOtherBlocks = (MAP_MATRIX[(int) shape.a.getX() / SQUARE_SIZE][((int) shape.a.getY() / SQUARE_SIZE) + 1] == 0)
                                    && (MAP_MATRIX[(int) shape.b.getX() / SQUARE_SIZE][((int) shape.b.getY() / SQUARE_SIZE) + 1] == 0)
                                    && (MAP_MATRIX[(int) shape.c.getX() / SQUARE_SIZE][((int) shape.c.getY() / SQUARE_SIZE) + 1] == 0)
                                    && (MAP_MATRIX[(int) shape.d.getX() / SQUARE_SIZE][((int) shape.d.getY() / SQUARE_SIZE) + 1] == 0);
            return (touchFloor && touchOtherBlocks);
        }
        
        public static void main(String[] args) {
		launch(args);
	}

}
