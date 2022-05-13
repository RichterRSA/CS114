
import java.awt.Color;
//
//// Notes on using this skeleton:
//// - The names of variables and functions (both those given in the skeleton and those made by yourself) are up to you, as long as they are sensible.
//// - You should document your code well with comments and (as stated above) sensible & logical names.
//// - You are free to delete the comments typed in this skeleton.
//// - Remember to follow the code specifications given in the project description.
//
//
//// Replace "xxxxxxxx" below with your student number.  Remember to rename this file accordingly.
public class SUxxxxxxxx {

    // No global variables or constants allowed.

    // For the first hand-in, it is possible and highly recommended to code the entire program within the main function.
    public static void main(String[] args) {
        //get game mode (0=first hand in, 1=second, 2=automatic solver)
        //change
        int mode = 0, gui = 0, n = 2, k = 3;
        int boardSize;
        String gameStatusText = "Valid";
        
        //validate argument amount
        if(args.length<4){ // too short
            StdOut.println("Not enough arguments.");
            return;
        }
        else if (args.length>4){ // too long
            StdOut.print("Too many arguments.");
            return;
        }
        else{ // correct amount, get vars mode, gui, n and k from args
            if (isInt(args[0]) && "012".contains(args[0]))
                mode = Integer.parseInt(args[0]);
            else
                StdOut.println("First input reset to default.");
            
            if (isInt(args[1]) && "01".contains(args[1]))
                gui = Integer.parseInt(args[1]);
            else
                StdOut.println("Second input reset to default.");
            
            if (isInt(args[2])){
                n = Integer.parseInt(args[2]);
                
                if((clampInt(n, 2, 4)!=n)){
                    n = 2;
                    StdOut.println("Third input reset to default.");
                }
            }
            else
                StdOut.println("Third input reset to default.");
            
            //formula to get board size
            boardSize = (int)(58*Math.pow(n-1, 3) - 310*Math.pow(n-1, 2) + 546*(n-1) - 286);//formula for game size 
            
            //validate fourth input
            if (isInt(args[3])){
                k = Integer.parseInt(args[3]);
                
                if((clampInt(k, 3, boardSize)!=k)){
                    k = 3;
                    StdOut.println("Fourth input reset to default.");
                }                
            }
            else
                StdOut.println("Fourth input reset to default.");
        }
                
        //Show game startup fields
        StdOut.println("The dimension of your board is: " + boardSize + "x" + boardSize);
        StdOut.println("The length of a blockade is: " + k);
        StdOut.println();
        
        //player starts at row 0, col 0
        int xPos = 0, yPos = 0, moveCount = 0;
        
        //Colors:
        //0 = white (inactive)
        //1 = gray (active)
        //2, 3, 4 ... = green, yellow, red ... 
        byte[][] gameBoard = new byte[boardSize][boardSize];
        
        //Determine how big blocks are to draw (for gui)
        double blockSize=1.0/boardSize;
               
        //set game board default values & drawing
        for (int y=0; y<boardSize; y++) {
            for (int x=0; x<boardSize; x++){
                //make first column active and everything else inactive
                gameBoard[y][x] = (byte) ((x==0)?1:0); 
            }
        }
        
        //Draw at start using gui or text
        if(gui==1){
            StdDraw.enableDoubleBuffering();
            StdDraw.clear(Color.BLACK);
        }
        else{
            DrawGameText(gameBoard, boardSize);
            StdOut.println();
        }
        
        // Handle program arguments (with validation). Arguments can be referenced through the "args" parameter above.
        // The arguments appear in "args" in the order that they were passed into the program on execution.

        // Setup game state variables. Important considerations: How will you store your board? How will you reference the currently selected square?
    
        boolean gameIsRunning = true;
        // "gameIsRunning" is an example of a game state variable. Add more as you see fit.

        // Enter the game loop. What will happen if you initialize gameIsRunning to false?
        while (gameIsRunning) {
            //gameIsRunning = false;
            if (mode==0){
                if(gui==1){
                    //StdDraw.setCanvasSize(600,600);
                    StdDraw.clear(Color.BLACK);
                    
                    //draw selection block
                    DrawPosition(xPos, yPos, blockSize);
                    
                    //draw game board
                    DrawGame(gameBoard, boardSize, blockSize);
                    
                    //draw bottom status text
                    StdDraw.setPenColor(Color.RED);
                    StdDraw.text(0.5, 0.01, gameStatusText);
                    //do for double buffering:
                    StdDraw.show();


                    //use temporary positions to validate if a move will be possible
                    int xPosNew = xPos;
                    int yPosNew = yPos;

                    //wait for keypress
                    while (!StdDraw.hasNextKeyTyped());
                    char c =  StdDraw.nextKeyTyped();
                    StdDraw.pause(100);

                        boolean isNum = false;
                        
                        //handle different keys
                        switch (c) {
                            case 'd':
                                xPosNew++;
                                break;
                            case 'a': 
                                xPosNew--;
                                break;
                            case 'w': 
                                yPosNew--;
                                break;
                            case 's': 
                                yPosNew++;
                                break;
                            case 'q': 
                                gameIsRunning = false;
                                System.exit(0);
                                break;
                            case 'x':
                                gameBoard = deleteGameRow(gameBoard, gameBoard.length, yPos, xPos);
                                break;
                            default :
                                if(isInt(c+""))
                                    isNum = true;
                                break;
                            
                        }
                        
                        //clamp values so that they dont go outside game board
                        xPosNew = clampInt(xPosNew, 0, boardSize-1);
                        yPosNew = clampInt(yPosNew, 0, boardSize-1);

                        //check if the block being moved to is open
                        if (canMove(gameBoard, xPosNew, yPosNew)){
                            xPos = xPosNew;
                            yPos = yPosNew;
                        }
                        
                        //if the key pressed is a number, a color should
                        //be assigned to that block
                        if (isNum){
                            int num = Integer.parseInt(c+"");
                            if (num == clampInt(num, 0, n-1) && gameBoard[yPos][xPos]==1){
                                gameBoard[yPos][xPos] = (byte)(num + 2);
                                if(xPos<gameBoard.length-1)
                                    gameBoard[yPos][xPos+1] = 1;
                            }
                        }              
                        
                        //we only check for blockades
                        boolean blockade = blockadeDetect(k, gameBoard);

                        //update status text
                        if(blockade)
                            gameStatusText = "You have caused a blockade!";
                        else 
                            gameStatusText = "Valid";
                                           
                    }
                 else {
                    
                    //check if game is won
                    if(getScoreAccurate(gameBoard, boardSize)==100){
                        StdOut.println("Termination: You have won!");
                        break;
                    }
                    
                    //prompt for player to enter a move
                    StdOut.print("Move: ");                

                    String sMove = "";

                    if(StdIn.isEmpty())
                        return;

                    try {
                        sMove = StdIn.readString();
                    }
                    catch(Exception e){
                        gameIsRunning = false;
                    }

                    if (!isInt(sMove))
                        continue;
                    int iMove = Integer.parseInt(sMove);

                    //check if move is valid/known
                    if((clampInt(iMove, 0, 2) != iMove)){
                        StdOut.println("Invalid move: Unknown move!");
                        continue;  
                    }
                    
                    //exit game
                    if (iMove==2){
                        StdOut.println("Termination: User terminated game!");
                        gameIsRunning=false;
                        continue;
                    }

                    String sRow, sCol;
                    int iRow, iCol;

                    //Get row to affect
                    StdOut.print("Row Number: ");
                    sRow = StdIn.readString();
                    //Is value a number?
                    if (!isInt(sRow))
                        continue;
                    iRow = Integer.parseInt(sRow);

                    //get column to affect
                    StdOut.print("Column Number: ");
                    sCol = StdIn.readString();
                    //is value a number?
                    if (!isInt(sCol))
                        continue;
                    iCol = Integer.parseInt(sCol);

                    if(iMove==0){//delete row          
                        if (isGameRowEmpty(gameBoard, boardSize, iRow)){
                            StdOut.println("Invalid move: Nothing to delete!");
                            continue;  
                        }

                        //is value valid?
                        if((clampInt(iCol, 0, boardSize-1) != iCol) || (clampInt(iRow, 0, boardSize-1) != iRow)){
                            StdOut.println("Invalid move: Outside of board!");
                            continue;  
                        }

                        gameBoard = deleteGameRow(gameBoard, boardSize, iRow, iCol);

                    } else {//place block
                        
                        StdOut.print("Color: ");
                        String sClr = StdIn.readString();
                        
                        //exit if a valid key is not entered
                        if (!isInt(sClr))
                            continue;
                        byte iClr = Byte.parseByte(sClr);

                        //is numebr valid?
                        if((clampInt(iCol, 0, boardSize-1) != iCol) || (clampInt(iRow, 0, boardSize-1) != iRow)){
                            StdOut.println("Invalid move: Outside of board!");
                            continue;  
                        }

                        //check if color is valid
                        if((clampInt(iClr, 0, n-1)!=iClr)){
                            StdOut.println("Invalid move: Unknown color!");
                            continue;
                        }

                        //check if the cell is open
                        if(gameBoard[iRow][iCol]!=1){
                            StdOut.println("Invalid move: Cell is not open!");
                            continue;
                        }

                        //assign color to gameboard
                        gameBoard[iRow][iCol] = (byte) (2 + iClr);
                        
                        //open next block if it exists
                        if(iCol<boardSize-1)
                            if(gameBoard[iRow][iCol+1]==0)
                                gameBoard[iRow][iCol+1] = 1;
                    }

                    moveCount++;
                    
                    //output game board in console
                    StdOut.println();
                    DrawGameText(gameBoard, boardSize);
                    StdOut.println();

                    //Handle blockade
                    int errorCode = moveValidator(k, gameBoard, false);

                    if(errorCode==1){
                        StdOut.println("Termination: Blockade!");
                        gameIsRunning = false;
                    }
                }
            }
            else {
                if(gui==1){
                    //clear window
                    StdDraw.clear(Color.BLACK);

                    //draw player position as block
                    DrawPosition(xPos, yPos, blockSize);
                    //draw game board
                    DrawGame(gameBoard, boardSize, blockSize);
                    
                    //show debug text
                    StdDraw.setPenColor(Color.RED);
                    StdDraw.text(0.5, 0.01, gameStatusText);
                    StdDraw.show();

                    //use temp values to validate if the next move is valid
                    int xPosNew = xPos;
                    int yPosNew = yPos;

                    //wait for input
                    while (!StdDraw.hasNextKeyTyped());
                    char c =  StdDraw.nextKeyTyped();
                    StdDraw.pause(100);

                        boolean isNum = false;
                        
                        //handle each key
                        switch (c) {
                            case 'd':
                                xPosNew++;
                                break;
                            case 'a': 
                                xPosNew--;
                                break;
                            case 'w': 
                                yPosNew--;
                                break;
                            case 's': 
                                yPosNew++;
                                break;
                            case 'q': 
                                gameIsRunning = false;
                                System.exit(0);
                                break;
                            case 'x':
                                gameBoard = deleteGameRow(gameBoard, gameBoard.length, yPos, xPos);
                                break;
                            default :
                                if(isInt(c+""))
                                    isNum = true;
                                break;
                            
                        }

                        //make sure position is not outide board
                        xPosNew = clampInt(xPosNew, 0, boardSize-1);
                        yPosNew = clampInt(yPosNew, 0, boardSize-1);

                        //check if block is open
                        if (canMove(gameBoard, xPosNew, yPosNew)){
                            xPos = xPosNew;
                            yPos = yPosNew;
                        }

                        //open next block if it is closed
                        if (isNum){
                            int num = Integer.parseInt(c+"");
                            if (num == clampInt(num, 0, n-1) && gameBoard[yPos][xPos]==1){
                                gameBoard[yPos][xPos] = (byte)(num + 2);
                                if(xPos<gameBoard.length-1)
                                    gameBoard[yPos][xPos+1] = 1;
                            }
                        }              

                        int errorCode = moveValidator(k, gameBoard, false);

                        //update game status text
                        switch (errorCode) {
                            case 1: 
                                gameStatusText = "You have caused a blockade!";
                                break;
                            case 2: 
                                gameStatusText = "You have caused a dead end!";
                                break;
                            case 3: 
                                gameStatusText = "You have caused a split!";
                                break;
                            case 4: 
                                gameStatusText = "You have won!";
                                break;
                            case 12: 
                                gameStatusText = "You have caused a blockade and a dead end!";
                                break;
                            case 13: 
                                gameStatusText = "You have caused a blockade and a split!";
                                break;
                            case 23:
                                gameStatusText = "You have caused a dead end and a split!";
                                break;
                            case 123: 
                                gameStatusText = "You have caused a blockade, a dead end and split!";
                                break;
                            case 0: 
                                if(isImpasse(gameBoard, n, k) && getBlocksLeft(gameBoard)>1)
                                    gameStatusText = "Impasse!";
                                else
                                    gameStatusText = "Valid";
                                break;
                        }                    
                    }
                 else {

                    //Check if game is won
                    if(getScoreAccurate(gameBoard, boardSize)==100){
                        StdOut.println("Termination: You have won!");
                        break;
                    }

                    //prompt for move
                    StdOut.print("Move: ");                

                    String sMove = "";

                    if(StdIn.isEmpty())
                        return;

                    try {
                        sMove = StdIn.readString();
                    }
                    catch(Exception e){
                        gameIsRunning = false;
                    }

                    if (!isInt(sMove))
                        continue;
                    int iMove = Integer.parseInt(sMove);

                    //check if move exists
                    if((clampInt(iMove, 0, 2) != iMove)){
                        StdOut.println("Invalid move: Unknown move!");
                        continue;  
                    }

                    //check if game should exit
                    if (iMove==2){
                        StdOut.println("Termination: User terminated game!");
                        gameIsRunning=false;
                        continue;
                    }

                    String sRow, sCol;
                    int iRow, iCol;

                    //Get row to affect
                    StdOut.print("Row Number: ");
                    sRow = StdIn.readString();
                    //Is value a number?
                    if (!isInt(sRow))
                        continue;
                    iRow = Integer.parseInt(sRow);

                    //get column to affect
                    StdOut.print("Column Number: ");
                    sCol = StdIn.readString();
                    //is value a number?
                    if (!isInt(sCol))
                        continue;
                    iCol = Integer.parseInt(sCol);

                    if(iMove==0){//delete row          
                        if (isGameRowEmpty(gameBoard, boardSize, iRow)){
                            StdOut.println("Invalid move: Nothing to delete!");
                            continue;  
                        }

                        //is value valid?
                        if((clampInt(iCol, 0, boardSize-1) != iCol) || (clampInt(iRow, 0, boardSize-1) != iRow)){
                            StdOut.println("Invalid move: Outside of board!");
                            continue;  
                        }

                        gameBoard = deleteGameRow(gameBoard, boardSize, iRow, iCol);

                    } else {//place block
                        StdOut.print("Color: ");
                        String sClr = StdIn.readString();
                        
                        //check if number
                        if (!isInt(sClr))
                            continue;
                        byte iClr = Byte.parseByte(sClr);

                        //is value valid?
                        if((clampInt(iCol, 0, boardSize-1) != iCol) || (clampInt(iRow, 0, boardSize-1) != iRow)){
                            StdOut.println("Invalid move: Outside of board!");
                            continue;  
                        }

                        //check if color exists
                        if((clampInt(iClr, 0, n-1)!=iClr)){
                            StdOut.println("Invalid move: Unknown color!");
                            continue;
                        }

                        //check if cell is open
                        if(gameBoard[iRow][iCol]!=1){
                            StdOut.println("Invalid move: Cell is not open!");
                            continue;
                        }

                        //open next block if it is closed
                        gameBoard[iRow][iCol] = (byte) (2 + iClr);
                        if(iCol<boardSize-1)
                            if(gameBoard[iRow][iCol+1]==0)
                                gameBoard[iRow][iCol+1] = 1;
                    }

                    moveCount++;

                    //draw game as text in console
                    StdOut.println();
                    DrawGameText(gameBoard, boardSize);
                    StdOut.println();

                    int errorCode = moveValidator(k, gameBoard, false);

                    //handle errors and terminate game
                    switch (errorCode) {
                        case 1: 
                            StdOut.println("Termination: You have caused a blockade!");
                            gameIsRunning=false;
                            break;
                        case 2: 
                            StdOut.println("Termination: You have caused a dead end!");
                            gameIsRunning=false;
                            break;
                        case 3: 
                            StdOut.println("Termination: You have caused a split!");
                            gameIsRunning=false;
                            break;
                        case 4:
                            StdOut.println("Termination: You have won!");
                            gameIsRunning = false;
                            break;
                        case 12:
                            StdOut.println("Termination: You have caused a blockade and a dead end!");
                            gameIsRunning = false;
                            break;
                        case 13:
                            StdOut.println("Termination: You have caused a blockade and a split!");
                            gameIsRunning = false;
                            break;
                        case 23:
                            StdOut.println("Termination: You have caused a dead end and a split!");
                            gameIsRunning = false;
                            break;
                        case 123:
                            StdOut.println("Termination: You have caused a blockade, a dead end and split!");
                            gameIsRunning = false;
                            break;    
                        case 0:
                            if(isImpasse(gameBoard, n, k) && getBlocksLeft(gameBoard)>1){
                                StdOut.println("Termination: Impasse!" );
                                gameIsRunning = false;
                            }
                            break;
                    }


                }
            }
        }
        // What will happen if you remove the "gameIsRunning = false" statement inside the While loop? 

        // After the game is concluded, report the score by printing the state of the game to the terminal. 
        StdOut.println("Score: " + getScore(gameBoard, boardSize) + "%");
        StdOut.println("Moves: " + moveCount);
        StdOut.println("Game ended!");
    }
    
