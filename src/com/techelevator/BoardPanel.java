package com.techelevator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import javax.swing.JPanel;

/**
 * The {@code BoardPanel} class is responsible for managing and displaying the
 * contents of the game board.
 *
 */
public class BoardPanel extends JPanel {
	
	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = -1102632585936750607L;

	/**
	 * The number of columns on the board. (Should be odd so we can start in
	 * the center).
	 */
	public static final int COL_COUNT = 32;
	
	/**
	 * The number of rows on the board. (Should be odd so we can start in
	 * the center).
	 */
	public static final int ROW_COUNT = 32;
	
	/**
	 * The size of each tile in pixels.
	 */
	public static final int TILE_SIZE = 20;
	
	/**
	 * The number of pixels to offset the eyes from the sides.
	 */
	private static final int EYE_LARGE_INSET = TILE_SIZE / 3;
	
	/**
	 * The number of pixels to offset the eyes from the front.
	 */
	private static final int EYE_SMALL_INSET = TILE_SIZE / 2;
	
	/**
	 * The length of the eyes from the base (small inset).
	 */
	private static final int EYE_LENGTH = TILE_SIZE / 5;
	
	/**
	 * The font to draw the text with.
	 */
	private static final Font FONT = new Font("Ubuntu", Font.PLAIN, 25);
		
	/**
	 * The SnakeGame instance.
	 */
	private SnakeGame game;
	
	/**
	 * The array of tiles that make up this board.
	 */
	private TileType[] tiles;
		
