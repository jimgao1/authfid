import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by jacky on 28/11/15.
 */
public class TOTPTokenUpdater extends Thread{
    public JButton token;
    public JProgressBar progress;
    public String secretKey;

    public TOTPTokenUpdater(JButton l, JProgressBar prog, String secret){
        this.token = l;
        this.progress = prog;
        this.secretKey = secret;
    }

    public void updateKey(String secret){
        this.secretKey = secret;
        this.secretKey = processSecret(this.secretKey);
        //token.setText(getKey());
    }

    public static String execCmd(String cmd) throws Exception {
        Process p = Runtime.getRuntime().exec(cmd);
        System.out.println(cmd);
        java.util.Scanner s = new java.util.Scanner(p.getInputStream()).useDelimiter("\\A");
        System.out.println("Process Exit: " + p.waitFor());
        return s.next();
    }

    public String processSecret(String sec){
        String result = "";

        for (int i = 0; i < sec.length(); i++){
            if (sec.charAt(i) >= '0' && sec.charAt(i) <= '9'){
                result += sec.charAt(i);
            } else if (sec.charAt(i) >= 'A' && sec.charAt(i) <= 'Z'){
                result += sec.charAt(i);
            } else if (sec.charAt(i) >= 'a' && sec.charAt(i) <= 'z'){
                result += Character.toUpperCase(sec.charAt(i));
            }
        }

        return result;
    }

    public String getKey(){
        String unixTime = Long.toString(System.currentTimeMillis() / 1000L);

        try{
            String response = execCmd("oathtool --totp -b " + this.secretKey);
            return response;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void run(){
        while(true){
            try{
                Thread.sleep(50);
                MainWindow.runningTime += 50;

                token.setText(getKey());
                System.out.println("New Key: " + getKey());

                int sec = 30 - (int)((System.currentTimeMillis() / 1000L ) % 30);
                System.out.println("Time left: " + sec);
                progress.setValue(sec);
                //progress.getParent().repaint();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