    // For the second hand-in, you must use functions effectively wherever possible. Put these functions here.
    
    //Summary:
    //Loop through all open blocks
    //and check if a valid move can be made.
    //If a valid move exists, return false.
    private static boolean isImpasse(byte[][] gameBoard, int n, int k){
        int boardSize = gameBoard.length;
        
        boolean impasse = true;
        for (int y = 0; y<boardSize; y++){
            for (int x = 0; x<boardSize; x++){
                
                if (gameBoard[y][x]==1){
                    
                    if (x==0)
                        return false;
                    
                    for (byte i = 2; i<n+2; i++){
                        gameBoard[y][x] = i;
                        if (x<boardSize-1)
                            gameBoard[y][x+1] = 0;
                        
                        int result = moveValidator(k, gameBoard, false);
                        
                        if (result==0){
                            impasse = false;
                            gameBoard[y][x] = 1;
                            if (x<boardSize-1)
                                gameBoard[y][x+1] = 0;
                            
                            return false;
                        }
                    }
                    
                    gameBoard[y][x] = 1;
                    if (x<boardSize-1)
                        gameBoard[y][x+1] = 0;
                }
            }
        }
        return impasse;
    }
    
    //Summary:
    //Delete row starting at column col
    private static byte[][] deleteGameRow(byte[][] gameBoard, int boardSize, int row, int col){
        for (int i = col; i<boardSize; i++){
            gameBoard[row][i] = 0;
            
            if(i==0 || i==col)
                gameBoard[row][i]=1;
        }
        
        return gameBoard;
    }
    
