import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class WordleGame extends JFrame {

    class WordPanel extends JPanel{
        JLabel[] wordColumns=new JLabel[wordLength];
        public WordPanel(){
            this.setLayout(new GridLayout(1,wordLength));
            for(int i=0;i<wordLength;i++){
                wordColumns[i]=new JLabel();
                wordColumns[i].setHorizontalAlignment(JLabel.CENTER);
                wordColumns[i].setOpaque(true);
                wordColumns[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); 
                this.add(wordColumns[i]);
            }
        }
        public void setPanelText(String letter,int position,Color color){
            this.wordColumns[position].setText(letter);
            this.wordColumns[position].setForeground(Color.WHITE);
            this.wordColumns[position].setBackground(color);
            this.wordColumns[position].setFont(new Font("Franklin Gothic", Font.BOLD, 16)); 
        }
    }
    class UserPanel extends JPanel{
        private JTextField userInput;
        private JButton confirmButton;
        public UserPanel(){
            this.setLayout(new GridLayout(1,2));
            userInput=new JTextField();

            confirmButton=new JButton("Confirm");
            confirmButton.setFocusable(false);

            

            this.add(userInput);
            this.add(confirmButton);
        }
        public JTextField getUserInput(){
            return userInput;
        }
        public JButton getConfirmButton(){
            return confirmButton;
        }

    }
    
        

    
    private WordPanel[] wordPanelArray=new WordPanel[6];
    private UserPanel userPanel;
    private String word;
    private int wordLength;
    private int count=0;

    private WordleMenu wordlemenu;

    public WordleGame(int wordLength,WordleMenu wordlemenu){
        this.wordlemenu = wordlemenu;
        this.wordLength=wordLength;
        setTitle("Wordle");
        setSize(400,400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        startGame();
        
        
    }
    
    public void startGame(){  
        this.word=getWordleWord(wordLength);

        //testing!!delete
        System.out.println(word);
        

        setLayout(new GridLayout(8,1));
        setSize(400,400);
        setLocationRelativeTo(null);

        for(int i=0;i<6;i++){
            wordPanelArray[i]=new WordPanel();
            add(wordPanelArray[i]);
        }

        //user panel
        userPanel=new UserPanel();
        add(userPanel);
        userPanel.getConfirmButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                checkUserInput();
            }
        });
        userPanel.getUserInput().addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                    checkUserInput();
                }
            }
        });

        //quit button
        JButton quitWordle=new JButton("Exit"); //change text
        quitWordle.setFocusable(false);
        add(quitWordle);
        quitWordle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setVisible(false); 
                wordlemenu.showMainMenu(); 
            }
        });
        revalidate();
        repaint();
        setVisible(true);
    }

    public String getWordleWord(int WordLength){
        ArrayList<String> words=WordReader.readWordsFromFile("textFiles/Letter"+wordLength+".txt");
        Random random=new Random();
        return words.get(random.nextInt(words.size()));
    }

    
    public void checkUserInput(){
        String userGuess = userPanel.getUserInput().getText().toUpperCase();
        userPanel.getUserInput().setText("");
        if(userGuess.trim().length()==wordLength) {
            if(isCorrectWord(userGuess.trim())){
                int choice=JOptionPane.showOptionDialog(null, "Congratulations! You got the wordle in "+count+"! The word is: "+word, "Yeahhh!", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,null,new Object[]{"Play again","Choose another word length","Exit"},"Play again");
                dispose();
                if(choice==0){
                    new WordleGame(wordLength, wordlemenu);
                }else if(choice==1){
                    wordlemenu.choosingLevel();
                }else{
                    wordlemenu.showMainMenu();
                }

                return;
            }
            if(count==6){
                int choice=JOptionPane.showOptionDialog(null, "You lost:( The word is: "+word, "Oopsies!", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,null,new Object[]{"Play again","Choose another word length","Exit"},"Play again");
                dispose();
                if(choice==0){
                    new WordleGame(wordLength, wordlemenu);
                }else if(choice==1){
                    wordlemenu.choosingLevel();
                }else{
                    wordlemenu.showMainMenu();
                }
                return;
            }
        }else{
            JOptionPane.showMessageDialog(null,"Guess must be "+wordLength+" letters long.");
        }
    }

    public boolean isCorrectWord(String guess){
        ArrayList<String> wordleWordList = new ArrayList<>(Arrays.asList(word.split("")));
        String[] guessArray=guess.split("");
        int correctLetters=0;
        boolean[] letterMarked=new boolean[wordLength];
        HashMap<String,Integer> letterOccur=new HashMap<>();
        //find all the green letters first
        for(int i=0;i<wordLength;i++){
            if(guessArray[i].equalsIgnoreCase(wordleWordList.get(i))){
                getActivePanel().setPanelText(guessArray[i],i,new Color(108,169,101));
                correctLetters++;
                letterMarked[i]=true;
                if(letterOccur.containsKey(guessArray[i])){
                    letterOccur.replace(guessArray[i],letterOccur.get(guessArray[i])+1);
                }else{
                    letterOccur.put(guessArray[i],1);
                }
            }
        }
        //yellow !!double check the logic here!!
        for(int i=0;i<wordLength;i++){
           if(letterMarked[i])continue;
           if(!wordleWordList.contains(guessArray[i]))continue;
           int wordOccur=Collections.frequency(wordleWordList, guessArray[i]);
           if(!letterOccur.containsKey(guessArray[i])){
                letterOccur.put(guessArray[i],0);
            }
            int usedTimes=letterOccur.get(guessArray[i]);
            if(usedTimes<wordOccur){
                getActivePanel().setPanelText(guessArray[i],i,new Color(200,182,83));
                letterMarked[i]=true;
                letterOccur.replace(guessArray[i],letterOccur.get(guessArray[i])+1);
            }
        }
        //gray
        for(int i=0;i<wordLength;i++){
            if(!letterMarked[i]){
                getActivePanel().setPanelText(guessArray[i],i,new Color(120,124,127));
            }
        }
        count++;
        return correctLetters==wordLength;
        
    }
    public WordPanel getActivePanel(){
        return this.wordPanelArray[count];
    }
     public static void main(String[] args) {
        
    }
}

