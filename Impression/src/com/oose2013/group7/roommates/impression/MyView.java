package com.oose2013.group7.roommates.impression;

import homework.Homework3.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;

/**
 * Main activity for game impression. It will calls a new canvas and gives the key word.
 * @author lbowen5
 */

public class MyView extends SurfaceView implements Callback,Runnable{

	//Refresh rate
	public static final int TIME_IN_FRAME=20;
	
	private Resources res;

	private BmpImage ic_clear, ic_color00, ic_color01, ic_color02, ic_color03, ic_color04, ic_color05, ic_color06, ic_color07;
	private Rect rClear, rColor00, rColor01, rColor02, rColor03, rColor04, rColor05, rColor06, rColor07;
	
	Canvas mCanvas=null;
	Paint mPaint=null;
	final private int pathUpto=500;
	private Path mPath[]=new Path[500];
	private int pathAmount=0;
	private int[] color1=new int[pathUpto];
	private int[] color2=new int[pathUpto];
	private int[] color3=new int[pathUpto];
	SurfaceHolder mSurfaceHolder=null;
	
	boolean changeColor=false;
	
	private boolean mIsRunning=false;
	
	private float mposX, mposY;

	/**
	 * The constructor method will draw all the buttons on the screen and figure out their area.
	 * @param context
	 */
	public MyView(Context context){
		super(context);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		
		res=context.getResources();
		
		ic_clear=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_clear));
		ic_clear.setStartPosition(20, 0);
		rClear=	new Rect(ic_clear.startx, ic_clear.starty, ic_clear.startx+ic_clear.w, ic_clear.starty+ic_clear.h);
		
		ic_color00=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color00));
		ic_color00.setStartPosition(ic_clear.startx, ic_clear.starty+ic_clear.h+10);
		rColor00=new Rect(ic_color00.startx, ic_color00.starty, ic_color00.startx+ic_color00.w, ic_color00.starty+ic_color00.h);
		
		ic_color01=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color01));
		ic_color01.setStartPosition(ic_color00.startx+ic_color00.w+10, ic_color00.starty);
		rColor01=new Rect(ic_color01.startx, ic_color01.starty, ic_color01.startx+ic_color01.w, ic_color01.starty+ic_color01.h);
		
		ic_color02=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color02));
		ic_color02.setStartPosition(ic_color01.startx+ic_color01.w+10, ic_color01.starty);
		rColor02=new Rect(ic_color02.startx, ic_color02.starty, ic_color02.startx+ic_color02.w, ic_color02.starty+ic_color02.h);
		
		ic_color03=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color03));
		ic_color03.setStartPosition(ic_color02.startx+ic_color02.w+10, ic_color02.starty);
		rColor03=new Rect(ic_color03.startx, ic_color03.starty, ic_color03.startx+ic_color03.w, ic_color03.starty+ic_color03.h);
		
		ic_color04=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color04));
		ic_color04.setStartPosition(ic_color03.startx+ic_color03.w+10, ic_color03.starty);
		rColor04=new Rect(ic_color04.startx, ic_color04.starty, ic_color04.startx+ic_color04.w, ic_color04.starty+ic_color04.h);
		
		ic_color05=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color05));
		ic_color05.setStartPosition(ic_color04.startx+ic_color04.w+10, ic_color04.starty);
		rColor05=new Rect(ic_color05.startx, ic_color05.starty, ic_color05.startx+ic_color05.w, ic_color05.starty+ic_color05.h);
		
		ic_color06=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_color06));
		ic_color06.setStartPosition(ic_color05.startx+ic_color05.w+10, ic_color05.starty);
		rColor06=new Rect(ic_color06.startx, ic_color06.starty, ic_color06.startx+ic_color06.w, ic_color06.starty+ic_color06.h);
		
		ic_color07=new BmpImage(BitmapFactory.decodeResource(res, R.drawable.ic_save));
		ic_color07.setStartPosition(ic_color06.startx+ic_color06.w+10, ic_color06.starty);
		rColor07=new Rect(ic_color07.startx, ic_color07.starty, ic_color07.startx+ic_color07.w, ic_color07.starty+ic_color07.h);
						
		mSurfaceHolder=this.getHolder();
		mSurfaceHolder.addCallback(this);
		mCanvas=new Canvas();
		
		// Set the paint
		mPaint=new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);
	}

	/**
	 * Method which interacts with user to record their finger trace.
	 */
    @Override
	public boolean onTouchEvent(MotionEvent event){
       	float x=event.getX();
       	float y=event.getY();
       	if (rClear.contains((int)(x), (int)(y))) pathAmount=0;
       	else if (rColor00.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=0;
       		color2[pathAmount]=0;
       		color3[pathAmount]=0;
       		changeColor=true;
       	}
       	else if (rColor01.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=255;
       		color2[pathAmount]=0;
       		color3[pathAmount]=0;
       		changeColor=true;
       	}
       	else if (rColor02.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=255;
       		color2[pathAmount]=165;
       		color3[pathAmount]=0;
       		changeColor=true;
       	}
       	else if (rColor03.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=255;
       		color2[pathAmount]=255;
       		color3[pathAmount]=0;
       		changeColor=true;
       	}
       	else if (rColor04.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=0;
       		color2[pathAmount]=255;
       		color3[pathAmount]=0;
       		changeColor=true;
       	}
       	else if (rColor05.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=0;
       		color2[pathAmount]=255;
       		color3[pathAmount]=255;
       		changeColor=true;
       	}
       	else if (rColor06.contains((int)(x), (int)(y))){
       		pathAmount++;
       		mPath[pathAmount]=new Path();
       		color1[pathAmount]=0;
       		color2[pathAmount]=0;
       		color3[pathAmount]=255;
       		changeColor=true;
       	}
       	else if (rColor07.contains((int)(x), (int)(y))){
       		try {
				saveImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
       	}
       	else if (event.getAction()==MotionEvent.ACTION_DOWN){
       		if (!changeColor){
       			pathAmount++;
       			mPath[pathAmount]=new Path();
       			color1[pathAmount]=color1[pathAmount-1];
       			color2[pathAmount]=color2[pathAmount-1];
       			color3[pathAmount]=color3[pathAmount-1];
       		}
       		mPath[pathAmount].moveTo(x, y);
       		changeColor=false;
       	}
       	else if (event.getAction()==MotionEvent.ACTION_MOVE) mPath[pathAmount].quadTo(mposX, mposY, x, y);
        mposX=x;
        mposY=y;
        return true;
   	}

    /**
     * Draw the picture based on user's finger trace and the color they chose.
     */
   	private void Draw(){
   		//Clear the canvas
   		mCanvas.drawColor(Color.WHITE);
   		//Draw curves
   		for (int i=1; i<=pathAmount; i++){
   			mPaint.setARGB(255, color1[i], color2[i], color3[i]);
   			mCanvas.drawPath(mPath[i], mPaint);	
   		}
   		
   		mCanvas.drawBitmap(ic_clear.bmp, ic_clear.startx, ic_clear.starty, new Paint());
   		mCanvas.drawBitmap(ic_color00.bmp, ic_color00.startx, ic_color00.starty, new Paint());
   		mCanvas.drawBitmap(ic_color01.bmp, ic_color01.startx, ic_color01.starty, new Paint());
   		mCanvas.drawBitmap(ic_color02.bmp, ic_color02.startx, ic_color02.starty, new Paint());
   		mCanvas.drawBitmap(ic_color03.bmp, ic_color03.startx, ic_color03.starty, new Paint());
   		mCanvas.drawBitmap(ic_color04.bmp, ic_color04.startx, ic_color04.starty, new Paint());
   		mCanvas.drawBitmap(ic_color05.bmp, ic_color05.startx, ic_color05.starty, new Paint());
   		mCanvas.drawBitmap(ic_color06.bmp, ic_color06.startx, ic_color06.starty, new Paint());
   		mCanvas.drawBitmap(ic_color07.bmp, ic_color07.startx, ic_color07.starty, new Paint());
        
    }

   	@Override
   	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

   	@Override
    public void surfaceCreated(SurfaceHolder holder){
   		//Start the main thread
    	mIsRunning=true;
    	new Thread(this).start();
    }

   	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
       	mIsRunning=false;
   	}


   	/**
   	 * This thread refreshes the view in a certain rate.
   	 */
   	@Override
	public void run(){
   		while (mIsRunning){
        	long startTime=System.currentTimeMillis();
        	
        	synchronized (mSurfaceHolder){
        		mCanvas=mSurfaceHolder.lockCanvas();
		        Draw();
		        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        	}

        	long endTime=System.currentTimeMillis();

        	int diffTime=(int)(endTime-startTime);

        	while (diffTime<=TIME_IN_FRAME){
        		diffTime=(int)(System.currentTimeMillis()-startTime);
        		Thread.yield();
        	}
        }
   	}

   	/**
   	 * Save the image to a specified path.
   	 * @throws IOException
   	 */
   	public void saveImage() throws IOException {
   		
   		DisplayMetrics dm= getResources().getDisplayMetrics();
   		
   		Bitmap cacheBitmap = null;
   		Canvas cacheCanvas = null;
   		cacheBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Config.ARGB_8888);
   		cacheCanvas = new Canvas();
   		cacheCanvas.setBitmap(cacheBitmap);
   		
   		cacheCanvas.drawColor(Color.WHITE);
   		for (int i=1; i<=pathAmount; i++){
   			mPaint.setARGB(255, color1[i], color2[i], color3[i]);
   			cacheCanvas.drawPath(mPath[i], mPaint);
   		}
   		
		cacheCanvas.save(Canvas.ALL_SAVE_FLAG ); 
		cacheCanvas.restore();
		
		File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Roommates/Image");
   		if (!file.exists()) file.mkdirs();
		try{
   			FileOutputStream out = new FileOutputStream(file.getPath() + "/impression.png");  
   			if (cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
   				out.flush();
   				out.close();
   			}
   		}
   		catch (FileNotFoundException e) {
   			e.printStackTrace();
   		}
   		catch (IOException e) {
   			e.printStackTrace();
   		}
		Toast.makeText(getContext(), "Successfully saved image.", Toast.LENGTH_SHORT).show();
   	}
   	
}