    //Summary:
    //Get rounded score for use in gui and console game end message
    private static int getScore(byte[][] gameBoard, int boardSize){
        int x, y, c = 0;
        
        for(y = 0; y<boardSize; y++){
            for(x = 0; x<boardSize; x++){
                if(gameBoard[y][x]>1)
                    c++;
            }
        }
        
        return (int)Math.round(((float)c/(boardSize*boardSize))*100);
    }
    
    //Summary:
    //Get accurate score to see if the game is won
    private static float getScoreAccurate(byte[][] gameBoard, int boardSize){
        int x, y, c = 0;
        
        for(y = 0; y<boardSize; y++){
            for(x = 0; x<boardSize; x++){
                if(gameBoard[y][x]>1)
                    c++;
            }
        }
        
        return ((float)c/(boardSize*boardSize))*100;
    }
    
    //Summary:
    //Couns how many blocks there still are before the game is won
    private static int getBlocksLeft(byte[][] gameBoard){
        int x, y, c = 0;
        
        for(y = 0; y<gameBoard.length; y++){
            for(x = 0; x<gameBoard.length; x++){
                if(gameBoard[y][x]>1)
                    c++;
            }
        }
        
        return gameBoard.length * gameBoard.length - c;
    }
    
    //Summary:
    //Checks if delete will be ineffective
    private static boolean isGameRowEmpty(byte[][] gameBoard, int boardSize, int row){
        for(int i = 0; i<boardSize; i++){
            if (gameBoard[row][i]>1)
                return false;
        }
        
        return true;
    }
    
