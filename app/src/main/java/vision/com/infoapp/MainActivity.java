package vision.com.infoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import vision.com.infoapp.utils.HttpUtils;
import vision.com.infoapp.widget.AsyncImageLoader;
import vision.com.infoapp.widget.GalleryImageAdapter;
import vision.com.infoapp.widget.GridViewImageAdapter;
import vision.com.infoapp.widget.ImageAndText;
import vision.com.infoapp.widget.ImageAndTextListAdapter;
import vision.com.infoapp.widget.MenuUtils;
import vision.com.infoapp.widget.MyGallery;

public class MainActivity extends Activity {

    private String baseUrl =  Globals.HTTP_SERVER_URL + "/api/queryBeforeNewsList";
    private String smallCatalog = "shouyedongtu";

    private ImageAndTextListAdapter adapter;

    private GalleryImageAdapter galleryImageAdapter;
    //显示选中图片的标题；
    private TextView galleryTextTitleView;
    private int gallery_selected_index = 0;
    private Timer autoGallery;
    private List __list = new ArrayList();
    private View viewGalleryPos = null;

    private List dataList = new ArrayList();
    private int screen_width 			= 0;
    private int menu_width 				= 0;

    private LinearLayout layoutGallery = null;


    //图片；
    private String baseGridUrl = Globals.HTTP_SERVER_URL + "/api/queryBigCatalog";
    private List gridDataList = new ArrayList();
    private GridViewImageAdapter gridViewImageAdapter =  null;
    private GridView gridView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        layoutGallery = this.findViewById(R.id.layoutGallery);
        gridView = this.findViewById(R.id.gridView);

        this.initGallery(this.smallCatalog,"");
        this.initGridView();

    }
    public void createGallery(){

        gallery_selected_index = 0;
//        ImageAndText _image_text = p_image_text;
        __list = new ArrayList();
        final MyGallery _gallery = new MyGallery(this);
        _gallery.setLayoutParams(new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,600,0,0));
        _gallery.setSpacing(0);
        _gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Intent intent0 = new Intent(MainActivity.this,ReportDetailActivity.class);
                String _id		=((ImageAndText)parent.getItemAtPosition(position)).getId();
                intent0.putExtra("id", _id);
                startActivity(intent0);
            }
        });
        _gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                MenuUtils.selectMenuTrace(MainActivity.this,viewGalleryPos,3, arg2%3, gallery_selected_index);
                gallery_selected_index = arg2%3;
                String __title		=((ImageAndText)parent.getItemAtPosition(arg2)).getText();
                galleryTextTitleView.setText(__title);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        galleryImageAdapter = new GalleryImageAdapter(this, __list);
        _gallery.setAdapter(galleryImageAdapter);
        for(int i=0;i<3;i++){
            final ImageAndText _iat = (ImageAndText) dataList.get(i);
            _iat.setDrawable(getResources().getDrawable(R.drawable.default_image));
            Drawable cachedImage = new AsyncImageLoader().loadDrawable(this,i+"",_iat.getImageUrl(), new AsyncImageLoader.ImageCallback() {
                public void imageLoaded(Drawable imageDrawable, String imageUrl,String id) {
                    _iat.setDrawable(imageDrawable);
                    galleryImageAdapter.notifyDataSetChanged();
//                  int _index = imageUrl.lastIndexOf("/");
//  	 			  String _img_name = imageUrl.substring(_index+1);
//  	 			  FileUtils.saveDrawable2SdCard(imageDrawable, _img_name);
                }
            });
            __list.add(_iat);
        }
        screen_width = getScreenWidth();
        AbsoluteLayout _lyt_ab = new AbsoluteLayout(this);

        _lyt_ab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        viewGalleryPos  = new View(this);

        menu_width 	 = screen_width/3;
        int _height  = 3;
        int _x 		 = 0 * menu_width;
        int _y 		 = 600 ;
        viewGalleryPos.setLayoutParams(new AbsoluteLayout.LayoutParams(menu_width,_height,_x,_y));
        viewGalleryPos.setBackgroundColor(Color.RED);
        _lyt_ab.addView(_gallery);

        galleryTextTitleView = new TextView(this);
        galleryTextTitleView.setText("");
        galleryTextTitleView.setTextSize(13);
        galleryTextTitleView.setSingleLine();
        galleryTextTitleView.setEllipsize(TextUtils.TruncateAt.END);
        galleryTextTitleView.setLayoutParams(new AbsoluteLayout.LayoutParams(screen_width,50,0,600-50));
        galleryTextTitleView.setTextColor(Color.WHITE);
        galleryTextTitleView.setGravity(Gravity.CENTER);
//        galleryTextTitleView.setBackgroundColor(R.color.gallery_title_color);

        _lyt_ab.addView(galleryTextTitleView);
        _lyt_ab.addView(viewGalleryPos);

