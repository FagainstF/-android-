package com.util.littlesnake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Help extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        Button button6 = (Button)this.findViewById(R.id.Button6);
        //��ťע�ᵽ������
    	button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent=new Intent();
                intent.setClass( Help.this,MainSnake.class);
                //��ת��������
                startActivity(intent);
                //ע����ǰ����
                Help.this.finish();
          }
    	});
    	}
}



