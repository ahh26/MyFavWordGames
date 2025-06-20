import java.util.ArrayList;
public class WordMatcher{
    //both wordSearch and hidden words use this class

    public static int getMatchedWordIndex(ArrayList<WordCell> selectedCells,ArrayList<Integer[]>wordPosition){
        for(int w=0;w<wordPosition.size();w++){
            Integer[] pos=wordPosition.get(w);
            if(pos.length/2!=selectedCells.size())continue; // if the length of word doesnt match, just skip
            
            boolean match=true;
            for(int i=0;i<selectedCells.size();i++){
                WordCell cell=selectedCells.get(i);
                int row=pos[i*2];
                int col=pos[i*2+1];
                if(cell.getRow()!=row||cell.getCol()!=col){
                    match=false;
                    break;
                }
            }
            if(match)return w;

            match=true; //do a comparison again backwards
            for(int i=selectedCells.size()-1;i>=0;i--){
                WordCell cell=selectedCells.get(i);
                int row=pos[(selectedCells.size()-1-i)*2];
                int col=pos[(selectedCells.size()-1-i)*2+1];
                if(cell.getRow()!=row||cell.getCol()!=col){
                    match=false;
                    break;
                }
            }
            if(match)return w;
        }
        return -1; //return -1 if not found
    }
    
}
