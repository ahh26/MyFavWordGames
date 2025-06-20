import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class HangmanMenu extends JFrame{
    class MainMenuPanel extends JPanel{
        private JLabel welcome;
        private JButton play;
        private JButton rules;
        private JButton exit;


        public MainMenuPanel(){
            this.setLayout(new GridLayout(2,1));
            ImageIcon hangmanWelcome=new ImageIcon(new ImageIcon("Pics/HangmanTitle.png").getImage().getScaledInstance(600, 450, Image.SCALE_SMOOTH));
            welcome=new JLabel(hangmanWelcome);
            welcome.setHorizontalAlignment(JLabel.CENTER);
            this.add(welcome);

            JPanel menuChoice=new JPanel(new GridLayout(3,1));
            ImageIcon playButton=new ImageIcon(new ImageIcon("Pics/Play_black.png").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            play=new JButton(playButton);
            ImageIcon rulesButton=new ImageIcon(new ImageIcon("Pics/Rules_black.png").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
            rules=new JButton(rulesButton);
            ImageIcon exitButton=new ImageIcon(new ImageIcon("Pics/Exit_black.png").getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH));
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
            ImageIcon rulesIcon=new ImageIcon(new ImageIcon("Pics/HangmanRules.png").getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH));
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
    private Runnable onExitToLobby;

    public HangmanMenu(){
        mainMenuPage();
    }
    public HangmanMenu(Runnable onExitToLobby) {
        this.onExitToLobby = onExitToLobby;
        mainMenuPage();
    }
    public void mainMenuPage(){
        getContentPane().removeAll();
        setTitle("Hangman");
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
                choosingCategory();
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
     
    public void choosingCategory(){
        remove(mainMenuPanel);
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
                    startGame(category.getText());
                    setVisible(false);
                    remove(categoryPanel); 
                }
            });
            choiceButtonPanel.add(category);
        }
        categoryPanel.add(question,BorderLayout.NORTH);
        categoryPanel.add(choiceButtonPanel,BorderLayout.CENTER);
        this.add(categoryPanel);
        setVisible(true);
        revalidate();
        repaint();
    }
    public void showMainMenu(){
        setVisible(true);
        mainMenuPage();
    }

    private void startGame(String category){
        new HangmanGame(category,this);
    }

    public static void main(String[] args){

    }


}
