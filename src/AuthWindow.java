import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AuthWindow extends JFrame {

    public JLabel lblWelcome;

    public AuthWindow(){
        this.setLocation(0, 0);
        this.setSize(800, 480);
        this.setLayout(new BorderLayout());
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("AuthFID");

        lblWelcome = new JLabel("Please swipe your RFID card...");
        lblWelcome.setFont(new Font("Courier New", Font.BOLD, 40));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setVerticalAlignment(SwingConstants.CENTER);

        this.add(lblWelcome, BorderLayout.CENTER);

        this.repaint();
        this.revalidate();

    }



}
