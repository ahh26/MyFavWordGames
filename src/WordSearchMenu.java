import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class WordSearchMenu extends JFrame{
    class MainMenuPanel extends JPanel{
        private JLabel welcome;
        private JButton play;
        private JButton rules;
        private JButton exit;


        public MainMenuPanel(){
            this.setLayout(new GridLayout(2,1));
            ImageIcon wordSearchWelcome=new ImageIcon(new ImageIcon("Pics/WordSearchTitle.png").getImage().getScaledInstance(600, 450, Image.SCALE_SMOOTH));
            welcome=new JLabel(wordSearchWelcome);
            welcome.setHorizontalAlignment(JLabel.CENTER);
            this.add(welcome);

            JPanel menuChoice=new JPanel(new GridLayout(3,1));
            ImageIcon playButton=new ImageIcon(new ImageIcon("Pics/Play_purple.jpg").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            play=new JButton(playButton);
            ImageIcon rulesButton=new ImageIcon(new ImageIcon("Pics/Rules_purple.jpg").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            rules=new JButton(rulesButton);
            ImageIcon exitButton=new ImageIcon(new ImageIcon("Pics/Exit_purple.jpg").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
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
            ImageIcon rulesIcon=new ImageIcon(new ImageIcon("Pics/WordSearchRules.png").getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH));
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


    public WordSearchMenu(){
        mainMenuPage();
    }
    public WordSearchMenu(Runnable onExitToLobby) {
        this.onExitToLobby = onExitToLobby;
        mainMenuPage();
    }
    public void mainMenuPage(){
        getContentPane().removeAll();
        setTitle("WordSearch");
        setVisible(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); 
        setSize(600,600);
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
        JLabel levelQuestion=new JLabel("Choose your level");
        levelQuestion.setHorizontalAlignment(JLabel.CENTER);
        LevelChoicePanel.add(levelQuestion);

        JButton easy=new JButton("Easy");
        JButton medium=new JButton("Medium");
        JButton hard=new JButton("Hard");
        JButton expert=new JButton("Expert");
        easy.setFocusable(false);
        medium.setFocusable(false);
        hard.setFocusable(false);
        expert.setFocusable(false);

        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(10,5);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });
        medium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(15,10);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });

        hard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(20,15);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });

        expert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                startGame(25,20);
                setVisible(false);
                remove(LevelChoicePanel);
            }
        });
        LevelChoicePanel.add(easy);
        LevelChoicePanel.add(medium);
        LevelChoicePanel.add(hard);
        LevelChoicePanel.add(expert);

        add(LevelChoicePanel);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void showMainMenu(){
        getContentPane().removeAll(); 
        setVisible(true);
        mainMenuPage();
    }
    private void startGame(int size,int count){
        new WordSearchGame(size, count,this);
    }

    public static void main(String[] args){

    }


}
