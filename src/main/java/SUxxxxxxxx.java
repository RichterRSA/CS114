
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
        //get game mode (0=first hand in, 1=second, 2=automatic solver)
        //change
        int mode = 0, gui = 0, n = 2, k = 3;
        int boardSize;
        
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
            
            boardSize = (int)(58*Math.pow(n-1, 3) - 310*Math.pow(n-1, 2) + 546*(n-1) - 286);//formula for game size 
            
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
                
        StdOut.println("The dimension of your board is: " + boardSize + "x" + boardSize);
        StdOut.println("The length of a blockade is: " + k);
        StdOut.println();
        
        int xPos = 0, yPos = 0, moveCount = 0;
        
        //Colors:
        //0 = white (inactive)
        //1 = gray (active)
        //2+ = yellow, green, red ... 
        byte[][] gameBoard = new byte[boardSize][boardSize];
        
        double blockSize=1.0/boardSize;
        
       
        //set game board default values & drawing
        for (int y=0; y<boardSize; y++) {
            for (int x=0; x<boardSize; x++){
                //make first column active and everything else inactive
                gameBoard[y][x] = (byte) ((x==0)?1:0); 
            }
        }
        
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
            
            if(gui==1){
                StdDraw.clear(Color.BLACK);
                DrawPosition(xPos, yPos, blockSize);
                DrawGame(gameBoard, boardSize, blockSize);
                StdDraw.show();
                
                            //Input
                if(StdIn.hasNextLine()){
                    char c = Character.toUpperCase(StdIn.readChar());
                    int xPosNew = xPos;
                    int yPosNew = yPos;

                    switch (c) {
                        case 'D': xPosNew++;
                        case 'A': xPosNew--;
                        case 'W': yPosNew--;
                        case 'S': yPosNew++;
                        case 'Q': gameIsRunning = false;
                    }

                    xPosNew = clampInt(xPosNew, 0, boardSize-1);
                    yPosNew = clampInt(yPosNew, 0, boardSize-1);

                    if (canMove(gameBoard, xPosNew, yPosNew)){
                        xPos = xPosNew;
                        yPos = yPosNew;
                    }
                }
            } else {
                
                if(!StdIn.isEmpty())
                    StdOut.print("Move: ");                
                
                String sMove = "";
                
                try {
                    sMove = StdIn.readString();
                }
                catch(Exception e){
                    if(getScore(gameBoard, boardSize)==100){
                        StdOut.println("Termination: You have won!");
                    }
                    gameIsRunning = false;
                }
                
                if (!isInt(sMove))
                    continue;
                int iMove = Integer.parseInt(sMove);
                
                if((clampInt(iMove, 0, 2) != iMove)){
                    StdOut.println("Invalid move: Unknown move!");
                    continue;  
                }
                
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
                    if (isGameRowEmpty(gameBoard, boardSize, iCol)){
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
                    if (!isInt(sClr))
                        continue;
                    byte iClr = Byte.parseByte(sClr);
                    
                                    //is value valid?
                    if((clampInt(iCol, 0, boardSize-1) != iCol) || (clampInt(iRow, 0, boardSize-1) != iRow)){
                        StdOut.println("Invalid move: Outside of board!");
                        continue;  
                    }
                    
                    if((clampInt(iClr, 0, n-1)!=iClr)){
                        StdOut.println("Invalid move: Unknown color!");
                        continue;
                    }
                    
                    if(gameBoard[iRow][iCol]!=1){
                        StdOut.println("Invalid move: Cell is not open!");
                        continue;
                    }
                    
                    gameBoard[iRow][iCol] = (byte) (2 + iClr);
                    if(iCol<boardSize-1)
                        if(gameBoard[iRow][iCol+1]==0)
                            gameBoard[iRow][iCol+1] = 1;
                }
                
                moveCount++;
                
                StdOut.println();
                DrawGameText(gameBoard, boardSize);
                StdOut.println();
                
                int errorCode = move_validator_new(k, gameBoard, false);
                
                switch (errorCode) {
                    case 1: 
                        StdOut.println("Termination: Blockade!");
                        gameIsRunning=false;
                        break;
                    case 2: 
                        StdOut.println("Termination: Dead end!");
                        gameIsRunning=false;
                        break;
                    case 3: 
                        StdOut.println("Termination: Split!");
                        gameIsRunning=false;
                        break;
                    case 4:
                        StdOut.println("Termination: You have won!");
                        gameIsRunning = false;
                        break;
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
    
    private static byte[][] deleteGameRow(byte[][] gameBoard, int boardSize, int row, int col){
        for (int i = col; i<boardSize; i++){
            gameBoard[row][i] = 0;
            
            if(i==0)
                gameBoard[row][i]=1;
        }
        
        return gameBoard;
    }
    
    private static int getScore(byte[][] gameBoard, int boardSize){
        int x, y, c = 0;
        
        for(y = 0; y<boardSize; y++){
            for(x = 0; x<boardSize; x++){
                if(gameBoard[y][x]>=2)
                    c++;
            }
        }
        
        return (int)Math.round(((float)c/(boardSize*boardSize))*100);
     
    }
    
    private static boolean isGameRowEmpty(byte[][] gameBoard, int boardSize, int row){
        for(int i = 0; i<boardSize; i++){
            if (gameBoard[row][i]>1)
                return false;
        }
        
        return true;
    }
    
    private static boolean isInt(String arg){
        try{
            Integer.parseInt(arg);
            return  true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
    
    //returns value clamped between min and max
    private static int clampInt(int value, int min, int max){
        if(value < min)
            value = min;
        else if (value > max)
            value = max;
        
        return value;
    }
    
    private static boolean canMove(byte[][] gameBoard, int xPos, int yPos){
        return gameBoard[yPos][xPos] > 0;
    }
    
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
    
    private static Color getTileColor(byte number){
        switch (number) {
            case 1: return Color.GRAY;
            default: return Color.WHITE;
        }
    }
    
    private static void DrawGame(byte[][] gameBoard, int boardSize, double blockSize){        
        for (int y=0; y<boardSize; y++) {
            for (int x=0; x<boardSize; x++){                
                StdDraw.setPenColor(getTileColor(gameBoard[y][x]));
                StdDraw.filledSquare(blockSize/2+x*blockSize, 1-blockSize/2-y*blockSize, blockSize/2-0.005);
            }
        }
    }
    
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
    
    private static void DrawPosition(int x, int y, double blockSize){
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.filledSquare(blockSize/2+x*blockSize, 1-blockSize/2-y*blockSize, blockSize/2);
    }
    
    public static boolean blockade_detect(int k,byte[][] gameboard){
        int same_counter = 0;
        boolean blockade_detect = false;

        
        
        
        for(int i = 0; i <= gameboard.length-1;i++){
            //System.out.println(i);
             
            for (int x = 0; x <= gameboard[i].length-1;x++){
                //System.out.println(gameboard[i][x]);
                
                if (gameboard[i][x]<2){
                    same_counter=0;
                    continue;
                } 
                
                if (x +1<= gameboard[i].length-1){
                    //System.out.println(gameboard[i][x]);
                if ((gameboard[i][x] == gameboard[i][x+1])&(gameboard[i][x]>=2)){
                    same_counter += 1;
                if (same_counter ==k-1){
                    blockade_detect = true;
                    break;}}
                else{
                same_counter=0;}
                }
            }
           

           }
        
        
     
        same_counter = 0;
        for (int i = 0; i <= gameboard[0].length-1;i++){
            //System.out.println(i);
            for (int x = 0; x <= gameboard.length-1;x++){
                
                if (gameboard[x][i]<2){
                    same_counter=0;
                    continue;
                } 
                
                if (x +1 <= gameboard.length-1){
                    //System.out.println(gameboard[x][i]+"to "+gameboard[x+1][i]);
                if ((gameboard[x][i] == gameboard[x+1][i])&(gameboard[x][i]>=2)){
                    same_counter += 1;
                    //System.out.println("s"+same_counter);
                    if (same_counter ==k-1){
                        blockade_detect = true;
                        break; 
                }
                }
                else{
                same_counter =0;}
            }
            
        }
        
        
    }
        return blockade_detect;
    }


public static boolean dead_end_detect(byte[][] gameboard){
    String brace_pattern = "";
    int brace_start = 0;
    int same_counter =0;
    int q = 0;
    boolean brace_found = false;
    boolean start_bool = true;
    String brace_pattern_current = "";
    boolean bool_inside_different = false;
    
    
    //Loop through rows
    for(int i = 0;i <= gameboard.length-1;i++){
        //System.out.println("i" +i);
        brace_start = gameboard[i][0];
        brace_pattern = Integer.toString(brace_start);
        //Loop through all columns for each row
        for(int x = 0;x <= gameboard[i].length-1;x++){
            
            //brace_start = gameboard[i][x];
            //System.out.println(brace_start+"to: "+gameboard[i][x]);
            if ((gameboard[i][x] == brace_start)&(x>0)&(brace_pattern.length()>=3)&(gameboard[i][x]>=2)&(bool_inside_different)){   
                q = 0;
                
                //System.out.println("Brace:"+brace_pattern);
                
                    for (int z = x;z<=gameboard.length-1;z++){
                        //System.out.println("z: "+z);
                        
                        brace_pattern_current =brace_pattern.charAt(q)+"";
                        //System.out.println(brace_pattern_current+"to "+gameboard[i][z]);
                        if (Integer.parseInt(brace_pattern_current) == gameboard[i][z]){

                        same_counter += 1; 
                        q +=1;}
                        
                        else{
                        same_counter = 0;
                        q+=1;}
                        
                        
                        if (q==brace_pattern.length()){
                            break;}
                    if (z==brace_pattern.length()-1){break;}}
//                        q = 0;
                        
                    if ((same_counter==brace_pattern.length())){
                           brace_found = true;
                           same_counter=0;
                           bool_inside_different = false;
                            break;}
                    
            }  
        else{
            if ((start_bool == false)){
            brace_pattern = brace_pattern + Integer.toString(gameboard[i][x]);
               // System.out.println("BraceP: "+brace_pattern);
            }
            start_bool = false;
            }
        if (brace_found){
        break;}
        if ((gameboard[i][x]!=brace_start)&(x>0)){
        bool_inside_different=true;}
            
        same_counter = 0;
        }

        
    }

return brace_found;
}


public static boolean split_detect(byte[][] gameboard){
    
    boolean split_found = true;
    // loop through rows of gameboard
    for (int i = 0;i<=gameboard[0].length-1;i++){
        //check if a split row has been found in previous iteration and terminate in case of...
        if ((split_found == true)&(i !=0)){
            break;}
        //loop through columns of gameboard
        for(int x =0;x<=gameboard.length-1;x++){
            //if to make sure that indexes out of array range are not referenced in code below
            if(i+1<=gameboard[0].length-1){
                //System.out.println(gameboard[x][i]+"to "+gameboard[x][i+1]);
            //if condition to disqualify a row as a split
            if (gameboard[x][i]!= gameboard[x][i+1]){
            split_found = false;
                //System.out.println("false");
            break;}}}   
    }
    return split_found;
}

public static String move_validator(int k, byte[][] gameboard, boolean self_solver){
 boolean blockade = false;
 boolean dead_end = false;
 boolean split = false;
 boolean termination = false;
 String error_message = "";
 blockade = blockade_detect(k,gameboard);
 dead_end = dead_end_detect(gameboard);
 split = split_detect(gameboard);
 if ((blockade)&(dead_end)&(split)){
     error_message = "Termination: You have caused a blockade, a dead end and split!";
    termination = true;}
     
 else if((blockade)&(dead_end)){
     error_message = "Termination: You have caused a blockade and a dead end!!";
    termination = true;}
 else if ((blockade)&(split)){
    error_message = "Termination: You have caused a blockade and a split!";
    termination = true;}
 else if((dead_end)&(split)){
    error_message = "Termination: You have caused a dead end and a split!";
    termination = true;}
 else if(blockade){
    error_message = "Termination: You have caused a blockade!";
    termination = true;}
 else if (dead_end){
    error_message = "Termination: You have caused a dead end!";
    termination = true;}
 else if (split){
    error_message = "Termination: You have caused a split!";
    termination = true;}
 
if (self_solver == false){
    return error_message;}
else if ((self_solver)&(termination)){
    return "true";}
else{
return "false";}
}

public static int move_validator_new(int k, byte[][] gameboard, boolean self_solver){
    boolean blockade = false;
    boolean dead_end = false;
    boolean split = false;
    int error_code = 0;
    blockade = blockade_detect(k,gameboard);
    dead_end = dead_end_detect(gameboard);
    split = split_detect(gameboard);

    if (blockade)
       error_code = 1;
    else if (dead_end)
       error_code = 2;
    else if (split)
       error_code = 3;
 
    return error_code;
}


public static int[] self_solver(byte[][] gameBoard, boolean termination,int[] currentPos, byte[] colours,int k,boolean play){
//    int[] pos1 = {currentPos[0],currentPos[1]-1};
//    int[] pos2 = {currentPos[0],currentPos[1]+1};
//    int[] pos3 = {currentPos[0]-1,currentPos[1]};
//    int[] pos4 = {currentPos[0]+1,currentPos[1]};
    int[][] possible_positions = {{currentPos[0],currentPos[1]-1},{currentPos[0],currentPos[1]+1},{currentPos[0]-1,currentPos[1]},{currentPos[0]+1,currentPos[1]}};
    int[] new_pos = {-1,-1};//{-1,-1} will be recognized as a error code from the method that called. This error code will signal that there is no further moves to be made.
    String valid_move = "";
    boolean move_possible = false;
    
    
        for (int i =0;i<=colours.length-1;i++){
            if (gameBoard[possible_positions[i][0]][possible_positions[i][1]]==0){
                if ((colours[i] != 0)&(colours[i]!=1))
                    gameBoard[possible_positions[i][0]][possible_positions[i][1]] = colours[i]; 
                valid_move = move_validator(3,gameBoard,true);
                if (valid_move == "true"){
                    new_pos = possible_positions[i];
                    break;}
            
            
        }
        
        }
        

        return new_pos;


    }
    
    
        

















}
        

