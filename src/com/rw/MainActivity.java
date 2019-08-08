package com.rw;

import com.rw.gen.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private Button pb;
	private Button ob;
	private ImageView div;
	private ImageView hiv;
	private OnClickListener cl;	
	private OnClickListener hl;	
	private Handler hd;
	private Runnable run;
	private int i;
	private float f;
	@SuppressLint({ "HandlerLeak", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pb = (Button) findViewById(R.id.play);
		ob = (Button) findViewById(R.id.ok);
		div = (ImageView) findViewById(R.id.dialog);
		hiv = (ImageView) findViewById(R.id.helpDialog);
		pb.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent io = new Intent();
				io.setClass(getBaseContext(), GameActivity.class);
				startActivity(io);
			}
		});
		run =  new Runnable(){
			@Override
			public void run() {
				for(int n = 0;n <= 11;n++){
					Message msg = new Message();
					f = ob.getAlpha()+(i*0.1f);
					msg.what = 0;
					hd.sendMessage(msg);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Message msg = new Message();
				msg.what = 0;
				f = 0.5f+(i*0.5f);
				hd.sendMessage(msg);
			}
		};
		hd = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				vsa(f);
			}
		};
		vsa(0f);
		final Button hb = (Button) findViewById(R.id.help);
		cl = new OnClickListener(){
			@Override
			public void onClick(View v) {
				hb.setOnClickListener(hl);
				ob.setOnClickListener(null);
				i = -1;
				new Thread(run).start();
			}
		};
		hl = new OnClickListener(){
			@Override
			public void onClick(View v) {
				ob.setOnClickListener(cl);
				hb.setOnClickListener(null);
				i = 1;
				new Thread(run).start();
			}
		};
		hb.setOnClickListener(hl);

	}
	@SuppressLint("NewApi")
	private void vsa(float f){
		ob.setAlpha(f);
		hiv.setAlpha(f);
		div.setAlpha(f);
		//Toast.makeText(getBaseContext(), ""+f, Toast.LENGTH_SHORT).show();
	}

}
