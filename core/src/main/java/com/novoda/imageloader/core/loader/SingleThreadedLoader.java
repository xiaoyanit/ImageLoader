package com.novoda.imageloader.core.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.model.ImageWrapper;

public abstract class SingleThreadedLoader {

  private static final String TAG = "ImageLoader";
  
  private BitmapLoader thread = new BitmapLoader();
  private Stack<ImageWrapper> stack = new Stack<ImageWrapper>();
  private List<String> notFoundImages = new ArrayList<String>();

  public void push(ImageWrapper image) {
    if (TextUtils.isEmpty(image.getUrl())) {
      return;
    }
    pushOnStack(image);
    startThread();
  }

  public int size() {
  	synchronized (stack) {	    
  		return stack.size();
    }
  }

  public ImageWrapper pop() {
  	synchronized (stack) {
  		try {
  			return stack.pop();
  		} catch (Exception e) {
  			return null;
  		}
  	}
  }
  
  protected abstract Bitmap loadMissingBitmap(ImageWrapper iw);
  
  protected abstract void onBitmapLoaded(ImageWrapper iw, Bitmap bmp);

  private void clean(ImageWrapper p) {
  	synchronized (stack) {
      for (int j = 0; j < stack.size(); j++) {
        if (stack.get(j).getUrl() != null && stack.get(j).getUrl().equals(p.getUrl())) {
          stack.remove(j);
          j--;
        }
      }
  	}
  }

  private void pushOnStack(ImageWrapper p) {
  	synchronized (stack) {	    
  		stack.push(p);
    }
  }

  private class BitmapLoader extends Thread {
    boolean isWaiting = false;
    
    public BitmapLoader() {
    	setPriority(Thread.NORM_PRIORITY - 1);
    }

    @Override
    public void run() {
      while (true) {
        pauseThreadIfnecessary();	
        ImageWrapper image = pop();
        if(image != null) {
        	loadAndShowImage(image);
        }
      }
    }

    private void pauseThreadIfnecessary() {
      if (size() != 0) {
      	return;
      }
      synchronized (thread) {
        try {
          isWaiting = true;
          wait();
        } catch (Exception e) {
          Log.v(TAG, "Pausing the thread error " + e.getMessage());
        }
      }
    }

    private void loadAndShowImage(ImageWrapper iw) {
      try {
      	if(!iw.getUrl().equals(iw.getCurrentUrl())) {
      		return;
      	}
        Bitmap bmp = loadMissingBitmap(iw);
        if (bmp == null) {
          clean(iw);
          onBitmapLoaded(iw, bmp);
          return;
        }
        onBitmapLoaded(iw, bmp);
      } catch(ImageNotFoundException inf) {
        notFoundImages.add(iw.getUrl());
      } catch (Throwable e) {
        Log.e(TAG, "Throwable : " + e.getMessage(), e);
      }
    }
  }
  
	private void startThread() {
	  if (thread.getState() == Thread.State.NEW) {
      thread.start();
      return;
    }
    synchronized (thread) {
      if (thread.isWaiting) {
        try {
          thread.isWaiting = false;
          thread.notify();
        } catch (Exception ie) {
          Log.e(TAG, "Check and resume the thread " + ie.getMessage());
        }
      }
    }
  }

}
