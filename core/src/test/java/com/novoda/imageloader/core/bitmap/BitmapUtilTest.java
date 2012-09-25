package com.novoda.imageloader.core.bitmap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BitmapUtilTest {

	@Test
	public void testCalculateScale() {				
		BitmapUtil bmUtil = new BitmapUtil();
		int scale = bmUtil.calculateScale(100, 50, 40);		
		assertEquals(scale, 1);

		scale = bmUtil.calculateScale(100, 150, 240);		
		assertEquals(scale, 1);

		scale = bmUtil.calculateScale(100, 250, 240);		
		assertEquals(scale, 2);
		
		scale = bmUtil.calculateScale(100, 350, 340);		
		assertEquals(scale, 2);

		scale = bmUtil.calculateScale(100, 450, 440);		
		assertEquals(scale, 4);
}

}
