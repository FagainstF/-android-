package com.util.littlesnake;


import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
public class SnakeView extends MyTile {
	private static final String tag = "swz";
	private int mMode = READY;
	//��Ϸ������״̬
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;
    //�����˶��ķ���ʶ��
	private int mDirection = NORTH;
	private int mNextDirection = NORTH;
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int EAST = 3;
	private static final int WEST = 4;
	//��Ϸ�н��е�����ש���Ӧ����ֵ yelow ��ͷ  green��ǽ red ������
	
	
	private static final int RIGHTHEAD =1;
    private static final int WALL =2;
    private static final int BODY =3;

	private long mScore = 0;//��¼��õķ���
	private long mMoveDelay = 600;//ÿ�ƶ�һ������ʱ����ʼʱ����Ϊ600ms
	private long mLastMoveTime;
	private TextView mStatusTextView;//������ʾ��Ϸ״̬��TextView
	//���������ֱ������洢����͹��ӵ�����
	private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
	private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();
    //�����������
	private static final Random RNG = new Random();
	//��Handler����ʵ�ֶ�ʱˢ��
	private RefreshHandler mRedrawHandler = new RefreshHandler();
	class RefreshHandler extends Handler {
        //��ȡ��Ϣ������
		@Override
		public void handleMessage(Message msg) {
			SnakeView.this.update();
			SnakeView.this.invalidate();//ˢ��viewΪ����Ľ���
			Log.i(tag, "handleMessage|Thread Name="+Thread.currentThread().getName());
		}
        //��ʱ������Ϣ��UI�̣߳��Դ˴ﵽ����Ч��
		public void sleep(long delayMillis) {
			this.removeMessages(0); //�����Ϣ���У�Handler���������Ϣ�ĵȴ�   
			Log.i(tag, "sleep|Thread Name="+Thread.currentThread().getName());
			//��ʱ��������Ϣ,����handler   
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};
	public SnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(tag, "SnakeView Constructor");
		initSnakeView();//���캯���У���ʼ����Ϸ
	}
	//��ʼ��SnakeView�࣬ע�⣬�����ʼ����Ϸ�ǲ�һ���ġ� 
	private void initSnakeView() {
		Log.e(tag, "initSnakeView");
		//���ý��㣬���ڴ��� ���ֽ��� �� ��Ϸ�������ת�����focus�ǲ��ɻ�ȱ�ġ�
		setFocusable(true);
		//ȡ����Դ�е�ͼƬ�����ص� ש���ֵ� �С� 
		Resources r = this.getContext().getResources();
		resetTiles(4);
		loadTile(BODY, r.getDrawable(R.drawable.body));
		loadTile(RIGHTHEAD, r.getDrawable(R.drawable.righthead));
		loadTile(WALL, r.getDrawable(R.drawable.wall));
	}
	//������Ϸ״̬
	public void setMode(int newMode) {
		int oldMode = mMode;
		mMode = newMode;
		Resources res = getContext().getResources();
		CharSequence str = "";
		if (newMode == RUNNING & oldMode != RUNNING) {
			mStatusTextView.setVisibility(View.INVISIBLE);
			update();
			return;
		}
		if (newMode == PAUSE) {
			str = res.getText(R.string.mode_pause);
		}
		if (newMode == READY) {
			str = res.getText(R.string.mode_ready);
		}
		if (newMode == LOSE) {
			str = res.getString(R.string.mode_lose_prefix) + mScore
					+ res.getString(R.string.mode_lose_suffix);
		}
		mStatusTextView.setText(str);
		mStatusTextView.setVisibility(View.VISIBLE);
	}
    //�󶨵���Ӧ��textview
	public void setStatusTextView(TextView newView) {
		mStatusTextView = newView;
	}
   //�����ļ���
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mMode == READY | mMode == LOSE) {
				initNewGame();
				setMode(RUNNING);
				update();
				return (true);
			}
			if (mMode == PAUSE) {
				setMode(RUNNING);
				update();
				return (true);
			}
			if (mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			return (true);
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			return (true);
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (mDirection != EAST) {
				mNextDirection = WEST;
			}
			return (true);
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (mDirection != WEST) {
				mNextDirection = EAST;
			}
			return (true);
		}
		return super.onKeyDown(keyCode, msg);
	}
   //ˢ����Ϸ״̬��ÿ����Ϸ����ĸ��¡���Ϸ���ݵĸ��£������������update()����ɵ�
	public void update() {
		if (mMode == RUNNING) {
			long now = System.currentTimeMillis();
			if (now - mLastMoveTime > mMoveDelay) {
				//
				clearTiles();
				updateWalls();
				updateSnake();
				updateApples();
				mLastMoveTime = now;
			}
			mRedrawHandler.sleep(mMoveDelay);
		}
	}
    //��setTile����ǽ��
	private void updateWalls() {
		Log.i(tag, "updateWalls");
		for (int x = 0; x < xcount; x++) {
			setTile(WALL, x, 0);
			setTile(WALL, x, ycount - 1);
		}
		for (int y = 1; y < ycount - 1; y++) {
			setTile(WALL, 0, y);
			setTile(WALL, xcount - 1, y);
		}
	}
    //���ƹ��� 
	private void updateApples() {
		Log.i(tag, "updateApples");

		for (Coordinate c : mAppleList) {
			setTile(BODY, c.x, c.y);
		}
	}
    //��������
	private void updateSnake() {
		//�Թ����ӵ��߻᳤�������������Ϊ���ı�ǡ�
		boolean growSnake = false;
		//ͷ������Ҫ��ֻ��ͷ�������������ӡ�
		Coordinate head = mSnakeTrail.get(0); 
		//����һ��һ����ǰ�ƣ�Ҳ����newHead������ֻ���β�����ӡ�
		Coordinate newHead = new Coordinate(1, 1);
		mDirection = mNextDirection;
		switch (mDirection) {
		case EAST: {
			newHead = new Coordinate(head.x + 1, head.y);
			break;
		}
		case WEST: {
			newHead = new Coordinate(head.x - 1, head.y);
			break;
		}
		case NORTH: {
			newHead = new Coordinate(head.x, head.y - 1);
			break;
		}
		case SOUTH: {
			newHead = new Coordinate(head.x, head.y + 1);
			break;
		}
		}
		if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > xcount - 2)
				|| (newHead.y > ycount - 2)) {
			setMode(LOSE);
			return;
		}
		int snakelength = mSnakeTrail.size();
		for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
			Coordinate c = mSnakeTrail.get(snakeindex);
			if (c.equals(newHead)) {
				setMode(LOSE);
				return;
			}
		}
		int applecount = mAppleList.size();
		for (int appleindex = 0; appleindex < applecount; appleindex++) {
			Coordinate c = mAppleList.get(appleindex);
			if (c.equals(newHead)) {
				mAppleList.remove(c);
				addRandomApple();
				mScore++;
				mMoveDelay *= 0.9;
				growSnake = true;
			}
		}
		mSnakeTrail.add(0, newHead);
		if (!growSnake) {
			mSnakeTrail.remove(mSnakeTrail.size() - 1);
		}
		int index = 0;
		for (Coordinate c : mSnakeTrail) {
			if (index == 0) {
				setTile(RIGHTHEAD, c.x, c.y);
			} else {
				setTile(BODY, c.x, c.y);
			}
			index++;
		}
	}
    //������������,�ܼ򵥵Ĵ洢XY����
	private class Coordinate {
		public int x;
		public int y;
		public Coordinate(int newX, int newY) {
			x = newX;
			y = newY;
		}
		public boolean equals(Coordinate other) {
			if (x == other.x && y == other.y) {
				return true;
			}
			return false;
		}
		@Override
		public String toString() {
			return "Coordinate: [" + x + "," + y + "]";
		}
	}
    //�ڵ�ͼ����������ӹ���
	private void addRandomApple() {
		Coordinate newCoord = null;
		boolean flag = true;
		while (flag) {
			int newX = 1 + RNG.nextInt(xcount - 2);
			int newY = 1 + RNG.nextInt(ycount - 2);
			newCoord = new Coordinate(newX, newY);
			boolean collision = false;
			int snakelength = mSnakeTrail.size();
			for (int index = 0; index < snakelength; index++) {

				if (mSnakeTrail.get(index).equals(newCoord)) {
					collision = true;
				}
			}
			flag = collision;
		}
		if (newCoord == null) {
			Log.e(tag, "Somehow ended up with a null newCoord!");
		}
		mAppleList.add(newCoord);
	}
	 //������Ǵ���ͣ�лظ�������Ҫ ��ʼ����Ϸ�ˡ�
	public void initNewGame() {
		Log.e(tag, "initNewGame!");
		//��ձ�������͹��ӵ����ݽṹ��  
		mSnakeTrail.clear();
		mAppleList.clear();
		// �趨��ʼ״̬�������λ�á�  
		mSnakeTrail.add(new Coordinate(7, 7));
		mSnakeTrail.add(new Coordinate(6, 7));
		mSnakeTrail.add(new Coordinate(5, 7));
		mSnakeTrail.add(new Coordinate(4, 7));
		mSnakeTrail.add(new Coordinate(3, 7));
		mSnakeTrail.add(new Coordinate(2, 7));
		mNextDirection = NORTH;
        //��ʼ����������
		
		addRandomApple();
		mMoveDelay = 600;
		mScore = 0;
	}
    //����������£���ʱ�Ա�����Ϸ����
	public Bundle saveState() {
		Bundle bundle = new Bundle();
		bundle.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
		bundle.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));
		bundle.putInt("mDirection", Integer.valueOf(mDirection));
		bundle.putInt("mNextDirection", Integer.valueOf(mNextDirection));
		bundle.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
		bundle.putLong("mScore", Long.valueOf(mScore));
		return bundle;
	}
   //�ظ���Ϸ����,��saveState()������� 

	public void restoreState(Bundle icicle) {
		setMode(PAUSE);
		mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
		mDirection = icicle.getInt("mDirection");
		mNextDirection = icicle.getInt("mNextDirection");
		mMoveDelay = icicle.getLong("mMoveDelay");
		mScore = icicle.getLong("mScore");
		mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
	}
   //coordArrayListToArray����������̣�������ȡ������Bundle�е�����
	private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
		ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();
		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 2) {
			Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
			coordArrayList.add(c);
		}
		return coordArrayList;
	}
    //����͹���λ�õ�����ת���ɼ򵥵����л���int����
	private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
		int count = cvec.size();
		int[] rawArray = new int[count * 2];
		for (int index = 0; index < count; index++) {
			Coordinate c = cvec.get(index);
			rawArray[2 * index] = c.x;
			rawArray[2 * index + 1] = c.y;
		}
		return rawArray;
	}
}
