package com.zxl.calendar.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zxl.calendar.R;

public class SecondActivity extends Activity {
    private Button bt;
    private String mStrDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showPop();
            }
        });
    }

    public void showPop() {
        new CalendarPop(SecondActivity.this, mStrDate, bt, onCalendarCompleteListener);
    }

    private CalendarPop.OnCalendarCompleteListener onCalendarCompleteListener = new CalendarPop.OnCalendarCompleteListener() {
        @Override
        public void complete(String mDate) {
            mStrDate = mDate;
            bt.setText("[" + mDate + "]");
        }
    };
}
