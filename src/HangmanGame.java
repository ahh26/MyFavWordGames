import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;

public class HangmanGame extends JFrame{
    public class DisplayPanel extends JPanel{
        private JLabel hangmanDisplay;
        public DisplayPanel(){
            setLayout(new BorderLayout());
            ImageIcon hangmanPic=new ImageIcon(new ImageIcon("Pics/Hangman6.png").getImage().getScaledInstance(600, 450, Image.SCALE_FAST));
            hangmanDisplay=new JLabel(hangmanPic);
            hangmanDisplay.setOpaque(true);
            hangmanDisplay.setBackground(Color.WHITE);
            this.add(hangmanDisplay,BorderLayout.CENTER);
            JLabel categoryText=new JLabel("Category: "+category);
            categoryText.setHorizontalAlignment(JLabel.CENTER);
            categoryText.setFont(new Font("Algerian", Font.BOLD, 20));
            categoryText.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
            categoryText.setOpaque(true);
            this.add(categoryText,BorderLayout.NORTH);
        }
        public void changeDisplay(String imageFile){
            ImageIcon newIcon=new ImageIcon(new ImageIcon(imageFile).getImage().getScaledInstance(600,450,Image.SCALE_FAST));
            hangmanDisplay.setIcon(newIcon);
            repaint();
            revalidate();
        }
        public JLabel getHangmanDisplay(){
            return hangmanDisplay;
        }
    }
    public class WordPanel extends JPanel{
        private JLabel[] wordLetters=new JLabel[wordLength];
        public WordPanel(){
            this.setLayout(new FlowLayout(FlowLayout.CENTER));
            for(int i=0;i<wordLength;i++){
                wordLetters[i]=new JLabel("_");
                wordLetters[i].setOpaque(true);
                wordLetters[i].setHorizontalAlignment(JLabel.CENTER);
                wordLetters[i].setFont(new Font("Franklin Gothic", Font.BOLD, 50));
                this.add(wordLetters[i]);
            }
        }

