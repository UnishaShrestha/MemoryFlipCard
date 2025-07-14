import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Collections;
import javax.sound.sampled.*;

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
    int cardHeight = 150;
    int cardWidth = 110;
    int boardHeight = rows * cardHeight;
    int boardWidth = columns * cardWidth;

    ArrayList<card> cardSet;
    ImageIcon cardBackImageIcon;

    JFrame frame = new JFrame("Memory Flip Card Game");
    JLabel errorLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JLabel moveLabel = new JLabel();
    JPanel textPanel = new JPanel(); 
    JPanel boardPanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0;
    int score = 0;
    int movecount = 0;
    final int totalPairs = 8;
    final int maxErrors = 10;

    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    matchCards(){
        setupCards();
        suffleCards();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        errorLabel.setFont(new Font("Arial", Font.BOLD, 20));
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setText("Errors: " + errorCount + " / " + maxErrors);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setText("Score: " + score);

        moveLabel.setFont(new Font("Arial", Font.BOLD, 20));
        moveLabel.setText("Moves: "+ movecount);

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 15));
        restartButton.setPreferredSize(new Dimension(140, 20));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameReady){
                    return;
                }
                playSound("Click_Sound1.wav");

                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                suffleCards();

                for(int i=0; i< board.size(); i++){
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }

                errorCount = 0;
                score = 0;
                movecount=0;
                errorLabel.setText("Errors: "+errorCount + " / " + maxErrors);
                scoreLabel.setText("Score: "+score);
                moveLabel.setText("Moves: "+movecount);
                hideCardTimer.start();
            }
        });

        textPanel = new JPanel(new BorderLayout());
        textPanel.setPreferredSize(new Dimension(boardWidth, 60)); // more height for 2 lines

        // Panel for stacked labels (vertical layout)
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(3, 1)); // 2 rows, 1 column
        labelPanel.add(errorLabel); // Line 1
        labelPanel.add(scoreLabel); // Line 2
        labelPanel.add(moveLabel);

        textPanel.add(labelPanel, BorderLayout.WEST);
        textPanel.add(restartButton, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.SOUTH);
        
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns, 5, 5));
        for (int i = 0 ; i< cardSet.size(); i++){
            JButton cardButton = new JButton();
            cardButton.setPreferredSize(new Dimension(cardWidth, cardHeight));
            cardButton.setOpaque(true);
            cardButton.setIcon(cardSet.get(i).cardImageIcon);
            cardButton.setFocusable(true);

            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    if(!gameReady){
                        return;
                    }
                    JButton cardButton = (JButton) e.getSource();
                    if(cardButton.getIcon()== cardBackImageIcon){
                        playSound("Click_Sound1.wav");

                        movecount++;
                        moveLabel.setText("Moves: "+ movecount);

                      if(card1Selected == null){
                        card1Selected = cardButton;
                        int index = board.indexOf(card1Selected);
                        card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                      }  else if(card2Selected == null){
                        card2Selected =cardButton;
                        int index = board.indexOf(card2Selected);
                        card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                        if(card1Selected.getIcon()!= card2Selected.getIcon()){
                            errorCount += 1;
                            errorLabel.setText("Error: " + errorCount + " / " + maxErrors);

                            if(errorCount >= maxErrors){
                                gameLost();
                            }else{
                                hideCardTimer.start();
                            }

                        }
                        else{
                            score++;
                            scoreLabel.setText("Score: " + score);
                            card1Selected = null;
                            card2Selected = null;

                            if(score == totalPairs){
                                gameWon();
                            }
                        }
                      }
                    }
                }
            });
            
            board.add(cardButton);
            boardPanel.add(cardButton);

        }
        frame.add(boardPanel);
        frame.pack();
        frame.setVisible(true);

        hideCardTimer = new Timer(1200, new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e){
                hideCards();
             }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

    }

    void setupCards(){
        cardSet = new ArrayList<card>();
        for (String cardName : cardList){
            try{
            Image cardImg = new ImageIcon(getClass().getResource("./img/"+cardName + ".png")).getImage();
            ImageIcon cardImageIcon =  new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new card(cardName, cardImageIcon));
            cardSet.add(new card(cardName, cardImageIcon));
            }
            catch(Exception e){
                System.out.println("Image not found: " + cardName);
            }

        }
        //cardSet.addAll(cardSet);
        try{
        Image cardBackImg = new ImageIcon(getClass().getResource("./img/back card1.png")).getImage();
        cardBackImageIcon =  new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
        }catch(Exception e){
            System.out.println("Back card Image not found ");
        }
    }

    void suffleCards(){
        Collections.shuffle(cardSet);
        }

        void hideCards(){
            if(gameReady && card1Selected != null && card2Selected != null){
                card1Selected.setIcon(cardBackImageIcon);
                card1Selected = null;
                card2Selected.setIcon(cardBackImageIcon);
                card2Selected = null; 
            }
            else{
                for (int i=0; i < board.size(); i++){
                board.get(i).setIcon(cardBackImageIcon);
            }
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }

        void gameWon(){
           JLabel message = new JLabel("YOU WON!", SwingConstants.CENTER );
           message.setFont(new Font("Arial", Font.BOLD, 20));
           JOptionPane.showMessageDialog(message, message, "Game Won", JOptionPane.INFORMATION_MESSAGE);

            saveScoreToFile();

        }

        void gameLost(){
            JLabel message = new JLabel("YOU LOST!", SwingConstants.CENTER );
            message.setFont(new Font("Arial", Font.BOLD, 20));
            JOptionPane.showMessageDialog(message, message, "Game Over", JOptionPane.ERROR_MESSAGE);
            saveScoreToFile();
        }

        void saveScoreToFile(){
            try (FileWriter writer = new FileWriter("score.txt", true)) {
            writer.write("Score: " + score + ", Errors: " + errorCount + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving score to file!");
        }
    }
    void playSound(String soundFileName) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(getClass().getResource("Sound/" + soundFileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }   

    public static void main(String[] args){
        matchCards matchcards  = new matchCards();
    }
}