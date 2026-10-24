import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

public class Tetris extends JPanel {

    //making shapes
    private final Point[][][] myPieces = createPieces();

    private static Point[][][] createPieces() {
        Point[][][] original = {

            //I shape (index = 0)
            {

                {new Point(0,1),new Point(1,1),new Point(2,1),new Point(3,1)},
                    {new Point(1,0),new Point(1,1),new Point(1,2),new Point(1,3)},
                    {new Point(0,1),new Point(1,1),new Point(2,1),new Point(3,1)},
                    {new Point(1,0),new Point(1,1),new Point(1,2),new Point(1,3)}

            },


            //J shape (index = 1)
            {
                    {new Point(0,0),new Point(0,1),new Point(1,1),new Point(2,1)},
                    {new Point(1,0),new Point(2,0),new Point(1,1),new Point(1,2)},
                    {new Point(0,1),new Point(1,1),new Point(2,1),new Point(2,2)},
                    {new Point(1,0),new Point(1,1),new Point(0,2),new Point(1,2)}

            },

            //L shape (index = 2)
            {
                    {new Point(0,1),new Point(1,1),new Point(2,1),new Point(2,0)},
                    {new Point(1,0),new Point(1,1),new Point(1,2),new Point(2,2)},
                    {new Point(0,1),new Point(1,1),new Point(2,1),new Point(0,0)},
                    {new Point(1,0),new Point(1,1),new Point(1,2),new Point(2,0)}

            },

            //O shape (index = 3)
            {
                    {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1)},
                    {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1)},
                    {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1)},
                    {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1)}

            }
    };
        Point[][][] all = java.util.Arrays.copyOf(original, 7);
        int[][][] extras = {{{1,0},{2,0},{0,1},{1,1}}, {{1,0},{0,1},{1,1},{2,1}}, {{0,0},{1,0},{1,1},{2,1}}};
        for (int i=0;i<3;i++) {
            all[i+4] = new Point[4][4];
            for (int p=0;p<4;p++) all[i+4][0][p] = new Point(extras[i][p][0], extras[i][p][1]);
            for (int r=1;r<4;r++) for (int p=0;p<4;p++) {
                Point previous=all[i+4][r-1][p];
                all[i+4][r][p]=new Point(2-previous.y,previous.x);
            }
        }
        return all;
    }

    //Colors
    private final Color[] myColors = {
            Color.cyan, Color.MAGENTA, Color.orange, Color.yellow, new Color(184,246,89), Color.pink,
            Color.red };

    public long score;
    private boolean gameOver;
    private Point starting_pt;
    private int currentPiece;
    private int rotation;
    private ArrayList<Integer> nextPiece=new ArrayList<Integer>();

    private Color[][] well;
    //we will initialize the well(container) array
    public void init(){
        score = 0; gameOver = false; nextPiece.clear();
        //well is the container of the game
        well=new Color[12][24];
        for (int i=0;i<12;i++){
            for (int j=0;j<23;j++){
                if (i==0||i==11||j==22){
                    well[i][j]=Color.PINK;
                }
                else {
                    well[i][j]=Color.black;
                }
            }
        }
        newPiece();
    }

    //Generate new piece every time
    public void newPiece(){
        starting_pt =new Point(5,2); //this piece will fall from this point
        rotation=0;

        //make a random piece
        if (nextPiece.isEmpty()){
            Collections.addAll(nextPiece,0,1,2,3,4,5,6);//put all possible Pieces in nextPiece list
            Collections.shuffle(nextPiece);//select random piece(shape)
        }
        currentPiece = nextPiece.get(0);
        nextPiece.remove(0);
        gameOver = collidesAt(starting_pt.x, starting_pt.y, rotation);
    }

    //Check the collision
    private boolean collidesAt(int x,int y,int rotation){
        for (Point p: myPieces[currentPiece][rotation]){
            if (p.x+x < 1 || p.x+x > 10 || p.y+y < 0 || p.y+y >= 22 || well[p.x+x][p.y+y]!= Color.black){
                return true;
            }
        }
        return false;
    }

    //Rotate the piece
    public void rotate(int r_indx){
        if (gameOver) return;
        int newRotation=(rotation+r_indx)%4;

        if (newRotation < 0){
            newRotation = 3;
        }

        if (!collidesAt(starting_pt.x, starting_pt.y,newRotation)){
            rotation=newRotation;
        }
        repaint();
    }

    //Move the piece
    public void move(int m_indx){
        if (!gameOver && !collidesAt(starting_pt.x + m_indx, starting_pt.y,rotation)){
            starting_pt.x+=m_indx;
        }
        repaint();
    }

    //Drop the piece
    public void drop(){
        if (gameOver) return;
        if (!collidesAt(starting_pt.x, starting_pt.y+1,rotation)){
            starting_pt.y+=1;
        }
        else { //fix the piece
            fixTowell();
        }
        repaint();
    }

    public void fixTowell(){
        for (Point p: myPieces[currentPiece][rotation]){
            well[starting_pt.x+p.x][starting_pt.y+p.y]=myColors[currentPiece];
        }
        clearRows();
        newPiece();
    }

    public void deleteRow(int row){
        for (int j=row-1;j>=0;j--){
            for (int i=1;i<11;i++){
                well[i][j+1]=well[i][j];
            }
        }
        for (int i=1;i<11;i++) well[i][0] = Color.black;
    }

    public void clearRows(){
        boolean gap;
        int num_deleted_rows=0;
        for (int j=21;j>0;j--){
            gap=false;
            for (int i=1;i<11;i++){
                if (well[i][j]==Color.black){
                    gap=true;
                    break;
                }
            }
            if(!gap){ //the row has no gaps, so we will delete the row
                deleteRow(j);
                j++;
                num_deleted_rows++;
            }
        }
        switch (num_deleted_rows){
            case 1:
                score+=100;
                break;
            case 2:
                score+=300;
                break;
            case 3:
                score+=500;
                break;
            case 4:
                score+=800;
                break;
        }
    }

    private void drawPiece(Graphics g){
        g.setColor(myColors[currentPiece]);
        for (Point p:myPieces[currentPiece][rotation]){
            g.fillRect((starting_pt.x+p.x)*26,(starting_pt.y+p.y)*26,25,25);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (well == null) return;
        g.setColor(new Color(12,15,24));
        g.fillRect(0,0,26*12,26*23);
        for (int i=0;i<12;i++){
            for (int j=0;j<23;j++){
                g.setColor(well[i][j]);
                g.fillRect(26*i,26*j,25,25);
            }
        }
        g.setColor(Color.white);
        g.drawString("Score: "+score,19*12,25);
        if (!gameOver) drawPiece(g);
        else g.drawString("GAME OVER · R to restart", 50, 280);
    }

}



