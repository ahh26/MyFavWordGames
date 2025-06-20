import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class WordCell extends JButton{
    private char letter;
    private boolean locked;
    private Color lockedColor;
    private int row;
    private int col;
    public WordCell(char letter,int row,int col){
        super(String.valueOf(letter).toUpperCase());
        this.letter=letter;
        this.row=row;
        this.col=col;
        setFocusable(false);
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setFont(new Font("sans-serif", Font.BOLD, 15));
        setPreferredSize(new Dimension(55,55)); 

        addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleHighlight();
        }
    });
    }
    public void lock(Color color){
        locked=true;
        lockedColor=color;
        setBackground(color);
    }
    public void reset(){
        if(!locked){
            setBackground(null);
        }else{
            setBackground(lockedColor);
        }   
    }
    private void toggleHighlight(){
        if(getBackground()==Color.lightGray){
            if(!locked){
                 setBackground(null);
            }else{
                 setBackground(lockedColor);
            }
        }else{
            setBackground(Color.lightGray);
        }
    }
    public char getLetter(){
        return letter;
    }
    public boolean isLocked(){
        return locked;
    }
    public boolean isSelected(){
        return this.getBackground().equals(Color.lightGray);
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
