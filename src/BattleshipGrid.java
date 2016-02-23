import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jskarda on 2/16/16.
 */
public class BattleshipGrid extends JFrame {


    public static void main(String[] args) {
        new BattleshipGrid();
    }

    public BattleshipGrid() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setSize(800,800);
            }
        });
    }

    public class TestPane extends JPanel {
        private CellPane[][] gridSpaces;
        public TestPane() {
            setLayout(new GridBagLayout());

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
        }
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(500,500);
        }
    }
    public class CellPane extends JLabel {

        private Color defaultBackground = Color.BLACK;
        private boolean isClicked;
        public CellPane() {
            setOpaque(true);
            setForeground(defaultBackground);
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e){
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
    }



}


