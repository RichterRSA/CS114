
import java.awt.Color;

// Notes on using this skeleton:
// - The names of variables and functions (both those given in the skeleton and those made by yourself) are up to you, as long as they are sensible.
// - You should document your code well with comments and (as stated above) sensible & logical names.
// - You are free to delete the comments typed in this skeleton.
// - Remember to follow the code specifications given in the project description.


// Replace "xxxxxxxx" below with your student number.  Remember to rename this file accordingly.
public class SUxxxxxxxx {

    // No global variables or constants allowed.

    // For the first hand-in, it is possible and highly recommended to code the entire program within the main function.
    public static void main(String[] args) {
        
        //game board setup
        int colorCount = 2;
        int boardSize = (int)(58*Math.pow(colorCount-1, 3) - 310*Math.pow(colorCount-1, 2) + 546*(colorCount-1) - 286);//formula for game size 
        char[][] gameBoard = new char[boardSize][boardSize];
        double blockSize=1.0/boardSize;
        StdOut.println("Board size: " + boardSize);
       
        //set game board default values & drawing
        
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledSquare(0, 0, 1);
        for (int y=0; y<boardSize; y++) {
            for (int x=0; x<boardSize; x++){
                gameBoard[y][x] = ((x==0)?'O':'C');  //close
                StdDraw.setPenColor(gameBoard[y][x]=='O'?Color.GRAY:Color.WHITE);
                StdDraw.filledSquare(blockSize/2+x*blockSize, 1-blockSize/2-y*blockSize, blockSize/2-0.005);
            }
        }
        
        // Handle program arguments (with validation). Arguments can be referenced through the "args" parameter above.
        // The arguments appear in "args" in the order that they were passed into the program on execution.

        // Setup game state variables. Important considerations: How will you store your board? How will you reference the currently selected square?
    
        boolean gameIsRunning = true;
        // "gameIsRunning" is an example of a game state variable. Add more as you see fit.

        // Enter the game loop. What will happen if you initialize gameIsRunning to false?
        while (gameIsRunning) {
            //gameIsRunning = false;
        }
        // What will happen if you remove the "gameIsRunning = false" statement inside the While loop? 

        // After the game is concluded, report the score by printing the state of the game to the terminal. 
        StdOut.println("Game ended!");
    }
    
    // For the second hand-in, you must use functions effectively wherever possible. Put these functions here.


}

