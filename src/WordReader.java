import java.io.*;
import java.util.*;
public class WordReader {
    public static ArrayList<String> readWordsFromFile(String fileName){
        ArrayList<String> words=new ArrayList<>();
        FileReader in;
        BufferedReader readFile;
        String line;
        try{
            in=new FileReader(fileName);
            readFile=new BufferedReader(in);
            while((line=readFile.readLine())!=null){
                words.add(line.toUpperCase().trim());
            }
        }catch(IOException e){
            System.err.println("IOException: "+e.getMessage());
        }
        return words;
    }
}
