/**
 * Created by Sean on 2/23/2016.
 */
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

/*
class DragMouseAdapter extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, e, TransferHandler.COPY);
    }
}
*/
public class IconDnD {
    public static void main(String[] args) throws MalformedURLException {
        JFrame f = new JFrame("Icon Drag & Drop");
        ImageIcon icon1 = null;

            icon1 = new ImageIcon(new URL("http://i.stack.imgur.com/1lgtq.png"));

        ImageIcon icon2 = new ImageIcon(new URL("http://i.stack.imgur.com/8BGfi.png"));
        ImageIcon icon3 = new ImageIcon(new URL("http://i.stack.imgur.com/gJmeJ.png"));

        JButton button = new JButton(icon2);

        JLabel label1 = new JLabel(icon1, JLabel.CENTER);
        JLabel label2 = new JLabel(icon3, JLabel.CENTER);

        MouseListener listener = new DragMouseAdapter2();
        label1.addMouseListener(listener);
        label2.addMouseListener(listener);

        label1.setTransferHandler(new TransferHandler("icon"));
        button.setTransferHandler(new TransferHandler("icon"));
        label2.setTransferHandler(new TransferHandler("icon"));

        f.setLayout(new FlowLayout());
        f.add(label1);
        f.add(button);
        f.add(label2);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setVisible(true);
    }
    public static class DragMouseAdapter2 extends MouseAdapter {


        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.COPY);
        }

    }
}
