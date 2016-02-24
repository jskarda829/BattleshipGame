import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jskarda on 2/16/16.
 */
public class BattleshipGrid extends JPanel {


    MouseListener listener;

    public BattleshipGrid () {


        listener = new DragMouseAdapter();
                setLayout(new BorderLayout());
                add(new TestPane());
                setVisible(true);
                setOpaque(true);



    }

    public class TestPane extends JPanel {
        private CellPane[][] gridSpaces;

        public TestPane()  {
            setLayout(new GridBagLayout());
            gridSpaces = new CellPane[10][10];
            GridBagConstraints gbc = new GridBagConstraints();
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;

                    CellPane cellPane = new CellPane();
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
    }
    public class CellPane extends JLabel {

        private Color defaultBackground = Color.BLACK;
        private boolean isClicked = false;
        private boolean isShipHere;
        public CellPane() {
            setOpaque(true);
            setForeground(defaultBackground);
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e){
                    System.out.println("F");
                    setForeground(Color.BLUE);
                    setBackground(Color.BLUE);
                    repaint();
                }
            });
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
    }

    class DragMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.COPY);
        }
    }



}


