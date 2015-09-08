package passcrambler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESCipher {
	
	protected byte[] key;
	protected byte[] seed;
	
	public byte[] encrypt(byte[] mes) 
	        throws NoSuchAlgorithmException,
	        NoSuchPaddingException,
	        InvalidKeyException,
	        InvalidAlgorithmParameterException,
	        IllegalBlockSizeException,
	        BadPaddingException, IOException {

	    AlgorithmParameterSpec ivSpec = new IvParameterSpec(this.seed);
	    SecretKeySpec newKey = new SecretKeySpec(this.key, "AES");
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
	    
	    byte[] out = cipher.doFinal(mes);
	    byte[] joined = new byte[this.seed.length + out.length];
	    
	    System.arraycopy(seed, 0, joined, 0, seed.length);
	    System.arraycopy(out, 0, joined, seed.length, out.length);
	    
	    return DatatypeConverter.printBase64Binary(joined).getBytes();
	}
	
	public AESCipher(byte[] vec, byte[] key) throws NoSuchAlgorithmException
	{
		this.seed = vec;
		this.key = key;
	}

}
