package cloudteam.hicube.utils;

/**
 * MD5 加密 32位加密
 * 
 * @author zhiyong.jing
 */
public class MD5 {

	public static String getMD5(String instr) {
		String s = null;
		// 用来将字节转换成 16 进制表示的字?
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(instr.getBytes());
			byte tmp[] = md.digest();

			char str[] = new char[16 * 2];

			int k = 0;
			for (int i = 0; i < 16; i++) {

				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str).toUpperCase(); // 换后的结果转换为字符?

		} catch (Exception e) {

		}
		return s;
	}

}
