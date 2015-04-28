package com.util.littlesnake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GAME extends Activity{
	//������Ϸ����
	private SnakeView mSnakeView;
	private static String ICICLE_KEY = "snake-view";
	private int mMode = READY;
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game); 
        mSnakeView = (SnakeView) findViewById(R.id.MainSnake);
        //���ý��汳��
        mSnakeView.setBackgroundResource(R.drawable.background);
        TextView tv = (TextView) findViewById(R.id.text);
        Button button4 = (Button)this.findViewById(R.id.button4);
        //����TextView״̬�ж��Ƿ�ʼ��Ϸ
        mSnakeView.setStatusTextView(tv);
        if (savedInstanceState == null) {
        	mSnakeView.setMode(SnakeView.READY);
		} 
        else {
			    Bundle bundle = savedInstanceState.getBundle(ICICLE_KEY);
			if (bundle != null) {
				mSnakeView.restoreState(bundle);
			} else {
                  mSnakeView.setMode(SnakeView.PAUSE);
			}
		}
        //��ťע�ᵽ������
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
           	 Intent intent=new Intent();
              intent.setClass(GAME.this, MainSnake.class);
              startActivity(intent);
             GAME.this.finish();
}
        });
}
	//���ü��̼����¼���������UP����ʼ��Ϸ
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mMode == READY | mMode == LOSE) {
				mSnakeView.initNewGame();
				mSnakeView.setMode(RUNNING);
				mSnakeView.update();
				return (true);
			}
		}
		return super.onKeyDown(keyCode, msg);
	}
	//�ж���Ϸ״̬�Ƿ�Ϊ��ͣ
	protected void onPause() {
        super.onPause();
        // Pause the game along with the activity
        mSnakeView.setMode(SnakeView.PAUSE);
    }
	 @Override
	 //�����¼�״̬
	    public void onSaveInstanceState(Bundle outState) {
               outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
	    }
}