//        if(null==_layout_header){
//            _layout_header = new LinearLayout(mPullRefreshListView.getContext());
//            actualListView.addHeaderView(_layout_header);
//        }else{
//            _layout_header.removeAllViews();
//        }
//        _layout_header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,155));
//        _layout_header.setOrientation(LinearLayout.HORIZONTAL);
//        _layout_header.setPadding(0, 0, 0, 0);
//        _layout_header.addView(_lyt_ab);
        layoutGallery.removeAllViews();
        layoutGallery.addView(_lyt_ab);

        final Handler autoGalleryHandler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 1:
                        _gallery.onFling(null, null,-1, 1);
                        break;
                }
            }
        };
//        if(null!=autoGallery) autoGallery.cancel();
//        autoGallery = new Timer();
//        autoGallery.schedule(new TimerTask() {
//            // int gallerypisition = 0;
//            @Override
//            public void run() {
//                if (gallery_selected_index < 2) {
//                    gallery_selected_index++;
//                } else {
//                    gallery_selected_index = 0;
//                }
//                Message msg = new Message();
//                Bundle date = new Bundle();
//                date.putInt("pos", gallery_selected_index);
//                msg.setData(date);
//                msg.what = 1;
//                autoGalleryHandler.sendMessage(msg);
//            }
//        }, 3000, 3000);//
    }

    //加载gallery数据；
    public void initGallery(final String smallCatalog, String time) {
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                JSONArray jsonArray = (JSONArray) message.obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        String id = obj.getString("id");
                        String img = Globals.HTTP_SERVER_URL + obj.getString("img");
                        String title = obj.getString("title");
                        String modifyDatetime = obj.getString("modifyDatetime");
                        ImageAndText it = new ImageAndText(id, img, title, title, title, modifyDatetime);
                        dataList.add(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                createGallery();

//                adapter = new ImageAndTextListAdapter(MainActivity.this, dataList, listView);
//                listView.setAdapter(adapter);

            }
        };
        //通过单独线程，加载远端数据；
        new Thread() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> _mapParam = new HashMap();
                    _mapParam.put("smallCatalog", MainActivity.this.smallCatalog);
                    _mapParam.put("time", "");
                    String str = HttpUtils.requestGet(baseUrl, _mapParam);
                    if (null != str) {
                        JSONObject json = new JSONObject(str);
                        String state = json.getString("state");
                        JSONArray array = json.getJSONArray("data");
                        Message message = handler.obtainMessage(0, array);
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //加载图片列表；
    private  void initGridView(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent  intent0 = new Intent(MainActivity.this,PullActivity.class);
                ImageAndText _image_text = (ImageAndText) gridDataList.get(position);
//					LinearLayout _layout =(LinearLayout) parent.getChildAt(position);
//					ImageView _imgView		= (ImageView)_layout.getChildAt(0);
//					TextView _textView		= (TextView)_layout.getChildAt(1);
//                try {
//                    if("more".equals(_image_text.getId())){
//                        System.out.println("--load...........more..............");
//                        ImageAndText _last_image = (ImageAndText) dataList.get(position-1);
//                        List _list = queryMoreList(PIC_QUERY_MORE,catalog,_last_image.getModifyDateTime(),1);
//                        if(null!=_list && !_list.isEmpty()){
//                            dataList.remove(dataList.size()-1);
//                            dataList.addAll(dataList.size(),_list);
//                            int _c_pageSize = _list.size();
//                            if(_c_pageSize==Globals.PAGE_SIZE) {
//                                ImageAndText _image_more=new ImageAndText("more","more.jpg","","","","");
//                                dataList.add(dataList.size(),_image_more);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                    }else{
                String catalog = _image_text.getCatalog();
                if("0".equals(catalog)) {
                    //多tab列表；
                }else if("1".equals(catalog)) {
                    //单列表；
                    intent0.putExtra("id", _image_text.getId());
                    intent0.putExtra("title", _image_text.getText());
                    startActivity(intent0);
                }else if("2".equals(catalog)){
                    //grid功能列表；
                }
//            }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                JSONArray jsonArray = (JSONArray) message.obj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        String id = obj.getString("id");
                        String img = Globals.HTTP_SERVER_URL + obj.getString("img");
                        String title = obj.getString("title");
                        String catalog = obj.getString("catalog");
                        ImageAndText it = new ImageAndText(id, img, title, title, catalog, null);
                        gridDataList.add(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                gridViewImageAdapter = new GridViewImageAdapter(MainActivity.this,gridDataList,gridView);
                gridView.setAdapter(gridViewImageAdapter);

            }
        };
        //通过单独线程，加载远端数据；
        new Thread() {
            @Override
            public void run() {
                try {
//                    HashMap<String, String> _mapParam = new HashMap();
//                    _mapParam.put("smallCatalog", MainActivity.this.smallCatalog);
//                    _mapParam.put("time", "");
                    String str = HttpUtils.requestGet(baseGridUrl,new HashMap<String, String>());
                    if (null != str) {
                        JSONObject json = new JSONObject(str);
                        String state = json.getString("state");
                        JSONArray array = json.getJSONArray("data");
                        Message message = handler.obtainMessage(0, array);
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    private int getScreenWidth(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        return screenWidth;
    }
}
