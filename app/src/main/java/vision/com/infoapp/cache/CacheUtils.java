package vision.com.infoapp.cache;

import android.graphics.Bitmap;

import vision.com.infoapp.utils.HttpUtils;

public class CacheUtils {
	private ImageMemoryCache memoryCache;
	private ImageFileCache fileCache;
	public CacheUtils(ImageMemoryCache p_memoryCache,ImageFileCache p_fileCache){
		this.memoryCache = p_memoryCache;
		this.fileCache 	 = p_fileCache;
	}
	public Bitmap getBitmap(String url) {
	    Bitmap result = memoryCache.getBitmapFromCache(url);
	    if (result == null) {
	        result = fileCache.getImage(url); 
	        if (result == null) { 
	            result = HttpUtils.downloadBitmap(url);
	            if (result != null) { 
	            	System.out.println("--load image from http server--------");
	                fileCache.saveBitmap(result, url); 
	                memoryCache.addBitmapToCache(url, result); 
	            } 
	        } else { 
	        	System.out.println("--load image from file--------");
	            memoryCache.addBitmapToCache(url, result);
	        } 
	    } else{
	    	System.out.println("--load image from memeroy cache--------");
	    }
	    return result; 
	}
}
