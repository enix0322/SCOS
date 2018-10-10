package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;

import android.view.MotionEvent;
import android.widget.ImageView;

import com.future.scos.R;

public class SCOSEntry extends Activity  {
    protected static final float FLIP_DISTANCE = 50;
    private ImageView imageView;
    GestureDetector mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        imageView = (ImageView) findViewById(R.id.imageview1);

        mDetector = new GestureDetector(this, new OnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向左滑...");
                    Intent intent = new Intent();
                    intent.setClass(SCOSEntry.this, MainScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    intent.putExtra("String", "FromEntry");
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    //设置切换动画，从右边进入，左边退出
                    return true;
                }
                if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向右滑...");
                    return true;
                }
                if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向上滑...");
                    return true;
                }
                if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
                    Log.i("MYTAG", "向下滑...");
                    return true;
                }

                Log.d("TAG", e2.getX() + " " + e2.getY());
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {

                // TODO Auto-generated method stub
                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mDetector.onTouchEvent(event);
    }

}
