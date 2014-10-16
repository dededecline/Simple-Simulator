package cs429;

/* instruction encoding
 * 
 * 0 00000 00    halt
 * 
 * 0000 11 dd    jle   dd
 * 0001 10 dd    outch dd
 * 0001 11 dd    show  dd
 * 0010 00 dd    ldi   dd
 * 1000 ss dd    add   ss dd
 * 1011 ss dd    cmp   ss dd
 * 
 */

public class Simulator {

	public static boolean DEBUG = false;

	public static void debug(String msg, Object... args) {
		if (DEBUG) {
			System.out.printf(msg, args);
		}
	}

	public static void outch(byte val) {
		System.out.print((char)val);
	}

	public static void show(int reg, byte val) {
		System.out.print("reg[" + reg + "] = " + val + " 0x");
		System.out.printf("%x\n", val);
	}

	public static byte run(byte mem[]) {
		byte[] reg = {0, 0, 0, 0};
		int pc = 0;
		byte results = 0;
		while (mem[pc] != 0){
			byte inst = mem[pc++];
			if((inst & 0xfc) == 0x0c){
				if(results <= 0){
					pc = reg[inst & 3];
				}
			}
			else if((inst & 0xfc) == 0x18){
				outch(reg[inst & 3]);
			}
			else if((inst & 0xfc) == 0x1c){
				show(inst & 3, reg[inst & 3]);
			}
			else if((inst & 0xfc) == 0x20){
				reg[inst & 3] = mem[pc++];
			}
			else if((inst & 0xf0) == 0x80){
				reg[inst & 3] += reg[(inst & 12) >> 2];
			}
			else if((inst & 0xf0) == 0xb0){
				results = (byte)(reg[inst & 3] - reg[inst & 12]);
			}
			else{
				System.out.printf("--Error: Unrecognized Command - 0x%x\n", inst);
			}
		}
		return (byte) pc;
	}
}
