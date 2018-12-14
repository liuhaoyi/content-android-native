package vision.com.infoapp.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import vision.com.infoapp.R;

public class GridViewImageAdapter extends ArrayAdapter {
	private AsyncImageLoader asyncImageLoader;
	private Context mContext;
	private List imageAndTextList;
	private GridView gridView;
	private int mCount =0;
	 private LayoutInflater layoutInflater;
	View tempView;
	public GridViewImageAdapter(Context c, List p_imageAndTextList,
								GridView gridView) {
		super(c, 0, p_imageAndTextList);
		layoutInflater = LayoutInflater.from(c);
		mContext = c;
		asyncImageLoader = new AsyncImageLoader();
		this.imageAndTextList = p_imageAndTextList;
		this.gridView = gridView;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("POS=" + position);
		final ViewHolder viewHolder;
//		String path = imgUrls.get(position);
		ImageAndText _img = (ImageAndText) imageAndTextList.get(position);
		if (convertView == null || convertView.getTag() !=null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.gridview_image_text_item, null);
			viewHolder.ivImage 	= (ImageView) convertView.findViewById(R.id.image);
//			viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
//			ImageView imageView = new ImageView(mContext);
//			
//			imageView.setLayoutParams(new GridView.LayoutParams(130, 130));
//			
//			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//			imageView.setPadding(2, 2, 2, 2);
//			imageView.setTag(_img.getId());
//			imageView.setImageResource(R.drawable.default_image);
//			viewHolder.ivImage = imageView;
//			convertView = viewHolder.ivImage;
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		Bitmap cachedImage = imageLoader.loadBitmap(path, viewHolder.ivImage,
//				new ImageCallback() {
//					public void imageLoaded(Bitmap imageBitmap,
//							ImageView image, String imageUrl) {
//						viewHolder.ivImage.setImageBitmap(imageBitmap);
//					}
//				});
		if ("more".equals(_img.getId())) {
			viewHolder.ivImage.setImageResource(R.drawable.more);
//			viewHolder.textView.setText("");
		} else {
			Drawable cachedImage = asyncImageLoader.loadDrawable(mContext,
			_img.getId(), _img.getImageUrl(), new AsyncImageLoader.ImageCallback() {
				public void imageLoaded(Drawable imageDrawable,
                                        String imageUrl, String id) {
//					ImageView imageViewByTag = (ImageView) gridView
//							.findViewWithTag(id);
//					if (imageViewByTag != null) {
					viewHolder.ivImage.setImageDrawable(imageDrawable);
//					}

				}
			});
			if (cachedImage != null) {
				viewHolder.ivImage.setImageDrawable(cachedImage);
			}
//			viewHolder.textView.setText(_img.getText());
		}
		

		return convertView;

	}
	  class ViewHolder { 
          /** 
           * ͼƬ 
           */
          public ImageView ivImage;
          public TextView textView;

  } 
}
