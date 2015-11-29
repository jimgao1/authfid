/**
 * Created by jacky on 29/11/15.
 */
public class LockThread extends Thread {

    public static long lastMove = System.currentTimeMillis();

    public void run() {

        while(true) {
            try {
                if(System.currentTimeMillis() - lastMove > 30000) {
                    Runtime.getRuntime().exec("bash launch.sh");
                    Thread.sleep(1500);

                    System.exit(0);
                }

            } catch (Exception ex) {
            }
        }
    }
}
