package vision.com.infoapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import vision.com.infoapp.Globals;
import vision.com.infoapp.cache.CacheUtils;
import vision.com.infoapp.cache.ImageFileCache;
import vision.com.infoapp.cache.ImageMemoryCache;

public class AsyncImageLoader {

	 private HashMap<String, SoftReference<Drawable>> imageCache;
	  
	     public AsyncImageLoader() {
	    	 imageCache = new HashMap<String, SoftReference<Drawable>>();
	     }
	  
	     public Drawable loadDrawable(final String id, final String imageUrl, final ImageCallback imageCallback) {
	    	if(null==imageUrl || "".equals(imageUrl)) return null;
    		int _index = imageUrl.lastIndexOf("/");
    		String _img_name = imageUrl.substring(_index+1);
    		if(null==_img_name || "".equals(_img_name) || "null".equals(_img_name)) return null;
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	                 imageCallback.imageLoaded((Drawable) message.obj, imageUrl,id);
	             }
	         };
	         new Thread() {
	             @Override
	             public void run() {
	 				int _index = imageUrl.lastIndexOf("/");
	 				String _img_name = imageUrl.substring(_index+1);
	 				String _path = Globals.PHONE_PIC_TEMP_DIR + _img_name;
	 				
	 				Drawable drawable = null;
	 				if(new File(_path).exists()){
	 					drawable = Drawable.createFromPath(_path);
	 				}else{
	 					drawable = loadImageFromUrl(imageUrl);
	 				}
	 				
//	 				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
	 				Message message = handler.obtainMessage(0, drawable);
	 				handler.sendMessage(message);
	             }
	         }.start();
	         return null;
	     }
	     public Drawable loadDrawable(final Context context, final String id, final String imageUrl, final ImageCallback imageCallback) {
		    	if(null==imageUrl || "".equals(imageUrl)) return null;
	    		int _index = imageUrl.lastIndexOf("/");
	    		String _img_name = imageUrl.substring(_index+1);
	    		if(null==_img_name || "".equals(_img_name) || "null".equals(_img_name)) return null;
	    		
		         final Handler handler = new Handler() {
		             public void handleMessage(Message message) {
		                 imageCallback.imageLoaded((Drawable) message.obj, imageUrl,id);
		             }
		         };
		         new Thread() {
		             @Override
		             public void run() {
//		 				int _index = imageUrl.lastIndexOf("/");
//		 				String _img_name = imageUrl.substring(_index+1);
//		 				String _path = Globals.PHONE_PIC_TEMP_DIR + _img_name;
		 				ImageMemoryCache _imageMemoryCache = new ImageMemoryCache(context);
		 				ImageFileCache _imageFileCache   = new ImageFileCache();
		 				CacheUtils _cacheUtils = new CacheUtils(_imageMemoryCache, _imageFileCache);
		 				System.out.println(imageUrl);
		 				Bitmap _bitMap= _cacheUtils.getBitmap(imageUrl);
		 				Drawable _drawable = null;
		 				if(null!=_bitMap){
		 					_drawable = new BitmapDrawable(_bitMap);
		 				}
//		 				Drawable drawable = null;
//		 				if(new File(_path).exists()){
//		 					drawable = Drawable.createFromPath(_path);
//		 				}else{
//		 					drawable = loadImageFromUrl(imageUrl);
//		 				}
//		 				
//		 				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
		 				Message message = handler.obtainMessage(0, _drawable);
		 				handler.sendMessage(message);
		             }
		         }.start();
		         return null;
		     }
	     public InputStream loadDrawable1(final String id, final String imageUrl, final ImageInputStreamCallback imageCallback) {
//	         if (imageCache.containsKey(imageUrl)) {
//	             SoftReference<Drawable> softReference = imageCache.get(imageUrl);
//	             Drawable drawable = softReference.get();
//	             if (drawable != null) {
//	                 return drawable;
//	             }
//	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	                 imageCallback.imageLoaded((InputStream) message.obj, imageUrl,id);
	             }
	         };
	         new Thread() {
	             @Override
	             public void run() {
	                 InputStream is = loadImageFromUrl1(imageUrl);
//	     			 Drawable drawable = Drawable.createFromStream(is, "src");
//
//	                 imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
	                 Message message = handler.obtainMessage(0, is);
	                 handler.sendMessage(message);
	             }
	         }.start();
	         return null;
	     }
	 	public InputStream loadImageFromUrl1(String url) {
			URL m;
			InputStream i = null;
			try {
				m = new URL(url);
				i = (InputStream) m.getContent();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(null!=i){
					System.out.println("---size=" + Integer.toString(i.available()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return i;
		}
		
		public static Drawable loadImageFromUrl(String url) {
			URL m;
			InputStream i = null;
			try {
				m = new URL(url);
				i = (InputStream) m.getContent();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(null!=i){
					System.out.println("---size=" + Integer.toString(i.available()));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Drawable d = Drawable.createFromStream(i, "src");
			return d;
		}
		
	  
	     public interface ImageCallback {
	         public void imageLoaded(Drawable imageDrawable, String imageUrl, String id);
	     }
	     public interface ImageInputStreamCallback {
	         public void imageLoaded(InputStream is, String imageUrl, String id);
	     }

}
