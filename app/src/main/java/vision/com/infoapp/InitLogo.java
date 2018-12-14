package vision.com.infoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import vision.com.infoapp.demo.SlidingTabActivity;

public class InitLogo extends BaseActivity {
	private Handler mHandler;
	private ImageView imageview;
	int alpha = 200; 
	int b = 0; 
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Globals.EXIT = false;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.init_logo);

        this.init();
    }
    public void init(){
    	mHandler	 = new Handler();
    	imageview  = (ImageView) this.findViewById(R.id.ImageView01);
    	new Thread(new Runnable() {
            public void run() { 
                initApp();
                while (b < 2) { 
                    try { 
                        if (b == 0) { 
                            Thread.sleep(2500);
                            b = 1; 
                        } else { 
                            Thread.sleep(20);
                        }
                        updateApp();

                    } catch (InterruptedException e) {
                        e.printStackTrace(); 
                    } 
                }
            } 
        }).start();
    	mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg); 
                imageview.setAlpha(alpha); 
                imageview.invalidate();
            } 
        };
    }
    public void updateApp() { 
        alpha += 5;
        if (alpha >=255) { 
            b = 2; 
//            Intent in = new Intent(this, MainActivity.class);
            Intent in = new Intent(this, SlidingTabActivity.class);
            startActivity(in); 
        }
        mHandler.sendMessage(mHandler.obtainMessage());

    } 
    public void initApp(){ 
        
    } 
}