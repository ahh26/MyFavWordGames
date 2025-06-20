import java.util.*;
public class WordGridGenerator {
    private final int size;
    private String[] wordList;
    private ArrayList<Integer[]> wordPosition=new ArrayList<>();
    private char[][] grid;
    private final char[] alphabet="abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    public WordGridGenerator(int size,String[] wordList){
        this.size=size;
        this.wordList=wordList;
        this.grid=new char[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                grid[i][j]=' ';
            }
        }
        generateGrid();
    }

   public void generateGrid(){
        int x,y,startx,starty,endx,endy,positionx,positiony;
        boolean again;
        for(String word:wordList){
            boolean bad=true; //if in incorrect position
            int[] direction={0,1,-1};
            while(bad){ //first check if the word can fit into the word grid
                while(true){
                    x=direction[(int)(Math.random()*3)];
                    y=direction[(int)(Math.random()*3)];
                    if(x==0&&y==0)continue;
                    break;
                }
                startx=(int)(Math.random()*size);
                starty=(int)(Math.random()*size);
                endx=startx+word.length()*x;
                endy=starty+word.length()*y;
                if(endx<0||endx>=size||endy<0||endy>=size)continue;
                again=false; //again=true ->unable to put in the word, generate again
                for(int j=0;j<word.length();j++){ // now check if the word can rly be put into the grid (if there's free space or the same letter)
                    positionx=startx+j*x;
                    positiony=starty+j*y;
                    char a=grid[positionx][positiony];
                    if(a!=' '){
                        if(a==word.charAt(j))continue;
                        else{
                            again=true;
                            break;
                        }
                    }
                }
                if(again)continue;
                else{//if the word can be put into the grid
                    Integer[] temp=new Integer[word.length()*2];
                    int index=0;
                    for(int j=0;j<word.length();j++){
                        grid[startx+j*x][starty+j*y]=word.charAt(j);
                        temp[index++]=startx+j*x;
                        temp[index++]=starty+j*y;
                    }
                    bad=false;
                    wordPosition.add(temp);
                }   
            }
        }
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(grid[i][j]==' '){
                    grid[i][j]=alphabet[(int)(Math.random()*26)];
                }
            }
        }
   }
   public char[][] getGrid(){
    return grid;
   }
   public ArrayList<Integer[]> getWordPosition(){
    return wordPosition;
   }
}
