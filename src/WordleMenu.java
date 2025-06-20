import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

public class WordleMenu extends JFrame{
    class MainMenuPanel extends JPanel{
        private JLabel welcome;
        private JButton play;
        private JButton rules;
        private JButton exit;

        public MainMenuPanel(){
            this.setLayout(new GridLayout(2,1));
            ImageIcon wordleWelcome=new ImageIcon(new ImageIcon("Pics/WordleTitle.png").getImage().getScaledInstance(600, 450, Image.SCALE_SMOOTH));
            welcome=new JLabel(wordleWelcome);
            welcome.setHorizontalAlignment(JLabel.CENTER);
            this.add(welcome);

            JPanel menuChoice=new JPanel(new GridLayout(3,1));
            ImageIcon playButton=new ImageIcon(new ImageIcon("Pics/Play.jpg").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            play=new JButton(playButton);
            ImageIcon rulesButton=new ImageIcon(new ImageIcon("Pics/Rules.jpg").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            rules=new JButton(rulesButton);
            ImageIcon exitButton=new ImageIcon(new ImageIcon("Pics/Exit.jpg").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            exit=new JButton(exitButton);
            play.setFocusable(false);
            rules.setFocusable(false);
            exit.setFocusable(false);
            menuChoice.add(play);
            menuChoice.add(rules);
            menuChoice.add(exit);
            this.add(menuChoice);
        }
        
    }
    class RulesPanel extends JPanel{
        private JLabel rulesLabel;
        private JButton exitRules;
        public RulesPanel(){
            this.setLayout(new BorderLayout());
            ImageIcon rulesIcon=new ImageIcon(new ImageIcon("Pics/WordleRules.png").getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH));
            rulesLabel=new JLabel(rulesIcon);
            exitRules=new JButton("Back to Menu");
            exitRules.setFocusable(false);
            exitRules.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    removeAll();
                    mainMenuPage();
                    revalidate();
                    repaint();
                }
            });
            this.add(rulesLabel,BorderLayout.CENTER);
            this.add(exitRules,BorderLayout.SOUTH);
        }
    }

    private MainMenuPanel mainMenuPanel;
    private JPanel LevelChoicePanel;
    private Runnable onExitToLobby;


    public WordleMenu(){ //constructor
        mainMenuPage();
    }

    public WordleMenu(Runnable onExitToLobby) {
        this.onExitToLobby = onExitToLobby;
        mainMenuPage();
    }

    public void mainMenuPage(){
        getContentPane().removeAll();
        setTitle("Wordle");
        setVisible(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); 
        setSize(600,500);
        setLocationRelativeTo(null);

        mainMenuPanel=new MainMenuPanel();
        add(mainMenuPanel);

        mainMenuPanel.play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                dispose();
                choosingLevel();
            }
        });
        mainMenuPanel.rules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                remove(mainMenuPanel);
                add(new RulesPanel());
                revalidate();
                repaint();
            }
        });
        mainMenuPanel.exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                onExitToLobby.run();
                dispose();
            }
        });
        setVisible(true);
    }
    public void choosingLevel(){
        remove(mainMenuPanel);
        setSize(200,400);
        setLocationRelativeTo(null);

        LevelChoicePanel=new JPanel(new GridLayout(5,1));
        JLabel letterQuestion=new JLabel("<html>Number of letters?</html>");
        letterQuestion.setHorizontalAlignment(JLabel.CENTER);
        LevelChoicePanel.add(letterQuestion);

        JButton letter5=new JButton("5 Letters");
        JButton letter6=new JButton("6 Letters");
        JButton letter7=new JButton("7 Letters");
        JButton letter8=new JButton("8 Letters");
        letter5.setFocusable(false);
        letter6.setFocusable(false);
        letter7.setFocusable(false);
        letter8.setFocusable(false);

        letter5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(5);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });
        letter6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(6);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });
        letter7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(7);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });
        letter8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(8);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });
        LevelChoicePanel.add(letter5);
        LevelChoicePanel.add(letter6);
        LevelChoicePanel.add(letter7);
        LevelChoicePanel.add(letter8);

        add(LevelChoicePanel);
        setVisible(true);
        revalidate();
        repaint();
        
    }


    public void showMainMenu() {
        setVisible(true); 
        mainMenuPage();   
    }
    private void startGame(int wordLength) {
        new WordleGame(wordLength,this);
    }

    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> new WordleMenu());
    }
  
}
