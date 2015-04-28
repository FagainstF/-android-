package com.util.littlesnake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainSnake extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_snake);
        initialstart();//��ʼ��      
    }
     public void initialstart(){
    	 ImageButton imageButton = (ImageButton)this.findViewById(R.id.ImageButton1);
    	 Button button1 = (Button)this.findViewById(R.id.Button1);
    	 Button button3 = (Button)this.findViewById(R.id.Button3);
    	 Button button5 = (Button)this.findViewById(R.id.Button5);
         button1.setText("������Ϣ");
         button3.setText("�˳�");
         button5.setText("��Ϸ˵��");
         imageButton.setImageResource(R.drawable.star1);
         //��ťע�ᵽ���Եļ�����
         imageButton.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View view) {
                	  Intent intent=new Intent();
                      intent.setClass(MainSnake.this, GAME.class);
                      startActivity(intent);//��ת��Game����
                     MainSnake.this.finish();
                    }
                });
          button1.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View view) {
               //setContentView(R.layout.author);
                	 Intent intent=new Intent();
                   intent.setClass( MainSnake.this,AuthorView.class);
                   startActivity(intent);//��ת��������Ϣ����
                  MainSnake.this.finish();//ע����ת֮ǰ�Ľ���
    
    }
  });
          button3.setOnClickListener(new View.OnClickListener() {
              public void onClick(View view) {
                      MainSnake.this.finish();//�˳�Ӧ�ó���
                }
            });
          button5.setOnClickListener(new View.OnClickListener() {
              public void onClick(View view) {
            	  Intent intent=new Intent();
                  intent.setClass( MainSnake.this,Help.class);
                  startActivity(intent);//��ת����Ϸ˵������
                 MainSnake.this.finish();
                }
            });
     }
}
