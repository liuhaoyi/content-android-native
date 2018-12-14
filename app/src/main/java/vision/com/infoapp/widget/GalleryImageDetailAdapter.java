package vision.com.infoapp.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GalleryImageDetailAdapter extends BaseAdapter {
	int mGalleryItemBackground;
	private Activity mContext;
	private List list = new ArrayList();
	public GalleryImageDetailAdapter(Activity context, List p_list) {
		this.mContext = context;
		this.list 	  = p_list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
//		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ImageAndText _imageAndText = (ImageAndText) getItem(position);
		final ImageView _imageView = new ImageView(mContext);
		_imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		_imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		_imageView.setImageDrawable(_imageAndText.getDrawable());
		return _imageView;
	}

}
