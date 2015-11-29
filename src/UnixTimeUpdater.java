import javax.swing.*;

/**
 * Created by jacky on 28/11/15.
 */
public class UnixTimeUpdater extends Thread{
    public JLabel time;

    public UnixTimeUpdater(JLabel fuck){
        this.time = fuck;
    }

    public void run(){
        while(true) {
            try {
                Thread.sleep(500);
                time.setText("Current UNIX time: " + Long.toString(System.currentTimeMillis() / 1000L));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
