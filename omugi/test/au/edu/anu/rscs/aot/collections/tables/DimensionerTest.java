package au.edu.anu.rscs.aot.collections.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DimensionerTest {
	
	Dimensioner dim1, dim2, dim3;

	@BeforeEach
	private void init() {
		dim1 = new Dimensioner(14);
		dim2 = new Dimensioner("blue","red","orange");
		dim3 = new Dimensioner("blue","red","orange");
	}
	
//	private void show(String method,String text) {
//		System.out.println(method+": "+text);
//	}
	
	@Test
	void testDimensionerInt() {
		assertNotNull(dim1);
	}

	@Test
	void testDimensionerStringArray() {
		assertNotNull(dim2);
	}

	@Test
	void testGetLast() {
		assertEquals(dim1.getLast(),13);
		assertEquals(dim2.getLast(),2);
	}

	@Test
	void testGetLength() {
		assertEquals(dim1.getLength(),14);
		assertEquals(dim2.getLength(),3);
	}

	@Test
	void testGetName() {
		assertEquals(dim2.getName(1),"red");
		try {
			dim1.getName(12);
			fail("Name not found exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testGetIndex() {
		assertEquals(dim2.getIndex("blue"),0);
		try {
			dim1.getIndex("yellow");
			fail("No names exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
		try {
			dim2.getIndex("yellow");
			fail("Name not found exception not raised");
		}
		catch (Exception e) {
			// test OK
		}
	}

	@Test
	void testToString() {
//		show("testToString",dim1.toString());
//		show("testToString",dim2.toString());
		assertEquals(dim1.toString(),"[Dimensioner 0..13]");
		assertEquals(dim2.toString(),"[Dimensioner 0..2( blue red orange)]");
	}

	@Test
	void testEqualsObject() {
		assertFalse(dim1.equals(dim2));
		assertTrue(dim2.equals(dim3));
	}

}
