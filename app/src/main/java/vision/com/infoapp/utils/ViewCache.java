package vision.com.infoapp.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vision.com.infoapp.R;


public class ViewCache {

	    private View baseView;
	    private TextView titleView;
	    private TextView briefView;

	    private ImageView imageView;

	    public ViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public TextView getTitleView() {
	        if (titleView == null) {
	        	
	            titleView = (TextView) baseView.findViewById(R.id.text);
	        }
	        return titleView;
	    }

	    public TextView getBriefView() {
	        if (briefView == null) {	        	
	        	briefView = (TextView) baseView.findViewById(R.id.text1);
	        }
	        return briefView;
	    }
	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.image);
	        }
	        return imageView;
	    }

}