    //Summary:
    //Check if a string can be converted to an integer
    private static boolean isInt(String arg){
        try{
            Integer.parseInt(arg);
            return  true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
    
    //Summary:
    //Returns value clamped between min and max
    private static int clampInt(int value, int min, int max){
        if(value < min)
            value = min;
        else if (value > max)
            value = max;
        
        return value;
    }
    
    //Summary:
    //Check if the board is not closed at xPos, yPos
    private static boolean canMove(byte[][] gameBoard, int xPos, int yPos){
        return gameBoard[yPos][xPos] > 0;
    }
    
    //Summary:
    //Get character to be used in console from its byte value
    private static char getTileChar(byte number){
        switch (number) {
            case 0: return '*';
            case 1: return '.';
            case 2: return 'G';
            case 3: return 'Y';
            case 4: return 'R';
            case 5: return 'B';
            default: return '*';
        }
    }
    
    //Summary:
    //Get color to be used for drawing gui
    private static Color getTileColor(byte number){
        switch (number) {
            case 1: return Color.GRAY;
            case 2 : return Color.GREEN;
            case 3 : return Color.YELLOW;
            case 4 : return Color.RED;
            default: return Color.WHITE;
        }
    }
    
    //Summary:
    //Draws the game using the specified block size
    private static void DrawGame(byte[][] gameBoard, int boardSize, double blockSize){  
        
        for (int y=0; y<boardSize-1; y++) {
            for (int x=0; x<boardSize; x++){                
                StdDraw.setPenColor(getTileColor(gameBoard[y][x]));
                StdDraw.filledSquare(blockSize/2+x*blockSize, 1-blockSize/2-y*blockSize, blockSize/2-0.005);
            }
        }
    }
    
    //Summary:
    //Shows the game board in the console
    private static void DrawGameText(byte[][] gameBoard, int boardSize){
        String line;
        
        for (int y=0; y<boardSize; y++) {
            line = "";
            for (int x=0; x<boardSize; x++){                
                byte b = gameBoard[y][x];
                line+=getTileChar(b);
            }
            StdOut.println(line);
        }
    }
    
    //Draws the selection rectangle from the player position
    private static void DrawPosition(int x, int y, double blockSize){
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.filledSquare(blockSize/2+x*blockSize, 1-blockSize/2-y*blockSize, blockSize/2);
    }
    
    //Summary:
    //Checks if a blockade exists
    private static boolean blockadeDetect(int k,byte[][] gameboard){
        int same_counter = 0;
        boolean blockadeDetect = false;        
        
        for(int i = 0; i <= gameboard.length-1;i++){
             
            for (int x = 0; x <= gameboard[i].length-1;x++){
                
                if (gameboard[i][x]<2){
                    same_counter=0;
                    continue;
                } 
                
                if (x +1<= gameboard[i].length-1){
                    if ((gameboard[i][x] == gameboard[i][x+1])&(gameboard[i][x]>=2)){
                        same_counter += 1;
                    if (same_counter>=k){
                        blockadeDetect = true;
                        break;}
                    }
                    else{
                        same_counter=0;
                    }
                }
            }
           

           }
        same_counter = 0;
        for (int i = 0; i <= gameboard[0].length-1;i++){
            
            for (int x = 0; x <= gameboard.length-1;x++){
                
                if (gameboard[x][i]<2){
                    same_counter=0;
                    continue;
                } 
                
                if (x +1 <= gameboard.length-1){
                if ((gameboard[x][i] == gameboard[x+1][i])&(gameboard[x][i]>=2)){
                    same_counter += 1;
                    if (same_counter ==k-1){
                        blockadeDetect = true;
                        break; 
                }
                }
                else{
                same_counter =0;}
            }
            
        }
        
        
    }
        return blockadeDetect;
    }

    //Summary:
    //Check if a dead end exists
    private static boolean deadEndNew(byte[][] gameBoard){
        String pattern = "", line = "", lineS  ="";
        int startNum=-1, secondNum=-1;
        int boardSize = gameBoard.length;

        for(int y = 0; y<boardSize; y++){
            pattern = "";
            line = "";
            lineS = "";

            for(int x = 0; x<boardSize; x++){
                line += gameBoard[y][x];
                lineS += getTileChar(gameBoard[y][x]);
            }

            for(int x = 0; x<boardSize; x++){
                startNum = gameBoard[y][x];
                pattern += gameBoard[y][x];

                secondNum = -1;

                boolean validPatternFound = false;
                for(int z = x+1; z<boardSize; z++){
                    pattern += gameBoard[y][z];
                    if(secondNum==-1){
                        secondNum = gameBoard[y][z];
                        if(secondNum==startNum)
                            break;
                    }
                    else{
                        if(startNum==gameBoard[y][z]){
                            validPatternFound = true;
                            break;
                        }
                    }
                }

                if(validPatternFound){
                    int count = 0;

                    String patternDouble = "";
                    for(int i = 0; i<pattern.length(); i++){
                        char c = pattern.charAt(i);
                        patternDouble += c;
                        patternDouble += c;
                    }

                    String line1 = line.replace(pattern, "*");
                    String line2 = line.replace(patternDouble, "*");

                    String comb = line1+line2;

                    for (int i = 0; i < comb.length(); i++)
                        if (comb.charAt(i)=='*')
                            count++;

                    if(count>1){
                        return true;
                    }
                }

                for (byte i = 2; i<5; i++)
                    if (line.contains(i+""+i+""+i))
                        return true;

            }
        }

        return false;
    }

    //Summary:
    //Check if a split exists
    private static boolean splitDetect(byte[][] gameBoard){

        int boardSize = gameBoard.length;

        for(int y = 0; y<boardSize-1; y++)
        {
            for (int i = y+1; i<boardSize; i++){

                if(gameBoard[i][0]==1 && gameBoard[i][1]==0)
                    continue;

                boolean diff = false;
                for (int z = 0; z<boardSize; z++){
                    if(gameBoard[y][z]<2){
                        diff=true;
                        break;
                    }
                    else if (gameBoard[y][z]!=gameBoard[i][z])
                        diff = true;
                }

                if(diff==false)
                    return true;
            }
        }

        return false;
    }

    public static int moveValidator(int k, byte[][] gameboard, boolean self_solver){
        boolean blockade = false;
        boolean dead_end = false;
        boolean split = false;
        int error_code = 0;
        blockade = blockadeDetect(k,gameboard);
        dead_end = deadEndNew(gameboard);
        split = splitDetect(gameboard);

         if ((blockade)&(dead_end)&(split))
             error_code = 123;
        else if((blockade)&(dead_end))
            error_code = 12;
        else if ((blockade)&(split))
            error_code = 13;
        else if((dead_end)&(split))
            error_code = 23;
        else if (blockade)
           error_code = 1;
        else if (dead_end)
           error_code = 2;
        else if (split)
           error_code = 3;

        return error_code;
    }

}