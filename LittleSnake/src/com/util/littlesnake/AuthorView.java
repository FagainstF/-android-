package com.util.littlesnake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthorView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	     setContentView(R.layout.authorview);
	        Button button2 = (Button)this.findViewById(R.id.Button2);
	        //��ťע�ᵽ������
	    	button2.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	Intent intent=new Intent();
	                intent.setClass( AuthorView.this,MainSnake.class);
	                //��ת��������
	                startActivity(intent);
	                //ע����ǰ����
	                AuthorView.this.finish();
	          }
	    	
	    	});
		}
}

