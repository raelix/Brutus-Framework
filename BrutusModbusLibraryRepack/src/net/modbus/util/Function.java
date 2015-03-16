package net.modbus.util;

import net.modbus.Modbus;

public interface Function extends Modbus{
	public static final int LIMIT_RTU = 1;
	public static final int LIMIT_TCP = 10;
	public static final int LIMIT_DB = 1;
	public final static int RTU = 0;
	public final static int TCP = 1;
	public final static int INT = 1;
	public final static int WORD = 2;
	public final static int FLOAT = 3;
	public final static int DOUBLE = 4;
	public final static int SIGNED = 0;
	public final static int UNSIGNED = 1;
	public final static int READ_INPUT_REGISTERS = 4;
	public final static int READ_MULTIPLE_REGISTERS = 3;
	public static final int WRITE_SINGLE_REGISTER = 6;
	public final static int READ_INPUT_DISCRETES = 2;
	public final static int READ_COILS = 1;
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
}
