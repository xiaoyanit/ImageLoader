package com.novoda.imageloader.acceptance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.Suppress;

import com.novoda.imageloader.core.bitmap.BitmapUtil;

public class BitmapUtilsInstrumentationTest extends InstrumentationTestCase {

	public BitmapUtilsInstrumentationTest(String name) {
		super();
		setName(name);
	}

    @Suppress
	public void testScaleBitmap(){
		Bitmap bmOriginal = BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(), R.drawable.icon);
		assertTrue(bmOriginal.getHeight() <= 72);
		assertTrue(bmOriginal.getWidth() <= 72);
		
		Bitmap bm = new BitmapUtil().decodeResourceBitmapAndScale(getInstrumentation().getContext(), 200, 200, R.drawable.icon, true);		
		
		assertEquals(200, bm.getHeight());
		assertEquals(200, bm.getWidth());
		
		bm = new BitmapUtil().decodeResourceBitmapAndScale(getInstrumentation().getContext(), 200, 200, R.drawable.icon, false);		

		assertEquals(bmOriginal.getHeight(), bm.getHeight());
		assertEquals(bmOriginal.getWidth(), bm.getWidth());

		bm = new BitmapUtil().scaleBitmap(bmOriginal, 200, 200, false);		
		assertEquals(bmOriginal, bm);
		
		bm = new BitmapUtil().scaleBitmap(bmOriginal, 200, 200, true);		
		assertNotSame(bmOriginal, bm);
		assertEquals(200, bm.getHeight());
		assertEquals(200, bm.getWidth());

	}

	
}