        public JLabel[] getWordLetters(){
            return wordLetters;
        }
        public void updateGuessedLetter(int index,char letter){
            wordLetters[index].setText(String.valueOf(letter));
        }
    }
    public class KeyboardPanel extends JPanel{ //keyboard & exitButton
        private HashMap<Character,JButton>letterButtons=new HashMap<>();
        private JPanel topRow;
        private JPanel midRow;
        private JPanel bottomRow;
        public KeyboardPanel(){
            setLayout(new GridLayout(3,1,2,2)); //3 rows-classic keyboard + exit button
            topRow=new JPanel(new FlowLayout(FlowLayout.CENTER,3,1));
            midRow=new JPanel(new FlowLayout(FlowLayout.CENTER,3,1));
            bottomRow=new JPanel(new FlowLayout(FlowLayout.CENTER,3,1));
            for(char i='A';i<='Z';i++){
                final char letter=i;
                final JButton button=new JButton(String.valueOf(letter));
                button.setFocusable(false);
                button.setPreferredSize(new Dimension(50,50));
                button.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        button.setEnabled(false);
                        handleGuess(letter);
                    }
                });
                letterButtons.put(letter,button);
            }
            for(char ch:"QWERTYUIOP".toCharArray()){
                topRow.add(letterButtons.get(ch));
            }
            for(char ch:"ASDFGHJKL".toCharArray()){
                midRow.add(letterButtons.get(ch));
            }
            for(char ch:"ZXCVBNM".toCharArray()){
                bottomRow.add(letterButtons.get(ch));
            }
            add(topRow);
            add(midRow);
            add(bottomRow);


        }
        public void resetKeyboard(){
            for(JButton button:letterButtons.values()){
                button.setEnabled(true);
            }
        }
        public void disableLetter(char letter){
            JButton button=letterButtons.get(letter);
            button.setEnabled(false);
        }
    }

    private String word;
    private String category;
    private int wordLength;
    private int wrongGuess;
    private int lettersFound;
    
    private HangmanMenu hangmanMenu;
    private DisplayPanel displayPanel;
    private WordPanel wordPanel;
    private KeyboardPanel keyboardPanel;
    private JButton exitButton;

    public HangmanGame(String category,HangmanMenu hangmanMenu){
        this.category=category;
        this.hangmanMenu=hangmanMenu;
        this.wrongGuess=0;
        this.lettersFound=0;
        setTitle("Hangman");
        setLayout(new BorderLayout());
        setSize(800,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        startGame();
    }
    public void startGame(){
        ArrayList<String>allWords = WordReader.readWordsFromFile("textFiles/"+category.toLowerCase()+".txt");
        this.word=allWords.get((int)(Math.random()*allWords.size()));
        System.out.println(word);
        this.wordLength=word.length();
    

        displayPanel=new DisplayPanel();
        displayPanel.setPreferredSize(new Dimension(700,370));
        wordPanel=new WordPanel();
        wordPanel.setPreferredSize(new Dimension(700,100));

        JPanel userPanel=new JPanel();//temp panel, just to add in the exit button
        userPanel.setLayout(new BorderLayout());
        userPanel.setPreferredSize(new Dimension(700,230));
        keyboardPanel=new KeyboardPanel();
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        userPanel.add(keyboardPanel,BorderLayout.CENTER);

        exitButton=new JButton("Exit");
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setVisible(false); 
                hangmanMenu.showMainMenu(); 
            }
        });
        userPanel.add(exitButton,BorderLayout.SOUTH);

        add(displayPanel,BorderLayout.NORTH);
        add(wordPanel,BorderLayout.CENTER);
        add(userPanel,BorderLayout.SOUTH);

    }
    public void handleGuess(char guessedLetter){
        boolean found=false;
        for(int i=0;i<wordLength;i++){
            if(word.charAt(i)==guessedLetter){
                wordPanel.updateGuessedLetter(i,guessedLetter);
                lettersFound++;
                found=true;
            }
        }
        if(lettersFound==wordLength){
            displayPanel.changeDisplay("Pics/HangmanVictory.png");
            displayPanel.getHangmanDisplay().setBackground(new Color(129,255,131));
            Timer timer=new Timer(900,new ActionListener() { //stop on the final pic for 1 sec
                public void actionPerformed(ActionEvent e){
                    int choice=JOptionPane.showOptionDialog(HangmanGame.this, "Congratulations you win! The words is: "+word, "Yeahhh",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new Object[]{"Play again","Choose another category","Exit"},"Play again");
                    dispose();
                    if(choice==0){
                        new HangmanGame(category,hangmanMenu);
                    }else if(choice==1){
                        hangmanMenu.setVisible(true);
                        hangmanMenu.choosingCategory();
                    }else{
                        hangmanMenu.showMainMenu();
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
           
        }
        if(!found){
            wrongGuess++;
            if(wrongGuess==6){
                displayPanel.changeDisplay("Pics/HangmanLose.png");
                displayPanel.getHangmanDisplay().setBackground(new Color(251,98,98));
                Timer timer=new Timer(900,new ActionListener(){ //stop on the final pic for 1 sec
                    public void actionPerformed(ActionEvent e){
                        int choice=JOptionPane.showOptionDialog(HangmanGame.this, "Oops... You've already used up all 6 chances. The word is: "+word, "You lose...",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new Object[]{"Play again","Choose another category","Exit"},"Play again");
                        dispose();
                        if(choice==0){
                            new HangmanGame(category,hangmanMenu);
                        }else if(choice==1){
                            hangmanMenu.setVisible(true);
                            hangmanMenu.choosingCategory();
                        }else{
                            hangmanMenu.showMainMenu();
                        }
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }else{
                displayPanel.changeDisplay("Pics/Hangman"+(6-wrongGuess)+".png");
            }
        }
    }
}
