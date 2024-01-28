import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {
    private static final String AES = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;

    // Generate a random 16 bytes recovery code
    public static String generateRecoveryCode() {
        byte[] recoveryCode = new byte[16];
        new SecureRandom().nextBytes(recoveryCode);
        return Base64.getEncoder().encodeToString(recoveryCode);
    }

    // Derive a key from the recovery code
    public static SecretKey deriveKeyFromRecoveryCode(String recoveryCode) {
        byte[] decodedKey = Base64.getDecoder().decode(recoveryCode);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    // Encrypt the MasterPassword
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        byte[] encryptedIVAndText = new byte[GCM_IV_LENGTH + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedBytes, 0, encryptedIVAndText, GCM_IV_LENGTH, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }

    // Decrypt the MasterPassword
    public static String decrypt(String encryptedIvTextBytes, SecretKey key) throws Exception {
        byte[] decodedEncryptedData = Base64.getDecoder().decode(encryptedIvTextBytes);

        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(decodedEncryptedData, 0, iv, 0, iv.length);
        int encryptedSize = decodedEncryptedData.length - GCM_IV_LENGTH;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(decodedEncryptedData, GCM_IV_LENGTH, encryptedBytes, 0, encryptedSize);

        Cipher cipher = Cipher.getInstance(AES);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
