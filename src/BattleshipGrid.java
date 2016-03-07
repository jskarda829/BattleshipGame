import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

/**
 * Created by jskarda on 2/16/16.
 */

public class BattleshipGrid extends JPanel {

    ShipMouseAdapter shipListener;
    ShipMovingAdaptor shipMovingAdaptor;
    public CellPane[][] gridSpaces;
    public static boolean shipBeingDragged = false;
    public boolean needToAddShips;
    public int carrierHits = 0;
    public int battleshipHits = 0;
    public int destroyerHits = 0;
    public int submarineHits = 0;
    public int patrolBoatHits = 0;

    Carrier c = new Carrier();
    Battleship b = new Battleship();
    Destroyer d = new Destroyer();
    Submarine s = new Submarine();
    PatrolBoat pb = new PatrolBoat();

    BattleshipGrid battleshipGrid = this;

    public BattleshipGrid (boolean addShips) {
        needToAddShips = addShips;
        setLayout(new BorderLayout());
        add(new TestPane());
        setVisible(true);
        setOpaque(true);
    }

    public void removeAllListeners(){
        shipMovingAdaptor.isClickable = false;
    }

    public boolean areShipsPlaced(){
        int count = 0;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(gridSpaces[i][j].getIsShipHere()){
                    count++;
                }
            }
        }
        if(count < 17){
            return false;
        }
        return true;
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
                    Border border;
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
                    cellPane.setBackground(Color.BLACK);
                    add(cellPane, gbc);
                    gridSpaces[row][col] = cellPane;
                }
            }

            if(needToAddShips) {

                shipListener = new ShipMouseAdapter(shipBeingDragged, gridSpaces, c, b, d, s, pb, battleshipGrid);
                shipMovingAdaptor = new ShipMovingAdaptor(gridSpaces, c, b, d, s, pb);
                addMouseListener(shipMovingAdaptor);

                gbc.gridx = 10;
                gbc.gridy = 0;
                gbc.gridheight = 5;
                //Carrier c = new Carrier();
                c.addMouseListener(shipListener);
                c.setName("Carrier");

                ImageIcon ii = new ImageIcon("src/carrier_1.png");

                add(c, gbc);

                //Set gridbagcontraints for Battleship
                gbc.gridx = 10;
                gbc.gridy = 6;
                gbc.gridheight = 4;
                b.addMouseListener(shipListener);
                b.setName("Battleship");
                add(b, gbc);

                //set gridbagconstraints for destroyer
                gbc.gridx = 11;
                gbc.gridy = 0;
                gbc.gridheight = 3;
                d.addMouseListener(shipListener);
                d.setName("Destroyer");
                add(d, gbc);

                //set gridbagconstraints for submarine
                gbc.gridx = 11;
                gbc.gridy = 4;
                gbc.gridheight = 3;
                s.addMouseListener(shipListener);
                s.setName("Sub");
                add(s, gbc);

                //set gridbagcontraints for patrol boat
                gbc.gridx = 11;
                gbc.gridy = 8;
                gbc.gridheight = 2;
                pb.addMouseListener(shipListener);
                pb.setName("PatrolBoat");
                add(pb, gbc);

            }else{
                //dont need to add ships to this grid
            }


            addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    if(!needToAddShips) {
                        changeSpaceSelection(e);
                    } else {

                    }
                }

                public void changeSpaceSelection(MouseEvent e){
                    setGridSpacesToDefault(e);
                    setClickedSpaceColor(e);
                }

                /**
                 * Used in changeSpaceSelection() above
                 * @param e
                 */
                private void setGridSpacesToDefault(MouseEvent e){
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (gridSpaces[i][j].getIsClicked()) {
                                if (!gridSpaces[i][j].isDone) {
                                    gridSpaces[i][j].setBackground(Color.BLACK);
                                    gridSpaces[i][j].setClicked(false);
                                    gridSpaces[i][j].setIcon(new ImageIcon("pics/wave.jpg"));
                                }
                            }
                        }
                    }
                }

                /**
                 * Used in changeSpaceSelection() above
                 * @param e
                 */
                private void setClickedSpaceColor(MouseEvent e){
                    if (e.getX() <= gridSpaces[9][9].getX() + 50 && e.getY() <= 500) {
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                if (e.getX() > gridSpaces[i][j].getX() && e.getX() < gridSpaces[i][j].getX() + 50) {
                                    if (e.getY() > gridSpaces[i][j].getY() && e.getY() < gridSpaces[i][j].getY() + 50) {
                                        if (!gridSpaces[i][j].isDone) {
                                            gridSpaces[i][j].setBackground(Color.BLUE);
                                            gridSpaces[i][j].setClicked(true);
                                            gridSpaces[i][j].setIcon(new ImageIcon("pics/target.PNG"));
                                        }
                                    }
                                }
                            }
                        }
                        repaint();
                    }
                }

                public void mouseEntered(MouseEvent e) {

                    if (!needToAddShips) {
                        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }

                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    shipBeingDragged = false;
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });


        }
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(600,500);
        }
    }//close TestPane()

    //BattleshipGrid helper functions

    public boolean assignRandomShips(){
        //generate 2 random numbers, row and col to set that cell as filled
        Random random = new Random();
        int row, col, vertical;
        int max = 9, min = 0;
        boolean shipAssigned = false;


        //place carrier
        while(!shipAssigned) {
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);
            vertical = random.nextInt() % 2;
            if (vertical == 1 && row < 6) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row + 1][col].getIsShipHere() && !gridSpaces[row + 2][col].getIsShipHere()
                        && !gridSpaces[row + 3][col].getIsShipHere() && !gridSpaces[row + 4][col].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row + 1][col].setIsShipHere(true);
                    gridSpaces[row + 2][col].setIsShipHere(true);
                    gridSpaces[row + 3][col].setIsShipHere(true);
                    gridSpaces[row + 4][col].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/carrier_1.png"));
                    gridSpaces[row+1][col].setIcon(new ImageIcon("pics/carrier_2.png"));
                    gridSpaces[row+2][col].setIcon(new ImageIcon("pics/carrier_3.png"));
                    gridSpaces[row+3][col].setIcon(new ImageIcon("pics/carrier_4.png"));
                    gridSpaces[row+4][col].setIcon(new ImageIcon("pics/carrier_5.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row+1][col].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row+2][col].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row+3][col].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row+4][col].setWhichShip(SocketSignals.CARRIER_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row+1][col].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row+2][col].setShipPiece(SocketSignals.THIRD_PIECE);
                    gridSpaces[row+3][col].setShipPiece(SocketSignals.FOURTH_PIECE);
                    gridSpaces[row+4][col].setShipPiece(SocketSignals.FIFTH_PIECE);

                    gridSpaces[row][col].setIsVertical(true);
                    shipAssigned = true;

                } else {//do nothing cuz theres already a ship there

                }
            } else if(vertical == 0 && col < 6) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row][col+1].getIsShipHere() && !gridSpaces[row][col+2].getIsShipHere()
                        && !gridSpaces[row][col+3].getIsShipHere() && !gridSpaces[row][col+4].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row][col+1].setIsShipHere(true);
                    gridSpaces[row][col+2].setIsShipHere(true);
                    gridSpaces[row][col+3].setIsShipHere(true);
                    gridSpaces[row][col+4].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/carrier_1 copy.png"));
                    gridSpaces[row][col+1].setIcon(new ImageIcon("pics/carrier_2 copy.png"));
                    gridSpaces[row][col+2].setIcon(new ImageIcon("pics/carrier_3 copy.png"));
                    gridSpaces[row][col+3].setIcon(new ImageIcon("pics/carrier_4 copy.png"));
                    gridSpaces[row][col+4].setIcon(new ImageIcon("pics/carrier_5 copy.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row][col+1].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row][col+2].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row][col+3].setWhichShip(SocketSignals.CARRIER_INT);
                    gridSpaces[row][col+4].setWhichShip(SocketSignals.CARRIER_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row][col+1].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row][col+2].setShipPiece(SocketSignals.THIRD_PIECE);
                    gridSpaces[row][col+3].setShipPiece(SocketSignals.FOURTH_PIECE);
                    gridSpaces[row][col+4].setShipPiece(SocketSignals.FIFTH_PIECE);

                    gridSpaces[row][col].setIsVertical(false);
                    shipAssigned = true;
                }
            }
        }
        shipAssigned = false;
        //assign battleship
        while(!shipAssigned){
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);
            vertical = random.nextInt() % 2;
            if (vertical == 1 && row < 7) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row + 1][col].getIsShipHere() && !gridSpaces[row + 2][col].getIsShipHere()
                        && !gridSpaces[row + 3][col].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row + 1][col].setIsShipHere(true);
                    gridSpaces[row + 2][col].setIsShipHere(true);
                    gridSpaces[row + 3][col].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/battleship_1.png"));
                    gridSpaces[row+1][col].setIcon(new ImageIcon("pics/battleship_2.png"));
                    gridSpaces[row+2][col].setIcon(new ImageIcon("pics/battleship_3.png"));
                    gridSpaces[row+3][col].setIcon(new ImageIcon("pics/battleship_4.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.BATTLESHIP_INT);
                    gridSpaces[row+1][col].setWhichShip(SocketSignals.BATTLESHIP_INT);
                    gridSpaces[row+2][col].setWhichShip(SocketSignals.BATTLESHIP_INT);
                    gridSpaces[row+3][col].setWhichShip(SocketSignals.BATTLESHIP_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row+1][col].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row+2][col].setShipPiece(SocketSignals.THIRD_PIECE);
                    gridSpaces[row+3][col].setShipPiece(SocketSignals.FOURTH_PIECE);

                    gridSpaces[row][col].setIsVertical(true);
                    shipAssigned = true;

                } else {

                }
            } else if(vertical == 0 && col < 7) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row][col+1].getIsShipHere() && !gridSpaces[row][col+2].getIsShipHere()
                        && !gridSpaces[row][col+3].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row][col+1].setIsShipHere(true);
                    gridSpaces[row][col+2].setIsShipHere(true);
                    gridSpaces[row][col+3].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/battleship_1 copy.png"));
                    gridSpaces[row][col+1].setIcon(new ImageIcon("pics/battleship_2 copy.png"));
                    gridSpaces[row][col+2].setIcon(new ImageIcon("pics/battleship_3 copy.png"));
                    gridSpaces[row][col+3].setIcon(new ImageIcon("pics/battleship_4 copy.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.BATTLESHIP_INT);
                    gridSpaces[row][col+1].setWhichShip(SocketSignals.BATTLESHIP_INT);
                    gridSpaces[row][col+2].setWhichShip(SocketSignals.BATTLESHIP_INT);
                    gridSpaces[row][col+3].setWhichShip(SocketSignals.BATTLESHIP_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row][col+1].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row][col+2].setShipPiece(SocketSignals.THIRD_PIECE);
                    gridSpaces[row][col+3].setShipPiece(SocketSignals.FOURTH_PIECE);

                    gridSpaces[row][col].setIsVertical(false);
                    shipAssigned = true;
                }
            }
        }
        shipAssigned = false;
        //assign destroyer
        while(!shipAssigned){
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);
            vertical = random.nextInt() % 2;
            if (vertical == 1 && row < 8) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row + 1][col].getIsShipHere() && !gridSpaces[row + 2][col].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row + 1][col].setIsShipHere(true);
                    gridSpaces[row + 2][col].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/destroyer_1.png"));
                    gridSpaces[row+1][col].setIcon(new ImageIcon("pics/destroyer_2.png"));
                    gridSpaces[row+2][col].setIcon(new ImageIcon("pics/destroyer_3.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.DESTROYER_INT);
                    gridSpaces[row+1][col].setWhichShip(SocketSignals.DESTROYER_INT);
                    gridSpaces[row+2][col].setWhichShip(SocketSignals.DESTROYER_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row+1][col].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row+2][col].setShipPiece(SocketSignals.THIRD_PIECE);

                    gridSpaces[row][col].setIsVertical(true);
                    shipAssigned = true;

                } else {

                }
            } else if(vertical == 0 && col < 8) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row][col+1].getIsShipHere() && !gridSpaces[row][col+2].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row][col+1].setIsShipHere(true);
                    gridSpaces[row][col+2].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/destroyer_1 copy.png"));
                    gridSpaces[row][col+1].setIcon(new ImageIcon("pics/destroyer_2 copy.png"));
                    gridSpaces[row][col+2].setIcon(new ImageIcon("pics/destroyer_3 copy.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.DESTROYER_INT);
                    gridSpaces[row][col+1].setWhichShip(SocketSignals.DESTROYER_INT);
                    gridSpaces[row][col+2].setWhichShip(SocketSignals.DESTROYER_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row][col+1].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row][col+2].setShipPiece(SocketSignals.THIRD_PIECE);

                    gridSpaces[row][col].setIsVertical(false);
                    shipAssigned = true;
                }
            }
        }
        shipAssigned = false;
        //assign submarine
        while(!shipAssigned){
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);
            vertical = random.nextInt() % 2;
            if (vertical == 1 && row < 8) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row + 1][col].getIsShipHere() && !gridSpaces[row + 2][col].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row + 1][col].setIsShipHere(true);
                    gridSpaces[row + 2][col].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/sub_1.png"));
                    gridSpaces[row+1][col].setIcon(new ImageIcon("pics/sub_2.png"));
                    gridSpaces[row+2][col].setIcon(new ImageIcon("pics/sub_3.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.SUBMARINE_INT);
                    gridSpaces[row+1][col].setWhichShip(SocketSignals.SUBMARINE_INT);
                    gridSpaces[row+2][col].setWhichShip(SocketSignals.SUBMARINE_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row+1][col].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row+2][col].setShipPiece(SocketSignals.THIRD_PIECE);

                    gridSpaces[row][col].setIsVertical(true);
                    shipAssigned = true;

                } else {

                }
            } else if(vertical == 0 && col < 8) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row][col+1].getIsShipHere() && !gridSpaces[row][col+2].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row][col+1].setIsShipHere(true);
                    gridSpaces[row][col+2].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/sub_1 copy.png"));
                    gridSpaces[row][col+1].setIcon(new ImageIcon("pics/sub_2 copy.png"));
                    gridSpaces[row][col+2].setIcon(new ImageIcon("pics/sub_3 copy.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.SUBMARINE_INT);
                    gridSpaces[row][col+1].setWhichShip(SocketSignals.SUBMARINE_INT);
                    gridSpaces[row][col+2].setWhichShip(SocketSignals.SUBMARINE_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row][col+1].setShipPiece(SocketSignals.SECOND_PIECE);
                    gridSpaces[row][col+2].setShipPiece(SocketSignals.THIRD_PIECE);

                    gridSpaces[row][col].setIsVertical(false);
                    shipAssigned = true;
                }
            }
        }
        shipAssigned = false;
        //assign patrol boat
        while(!shipAssigned){
            row = (random.nextInt(max - min + 1) + min);
            col = (random.nextInt(max - min + 1) + min);
            vertical = random.nextInt() % 2;
            if (vertical == 1 && row < 9) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row + 1][col].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row + 1][col].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/patrol_boat_1.png"));
                    gridSpaces[row+1][col].setIcon(new ImageIcon("pics/patrol_boat_2.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.SUBMARINE_INT);
                    gridSpaces[row+1][col].setWhichShip(SocketSignals.SUBMARINE_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row+1][col].setShipPiece(SocketSignals.SECOND_PIECE);

                    gridSpaces[row][col].setIsVertical(true);
                    shipAssigned = true;

                } else {

                }
            } else if(vertical == 0 && col < 9) {
                if (!gridSpaces[row][col].getIsShipHere() && !gridSpaces[row][col+1].getIsShipHere()) {
                    gridSpaces[row][col].setIsShipHere(true);
                    gridSpaces[row][col+1].setIsShipHere(true);

                    gridSpaces[row][col].setIcon(new ImageIcon("pics/patrol_boat_1 copy.png"));
                    gridSpaces[row][col+1].setIcon(new ImageIcon("pics/patrol_boat_2 copy.png"));

                    gridSpaces[row][col].setWhichShip(SocketSignals.SUBMARINE_INT);
                    gridSpaces[row][col+1].setWhichShip(SocketSignals.SUBMARINE_INT);

                    gridSpaces[row][col].setShipPiece(SocketSignals.FIRST_PIECE);
                    gridSpaces[row][col+1].setShipPiece(SocketSignals.SECOND_PIECE);

                    gridSpaces[row][col].setIsVertical(false);
                    shipAssigned = true;
                }
            }
        }
        return true;
    }


    public class CellPane extends JLabel {

        private Color defaultBackground = Color.BLACK;
        private boolean isClicked = false;
        private boolean isDone = false;
        private boolean isShipHere = false;
        private int row;
        private int column;
        private int whichShip; //1 - 5 carrier, battleship, destroyer, sub, patrol boat
        private int shipPiece; //1 top of ship
        private boolean isShipVertical = true;



        public CellPane(int r, int c) {

            setIcon(new ImageIcon("pics/wave.jpg"));
            setOpaque(true);
            setForeground(defaultBackground);
            setMinimumSize(new Dimension(50, 50));
            setMaximumSize(new Dimension(50, 50));
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

        public int getWhichShip() {
            return whichShip;
        }

        public void setWhichShip(int whichShip) {
            this.whichShip = whichShip;
        }

        public int getShipPiece() {
            return shipPiece;
        }

        public void setShipPiece(int shipPiece) {
            this.shipPiece = shipPiece;
        }

        public void clearCell(){
            isShipHere = false;
            whichShip = -1;
            shipPiece = -1;
            setIcon(new ImageIcon("pics/wave.jpg"));
            isShipVertical = true;

        }

        public void setIsVertical(boolean b){
            isShipVertical = b;
        }

        public boolean getIsVertical(){
            return isShipVertical;
        }
    }//close CellPane class

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

    public boolean markShot(int row, int col){
        //mark shot on your board

        if(gridSpaces[row][col].getIsShipHere() == true){
            SocketSignals.playSound("sounds/missile_launch.wav");
            switch(gridSpaces[row][col].getWhichShip()){
                case SocketSignals.CARRIER_INT:
                    carrierHits++;
                    break;
                case SocketSignals.BATTLESHIP_INT:
                    battleshipHits++;
                    break;
                case SocketSignals.DESTROYER_INT:
                    destroyerHits++;
                    break;
                case SocketSignals.SUBMARINE_INT:
                    submarineHits++;
                    break;
                case SocketSignals.PATROL_BOAT_INT:
                    patrolBoatHits++;
                    break;
            }
            gridSpaces[row][col].setIcon(null);
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_HIT);
            gridSpaces[row][col].setIcon(new ImageIcon("pics/giphy.gif"));
            gridSpaces[row][col].setIsDone(true);
            return true;
        }else{
            SocketSignals.playSound("sounds/splash.wav");
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_MISS);
            gridSpaces[row][col].setIcon(new ImageIcon("pics/x.jpg"));
            gridSpaces[row][col].setIsDone(true);
            return false;
        }


    }

    public void markShot(int row, int col, boolean hit){
        //mark shot on opponents board

        if(hit == true){
            SocketSignals.playSound("sounds/missile_launch.wav");
            gridSpaces[row][col].setIcon(null);
            gridSpaces[row][col].setIcon(new ImageIcon("pics/giphy.gif"));
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_HIT);
        }else{
            SocketSignals.playSound("sounds/splash.wav");
            gridSpaces[row][col].setBackground(SocketSignals.BATTLESHIP_COLOR_SHIP_MISS);
            gridSpaces[row][col].setIcon(new ImageIcon("pics/x.jpg"));
        }
        gridSpaces[row][col].setIsDone(true);

    }

    public boolean isGameOver(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(gridSpaces[i][j].isShipHere && !gridSpaces[i][j].isDone){
                    return false;
                }
            }
        }
        return true;
    }
}


