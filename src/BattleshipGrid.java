import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jskarda on 2/16/16.
 */
public class BattleshipGrid extends JPanel {
    public BattleshipGrid() {
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
            }
        }
    }

    public class CellPane extends JPanel {

        private Color defaultBackground = Color.BLACK;
        private boolean isClicked = false;

        public CellPane() {
            setBackground(defaultBackground);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    //defaultBackground = Color.BLACK;
                    //setBackground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //setBackground(defaultBackground);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isClicked){
                        setBackground(defaultBackground);
                    } else {
                        setBackground(Color.WHITE);
                    }
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }
    }
}


