public class T08_TimingAuth_MAL {

	public static void main(String[] args) throws Exception {
		System.out.println(hexstringToUuid("000102030405060708090a0b0c0d0e0f"));

	}

	private static String littleendianToUuid(int time) {

		return null;
	}

	private static String hexstringToUuid(String token) {
		// String token = "00000000000000000000000000000000";
		// String token = "000102030405060708090a0b0c0d0e0f";
		int[] bytes = hexStringToByteArray(token);
		int[] shorts = bytesToShort(bytes);
		return String.format("%04x%04x-%04x-%04x-%04x-%04x%04x%04x", shorts[0], shorts[1], shorts[2],
				shorts[3] & 0x0fff | 0x4000, shorts[4] & 0x3fff | 0x8000, shorts[5], shorts[6], shorts[7]);
	}

	private static int[] bytesToShort(int[] bytes) {
		int len = bytes.length / 2;
		int[] ret = new int[len];
		for (int i = 0; i < len; i++) {
			int a = bytes[i * 2];
			int b = bytes[i * 2 + 1];
			ret[i] = (b << 8 | a);
		}
		return ret;
	}

	public static int[] hexStringToByteArray(String s) {
		int len = s.length();
		int[] data = new int[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
