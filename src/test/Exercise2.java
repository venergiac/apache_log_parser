package test;


import java.util.Arrays;

public class Exercise2 {


	// simple sum bytye by byte
	private byte[] sumDigits(final byte[] x, final byte[] y) {

		byte[] z = new byte[Math.max(x.length, y.length)];

		int r = 0;
		int i=0;
		for (; i<z.length; i++) {
			int s = (int) (i<x.length ? x[i] : 0) 
					+ (int)(i<y.length ? y[i] : 0) 
					+ r;
			z[i] = (byte)(s % 10);
			r= (int)(s/10);
					
		}
		

		if (r>0) {
			byte[] zz = new byte[z.length+1];
			System.arraycopy(z, 0, zz, 0, z.length);
			zz[i] = (byte)r;
			z=zz;
		}
		
		return z;
	}
	
	private boolean equalsDigits(final byte[] x, final byte[] y) {
		
		for (int i=0; i<Math.max(x.length, y.length); i++)
			if ((i<x.length ? x[i] : 0) != (i<y.length ? y[i] : 0) )
				return false;
		
		return true;
				
	}
	

	// the core of the app
	private byte[] multiplyDigits(final byte[] x, final byte[] y) {

		byte[] r = new byte[1];
		byte[] ONE  =  new byte[] {1};
		
		for (byte[] i = new byte[1] ; !equalsDigits(i, x); i = sumDigits(i,ONE) ) {
			r = sumDigits(r,y);
		}
		
		return r;

	}

	public static void main(String[] args) throws Exception{
		
		// start with test
		test_sumDigits_0( );
		test_sumDigits_1( );
		test_sumDigits_2( );
		test_equalsDigits_0( );
		
		
		Exercise2 ex  = new Exercise2();
		
		
		byte[] res = ex.multiplyDigits(new byte[] { 5,1,0,1 }, new byte[] { 2,0,0,0 });

		if (!(Arrays.equals(res, new byte[] {0,3,0,2}))) {
			System.err.println("error");
		}
		
		System.out.println(Arrays.toString(res));

	}
	
	//unit test sumDigits
	private static void sumDigits_X(String name, byte[] x, byte[] y, byte[] attended ) throws Exception {
		
		Exercise2 ex  = new Exercise2();
		byte[] res = ex.sumDigits(x, y);

		if (!(Arrays.equals(res, attended))) {

			System.out.println(Arrays.toString(res) + "=/=" + Arrays.toString(attended));
			throw new Exception(name + " error");
		} else {
			System.out.println(name + " passed");
		}
	}
	
	
	//unit test sumDigits
	public static void test_sumDigits_0( ) throws Exception {
		
		
		sumDigits_X("test_sumDigits_0", new byte[] { 5,1,1,9 }, new byte[] {1}, new byte[] { 6,1,1,9 } );
	}
	
	//unit test sumDigits
	public static void test_sumDigits_2( ) throws Exception {

		sumDigits_X("test_sumDigits_2", new byte[] { 5,1,1,9 }, new byte[] { 6,1,0,1 }, new byte[] {1, 3, 1, 0, 1} );
	}
	
	//unit test sumDigits
	public static void test_sumDigits_1( ) throws Exception {
		
		sumDigits_X("test_sumDigits_1", new byte[] { 5,1,1,9 }, new byte[] { 0 }, new byte[] {5,1,1,9} );

	}
	
	
	//unit test sumDigits
	public static void test_equalsDigits_0( ) throws Exception {
		
		Exercise2 ex  = new Exercise2();
		boolean r = ex.equalsDigits(new byte[] {5,1,1,9}, new byte[] {5,1,1,9});

		if (!r) {

			System.out.println();
			throw new Exception( "test_equalsDigits_0 error");
		} else {
			System.out.println( "test_equalsDigits_0 passed");
		}

	}

}
