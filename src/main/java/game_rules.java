/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author ljlpr
 */
public class game_rules {
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
        split_found = true;
        for(int x =0;x<=gameboard.length-1;x++){
            if(i+1<=gameboard[i].length-1){
            if (gameboard[i][x]!= gameboard[i+1][x]){
            split_found = false;}
        }}
        
        
        
    }
    return split_found;
}












}

    
    

   


