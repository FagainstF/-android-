package com.util.littlesnake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyTile extends View {

	private int size= 14;
	
	protected static int xcount;
	protected static int ycount;
	protected  int xoffset;
	protected  int yoffset;
	
	
	private static final String tag = "swz";


	public MyTile(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.i(tag, "TileView Constructor");
		Log.i(tag, "mTileSize=" + size);
	}
	
	private Bitmap[] mTileArray;//λͼ����
	private int[][] mTileGrid;//ӳ��������Ϸ���������
	private final Paint mPaint = new Paint();//����

	//����λͼ����ĳ���
	public void resetTiles(int tilecount) {
		mTileArray = new Bitmap[tilecount];
	}
	@Override
	//��Ӧ���ֱַ��ʵ���Ļ�����ı���Ļ��С�ߴ�ʱ��ͬʱ�޸�tile����ؼ���ָ��
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.i(tag, "onSizeChanged," + "w=" + w + " h=" + h + " oldw=" + oldw + " oldh=" + oldh);
		xcount = (int) Math.floor(w / size);
		ycount = (int) Math.floor(h / size);
		Log.i(tag, "mXTileCount=" + xcount);
		Log.i(tag, "mYTileCount=" + ycount);
		xoffset = ((w - (size * xcount)) / 2);
		yoffset = ((h - (size * ycount)) / 2);
		Log.i(tag, "mXOffset=" + xoffset);
		Log.i(tag, "mYOffset=" + yoffset);

		mTileGrid = new int[xcount][ycount];
		clearTiles();
	}
	//��������һ�� Drawable �� bitmap ��ת��
	public void loadTile(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, size, size);
		tile.draw(canvas);
		mTileArray[key] = bitmap;
	}
//���ͼ����ʾ
	public void clearTiles() {
		Log.i(tag, "TileView.clearTiles");
		for (int x = 0; x < xcount; x++) {
			for (int y = 0; y < ycount; y++) {
				setTile(0, x, y);
			}
		}
	}
//����Ӧ������λ�û�����Ӧ��ש��
	public void setTile(int tileindex, int x, int y) {
		mTileGrid[x][y] = tileindex;
	}
	// onDraw
	@Override
//��ֱ�Ӳ����Ļ������Ƶ��ֻ�������
	public void onDraw(Canvas canvas) {
		Log.i(tag, "onDraw");
		super.onDraw(canvas);
		Bitmap bmp;
		float left;
		float top;
		for (int x = 0; x < xcount; x++) {
			for (int y = 0; y < ycount; y++) {
				if (mTileGrid[x][y] > 0) {
					bmp = mTileArray[mTileGrid[x][y]];
					left = x * size + xoffset;
					top = y * size + yoffset;
					canvas.drawBitmap(bmp, left, top, mPaint);
				}
			}
		}
	}
}
