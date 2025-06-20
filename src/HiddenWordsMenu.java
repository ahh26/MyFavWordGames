import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class HiddenWordsMenu extends JFrame{
    class MainMenuPanel extends JPanel{
        private JLabel welcome;
        private JButton play;
        private JButton rules;
        private JButton exit;


        public MainMenuPanel(){
            this.setLayout(new GridLayout(2,1));
            ImageIcon hiddenWelcome=new ImageIcon(new ImageIcon("Pics/HiddenWordsTitle.png").getImage().getScaledInstance(600, 450, Image.SCALE_SMOOTH));
            welcome=new JLabel(hiddenWelcome);
            welcome.setHorizontalAlignment(JLabel.CENTER);
            this.add(welcome);

            JPanel menuChoice=new JPanel(new GridLayout(3,1));
            ImageIcon playButton=new ImageIcon(new ImageIcon("Pics/Play_green.png").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            play=new JButton(playButton);
            ImageIcon rulesButton=new ImageIcon(new ImageIcon("Pics/Rules_green.png").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            rules=new JButton(rulesButton);
            ImageIcon exitButton=new ImageIcon(new ImageIcon("Pics/Exit_green.png").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
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
            ImageIcon rulesIcon=new ImageIcon(new ImageIcon("Pics/HiddenWordsRules.png").getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH));
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

    public HiddenWordsMenu(){
        mainMenuPage();
    }
    public HiddenWordsMenu(Runnable onExitToLobby) {
        this.onExitToLobby = onExitToLobby;
        mainMenuPage();
    }
    public void mainMenuPage(){
        getContentPane().removeAll();
        setTitle("Hidden Words");
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

        LevelChoicePanel=new JPanel(new GridLayout(4,1));
        JLabel levelQuestion=new JLabel("Choose your level");
        levelQuestion.setHorizontalAlignment(JLabel.CENTER);
        LevelChoicePanel.add(levelQuestion);

        JButton easy=new JButton("Easy");
        JButton medium=new JButton("Medium");
        JButton hard=new JButton("Hard");
        easy.setFocusable(false);
        medium.setFocusable(false);
        hard.setFocusable(false);

        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                choosingCategory(5,1);
            }
        });
        medium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                choosingCategory(10,5);
            }
        });

        hard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                choosingCategory(15,9);
            }
        });

        LevelChoicePanel.add(easy);
        LevelChoicePanel.add(medium);
        LevelChoicePanel.add(hard);

        add(LevelChoicePanel);
        setVisible(true);
        revalidate();
        repaint();
    }
    public void choosingCategory(int size, int count){ //after choosing level->choose category
        remove(LevelChoicePanel);
        setSize(300,500);
        setLocationRelativeTo(null);
        JPanel categoryPanel=new JPanel(new BorderLayout());
        JLabel question=new JLabel("Choose a category: ");
        question.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        question.setHorizontalAlignment(JLabel.CENTER);

        JPanel choiceButtonPanel=new JPanel(new GridLayout(4,2));
        JButton[] categoryOptions={new JButton("Animals"),new JButton("Food"),new JButton("Music"),new JButton("Geography"),new JButton("Colours"),new JButton("Sports"),new JButton("Space"),new JButton("Random")};
        for(JButton category:categoryOptions){
            category.setFocusable(false);
            category.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    startGame(size, count,category.getText());
                    setVisible(false);
                    remove(categoryPanel); 
                }
            });
            choiceButtonPanel.add(category);
        }
        categoryPanel.add(question,BorderLayout.NORTH);
        categoryPanel.add(choiceButtonPanel,BorderLayout.CENTER);
        this.add(categoryPanel);
        revalidate();
        repaint();
    }
    public void showMainMenu(){
        setVisible(true);
        mainMenuPage();
    }

    private void startGame(int size,int count,String category){
        new HiddenWordsGame(size,count,category,this); //also change all other games ->delete HiddenwordGame hiddenwordgame, just new ...
    }

    public static void main(String[] args){

    }


}
