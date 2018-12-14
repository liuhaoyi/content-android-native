package vision.com.infoapp.widget;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Activity mContext;

    //	private Integer[] mImageIds =null;
    private List list = new ArrayList();

    public GalleryImageAdapter(Activity context, List p_list) {
        this.mContext = context;
        this.list = p_list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position % list.size());
//		return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int _screen_width = displayMetrics.widthPixels;
        final ImageAndText _imageAndText = (ImageAndText) getItem(position);
        final ImageView _imageView = new ImageView(mContext);
        _imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        _imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        _imageView.setImageDrawable(_imageAndText.getDrawable());
        return _imageView;
    }

}
