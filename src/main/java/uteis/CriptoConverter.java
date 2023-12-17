package uteis;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CriptoConverter implements AttributeConverter<String, String> {

	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String KEY = "Ch4v3SupS3cr3t4!";

	private static Key SECRET_KEY_SPEC = null;
	private static Cipher CIPHER_INSTANCE = null;

	private Cipher getCipherInstance(int mode) throws InvalidKeyException {
		if (CIPHER_INSTANCE == null) {
			try {
				SECRET_KEY_SPEC = new SecretKeySpec(KEY.getBytes(), "AES");
				CIPHER_INSTANCE = Cipher.getInstance(ALGORITHM);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		CIPHER_INSTANCE.init(mode, SECRET_KEY_SPEC);
		return CIPHER_INSTANCE;
	}

	@Override
	public String convertToDatabaseColumn(String value) {
		if (value == null) {
			return null;
		}
		try {
			byte[] cipherText = this.getCipherInstance(Cipher.ENCRYPT_MODE).doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(cipherText);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		try {
			byte[] encypted = Base64.getDecoder().decode(value.getBytes());
			byte[] decrypted = this.getCipherInstance(Cipher.DECRYPT_MODE).doFinal(encypted);
			return new String(decrypted);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
}
