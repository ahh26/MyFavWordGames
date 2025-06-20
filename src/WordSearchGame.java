import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class WordSearchGame extends JFrame{
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
        private JLabel[] wordLabels;
        private JPanel wordDisplayPanel;
        private JButton exitButton;

        public UserPanel(){
            this.setLayout(new BorderLayout());

            resetButton=new JButton("Reset Highlights");
            resetButton.setFocusable(false);
            resetButton.setBackground(new Color(177,192,233));


            exitButton=new JButton("Exit");
            exitButton.setFocusable(false);
            exitButton.setBackground(new Color(128,135,211));

            JPanel tempButtonPanel=new JPanel();
            tempButtonPanel.setLayout(new BorderLayout());
            tempButtonPanel.add(resetButton,BorderLayout.CENTER);
            tempButtonPanel.add(exitButton,BorderLayout.SOUTH);

        
            this.add(tempButtonPanel,BorderLayout.EAST);

            revalidate();
            repaint();

        }
        public JButton getResetButton(){
            return resetButton;
        }
        public JButton getExitButton(){
            return exitButton;
        }
        public void setWordsDisplay(String[] words) {
            wordDisplayPanel=new JPanel();
            wordDisplayPanel.setLayout(new GridLayout(3,7));
            wordLabels=new JLabel[words.length];
            for(int i=0;i<words.length;i++){
                wordLabels[i]=new JLabel(words[i]);
                wordLabels[i].setFont(new Font("SansSerif",Font.BOLD,14));
                wordDisplayPanel.add(wordLabels[i]);
            }
            // this.removeAll();
            wordDisplayPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            wordDisplayPanel.setPreferredSize(new Dimension(gridLength * 60, 100));
            this.add(wordDisplayPanel,BorderLayout.CENTER);
        }
        public void crossOutWord(int index,String[] words){
            wordLabels[index].setText("<html><strike>"+words[index]+"</strike></html>"); //cross out the word found
            wordLabels[index].setForeground(Color.lightGray); //also make the word lighter
        }
        

    }
    public class TimerPanel extends JPanel{
        private JLabel bestTimeLabel;
        private JLabel currentTimeLabel;
        public TimerPanel(){
            this.setLayout(new GridLayout(2,1));
            currentTimeLabel=new JLabel("Time: 00:00");
            currentTimeLabel.setFont(new Font("SansSerif",Font.BOLD,14));
            currentTimeLabel.setHorizontalAlignment(JLabel.CENTER);

            bestTimeLabel=new JLabel("Best for "+level+": --:--");
            bestTimeLabel.setFont(new Font("SansSerif",Font.BOLD,14));
            bestTimeLabel.setHorizontalAlignment(JLabel.CENTER);

            this.add(currentTimeLabel);
            this.add(bestTimeLabel);
        }
        public String formatTime(long seconds){
            long mins=seconds/60;
            long secs=seconds%60;
            return String.format("%02d:%02d", mins,secs);
        }
        public void setCurrentTime(long seconds){
            currentTimeLabel.setText("Time: "+formatTime(seconds));
        }
        public void setBestTime(long seconds){
            bestTimeLabel.setText("Best for "+level+": "+formatTime(seconds));
        }
        public void clearBestTime(){
            bestTimeLabel.setText("Best for "+level+": --:--");
        }
    }

    private int gridLength;
    private int wordNum;
    private int wordsFound;
    private String[] wordList;
    private WordPanel wordPanel;
    private UserPanel userPanel;
    private String level;
    private WordSearchMenu wordSearchMenu;
    private ArrayList<Integer[]> wordPosition;
    private Timer selectionCheckTimer;
    private  WordGridGenerator generator;
    private ArrayList<Color> wordColors=new ArrayList<>(Arrays.asList(new Color(255,179,186),new Color(255,223,186),new Color(236,192,138),new Color(137,225,174),new Color(128,135,211),new Color(105,80,208),new Color(231,210,146),new Color(231,151,117),new Color(228,122,128),new Color(213,148,183),new Color(137,157,210),new Color(100,139,127),new Color(214,193,170),new Color(181,86,200),new Color(215,248,181),new Color(175,95,86),new Color(53,122,161),new Color(229,112,153),new Color(207,230,94),new Color(128,65,229),new Color(118,195,229),new Color(229,70,42)));

    //recording the bestTime
    private static HashMap<String,Long> bestTimes=new HashMap<>(Map.of("EASY",Long.MAX_VALUE,"MEDIUM",Long.MAX_VALUE,"HARD",Long.MAX_VALUE,"EXPERT",Long.MAX_VALUE));
    private long startTime;
    private long recordTime;
    private TimerPanel timerPanel;
    private Timer gameTimer;

    public WordSearchGame(int gridLength,int wordNum,WordSearchMenu wordSearchMenu){
        this.gridLength=gridLength;
        this.wordNum=wordNum;
        this.wordSearchMenu=wordSearchMenu;
        wordsFound=0;
        Collections.shuffle(wordColors);
        if(gridLength==10)this.level="EASY";
        else if(gridLength==15)this.level="MEDIUM";
        else if(gridLength==20)this.level="HARD";
        else this.level="EXPERT";

        setTitle("WordSearch");
        setSize(gridLength*55,gridLength*55+100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        startGame();
        setVisible(true);

    }

    public void startGame(){
        ArrayList<String>allWords = WordReader.readWordsFromFile("textFiles/random.txt");
        wordList=new String[wordNum];
        for(int i=0;i<wordNum;i++){
            String word;
            while(true){
                word=allWords.get((int)(Math.random()*allWords.size()));
                allWords.remove(word);
                if(word.length()<gridLength-2)break;
            }
            wordList[i]=word;
        }

        generator=new WordGridGenerator(gridLength, wordList);
        char[][] gridLetter=generator.getGrid();
        wordPosition=generator.getWordPosition();

        wordPanel=new WordPanel(gridLetter);
        userPanel=new UserPanel();
        userPanel.setWordsDisplay(wordList);

        userPanel.getResetButton().addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    wordPanel.resetHighlights();
                }
            });
        userPanel.getExitButton().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                selectionCheckTimer.stop();
                setVisible(false); 
                wordSearchMenu.showMainMenu(); 
            }
        });

        timerPanel=new TimerPanel();
        if(bestTimes.get(level)<Long.MAX_VALUE)timerPanel.setBestTime(bestTimes.get(level));//update the bestTime
        startTime=System.currentTimeMillis();
        gameTimer=new Timer(1000,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                long timePasses=(System.currentTimeMillis()-startTime)/1000;
                timerPanel.setCurrentTime(timePasses);
                recordTime=System.currentTimeMillis();
            }
        });
        gameTimer.start();

        setLayout(new BorderLayout());
        add(wordPanel,BorderLayout.CENTER);
        add(userPanel,BorderLayout.SOUTH);
        add(timerPanel,BorderLayout.NORTH);
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
            userPanel.crossOutWord(matchIndex,wordList);
        }
        if(wordsFound==wordNum){
            long finalTime=(recordTime-startTime)/1000;
            long currentBest=bestTimes.get(level);
            selectionCheckTimer.stop(); // stop the timer
            gameTimer.stop();
            
            String timeMessage;
            if(finalTime<currentBest){
                bestTimes.put(level,finalTime);
                timerPanel.setBestTime(finalTime);
                updateBestTime(finalTime);
                timeMessage="New Best Time for "+level+": "+timerPanel.formatTime(finalTime)+" !!";
            }else{
                timeMessage="Completed in: "+timerPanel.formatTime(finalTime)+"\nBest for "+level+": "+timerPanel.formatTime(currentBest);
            }

            int choice=JOptionPane.showOptionDialog(this, "Congratulations! You've found all words!\n"+timeMessage, "Yeahhh",JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,null,new Object[]{"Play again","Choose another level","Exit"},"Play again");
            dispose();
            if(choice==0){
                new WordSearchGame(gridLength,wordNum,wordSearchMenu);
            }else if(choice==1){
                wordSearchMenu.choosingLevel();
            }else{
                wordSearchMenu.showMainMenu();
            }
        }
    }
    private void updateBestTime(long seconds){
        bestTimes.put(level,seconds);
    }
}
