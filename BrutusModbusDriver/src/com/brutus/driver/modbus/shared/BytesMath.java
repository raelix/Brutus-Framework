package com.brutus.driver.modbus.shared;

public class BytesMath{
	/* define Order B = BigEndian , L = LittleEndian, Default = B; 
	 * define Data U = Unsigned , S = Signed, Default = S; 
	 * define prototype:
	 * define INT = 2 byte , default U,B;
	 * define WORD = 4 byte, default S,B;  if assign Unsigned return long 
	 * define FLOAT = 4byte, default S,B;  S is option if assign Unsigned no effects return float
	 * define DOUBLE = 8byte, default S,B; S is option  if assign Unsigned no effects return double
	 * Max Expression es. INT,U,B
	 * Min Expression es. INT
	 */
	
	public static final int twoBytesToUnsignedShort(byte[] bytes) {
		return ((bytes[0] & 0xff) << 8 | (bytes[1] & 0xff));
	}

	public static final short twoBytesToShort(byte[] bytes) {
		return (short) ((bytes[0] << 8) | (bytes[1] & 0xff));
	}

	public static final int fourBytesToInt(byte[] bytes) {
		return (((bytes[0] & 0xff) << 24) |
				((bytes[1] & 0xff) << 16) |
				((bytes[2] & 0xff) << 8) |
				(bytes[3] & 0xff)
				);
	}

	public static final int fourBytesToInt(byte[] bytes0,byte[] bytes1) {
		return (((bytes0[0] & 0xff) << 24) |
				((bytes0[1] & 0xff) << 16) |
				((bytes1[0] & 0xff) << 8) |
				(bytes1[1] & 0xff)
				);
	}

	public static final long fourBytesToIntUnsigned (byte[] b1){
		return (long) (b1[0] & 0xFF) << 24 | (b1[1] & 0xFF) << 16 | (b1[2] & 0xFF) << 8 | (b1[3] & 0xFF);
	}

	public static final long fourBytesToIntUnsigned (byte[] b1, byte[] b2){
		return (long) (b1[0] & 0xFF) << 24 | (b1[1] & 0xFF) << 16 | (b2[0] & 0xFF) << 8 | (b2[1] & 0xFF);
	}

	public static final float fourBytesToFloat(byte[] bytes) {
		return Float.intBitsToFloat((
				((bytes[0] & 0xff) << 24) |
				((bytes[1] & 0xff) << 16) |
				((bytes[2] & 0xff) << 8) |
				(bytes[3] & 0xff)
				));
	}

	public static final float fourBytesToFloat(byte[] bytes0,byte[] bytes1) {
		return Float.intBitsToFloat((
				((bytes0[0] & 0xff) << 24) |
				((bytes0[1] & 0xff) << 16) |
				((bytes1[0] & 0xff) << 8) |
				(bytes1[1] & 0xff)
				));
	}


	public static double eightBytesArrayToDouble(byte[] bytes1,byte[] bytes2,byte[] bytes3,byte[] bytes4) {
		return Double.longBitsToDouble(eightBytesArrayToLong(bytes1, bytes2,bytes3,bytes4));
	}


	public static long eightBytesArrayToLong(byte[] bytes1,byte[] bytes2,byte[] bytes3,byte[] bytes4) {
		return (long) ( 
				(long) (0xFF & bytes4[1]) << 56
				| (long) (0xFF & bytes4[0]) << 48
				| (long) (0xFF & bytes3[1]) << 40
				| (long) (0xFF & bytes3[0]) << 32
				| (long) (0xFF & bytes1[1]) << 24
				| (long) (0xFF & bytes2[0]) << 16
				| (long) (0xFF & bytes1[1]) << 8
				| (long) (0xFF & bytes1[0]) << 0);
	}


}