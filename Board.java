package Tetris;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import gfx.*;


/* This is the DrawingPanel class. It is associated with all of my shapes.
It is in charge of the details of the game including jumping, physics
stimulation, rebounding, scrolling, deleting platforms, and key recognition
*/

public class Board extends JPanel {

	private Square		_square;
	private MainPanel	_mainPanel;
	private boolean		_right, _left, _down, _pauseGame;
	private int		_dx1, _dy1, _dx2, _dy2, _dx3, _dy3, _dx4, _dy4, 
				_lx1, _ly1, _lx2, _ly2, _lx3, _ly3, _lx4, _ly4,
				_rx1, _ry1, _rx2, _ry2, _rx3, _ry3, _rx4, _ry4, _row;
	private JLabel		_gameOverLabel;
	private NextProxy	_proxy;
	private PieceFactory	_factory;
	private MyTimer		_myTimer;
	private Board		_board;
	private BasicGamePiece	_bgp;
	private Square[][]	_squareArray;


	public Board(MainPanel mainPanel) { //drawing panel constructor		
		super();
// Setting up size, properties, and color of background		
		this.setVisible(true);	
		this.setBackground(new Color(32,0,120));
		Dimension size = new Dimension((SquareConstants.NUM_ROWS*SquareConstants.SQUARE_SIZE)-(3*SquareConstants.SQUARE_SIZE), 
						SquareConstants.NUM_COLUMNS*(3*SquareConstants.SQUARE_SIZE)); 
		this.setPreferredSize(size);
		this.setSize(size);
		this.setFocusable(true);
		this.grabFocus();
		this.setFocusTraversalKeysEnabled(true);
		
// Initialize variables, start timer, and add elements
		_squareArray = new Square[SquareConstants.NUM_ROWS][SquareConstants.NUM_COLUMNS]; 
		_proxy 	     = new NextProxy(_squareArray);
		_myTimer     = new MyTimer(this, _proxy);	
		_factory     = new PieceFactory(this, _proxy, _squareArray);
		this.addKeyListener(new MyKeyListener());
		_bgp 	     = _factory.getNext();

// Link instance variables with parameters and booleans
		_mainPanel = mainPanel;
		_pauseGame = false;
		_down = false;

// Set first piece
		_bgp.setLocation(SquareConstants.SQUARE_SIZE*((SquareConstants.NUM_COLUMNS)/6),
		SquareConstants.SQUARE_SIZE*((SquareConstants.NUM_ROWS)/8));
		_bgp.react();

//SET UP BORDER!!!		
//To put squares in border (top edge for 3 rows thick)
		    for (int row = 0; row < 3; row++) {
			Square square;
			  for (int col = 0; col < SquareConstants.NUM_COLUMNS; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

//To put squares in border (left hand side for 3 columns thick)
		      for (int row = 0; row < 20; row++) {
			Square square;
			  for (int col = 0; col < 3; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

//To put squares in border (right hand side for 3 columns thick)
		      for (int row = 0; row < 20; row++) {
			Square square;
			  for (int col = 37; col < SquareConstants.NUM_COLUMNS; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

//To put squares in border (bottom edge for 3 rows thick)
		      for (int row = 17; row < 20; row++) {
			Square square;
			  for (int col = 0; col < SquareConstants.NUM_COLUMNS ; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

// End of constructor touch ups
		this.repaint();
		_myTimer.start();
}



// Game over method tells timer to stop, game to pause, score to reset, and background to change to black
// when piece is in top row below border
	public void gameOver() {
	  _myTimer.stop();
	  _pauseGame = true;
	  _mainPanel.resetScore();
	  this.setBackground(Color.BLACK);
	} 

// Pause game method called by P key. 
// pauses play, stops timer, changes background to purple
	public void pauseGame() {
	  _pauseGame = true;
	  _myTimer.stop();
	  this.setBackground(new Color(106,0,255));
	}

//Unpause game method called by P key.
// Unpaused play, starts timer, changes background color black
	public void unpauseGame() {
	  _pauseGame = false;
	  this.setBackground(new Color(32,0,120));
	  _myTimer.start();
	}

//method to drop pieces from the board into play
	public void dropPiece() {
	  if ((_proxy.gets1().getY())<4*SquareConstants.SQUARE_SIZE) {
	    this.gameOver();
	  }
	  else {
	  if (_proxy.gets1().getY() < (3*SquareConstants.SQUARE_SIZE)) {
		this.gameOver();
	    }
    // correlates position on the board to position in array
	  else {
	    int x1 = (int) (_proxy.gets1().getX()/SquareConstants.SQUARE_SIZE);
	    int y1 = (int) (_proxy.gets1().getY()/SquareConstants.SQUARE_SIZE);
	    _squareArray[x1][y1] = _proxy.gets1();

	    int x2 = (int) ((_proxy.gets2().getX())/SquareConstants.SQUARE_SIZE);
	    int y2 = (int) ((_proxy.gets2().getY())/SquareConstants.SQUARE_SIZE);
	    _squareArray[x2][y2] = _proxy.gets2();

	    int x3 = (int) ((_proxy.gets3().getX())/SquareConstants.SQUARE_SIZE);
	    int y3 = (int) ((_proxy.gets3().getY())/SquareConstants.SQUARE_SIZE);
	    _squareArray[x3][y3] = _proxy.gets3();

	    int x4 = (int) ((_proxy.gets4().getX())/SquareConstants.SQUARE_SIZE);
	    int y4 = (int) ((_proxy.gets4().getY())/SquareConstants.SQUARE_SIZE);
	    _squareArray[x4][y4] = _proxy.gets4();

	    _bgp = _factory.getNext();
	    _bgp.setLocation(SquareConstants.SQUARE_SIZE*((SquareConstants.NUM_COLUMNS)/5),
			      SquareConstants.SQUARE_SIZE*3);
	    _bgp.react();
	    }
	}
	}

// method called when reset button is pressed (basically just a second copy of the constructor)
	public void setBoard() {
	  _myTimer.stop();
	  this.setBackground(new Color(32,0,120));
		    for (int row = 0; row < SquareConstants.NUM_ROWS; row++) {
			  for (int col = 0; col < SquareConstants.NUM_COLUMNS; col++) {
			      _squareArray[row][col] = null;
			  }
		    }

//To put squares in border (top edge for 3 rows thick)
		    for (int row = 0; row < 3; row++) {
			Square square;
			  for (int col = 0; col < SquareConstants.NUM_COLUMNS; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

//To put squares in border (left hand side for 3 columns thick)
		      for (int row = 0; row < 20; row++) {
			Square square;
			  for (int col = 0; col < 3; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

//To put squares in border (right hand side for 3 columns thick)
		      for (int row = 0; row < 20; row++) {
			Square square;
			  for (int col = 37; col < SquareConstants.NUM_COLUMNS; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }

//To put squares in border (bottom edge for 3 rows thick)
		      for (int row = 17; row < 22; row++) {
			Square square;
			  for (int col = 0; col < SquareConstants.NUM_COLUMNS ; col++) {
			      square = new Square(this);
			      square.setFillColor(Color.WHITE);
			      square.setLocation(row * SquareConstants.SQUARE_SIZE, 
					  col * SquareConstants.SQUARE_SIZE);
			      _squareArray[row][col] = square;
			  }
		      }	      
		_bgp = _factory.getNext();
		_bgp.setLocation(SquareConstants.SQUARE_SIZE*(SquareConstants.NUM_COLUMNS/2),
		SquareConstants.SQUARE_SIZE*(SquareConstants.NUM_ROWS/2));
		_bgp.react();
		_myTimer.start();
		_pauseGame = true;
		_mainPanel.resetScore();
		this.setFocusable(true);
		this.grabFocus();
		this.setFocusTraversalKeysEnabled(true);

		}
	    
		
	public void paintComponent(Graphics g) {  // method to paint objects/make objects appear on drawing panel
		super.paintComponent(g);  // Partial override
		Graphics2D brush = (Graphics2D) g;
		_proxy.paint(brush);
		for (int row = 0; row < SquareConstants.NUM_ROWS; row++) {
		  for (int col = 0; col < SquareConstants.NUM_COLUMNS; col++) {
		    Square square = _squareArray[row][col];
		if (square != null) {
		    square.paint(brush);
		}
		}
		}
	} // end of paint component
	  

// method to check if a line should be cleared
	public void clearLineCheck() {
	    for (int row = 3; row < 37; row++) {
		  if (this.shouldLineEmpty(row) == true) {
			this.emptyLine(row);

		  }
	    }
	}

// boolean to see if a line is supposed to be cleared
	public boolean shouldLineEmpty(int line) {
	    for (int col = 3; col < 17; col++) {
			if (_squareArray[col][line] == null) {
				return false;
	     	}
	    }	 
	  return true;
	}

// method to empty the line
	public void emptyLine(int line) {
			System.out.println("emptying line " + line);
	    	for (int c = 3; c < 17; c++) {
		  	  	_squareArray[c][line] = null;
			}		  
			for (int row = line; row > 3; row--) {
				for (int col = 3; col < 17; col++) {
					_squareArray[col][row] = _squareArray[col][row-1];
					if (_squareArray[col][row] != null) {
			       		System.out.println("Square in row " + row + ", col " + col +" exists.");
						_squareArray[col][row].setLocation((col)*SquareConstants.SQUARE_SIZE, (row)*SquareConstants.SQUARE_SIZE);
						this.repaint();
					}					
				}
			}
/*		    for (int row = line; row > 3; row--) {
				for (int col = 3; col < 17; col++) {
					//System.out.print(_squareArray[col][row] + " ");
			      	_squareArray[col][row] = _squareArray[col][row-1];
			       	if (_squareArray[col][row] != null) {
			       		System.out.println("Square in row " + row + ", col " + col +" exists.");
						_squareArray[col][row].setLocation((col)*SquareConstants.SQUARE_SIZE, (row+1)*SquareConstants.SQUARE_SIZE);
						this.repaint();
					}
					this.repaint();
				}
				//System.out.println(" ");
			} */
			    
	}
	 

	private class MyKeyListener implements KeyListener {

		/* This class does not need an explicit constructor
		* because there is nothing to initialize. Java
		* defines an empty constructor as default.
		* Note: Private inner classes have access to
		* private instance variables of container class!
		*/


		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if ((keyCode == KeyEvent.VK_LEFT) && (_pauseGame == false)) { //left key checks and moves piece left
				_proxy.moveLeft(-SquareConstants.SQUARE_SIZE, 0);	
			}
			else if ((keyCode == KeyEvent.VK_RIGHT) && (_pauseGame == false)) { //right key checks and moves right
				_proxy.moveRight(SquareConstants.SQUARE_SIZE, 0);
			}
			else if ((keyCode == KeyEvent.VK_SPACE) && (_pauseGame == false)) { //spacebar checks and drops piece down until it reaches bottom
				_proxy.dropDown(0, SquareConstants.SQUARE_SIZE);
			}
			else if ((keyCode == KeyEvent.VK_UP) && (_pauseGame == false)) { //up key checks and rotates piece
				_proxy.rotate();
			}
			else if ((keyCode == KeyEvent.VK_DOWN) && (_pauseGame == false)) { //down key checks and moves down
				_proxy.moveDown(0, SquareConstants.SQUARE_SIZE);
			}
			else if (keyCode == KeyEvent.VK_P && _pauseGame == true) { 
			    //P key checks and pauses or unpauses game based on the current _pauseGame boolean value
				_mainPanel.unpauseGame();
			}
			else if (keyCode == KeyEvent.VK_P && _pauseGame == false) {
			    //P key checks and pauses or unpauses game based on the current _pauseGame boolean value
				_mainPanel.pauseGame();
			}
		} // end of keyPressed method

		public void keyTyped(KeyEvent e) {} //must write this bc of KeyListener interface
		public void keyReleased(KeyEvent e) {} //must write this bc of KeyListener interface
			    
      
	} // end of myKeyListener


}	
		
		
		



