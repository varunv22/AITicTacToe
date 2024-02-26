import java.awt.*;
import java.awt.event.*;

public class Board extends MouseAdapter {
    private static final int squareLength = 200;
    private Tiles[][] board;
    private Tiles winner;
    private GameAPI api;
    private String curr_turn;
    private int rows, cols, num_moves;
    private boolean winnerFound;
    private String playerChoice;
    private boolean playerMoveDone;

    public Board() {
        board = new Tiles[3][3];
        api = new GameAPI();
        rows = board.length;
        cols = board[0].length;
        curr_turn = " ";
        winner = null;
        winnerFound = false;
        num_moves = 0;
        playerChoice = "";
        playerMoveDone = true;
    }

    public void update() 
    {
        if (curr_turn.equals(" "))
            curr_turn = api.changeTurns(curr_turn);
        if (playerMoveDone) 
        {
        	if(board != null)
        	{
        		computerMove();
        		curr_turn = api.changeTurns(curr_turn);
                playerMoveDone = false;
        	}
        }
        winner = api.winner(board, rows, cols);
        if (winner != null && !winner.get_elem().getValue().equals(" ")) 
        {
            winnerFound = true;
        }
        if (winner == null && num_moves == 9) 
        {
            winnerFound = true;
        }

    }

    public void draw(Graphics g) {
        g.setFont(new Font("Roboto", Font.BOLD, 180));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null)
                    board[i][j] = new Tiles(i + 1, j + 1, new Elements(curr_turn, -1, -1));
                g.setColor(Color.WHITE);
                g.fillRect(i * squareLength + 100, j * squareLength + 90, squareLength, squareLength);
                ((Graphics2D) g).setStroke(new BasicStroke(4));
                g.setColor(Color.BLACK);
                g.drawRect(i * squareLength + 100, j * squareLength + 90, squareLength + 1, squareLength + 1);
                g.drawString(board[i][j].get_elem().getValue(), i * squareLength + 95 + (squareLength / 5),
                        j * squareLength + 160 + (squareLength / 2));
            }
        }
        if (winnerFound) {
            if (winner != null) {
                api.drawWinningPath(g, winner, squareLength);
            }
            if (num_moves == 9 && winner == null) {
                api.gameResult(g, "TIE");
            } else {
                api.gameResult(g, winner.get_elem().getValue());
            }
            askToPlayAgain(g);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("Welcome to Tic-Tac-Toe!", 50, 60);
        }
    }

    public void askToPlayAgain(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(new Color(247, 220, 111));
        g.drawString("Would you like to play again?", 20, 60);

        g.setColor(new Color(17, 122, 101));
        g.fill3DRect(550, 20, 100, 50, true);
        g.setColor(new Color(231, 76, 60));
        g.fill3DRect(670, 20, 100, 50, true);

        g.setColor(Color.WHITE);
        g.drawString("Yes", 565, 55);
        g.drawString("No", 695, 55);
    }

    public void resetGame() {
        num_moves = 0;
        curr_turn = " ";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Tiles(i + 1, j + 1, new Elements(curr_turn, -1, -1));
            }
        }
        winnerFound = false;
        winner = null;
        playerChoice = "";
        playerMoveDone = true;
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (getRowClicked(mx, my) != -1 && getColClicked(mx, my) != -1 && !winnerFound) {
            int squareRow = getRowClicked(mx, my);
            int squareCol = getColClicked(mx, my);
            if (curr_turn.equals("X")) {
                if (board[squareCol - 1][squareRow - 1].get_elem().getValue().equals(" ")) {
                    board[squareCol - 1][squareRow - 1].set_elem(new Elements(curr_turn, squareRow, squareCol));
                    num_moves++;
                    curr_turn = api.changeTurns(curr_turn);
                    playerMoveDone = true;
                }
            }
            // boardTester();
        }
        if (winnerFound) {
            if (buttonClicked(e, 550, 20, "Yes")) {
                resetGame();
            } else if (buttonClicked(e, 670, 20, "No")) {
                System.exit(1);
            }
        }
    }

    public void computerMove() {
        Tiles[][] tempBoard = copyBoard(board);
        Tiles tempWinner = null;
        String comp = "O";
        String playMove = "X";
        /**
         * Take the winning move
         * */
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                if (tempBoard[j][i].get_elem().getValue().equals(" ")) 
                {
                    tempBoard[j][i].set_elem(new Elements(comp, i + 1, j + 1));
                    tempWinner = api.winner(tempBoard, 3, 3);
                    if (tempWinner != null && winner == null) 
                    {
                    	board[j][i].set_elem(new Elements(comp, i + 1, j + 1));
                    	num_moves++;
                        return;
                    }
                    else if(winner == null)
                    {
                    	tempBoard[j][i].set_elem(new Elements(" ",i+1,j+1));
                    	tempWinner = null;
                    }
                }
            }
        }
        /**
         * Stop the opponent from winning
         * */
        for(int i = 0;i<rows;i++)
        {
        	for(int j = 0;j<cols;j++)
        	{
        		if (tempBoard[j][i].get_elem().getValue().equals(" ")) 
        		{
        			tempBoard[j][i].set_elem(new Elements(playMove, i + 1, j + 1));
        			tempWinner = api.winner(tempBoard, rows, cols);
        			if(tempWinner != null && winner == null)
        			{
        				board[j][i].set_elem(new Elements(comp,i+1,j+1));
        				num_moves++;
        				return;
        			}
        			else
        			{
        				tempBoard[j][i].set_elem(new Elements(" ",i+1,j+1));
                    	tempWinner = null;
        			}
        		}
        	}
        }
        /**
         * Select center if open
         * */
        if(board[1][1].get_elem().getValue().equals(" "))
        {
        	board[1][1].set_elem(new Elements(comp, 2,2));
        	num_moves++;
        	return;
        }
        /**
         * Select any corners
         * */
        for(int i = (int)(Math.random()*rows);i<rows;i++)
        {
        	for(int j = (int)(Math.random()*rows);j<cols;j++)
        	{
        		if(board[j][i].get_elem().getValue().equals(" ") && (i != 1 || j != 1))
        		{
        			 board[j][i].set_elem(new Elements(comp, j,i));
        			 num_moves++;
        			 return;
        		}
        	}
        }
        for(int i = 0;i<rows;i++)
        {
        	for(int j = 0;j<cols;j++)
        	{
        		if(board[j][i].get_elem().getValue().equals(" "))
        		{
        			board[j][i].set_elem(new Elements(comp, j,i));
       			 	num_moves++;
       			 	return;
        		}
        	}
        }
    }
    public Tiles[][] copyBoard(Tiles[][] b) {
        Tiles[][] toReturn = new Tiles[rows][cols];
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                Elements p = b[i][j].get_elem();
                int row = b[i][j].getRow();
                int col = b[i][j].getCol();
                toReturn[i][j] = new Tiles(row, col, new Elements(p.getValue(),p.getRow(),p.getCol()));
            }
        }
        return toReturn;
    }

    public boolean buttonClicked(MouseEvent e, int bx, int by, String buttonName) {
        int mx = e.getX();
        int my = e.getY();
        if (buttonName.equals("Yes")) {
            if (mx >= bx && mx <= bx + 100) {
                if (my >= by && my <= by + 50) {
                    return true;
                }
            }
            return false;
        } else if (buttonName.equals("No")) {
            if (mx >= bx && mx <= bx + 100) {
                if (my >= by && my <= by + 50) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getRowClicked(int mx, int my) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (my >= (board[i][j].getRow() - 1) * squareLength + 90
                        && my <= (board[i][j].getRow() - 1) * squareLength + 90 + squareLength + 1) {
                    return board[i][j].getRow();
                }
            }
        }
        return -1;
    }

    public int getColClicked(int mx, int my) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mx >= (board[i][j].getCol() - 1) * squareLength + 100
                        && mx <= (board[i][j].getCol() - 1) * squareLength + 95 + squareLength + 1) {
                    return board[i][j].getCol();
                }
            }
        }
        return -1;
    }

}