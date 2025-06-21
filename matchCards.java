import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Collections;

public class matchCards {
    class card{
        String cardName;
        ImageIcon cardImageIcon;

        card(String cardName, ImageIcon cardImageIcon){
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString(){
            return cardName;
        }

    }

    String[] cardList = { //tracks cardname
        "Dekisuki",
        "Doreamon",
        "Gyan",
        "Nobita",
        "Sijuka",
        "Suniyo",
        "Jyako",
        "Siwasi"
    };
    int rows = 4;
    int columns = 4;
    int cardHeight = 128;
    int cardWidth = 90;

    ArrayList<card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardHeight = rows * cardHeight;
    int boardWidth = columns * cardWidth;

    JFrame frame = new JFrame("Memory Flip Card Game");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel(); 
    JPanel boardPanel = new JPanel();

    int errorCount = 0;
    ArrayList<JButton> board;

    matchCards(){
        setupCards();
        suffleCards();

        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: "+Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel,BorderLayout.NORTH);

        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0 ; i< cardSet.size(); i++){
            JButton cardButton = new JButton();
            cardButton.setPreferredSize(new Dimension(cardWidth, cardHeight));
            cardButton.setOpaque(true);
            cardButton.setIcon(cardSet.get(i).cardImageIcon);
            cardButton.setFocusable(true);
            board.add(cardButton);
            boardPanel.add(cardButton);

        }
        frame.add(boardPanel);
        frame.pack();
        frame.setVisible(true);

    }

    void setupCards(){
        cardSet = new ArrayList<card>();
        for (String cardName : cardList){
            Image cardImg = new ImageIcon(getClass().getResource("./img/"+cardName + ".png")).getImage();
            ImageIcon cardImageIcon =  new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            card card1 = new card(cardName, cardImageIcon);
            card card2 = new card(cardName, cardImageIcon);

            cardSet.add(card1);
            cardSet.add(card2);

            //card Card = new card(cardName, cardImageIcon);
            //cardSet.add(Card);

        }
        //cardSet.addAll(cardSet);

        Image cardBackImg = new ImageIcon(getClass().getResource("./img/back card1.png")).getImage();
        cardBackImageIcon =  new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void suffleCards(){
        Collections.shuffle(cardSet);
        System.out.println(cardSet);
        //suffle cards
        
        
        }
    



    public static void main(String[] args) throws Exception{
        matchCards matchcards = new matchCards();
    }
}
