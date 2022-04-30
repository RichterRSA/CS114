
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
        
        int mode = 0, gui = 1, n = 2, k = 3;
        
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
            if (isInt(args[0]))
                mode = Integer.parseInt(args[0]);
            else
                StdOut.println("First input reset to default.");
            
            if (isInt(args[1]))
                gui = Integer.parseInt(args[1]);
            else
                StdOut.println("Second input reset to default.");
            
            if (isInt(args[2]))
                n = Integer.parseInt(args[2]);
            else
                StdOut.println("Third input reset to default.");
            
            if (isInt(args[3]))
                k = Integer.parseInt(args[3]);
            else
                StdOut.println("Fourth input reset to default.");
        }
        
        //game board setup
        int boardSize = (int)(58*Math.pow(n-1, 3) - 310*Math.pow(n-1, 2) + 546*(n-1) - 286);//formula for game size 
        
        StdOut.println("The dimension of your board is: " + boardSize + "x" + boardSize);
        StdOut.println("The length of a blockade is: " + k);
        
        int xPos = 0, yPos = 0;
        
        //Colors:
        //0 = white (inactive)
        //1 = gray (active)
        //2+ = yellow, green, red ... 
        byte[][] gameBoard = new byte[boardSize][boardSize];
        
        double blockSize=1.0/boardSize;
        
        if(gui==1){
            StdDraw.enableDoubleBuffering();
            StdDraw.clear(Color.BLACK);
        }
       
        //set game board default values & drawing
        for (int y=0; y<boardSize; y++) {
            for (int x=0; x<boardSize; x++){
                //make first column active and everything else inactive
                gameBoard[y][x] = (byte) ((x==0)?1:0); 
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
                
                if (c=='D')
                    xPosNew++;
                else if (c=='A')
                    xPosNew--;
                else if (c=='W')
                    yPosNew--;
                else if (c=='S')
                    yPosNew++;
                else if (c=='Q')
                    gameIsRunning = false;
                
                xPosNew = clampInt(xPosNew, 0, boardSize-1);
                yPosNew = clampInt(yPosNew, 0, boardSize-1);
                      
                if (canMove(gameBoard, xPosNew, yPosNew)){
                    xPos = xPosNew;
                    yPos = yPosNew;
                }
            }
            }
            else{
                DrawGameText(gameBoard, boardSize);
            }
        }
        // What will happen if you remove the "gameIsRunning = false" statement inside the While loop? 

        // After the game is concluded, report the score by printing the state of the game to the terminal. 
        StdOut.println("Game ended!");
    }
    
    // For the second hand-in, you must use functions effectively wherever possible. Put these functions here.
    
    private static boolean isInt(String arg){
        try{
            Integer.parseInt(arg);
            return  true;
        }
        catch(Exception e){
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
        if (gameBoard[yPos][xPos] > 0)
            return true;
        return false;
    }
    
    private static Color getTileColor(byte number){
        switch (number) {
            case 1:
                return Color.GRAY;
            default:
                return Color.WHITE;
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
                if (b==0) //Closed
                    line += '*';
                else if (b==1) //Open
                    line += '.';
            }
            StdOut.println(line);
        }
    }
    
    private static void DrawPosition(int x, int y, double blockSize){
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.filledSquare(blockSize/2+x*blockSize, 1-blockSize/2-y*blockSize, blockSize/2);
    }
    
    public static boolean blockade_detect(int k,int[][] gameboard){
        int same_counter = 0;
        boolean blockade_detect = false;

        
        for(int i = 0; i <= gameboard.length-1;i++){
            //System.out.println(i);
            for (int x = 0; x <= gameboard[i].length-1;x++){
                //System.out.println(gameboard[i][x]);
                if (x +1<= gameboard[i].length-1){
                if (gameboard[i][x] == gameboard[i][x+1]){
                    same_counter += 1;
                if (same_counter ==k){
                    blockade_detect = true;
                    break;
            }
                }
            }
            }
           

           }
        same_counter = 0;
        for (int i = 0; i <= gameboard[0].length-1;i++){
            //System.out.println(i);
            for (int x = 0; x <= gameboard.length-1;x++){
                
                if (x +1 <= gameboard.length-1){
                    //System.out.println(gameboard[x][i]+"to "+gameboard[x+1][i]);
                if (gameboard[x][i] == gameboard[x+1][i]){
                    same_counter += 1;
                    System.out.println("s"+same_counter);
                    if (same_counter ==k){
                        blockade_detect = true;
                        break;
                   
                }
                }
            }
            
        }
        
        
    }
        return blockade_detect;
    }


public static boolean dead_end_detect(int[][] gameboard){
    String brace_pattern = "";
    int brace_start = 0;
    int same_counter =0;
    int q = 0;
    boolean brace_found = false;
    boolean start_bool = true;
    String brace_pattern_current = "";
    boolean bool_inside_different = false;
    
    
    //Loop through rows
    for(int i = 0;i <= gameboard[0].length-1;i++){
        System.out.println("i" +i);
        brace_start = gameboard[0][i];
        brace_pattern = Integer.toString(brace_start);
        //Loop through all columns for each row
        for(int x = 0;x <= gameboard.length-1;x++){
            
            brace_start = gameboard[x][i];
            if ((gameboard[x][i] == brace_start)&(x>0)&(brace_pattern.length()>=3)){   
                
                System.out.println("Brace:"+brace_pattern);
                
                    for (int z = x;z<=gameboard.length-1;z++){
                       
                        brace_pattern_current =brace_pattern.charAt(q)+"";

                        if (Integer.parseInt(brace_pattern_current) == gameboard[z][i]){
                            if ((gameboard[z][i]!=brace_start)&(q>0)&(q<brace_pattern.length()-1)){
                                bool_inside_different = true;}
                            same_counter +=1;}
                        
                        else{
                        same_counter = 0;}
                        q+=1;
                        
                        if (q==brace_pattern.length()){
                            break;}}
                        q = 0;
                        
                    if ((same_counter==brace_pattern.length())&(bool_inside_different)){
                           brace_found = true;
                           same_counter=0;
                           bool_inside_different = false;
                            break;}
                    
            }  
        else{
            if (start_bool == false){
            brace_pattern = brace_pattern + Integer.toString(gameboard[i][x]);
            }
            start_bool = false;
            }
            
        same_counter = 0;
        }

        
    }

return brace_found;
}


public static boolean split_detect(int[][] gameboard){
    
    boolean split_found = true;
    for (int i = 0;i<=gameboard[0].length-1;i++){
        
        if ((split_found == true)&(i !=0)){
            break;}
        
        for(int x =0;x<=gameboard.length-1;x++){
            if(i+1<=gameboard[0].length-1){
                System.out.println(gameboard[x][i]+"to "+gameboard[x][i+1]);
            if (gameboard[x][i]!= gameboard[x][i+1]){
            split_found = false;
                System.out.println("false");
            break;}}}   
    }
    return split_found;
}




}

