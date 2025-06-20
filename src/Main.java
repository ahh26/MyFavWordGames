import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame{
  
    public Main(){
        setTitle("Welcome!");
        setSize(550,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //set up game options
        JPanel gameOptions=new JPanel();
        gameOptions.setLayout(new GridLayout(4,1));
        ImageIcon hangmanIcon = new ImageIcon(new ImageIcon("Pics/HangManButton.jpg").getImage().getScaledInstance(230, 145, Image.SCALE_SMOOTH));
        JButton hangmanMenu=new JButton(hangmanIcon);
    
        ImageIcon wordleIcon = new ImageIcon(new ImageIcon("Pics/WordleButton.jpg").getImage().getScaledInstance(200, 145, Image.SCALE_SMOOTH));
        JButton wordleMenu=new JButton(wordleIcon);  
        
        ImageIcon wordSearchIcon = new ImageIcon(new ImageIcon("Pics/WordSearchButton.jpg").getImage().getScaledInstance(250, 145, Image.SCALE_SMOOTH));
        JButton wordSearchMenu=new JButton(wordSearchIcon);

        ImageIcon hiddenWordsIcon = new ImageIcon(new ImageIcon("Pics/HiddenWordsButton.jpg").getImage().getScaledInstance(250, 145, Image.SCALE_SMOOTH));
        JButton hiddenWordsMenu=new JButton(hiddenWordsIcon);

        hangmanMenu.setFont(new Font("Comic Sans MS",Font.BOLD,18));
        wordleMenu.setFont(new Font("Comic Sans MS",Font.BOLD,18));
        wordSearchMenu.setFont(new Font("Comic Sans MS",Font.BOLD,18));
        hiddenWordsMenu.setFont(new Font("Comic Sans MS",Font.BOLD,18));

        hangmanMenu.setFocusable(false);
        wordleMenu.setFocusable(false);
        wordSearchMenu.setFocusable(false);
        hiddenWordsMenu.setFocusable(false);

        gameOptions.add(hangmanMenu);
        gameOptions.add(wordleMenu);
        gameOptions.add(wordSearchMenu);
        gameOptions.add(hiddenWordsMenu); 

        add(gameOptions,BorderLayout.CENTER);

        //set up game Title/Welcoming
        ImageIcon lobbyIcon = new ImageIcon(new ImageIcon("Pics/Lobby.png").getImage().getScaledInstance(550, 300, Image.SCALE_SMOOTH));
        JLabel lobby=new JLabel(lobbyIcon);
        
        add(lobby,BorderLayout.NORTH);
        

        setVisible(true);

        hangmanMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                new HangmanMenu(() -> {
                    setVisible(true); 
                });
            }
        });
        wordleMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                new WordleMenu(() -> {
                    setVisible(true); 
                });
            }
        });
        wordSearchMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                new WordSearchMenu(() -> {
                    setVisible(true); 
                });
            }
        });
        hiddenWordsMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                new HiddenWordsMenu(() -> {
                    setVisible(true); 
                });
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              new Main();
            }
          });
    }
}