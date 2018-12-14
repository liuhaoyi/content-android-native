package vision.com.infoapp.widget;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import vision.com.infoapp.R;
import vision.com.infoapp.utils.ViewCache;

/**
 * Created by liuhy on 2018/12/12.
 */

public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {

    private ListView listView;
    private AsyncImageLoader asyncImageLoader;

    public ImageAndTextListAdapter(Activity activity, List<ImageAndText> imageAndTexts, ListView listView) {
        super(activity, 0, imageAndTexts);
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();

        // Inflate the views from XML
        View rowView = convertView;
        ViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.image_and_text_row, null);
            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (ViewCache) rowView.getTag();
        }
        final ImageAndText imageAndText = getItem(position);

        // Load the image and set it on the ImageView
        String id = imageAndText.getId();
        String imageUrl = imageAndText.getImageUrl();
        ImageView imageView = viewCache.getImageView();
        imageView.setTag(id);
        Drawable cachedImage = asyncImageLoader.loadDrawable(activity, id, imageUrl, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl, String id) {
                ImageView imageViewByTag = (ImageView) listView.findViewWithTag(id);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
//	                    int _index = imageUrl.lastIndexOf("/");
//		 				String _img_name = imageUrl.substring(_index+1);
//
//	                    FileUtils.saveDrawable2SdCard(imageDrawable, _img_name);

                }
            }
        });
        if (cachedImage == null) {
            imageView.setImageResource(R.drawable.default_image);
        }
//			else{
//				imageView.setImageDrawable(cachedImage);
//			}
        // Set the text on the TextView
        TextView textView = viewCache.getTitleView();
        textView.setText(imageAndText.getText());

        TextView _briefView = viewCache.getBriefView();
        String _brief = imageAndText.getBrief();
        if (null == _brief) _brief = "";
        _briefView.setText(_brief);
        return rowView;
    }
}
