package vision.com.infoapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery {
	
	public MyGallery(Context context) {
        super(context);  
    }  
	public MyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  

    public MyGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);  
    }    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	if (velocityX > 0) {   
    	     super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
    	 }else{   
    	     super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
    	 }   
    	 return false;  
    }  
   
}
