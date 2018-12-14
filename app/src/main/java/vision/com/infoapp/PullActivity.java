package vision.com.infoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import vision.com.infoapp.utils.HttpUtils;
import vision.com.infoapp.widget.ImageAndText;
import vision.com.infoapp.widget.ImageAndTextListAdapter;

/**
 * Created by liuhy on 2018/12/11.
 */

public class PullActivity extends Activity {
    String baseUrl = "http://39.96.17.251:8080/api/queryBeforeNewsList";
    private PtrClassicFrameLayout ptrFrame;
    private HttpURLConnection conn;
    private ListView listView;
    private List dataList = new ArrayList();
    ImageAndTextListAdapter adapter = null;
    private String smallCatalog = "shouyedongtu";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_activity);

        //获取smallcatalog;
        smallCatalog = this.getIntent().getExtras().getString("id");
//        String _title = this.getIntent().getExtras().getString("title");
//        new Thread() {
//            @Override
//            public void run() {
//                PullActivity.this.test();
//            }
//        }.start();

        ptrFrame = ((PtrClassicFrameLayout) findViewById(R.id.ultra_ptr_frame));
        ptrFrame.setLastUpdateTimeRelateObject(this);
        //下拉刷新的阻力，下拉时，下拉距离和显示头部的距离比例，值越大，则越不容易滑动
        ptrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrame.setDurationToClose(200);//返回到刷新的位置（暂未找到）
        ptrFrame.setDurationToCloseHeader(1000);//关闭头部的时间 // default is false
        ptrFrame.setPullToRefresh(false);//当下拉到一定距离时，自动刷新（true），显示释放以刷新（false）
        ptrFrame.setKeepHeaderWhenRefresh(true);//见名只意
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //数据刷新的回调
                PullActivity.this.loadNewData();


            }
        });

        listView = (ListView) this.findViewById(R.id.listView);

        AdapterView.OnItemClickListener _listener = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                //setTitle(parent.getItemAtPosition(position).toString());
                Intent intent0 = new Intent(PullActivity.this,ReportDetailActivity.class);
                String _id			=(String)((ImageAndText)parent.getItemAtPosition(position)).getId();
                String _title		=(String)((ImageAndText)parent.getItemAtPosition(position)).getText();
                intent0.putExtra("id", _id);
                intent0.putExtra("title", _title);
                startActivity(intent0);
            }
        };
        listView.setOnItemClickListener(_listener);
        this.initListView("shouyedongtu", "");
    }
    //下拉刷新加载最新的数据；
    public void loadNewData(){
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                JSONArray jsonArray = (JSONArray) message.obj;
                for (int i = jsonArray.length()-1; i >-1 ; i--) {
                    try {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        String id = obj.getString("id");
                        String img = Globals.HTTP_SERVER_URL + obj.getString("img");
                        String title = obj.getString("title");
                        String modifyDatetime = obj.getString("modifyDatetime");
                        ImageAndText it = new ImageAndText(id, img, title, title, title, modifyDatetime);
//                        dataList.add(it);
                        dataList.add(0,it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                adapter = new ImageAndTextListAdapter(PullActivity.this, dataList, listView);
//                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ptrFrame.refreshComplete();
            }
        };
        //通过单独线程，加载远端数据；
        new Thread() {
            @Override
            public void run() {
                try {
                    String modifyDateTime = "";
                    //获取dataList 最近记录；
                    if(null!=dataList && dataList.size()>0){
                        ImageAndText it = (ImageAndText)dataList.get(0);
                        modifyDateTime = it.getModifyDateTime();
                    }
                    HashMap<String, String> _mapParam = new HashMap();
                    _mapParam.put("smallCatalog", PullActivity.this.smallCatalog);
                    _mapParam.put("time", modifyDateTime);
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

    public void initListView(final String smallCatalog, String time) {
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

                adapter = new ImageAndTextListAdapter(PullActivity.this, dataList, listView);
                listView.setAdapter(adapter);
            }
        };
        //通过单独线程，加载远端数据；
        new Thread() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> _mapParam = new HashMap();
                    _mapParam.put("smallCatalog", PullActivity.this.smallCatalog);
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
}
