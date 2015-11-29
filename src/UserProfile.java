import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jacky on 28/11/15.
 */
public class UserProfile implements Serializable{
    public static class ProfileAuthInfo{
        public String profileName;
        public String profileSecret;
        public String iconFileName;

        public ProfileAuthInfo(String name, String secret){
            this.profileName = name;
            this.profileSecret = secret;
            this.profileSecret = getSecretBase32();

        }

        public String getSecretBase32(){
            String result = "";

            for (int i = 0; i < profileSecret.length(); i++){
                if (profileSecret.charAt(i) >= '0' && profileSecret.charAt(i) <= '9'){
                    result += profileSecret.charAt(i);
                } else if (profileSecret.charAt(i) >= 'A' && profileSecret.charAt(i) <= 'Z'){
                    result += profileSecret.charAt(i);
                } else if (profileSecret.charAt(i) >= 'a' && profileSecret.charAt(i) <= 'z'){
                    result += Character.toUpperCase(profileSecret.charAt(i));
                }
            }

            this.profileSecret = result;
            return this.profileSecret;
        }
    }

    public String rfidIdentifier;
    public ArrayList<ProfileAuthInfo> services;

    public UserProfile(){
        this.services = new ArrayList<>();
    }

    public static ProfileAuthInfo parseRequest(String req){
        if (!req.startsWith("otpauth://")) return null;
        if (!req.contains("secret")) return null;

        String authSecret = req.substring(req.lastIndexOf('=') + 1);
        String authWebURL = req.substring(req.indexOf('@') + 1, req.lastIndexOf('?') - 1);

        return new ProfileAuthInfo(authWebURL, authSecret);
    }


}
