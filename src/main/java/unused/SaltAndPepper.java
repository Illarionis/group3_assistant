package unused;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
//https://www.javainterviewpoint.com/java-salted-password-hashing/
//https://stackoverflow.com/questions/16891729/best-practices-salting-peppering-passwords
//resource
//Problem : Add the pepper at some point in there

public class SaltAndPepper {
        public static void main(String[] args){
            String password = "goodmorning123"; //get Passwords from user login
            MessageDigest md;
            try
            {
                md = MessageDigest.getInstance("SHA-256");
                // Generate the random salt
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16];
                random.nextBytes(salt);
                // Passing the salt to the digest for the computation
                md.update(salt);
                // Generate the salted hash
                byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedPassword)
                    sb.append(String.format("%02x", b));
                System.out.println("-----------------------");
                System.out.println(sb);
                System.out.println("-----------------------");
            } catch (NoSuchAlgorithmException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


