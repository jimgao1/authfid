import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    public Timer runTimer;

    public JPanel pnlProfiles;
    public JPanel pnlCenter;
    public JPanel pnlCountdown;

    public JLabel lblServiceName;
    public JButton lblAuthCode;
    public JLabel lblCompanyInfo;
    public JLabel lblUnixTime;

    public JProgressBar barCountdown;

    public ArrayList<JButton> btnProfiles;

    public TOTPTokenUpdater tokenUpdater;

    public boolean totpRunning = false;
    public static int runningTime = 0;

    public MainWindow(){
        try{
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex){}

        new LockThread().start();

        this.setSize(800, 480);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        this.setTitle("AuthFID");
        this.setVisible(true);

        pnlProfiles = new JPanel();
        pnlProfiles.setLayout(new GridLayout(1, 8));
        pnlProfiles.setPreferredSize(new Dimension(800, 100));

        btnProfiles = new ArrayList<>();

        this.add(pnlProfiles, BorderLayout.NORTH);

        pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());

        lblServiceName = new JLabel();
        lblServiceName.setFont(new Font("Courier New", Font.PLAIN, 25));
        lblServiceName.setHorizontalAlignment(SwingConstants.CENTER);
        lblServiceName.setVerticalAlignment(SwingConstants.BOTTOM);

        pnlCenter.add(lblServiceName, BorderLayout.NORTH);

        lblAuthCode = new JButton();
        lblAuthCode.setFont(new Font("Courier New", Font.BOLD, 40));
        lblAuthCode.setBackground(Color.white);
        lblAuthCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)e.getSource();
                String s = button.getText();
                s = s.trim();
                try {
                    Integer.parseInt(s);
                    Runtime.getRuntime().exec("sudo /home/pi/rf " + s);
                }
                catch(Exception ex){
                }
            }
        });
        lblAuthCode.setHorizontalAlignment(SwingConstants.CENTER);
        lblAuthCode.setVerticalAlignment(SwingConstants.CENTER);
        pnlCenter.add(lblAuthCode, BorderLayout.CENTER);

        lblUnixTime = new JLabel();
        lblUnixTime.setFont(new Font("Courier New", Font.PLAIN, 18));
        lblUnixTime.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUnixTime.setVerticalAlignment(SwingConstants.BOTTOM);

        pnlCenter.add(lblUnixTime, BorderLayout.SOUTH);
        this.add(pnlCenter, BorderLayout.CENTER);

        pnlCountdown = new JPanel();
        pnlCountdown.setLayout(new GridLayout(1, 1));
        pnlCountdown.setPreferredSize(new Dimension(800, 60));

        barCountdown = new JProgressBar(0, 30);
        barCountdown.setValue(30);
        pnlCountdown.add(barCountdown);
        this.add(pnlCountdown, BorderLayout.SOUTH);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.repaint();
        this.revalidate();

        new UnixTimeUpdater(this.lblUnixTime).start();
        tokenUpdater = new TOTPTokenUpdater(this.lblAuthCode, barCountdown, "IJFEIJFEWOIJFEWOFEOIEWF");

        /*try{
            updateMockProfiles();
        } catch (Exception ex){
            ex.printStackTrace();
        }*/
    }

    public void resetView(){
        this.lblAuthCode.setText("Please select a profile.");

        this.repaint();
        this.revalidate();
    }

    public void updateProfiles(UserProfile u){
        btnProfiles.clear();
        for (UserProfile.ProfileAuthInfo info : u.services){

            JButton cur = new JButton();
            cur.setBackground(Color.white);
            cur.setActionCommand(info.getSecretBase32() + "/" + info.profileName);
            try {
                BufferedImage image = ImageIO.read(MainWindow.class.getResourceAsStream("/res/icons/" + info.profileName + ".png"));
                BufferedImage output = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics g = output.createGraphics();
                ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.drawImage(image, 0, 0, 100, 100, null);
                cur.setIcon(new ImageIcon(output));
            }
            catch(Exception e) {}

            btnProfiles.add(cur);
        }

        pnlProfiles.removeAll();

        for (JButton b : btnProfiles) {
            b.addActionListener(this);
            pnlProfiles.add(b);
        }

        for (int i = 0; i < 7 - btnProfiles.size(); i++)
            pnlProfiles.add(new JLabel());

        JPanel misc = new JPanel();
        misc.setLayout(new GridLayout(2, 1));

        JButton btnAddProfile = new JButton("New");
        btnAddProfile.setActionCommand("btn_add_profile");
        btnAddProfile.addActionListener(this);

        misc.add(btnAddProfile);

        JButton quit = new JButton("Lock");
        quit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Runtime.getRuntime().exec("bash launch.sh");
                    Thread.sleep(1500);
                } catch (Exception ex){}
                System.exit(0);
            }
        });

        misc.add(quit);

        pnlProfiles.add(misc);

        this.repaint();
        this.revalidate();
    }

    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("btn_add_profile")) {
            String strTotp = JOptionPane.showInputDialog(null, "TOTP String");
            AuthFID.profile.services.add(UserProfile.parseRequest(strTotp));
        } else {
            LockThread.lastMove = System.currentTimeMillis();

            if (!totpRunning) {
                tokenUpdater.start();
                totpRunning = true;
            }

            lblServiceName.setText(e.getActionCommand().split("/")[1]);
            tokenUpdater.updateKey(e.getActionCommand().split("/")[0]);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        LockThread.lastMove = System.currentTimeMillis();
    }
}
