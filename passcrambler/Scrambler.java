package passcrambler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class Scrambler {
	
	public class ScramblerSettings
	{
		String specialchars;
		int maxLen;
		
		public ScramblerSettings()
		{
			this.specialchars = "_&#";
			this.maxLen = 30;
		}
		
		public ScramblerSettings(String specialchars, int maxLen)
		{
			this.specialchars = specialchars; // "_&#";
			this.maxLen = maxLen;
		}
		
		private String convertToCharset(String password, String specialchars)
		{
		    String output = "";
		    int j = 0;
		    for (int i = 0; i < password.length(); i++) {
		    	char c = password.charAt(i);
		    	if (c >= 'A' && c <= 'Z') {
		    		output += c;
		    		continue;
		    	}
		    	if (c >= 'a' && c <= 'z') {
		    		output += c;
		    		continue;
		    	}
		    	if (c >= '0' && c <= '9') {
		    		output += c;
		    		continue;
		    	}
		    	output += specialchars.charAt(j % specialchars.length());
		    	j++;
		    }
			return output;
		}
		
		protected String applySettings(String longPass)
		{
			return this.convertToCharset(longPass, this.specialchars).substring(0, this.maxLen);
		}
	}

	
	public static byte[] preprocess(String str) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(str.getBytes());
	}
	
	public static String hashString(byte []array)
	{
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
       }
        return sb.toString();
	}
	
	public static String generateLongpass(String login, String password, byte[] bytesArr, ScramblerSettings settings) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, IOException
	{
		byte []key = preprocess(password);
		AESCipher aes = new AESCipher(preprocess(login), key);
		byte []outArr = aes.encrypt(bytesArr);

		MessageDigest digest1 = MessageDigest.getInstance("SHA-512");
		byte[] sha_digest = digest1.digest(outArr);
		int offset = password.length() % sha_digest.length;
		
		byte[] key2 = new byte[32];
		for (int i = offset, j = 0; i < offset + 32; i++, j++) {
			key2[j] = sha_digest[i];
		}
		aes = new AESCipher(key, key2);
		byte []out2 = aes.encrypt(outArr);

		int key_0 = (int)key[0] & 0xFF;
		int start = key_0 % out2.length;
		byte []portion = Arrays.copyOfRange(out2, start, out2.length);
		
		MessageDigest digest2 = MessageDigest.getInstance("SHA-512");
		byte[] sha_diges2 = digest2.digest(portion);
		String longPass = DatatypeConverter.printBase64Binary(sha_diges2);
		return settings.applySettings(longPass);
	}
}
