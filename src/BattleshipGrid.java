import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by jskarda on 2/16/16.
 */
public class BattleshipGrid extends JPanel {


    MouseListener listener;
    private CellPane[][] gridSpaces;
    public Color COLOR_CELL_HAS_SHIP = Color.magenta;

    public BattleshipGrid () {


        listener = new DragMouseAdapter();
                setLayout(new BorderLayout());
                add(new TestPane());
                setVisible(true);
                setOpaque(true);



    }

    public class TestPane extends JPanel {


        public TestPane()  {
            setLayout(new GridBagLayout());
            gridSpaces = new CellPane[10][10];
            GridBagConstraints gbc = new GridBagConstraints();
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;

                    CellPane cellPane = new CellPane(row, col);
                    Border border = null;
                    if (row < 9) {
                        if (col < 9) {
                            border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                        }
                    } else {
                        if (col < 9) {
                            border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                        }
                    }
                    cellPane.setBorder(border);
                    add(cellPane, gbc);
                    gridSpaces[row][col] = cellPane;
                }
            }

            gbc.gridx = 10;
            gbc.gridy = 0;

            JLabel jl = null;
            try {
                jl = new JLabel(new ImageIcon(new URL("http://i.stack.imgur.com/8BGfi.png")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            jl.addMouseListener(listener);
            jl.setSize(50, 50);

            jl.setTransferHandler(new TransferHandler("icon"));

            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    gridSpaces[i][j].setTransferHandler(new TransferHandler("icon"));
                }
            }


            add(jl, gbc);

        }
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(500,600);
        }
    }//close TestPane()

    //BattleshipGrid helper functions

    public boolean assignRandomShips(){
        //generate 2 random numbers, row and col to set that cell as filled
        Random random = new Random();
        int row, col;
        int max = 9, min = 0;
        int numShipsAssigned = 0;

        while(numShipsAssigned < 10){
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);

            if(gridSpaces[row][col].getIsShipHere() == false) {
                gridSpaces[row][col].setIsShipHere(true);
                gridSpaces[row][col].setBackground(COLOR_CELL_HAS_SHIP);

                numShipsAssigned++;

                System.out.print("Row: " + row + " Col: " + col);
            }else{
                //do nothing cuz theres already a ship there
                System.out.print("Already ship there");
            }
        }

        System.out.println();

        return true;
    }


    public class CellPane extends JLabel {

        private Color defaultBackground = Color.BLACK;
        private boolean isClicked = false;
        private boolean isDone = false;
        private boolean isShipHere;
        private int row;
        private int column;

        public CellPane(int r, int c) {

            setOpaque(true);
            setForeground(defaultBackground);
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e){
                    System.out.println("F");
                    setForeground(Color.BLUE);
                    setBackground(Color.BLUE);
                    setClicked(true);
                    repaint();
                }
            });

            isShipHere = false;
            row = r;
            column = c;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }

        public boolean getIsClicked(){
            return isClicked;
        }

        public void setClicked(boolean trueFalse){
            isClicked = trueFalse;
        }

        public boolean getIsShipHere(){
            return isShipHere;
        }

        public void setIsShipHere(boolean b){
            isShipHere = b;
        }

        public int getRow(){return row;}

        public int getColumn(){return column;}

        public boolean getIsDone(){  return isDone; }

        public void setIsDone(boolean b){isDone = b;}

    }//close CellPane class

    class DragMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.COPY);
        }
    }

    public CellPane getTargetedCell(){
        //cycle through the grid cells to find the one clicked
        //mark the targeted cell as done

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if((gridSpaces[i][j].getIsClicked() == true) && (gridSpaces[i][j].getIsDone() == false)){
                    gridSpaces[i][j].setIsDone(true);
                    return gridSpaces[i][j];
                }
            }
        }

        return null;
    }

    public void markShot(int row, int col){
        gridSpaces[row][col].setBackground(Color.red);
    }



}