	/**
	 * Creates a new BoardPanel instance.
	 * @param game The SnakeGame instance.
	 */
	public BoardPanel(SnakeGame game) {
		this.game = game;
		this.tiles = new TileType[ROW_COUNT * COL_COUNT];
		
		setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT * TILE_SIZE));
		setBackground(Color.BLACK);
	}
	
	/**
	 * Clears all of the tiles on the board and sets their values to null.
	 */
	public void clearBoard() {
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = null;
		}
	}
	
	/**
	 * Sets the tile at the desired coordinate.
	 * @param point The coordinate of the tile.
	 * @param type The type to set the tile to.
	 */
	public void setTile(Point point, TileType type) {
		setTile(point.x, point.y, type);
	}
	
	/**
	 * Sets the tile at the desired coordinate.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param type The type to set the tile to.
	 */
	public void setTile(int x, int y, TileType type) {
		tiles[y * ROW_COUNT + x] = type;
	}
	
	/**
	 * Gets the tile at the desired coordinate.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @return
	 */
	public TileType getTile(int x, int y) {
		return tiles[y * ROW_COUNT + x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*
		 * Loop through each tile on the board and draw it if it
		 * is not null.
		 */
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				TileType type = getTile(x, y);
				if(type != null) {
					drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
				}
			}
		}
		
		/*
		 * Draw the grid on the board. This makes it easier to see exactly
		 * where we in relation to the fruit.
		 * 
		 * The panel is one pixel too small to draw the bottom and right
		 * outlines, so we outline the board with a rectangle separately.
		 */
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
				g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
			}
		}		
		
		/*
		 * Show a message on the screen based on the current game state.
		 */
		if(game.isGameOver() || game.isNewGame() || game.isPaused()) {
			g.setColor(Color.WHITE);
			
			/*
			 * Get the center coordinates of the board.
			 */
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			
			/*
			 * Allocate the messages for and set their values based on the game
			 * state.
			 */
			String largeMessage = null;
			String smallMessage = null;
			if(game.isNewGame()) {
				largeMessage = "Play Snake!";
				smallMessage = "Press Enter to Start";
			} else if(game.isGameOver()) {
				largeMessage = "Game Over!";
				smallMessage = "Press Enter to Restart";
			} else if(game.isPaused()) {
				largeMessage = "Paused";
				smallMessage = "Press P to Resume";
			}
			
			/*
			 * Set the message font and draw the messages in the center of the board.
			 */
			g.setFont(FONT);
			g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
			g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
		}
	}
	
	/**
	 * Draws a tile onto the board.
	 * @param x The x coordinate of the tile (in pixels).
	 * @param y The y coordinate of the tile (in pixels).
	 * @param type The type of tile to draw.
	 * @param g The graphics object to draw to.
	 */
	private void drawTile(int x, int y, TileType type, Graphics g) {
		/*
		 * Because each type of tile is drawn differently, it's easiest
		 * to just run through a switch statement rather than come up with some
		 * overly complex code to handle everything.
		 */
		
		switch(type) {
		
		/*
		 * A fruit is depicted as a small red circle that with a bit of padding
		 * on each side.
		 */
		case Fruit:
			g.setColor(Color.MAGENTA);
			g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
			break;
			
		/*
		 * The snake body is depicted as a green square that takes up the
		 * entire tile.
		 */
		case SnakeBody:
			
			g.setColor(new Color(0, 255, 0)); // set base color to green
			g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
			for (int i = 1; i < game.getSnake().size(); i+=2) {
				g.setColor(new Color(0, 165, 0)); // set secondary main color
				g.fillRect(((int)game.getSnake().get(i).getX()*TILE_SIZE), ((int)game.getSnake().get(i).getY())*TILE_SIZE, TILE_SIZE, TILE_SIZE);// get cords for i and fill
		}
			for (int i = 10; i < game.getSnake().size(); i+=10) {
					g.setColor(new Color(240, 240, 0)); // set color for 10+ length indicator
					g.fillRect(((int)game.getSnake().get(i).getX()*TILE_SIZE), ((int)game.getSnake().get(i).getY())*TILE_SIZE, TILE_SIZE, TILE_SIZE);// get cords for i and fill
			}
			if(game.getSnake().size()>41) { // if length greater than 41
				int r = new Random().nextInt( (255 -80) +1) + 80; // blinking red tail
				g.setColor(new Color(r, 0, 0)); // set color for tail on 40+ snake. 
				g.fillRect(((int)game.getSnake().peekLast().getX()*TILE_SIZE), ((int)game.getSnake().peekLast().getY())*TILE_SIZE, TILE_SIZE, TILE_SIZE);// get cords for i and fill
			}
		break;
			
		/*
		 * The snake head is depicted similarly to the body, but with two
		 * lines (representing eyes) that indicate it's direction.
		 */
		case SnakeHead:
			//Fill the tile in with green.
			g.setColor(new Color(0, 255, 0));
			g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
			
			//Set the color to black so that we can start drawing the eyes.
			g.setColor(Color.BLACK);
			
			/*
			 * The eyes will always 'face' the direction that the snake is
			 * moving.
			 * 
			 * Vertical lines indicate that it's facing North or South, and
			 * Horizontal lines indicate that it's facing East or West.
			 * 
			 * Additionally, the eyes will be closer to whichever edge it's
			 * facing.
			 * 
			 * Drawing the eyes is fairly simple, but is a bit difficult to
			 * explain. The basic process is this:
			 * 
			 * First, we add (or subtract) EYE_SMALL_INSET to or from the
			 * side of the tile representing the direction we're facing. This
			 * will be constant for both eyes, and is represented by the
			 * variable 'baseX' or 'baseY' (depending on orientation).
			 * 
			 * Next, we add (or subtract) EYE_LARGE_INSET to and from the two
			 * neighboring directions (Example; East and West if we're facing
			 * north).
			 * 
			 * Finally, we draw a line from the base offset that is EYE_LENGTH
			 * pixels in length at whatever the offset is from the neighboring
			 * directions.
			 * 
			 */
			switch(game.getDirection()) {
			case North: {
				int baseY = y + EYE_SMALL_INSET;
				g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY + EYE_LENGTH);
				g.drawLine(x + EYE_LARGE_INSET + 1, baseY, x + EYE_LARGE_INSET + 1, baseY + EYE_LENGTH); // thicker eyes
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET -1 , baseY, x + TILE_SIZE - EYE_LARGE_INSET - 1, baseY + EYE_LENGTH); // thicker eyes
				// add a happy little mouth
				int distFromEye = 3;
				g.drawLine(x + EYE_LARGE_INSET -2 , baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET +2 , baseY - distFromEye );// top of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET -2, baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET  +2, baseY - distFromEye );// top of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET -1 , baseY - distFromEye, x + EYE_LARGE_INSET +1 , baseY - distFromEye );// left of mouth
				g.setColor(Color.RED);// fill mouth
				g.drawLine(x + EYE_LARGE_INSET + 2 , baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET +1 , baseY - distFromEye );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET +1 , baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET  -1, baseY - distFromEye);// right of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET  , baseY - distFromEye, x + EYE_LARGE_INSET +2 , baseY - distFromEye );// left of mouth
				g.setColor(Color.RED);// fill mouth
				g.drawLine(x + EYE_LARGE_INSET + 3 , baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET , baseY - distFromEye );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET  , baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET  -2 , baseY - distFromEye); // right of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET +1, baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET -1 , baseY - distFromEye );// bottom of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET +2, baseY - distFromEye, x + TILE_SIZE - EYE_LARGE_INSET -2 , baseY - distFromEye );// bottom of mouth
				
				break;
			}
				
			case South: {
				int baseY = y + TILE_SIZE - EYE_SMALL_INSET;
				g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY - EYE_LENGTH);
				g.drawLine(x + EYE_LARGE_INSET + 1, baseY, x + EYE_LARGE_INSET + 1, baseY - EYE_LENGTH); // larger eyes
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY - EYE_LENGTH);
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET - 1, baseY, x + TILE_SIZE - EYE_LARGE_INSET - 1 , baseY - EYE_LENGTH); // thicker eyes
				
				// add a happy little mouth
				int distFromEye = 3;
				g.drawLine(x + EYE_LARGE_INSET -2 , baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET +2 , baseY + distFromEye );// top of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET -2, baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET  +2, baseY + distFromEye );// top of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET -1 , baseY + distFromEye, x + EYE_LARGE_INSET +1 , baseY + distFromEye );// left of mouth
				g.setColor(Color.RED);// fill mouth
				g.drawLine(x + EYE_LARGE_INSET + 2 , baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET +1 , baseY + distFromEye );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET +1 , baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET  -1, baseY + distFromEye);// right of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET  , baseY + distFromEye, x + EYE_LARGE_INSET +2 , baseY + distFromEye );// left of mouth
				g.setColor(Color.RED);// fill mouth
				g.drawLine(x + EYE_LARGE_INSET + 3 , baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET , baseY + distFromEye );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET  , baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET  -2 , baseY + distFromEye); // right of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET +1, baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET -1 , baseY + distFromEye );// bottom of mouth
				distFromEye++;
				g.drawLine(x + EYE_LARGE_INSET +2, baseY + distFromEye, x + TILE_SIZE - EYE_LARGE_INSET -2 , baseY + distFromEye );// bottom of mouth

				break;
			}
			
			case West: {
				int baseX = x + EYE_SMALL_INSET;
				g.drawLine(baseX, y + EYE_LARGE_INSET, baseX + EYE_LENGTH, y + EYE_LARGE_INSET);
				g.drawLine(baseX, y + EYE_LARGE_INSET + 1, baseX + EYE_LENGTH, y + EYE_LARGE_INSET + 1); //thicker eyes
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET - 1, baseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET - 1); //thicker eyes
				
				// add a happy little mouth
				int distFromEye = 3;
				g.drawLine(baseX - distFromEye, y + EYE_LARGE_INSET -2 , baseX - distFromEye , y + TILE_SIZE - EYE_LARGE_INSET +2 );// top of mouth
				distFromEye++;
				g.drawLine(baseX - distFromEye, y + EYE_LARGE_INSET -2, baseX - distFromEye, y + TILE_SIZE - EYE_LARGE_INSET  +2);// top of mouth
				distFromEye++;
				g.drawLine(baseX - distFromEye, y + EYE_LARGE_INSET -1 , baseX - distFromEye ,  y + EYE_LARGE_INSET +1 );
				g.setColor(Color.RED);// fill mouth
				g.drawLine( baseX - distFromEye, y + EYE_LARGE_INSET + 2 , baseX - distFromEye , y + TILE_SIZE - EYE_LARGE_INSET +1 );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine( baseX - distFromEye, y + TILE_SIZE - EYE_LARGE_INSET +1 , baseX - distFromEye , y + TILE_SIZE - EYE_LARGE_INSET  -1);
				distFromEye++;
				g.drawLine( baseX - distFromEye, y + EYE_LARGE_INSET , baseX - distFromEye, y + EYE_LARGE_INSET +2 );
				g.setColor(Color.RED);// fill mouth
				g.drawLine(baseX - distFromEye, y + EYE_LARGE_INSET + 3 , baseX - distFromEye  , y + TILE_SIZE - EYE_LARGE_INSET );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine(baseX - distFromEye , y + TILE_SIZE - EYE_LARGE_INSET , baseX - distFromEye , y + TILE_SIZE - EYE_LARGE_INSET  -2 ); 
				distFromEye++;
				g.drawLine(baseX - distFromEye, y + EYE_LARGE_INSET +1 , baseX - distFromEye , y + TILE_SIZE - EYE_LARGE_INSET -1 );// bottom of mouth
				distFromEye++;
				g.drawLine(baseX - distFromEye, y + EYE_LARGE_INSET +2 , baseX - distFromEye ,  y + TILE_SIZE - EYE_LARGE_INSET -2 );// bottom of mouth
				break;
			}
				
			case East: {
				int baseX = x + TILE_SIZE - EYE_SMALL_INSET;
				g.drawLine(baseX, y + EYE_LARGE_INSET, baseX - EYE_LENGTH, y + EYE_LARGE_INSET);
				g.drawLine(baseX, y + EYE_LARGE_INSET + 1, baseX - EYE_LENGTH, y + EYE_LARGE_INSET + 1 ); // thicker eyes
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET - 1, baseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET - 1); // thicker eyes
				
				// add a happy little mouth
				int distFromEye = 3;
				g.drawLine(baseX + distFromEye, y + EYE_LARGE_INSET -2 , baseX + distFromEye , y + TILE_SIZE - EYE_LARGE_INSET +2 );// top of mouth
				distFromEye++;
				g.drawLine(baseX + distFromEye, y + EYE_LARGE_INSET -2, baseX + distFromEye, y + TILE_SIZE - EYE_LARGE_INSET  +2);// top of mouth
				distFromEye++;
				g.drawLine(baseX + distFromEye, y + EYE_LARGE_INSET -1 , baseX + distFromEye ,  y + EYE_LARGE_INSET +1 );
				g.setColor(Color.RED);// fill mouth
				g.drawLine( baseX + distFromEye, y + EYE_LARGE_INSET + 2 , baseX + distFromEye , y + TILE_SIZE - EYE_LARGE_INSET +1 );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine( baseX + distFromEye, y + TILE_SIZE - EYE_LARGE_INSET +1 , baseX + distFromEye , y + TILE_SIZE - EYE_LARGE_INSET  -1);
				distFromEye++;
				g.drawLine( baseX + distFromEye, y + EYE_LARGE_INSET , baseX + distFromEye, y + EYE_LARGE_INSET +2 );
				g.setColor(Color.RED);// fill mouth
				g.drawLine(baseX + distFromEye, y + EYE_LARGE_INSET + 3 , baseX + distFromEye  , y + TILE_SIZE - EYE_LARGE_INSET );// fill
				g.setColor(Color.BLACK);// back to black
				g.drawLine(baseX + distFromEye , y + TILE_SIZE - EYE_LARGE_INSET , baseX + distFromEye , y + TILE_SIZE - EYE_LARGE_INSET  -2 ); 
				distFromEye++;
				g.drawLine(baseX + distFromEye, y + EYE_LARGE_INSET +1 , baseX + distFromEye , y + TILE_SIZE - EYE_LARGE_INSET -1 );// bottom of mouth
				distFromEye++;
				g.drawLine(baseX + distFromEye, y + EYE_LARGE_INSET +2 , baseX + distFromEye ,  y + TILE_SIZE - EYE_LARGE_INSET -2 );// bottom of mouth
				break;
			}
			
			}
			break;
		}
	}

}
