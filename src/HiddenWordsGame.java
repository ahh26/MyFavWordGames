import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class HiddenWordsGame extends JFrame{
    class WordPanel extends JPanel {
        WordCell[][] wordGrid;
        public WordPanel(char[][] gridLetter) {
            this.setLayout(new GridLayout(gridLength,gridLength));
            wordGrid=new WordCell[gridLength][gridLength];
            for(int i=0;i<gridLength;i++) {
                for(int j=0;j<gridLength;j++) {
                    wordGrid[i][j]=new WordCell(gridLetter[i][j],i,j);
                    this.add(wordGrid[i][j]);
                }
            }
        }
        public ArrayList<WordCell> getHighlightedCells(){
            ArrayList<WordCell> highlighted=new ArrayList<>();
            for(int i=0;i<gridLength;i++){
                for(int j=0;j<gridLength;j++){
                    WordCell cell=wordGrid[i][j];
                    if(cell.isSelected()) {
                        highlighted.add(cell);
                }
            }
        }
        return highlighted;
        }
        public void resetHighlights() {
            for(int i=0;i<gridLength;i++) {
                for(int j=0;j<gridLength;j++){
                    wordGrid[i][j].reset();
                }
            }
        }
        public WordCell[][] getGrid(){
            return wordGrid;
        }
    }
    class UserPanel extends JPanel{
        private JButton resetButton;
        private JButton exitButton;
        private JButton hintButton;

        public UserPanel(){
            this.setLayout(new GridLayout(1,3));

            resetButton=new JButton("Reset");
            resetButton.setFocusable(false);
            resetButton.setOpaque(true);
            resetButton.setBackground(new Color(195,207,251));

            hintButton=new JButton("Hint");
            hintButton.setFocusable(false);
            hintButton.setOpaque(true);
            hintButton.setBackground(new Color(149,172,253));

            exitButton=new JButton("Exit");
            exitButton.setFocusable(false);
            exitButton.setOpaque(true);
            exitButton.setBackground(new Color(104,137,255));

        
            this.add(resetButton);
            this.add(hintButton);
            this.add(exitButton);

            revalidate();
            repaint();

        }
        public JButton getResetButton(){
            return resetButton;
        }
        public JButton getHintButton(){
            return hintButton;
        }
        public JButton getExitButton(){
            return exitButton;
        }

    }
    class TitlePanel extends JPanel{
        private JLabel categoryLabel;
        private JLabel wordsLeftLabel;

        public TitlePanel(){
            setLayout(new GridLayout(2,1));
            categoryLabel=new JLabel("Category: "+category);
            categoryLabel.setHorizontalAlignment(JLabel.CENTER);
            categoryLabel.setFont(new Font("Arial",Font.BOLD,16));
            wordsLeftLabel=new JLabel("Words left: "+wordNum);
            wordsLeftLabel.setHorizontalAlignment(JLabel.CENTER);
            wordsLeftLabel.setFont(new Font("Arial",Font.BOLD,14));
            add(categoryLabel);
            add(wordsLeftLabel);
        }
        public void updateWordsLeft(int wordsLeft){
            wordsLeftLabel.setText("Words left: "+wordsLeft);
        }
    }

    private int gridLength;
    private int wordNum;
    private int wordsFound;
    private String category;
    private String[] wordList;
    private ArrayList<Integer[]> wordPosition;

    private WordPanel wordPanel;
    private UserPanel userPanel;
    private TitlePanel titlePanel;

    private HiddenWordsMenu hiddenWordsMenu;
    private Timer selectionCheckTimer;
    private ArrayList<Color> wordColors=new ArrayList<>(Arrays.asList(new Color(255,179,186),new Color(255,223,186),new Color(236,192,138),new Color(137,225,174),new Color(128,135,211),new Color(105,80,208),new Color(231,210,146),new Color(231,151,117),new Color(228,122,128),new Color(213,148,183),new Color(137,157,210),new Color(100,139,127),new Color(214,193,170),new Color(181,86,200),new Color(215,248,181),new Color(175,95,86),new Color(53,122,161),new Color(229,112,153),new Color(207,230,94),new Color(128,65,229),new Color(118,195,229),new Color(229,70,42)));

    private int hintWordIndex;
    private int hintLetterIndex;

    public HiddenWordsGame(int gridLength,int wordNum,String category,HiddenWordsMenu hiddenWordsMenu){
        this.gridLength=gridLength;
        this.wordNum=wordNum;
        this.category=category;
        this.hiddenWordsMenu=(hiddenWordsMenu);
        wordsFound=0;
        hintWordIndex=0;
        hintLetterIndex=0;
        Collections.shuffle(wordColors);

        setTitle("HiddenWords");
        setSize(gridLength*55,gridLength*55+20);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        startGame();
        setVisible(true);

    }

    public void startGame(){
        ArrayList<String>allWords = WordReader.readWordsFromFile("textFiles/"+category.toLowerCase()+".txt");
        wordList=new String[wordNum];
        for(int i=0;i<wordNum;i++){
            String word;
            while(true){
                word=allWords.get((int)(Math.random()*allWords.size()));
                allWords.remove(word);
                if(word.length()<gridLength)break;
            }
            System.out.println(word);
            wordList[i]=word.replace(" ","");
        }

        WordGridGenerator generator=new WordGridGenerator(gridLength, wordList);
        char[][] gridLetter=generator.getGrid();
        wordPosition=generator.getWordPosition();

        wordPanel=new WordPanel(gridLetter);
        userPanel=new UserPanel();
        titlePanel=new TitlePanel();

        userPanel.getResetButton().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                    wordPanel.resetHighlights();
            }
        });
        userPanel.getHintButton().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hintLetters();
            }
        });
        userPanel.getExitButton().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(selectionCheckTimer!=null)selectionCheckTimer.stop();
                setVisible(false); 
                hiddenWordsMenu.showMainMenu(); 
            }
        });
        userPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        setLayout(new BorderLayout());
        add(titlePanel,BorderLayout.NORTH);
        add(wordPanel,BorderLayout.CENTER);
        add(userPanel,BorderLayout.SOUTH);
        revalidate();
        repaint();

        selectionCheckTimer=new Timer(100,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                checkSelectedCells();
            }
        });
        selectionCheckTimer.start();
    }
    private void checkSelectedCells(){
        ArrayList<WordCell> selectedCells=wordPanel.getHighlightedCells();
        int matchIndex=WordMatcher.getMatchedWordIndex(selectedCells, wordPosition);
        if(matchIndex!=-1){ //if match, lock the matched cells
            Color lockColor=wordColors.get(wordsFound);
            wordsFound++;
            for(WordCell cell:selectedCells){
                cell.lock(lockColor);
            }
        }
        titlePanel.updateWordsLeft(wordNum-wordsFound);
        if(wordsFound==wordNum){
            String allwordsMessage=String.join(", ", wordList);
            if(wordNum==1)allwordsMessage="The hidden word is: "+allwordsMessage;
            else allwordsMessage="The hidden words are: "+allwordsMessage;
            selectionCheckTimer.stop(); // stop the timer
            int choice=JOptionPane.showOptionDialog(this, "Congratulations! You've found all words! "+allwordsMessage, "Yeahhh",JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,null,new Object[]{"Play again","Choose another level","Choose another category","Exit"},"Play again");
            dispose();
            if(choice==0){
                new HiddenWordsGame(gridLength,wordNum,category,hiddenWordsMenu);
            }else if(choice==1){
                hiddenWordsMenu.setVisible(true);
                hiddenWordsMenu.choosingLevel();
            }else if(choice==2){
                hiddenWordsMenu.setVisible(true);
                hiddenWordsMenu.choosingCategory(gridLength, wordNum);
            }else{
                hiddenWordsMenu.showMainMenu();
            }
        }
    }
    private void hintLetters(){
        while(hintWordIndex<wordNum){
            Integer[] wordPos=wordPosition.get(hintWordIndex); //get index of the word we want to hint
            boolean isFound=true;
            if(!wordPanel.getGrid()[wordPos[0]][wordPos[1]].isLocked())isFound=false; //just check the first letter, if first letter of word is locked, then it's found
            
            if(isFound){
                hintWordIndex++;
                hintLetterIndex=0;
                continue;
            }
            
            //for unfound words
            int revealCount=Math.min(hintLetterIndex+1,2);
            ArrayList<WordCell> toFlash=new ArrayList<>();
            ArrayList<WordCell> selectedBefore=new ArrayList<>();
            for(int i=0;i<revealCount;i++){
                int row=wordPos[i*2];
                int col=wordPos[i*2+1];
                WordCell cell=wordPanel.getGrid()[row][col];
                if(cell.isSelected())selectedBefore.add(cell);
                cell.setBackground(Color.YELLOW);
                toFlash.add(cell);
            }
            Timer timer=new Timer(500,new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    for(WordCell cell:toFlash){
                        if(!selectedBefore.contains(cell)){
                            cell.reset();
                        }else{
                            cell.setBackground(Color.lightGray);
                        }
                 }
                }
            });
            timer.setRepeats(false);
            timer.start();
            if(hintLetterIndex<2){
                hintLetterIndex++;
            }
            return; //only hint one word at a time
        }
    }
}

