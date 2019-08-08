package com.rw;

import java.util.Random;

import com.rw.gen.R;
import com.rw.gen.R.drawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	private Button babu;
	private ImageView tnt;
	private ImageView rwv;
	private ImageView tntl;
	private Button afterv;
	private TextView tv;
	private Handler bdl;
	private Handler widl;
	private Handler tdl;
	private Thread tgt;
	private Thread bgt;
	private Thread wrt;
	private int b;
	private boolean bb;
	private int win;
	private float wx;
	private float wy;
	private float tnx;
	private int screenWidth;
	private int screenHeight;
	private OnTouchListener wcl;
	private int s;

	@SuppressLint({ "HandlerLeak", "NewApi" })
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		s = 0;
		bb = true;
		babu = (Button) findViewById(R.id.backbu);
		tnt = (ImageView) findViewById(R.id.tnt);
		rwv = (ImageView) findViewById(R.id.rwiv);
		tntl = (ImageView) findViewById(R.id.tntl);
		afterv = (Button) findViewById(R.id.afterv);
		tv = (TextView) findViewById(R.id.textView1);
		final byte[] xh = { 0, 20, 40, 60, 30, 0 };
		final int[] yh = { 0, -45, -125, -200, -90, 0 };
		final Resources R = getResources();
		tv.setAlpha(0f);
		afterv.setBackground(R.getDrawable(drawable.ic_a0001));
		DisplayMetrics dm = R.getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		tnt.setBackground(R.getDrawable(drawable.ic_tnt));
		tnt.setX(screenWidth);
		tnt.setY(screenHeight/40*33-71);
		rwv.setBackground(R.getDrawable(drawable.ic_bg0001));
		wx = 15;
		wy = screenHeight/40*33-116;
		rwv.setX(wx);
		rwv.setY(wy);
		b = 0;
		bdl = new Handler() {
			public void handleMessage(Message msg) {
				babu.setBackground(R.getDrawable(msg.what));
			}
		};
		widl = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					rwv.setBackground(R.getDrawable(msg.arg1));
					break;
				case 1:
					rwv.setX(wx + xh[msg.arg1]);
					rwv.setY(wy + yh[msg.arg1]);
					break;
				}
			}
		};
		tdl = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					while (true) {
						Random rd = new Random();
						float r = rd.nextInt(2 * screenWidth);
						if (r > screenWidth && r < 2 * screenWidth) {
							tnx = r;
							tnt.setX(r);
							break;
						}
					}
					synchronized(tgt) {
					    tgt.notify();
					}
					break;
				case 1:
					tnx = tnx - 70;
					tnt.setX(tnx);
					break;
				case 2:
					afterv.setBackground(R.getDrawable(msg.arg1));
					if(msg.arg1 == drawable.ic_a0010){
						tv.setAlpha(100f);
						tv.setText("Your score:"+s);
						afterv.setOnTouchListener(new OnTouchListener(){
							@Override
							public boolean onTouch(View arg0, MotionEvent arg1) {
								Intent io = new Intent();
								io.setClass(getBaseContext(), MainActivity.class);
								startActivity(io);
								return false;
							}
						});
					}
					break;
				case 3:
					float tntlx = (tntl.getWidth()-tnt.getWidth())/2;
					float tntly = (tntl.getHeight()-tnt.getHeight())/2;
					tntl.setX(tnt.getX()-tntlx);
					tntl.setY(tnt.getY()-tntly);
					tntl.setBackground(R.getDrawable(msg.arg1));
					break;
				}
			}
		};
		tgt = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(bb == false){
					     break;
				    }
					tdl.sendEmptyMessage(0);
					synchronized(tgt) {
						try {
							tgt.wait();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					while (true) {
						if(tnx < - tnt.getWidth()){
							s++;
							break;
						}
						tdl.sendEmptyMessage(1);
						if(tnt.getX()<rwv.getWidth()+15){
							if(tnt.getX()<30){	
							}else if(rwv.getY() + 86>tnt.getY()){
								bb=false;
								break;
							}
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				for(int i = drawable.ic_a0001; i <= drawable.ic_a0010;i++){
					Message msg = new Message();
					msg.what = 2;
					msg.arg1 = i;
					tdl.sendMessage(msg);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}			
			}
		});
		bgt = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					for (int bn = drawable.ic_bg0001; bn <= drawable.ic_bg0009; bn++) {
						bdl.sendEmptyMessage(bn);
						if(bb == false){
							break;
						}
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(bb == false){
						break;
					}
				}
				for(int i = drawable.ic_l0001; i <= drawable.ic_l0004;i++){
					Message msg = new Message();
					msg.what = 3;
					msg.arg1 = i;
					tdl.sendMessage(msg);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		wcl = new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				b = 1;
				return false;
			}
		};
		wrt = new Thread(new Runnable() {
			@Override
			public void run() {
				afterv.setOnTouchListener(wcl);
				win = drawable.ic_wolf0001;
				while (true) {
					if(bb == false){
						break;
					}
					if (win > drawable.ic_wolf0006) {
						win = drawable.ic_wolf0001;
					}
					if (b > 0) {
						if (b == 1) {
							win = drawable.ic_wolf0001;
						}
						b = 2;
						babu.setOnTouchListener(null);
						Message msg = new Message();
						msg.what = 1;
						msg.arg1 = win - drawable.ic_wolf0001;
						widl.sendMessage(msg);
						if (win == drawable.ic_wolf0006) {
							win = drawable.ic_wolf0001;
							b = 0;
							babu.setOnTouchListener(wcl);
						}
					}
					Message msg = new Message();
					msg.what = 0;
					msg.arg1 = win;
					widl.sendMessage(msg);
					win++;
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		tgt.start();
		bgt.start();
		wrt.start();
	}
}
