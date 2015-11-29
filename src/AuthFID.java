import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by jacky on 28/11/15.
 */
public class AuthFID {

    static {
        System.loadLibrary("SerialRFID");
    }

    public static UserProfile profile = new UserProfile();

    public static MainWindow window;
    public static AuthWindow authWindow;

    public static void readProfile(){
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("config.dat")));
            profile = (UserProfile)is.readObject();
            is.close();
        } catch (Exception ex) { /*I don't give a shit */ }
    }

    public static void writeProfile(){
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(("config.dat"))));
            os.writeObject(profile);
            os.close();
        } catch (Exception ex) { /*I don't give a shit */ }
    }

    public static void main(String[] args) {
        profile.services.add(new UserProfile.ProfileAuthInfo("Google", "NSDCNZHIX22UCY4IEDITV76ODQDBDFM7"));
        profile.services.add(new UserProfile.ProfileAuthInfo("Facebook", "HXDMVJECHHWSRB3HWIZR4IFUGFTMXBOZ"));
        profile.services.add(new UserProfile.ProfileAuthInfo("GitHub", "qvzezxbeyl6mib3x".toUpperCase()));
        profile.services.add(new UserProfile.ProfileAuthInfo("DigitalOcean", "w5g522vnoyf53wpu".toUpperCase()));

        AuthWindow authWindow = new AuthWindow();

        while (true) {
            String str = SerialRFID.read();
            System.out.println("str = " + str);

            if (str.trim().equals("0bbba285")) break;
            try {
                authWindow.lblWelcome.setForeground(Color.RED);
                authWindow.lblWelcome.setText("Incorrect Card ID. ");
                Thread.sleep(2000);
                authWindow.lblWelcome.setForeground(Color.black);
                authWindow.lblWelcome.setText("Please swipe your RFID card...");
            } catch (Exception ex) {

            }
        }

        authWindow.setVisible(false);
        authWindow.dispose();

        MainWindow window = new MainWindow();
        window.setVisible(true);
        window.resetView();
        window.updateProfiles(profile);

    }
}
