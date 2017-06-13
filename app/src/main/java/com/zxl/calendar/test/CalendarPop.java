package com.zxl.calendar.test;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxl.calendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zxl
 */

public class CalendarPop extends PopupWindow {
    private String mDate = null;        // 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    private Context mContext;           //上下文
    private View mParent;               //pop依赖的父布局

    private KCalendar mKcalendar;       //日历布局
    private TextView mTvMonth;          //显示当前年月
    private Button mBtnComplete;        //完成按钮
    private RelativeLayout mLayoutNext; //下月监听按钮
    private RelativeLayout mLayoutPre;  //上月监听按钮
    private OnCalendarCompleteListener onCalendarCompleteListener;

    public CalendarPop(Context context, String strDate, View parent, OnCalendarCompleteListener onCalendarCompleteListener) {
        this.mContext = context;
        this.mParent = parent;
        this.mDate = strDate;
        this.onCalendarCompleteListener = onCalendarCompleteListener;
        initPop();
        initView();
    }

    public void initPop() {
        View view = View.inflate(mContext, R.layout.popupwindow_calendar, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        view.findViewById(R.id.ll_popup).startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_1));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        update();
        //initView
        mTvMonth = (TextView) view.findViewById(R.id.tv_month);
        mKcalendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
        mBtnComplete = (Button) view.findViewById(R.id.btn_complete);
        mLayoutNext = (RelativeLayout) view.findViewById(R.id.layout_next);
        mLayoutPre = (RelativeLayout) view.findViewById(R.id.layout_pre);
    }
    
    public void initView() {
        initMonthYear();
        initListener();
    }

    public void initMonthYear() {
        setMonthYear(mKcalendar.getCalendarYear(), mKcalendar.getCalendarMonth());
        if (!TextUtils.isEmpty(mDate)) {
            int years = Integer.parseInt(mDate.substring(0, mDate.indexOf("-")));
            int month = Integer.parseInt(mDate.substring(mDate.indexOf("-") + 1, mDate.lastIndexOf("-")));
            setMonthYear(years, month);
            mKcalendar.showCalendar(years, month);
            mKcalendar.setCalendarDayBgColor(mDate, R.drawable.calendar_date_focused);
        }
        List<String> list = new ArrayList<>(); //设置标记列表
        list.add("2014-04-01");
        list.add("2014-04-02");
        mKcalendar.addMarks(list, 0);
    }

    public void initListener() {
        //监听所选中的日期
        mKcalendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {
            public void onCalendarClick(int row, int col, String dateFormat) {
                calendarClick(row, col, dateFormat);
            }
        });
        //监听当前月份
        mKcalendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                setMonthYear(year, month);
            }
        });
        //上个月
        mLayoutPre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mKcalendar.lastMonth();
            }
        });
        //下个月
        mLayoutNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mKcalendar.nextMonth();
            }
        });
        //关闭窗口
        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onCalendarCompleteListener != null && !TextUtils.isEmpty(mDate)) {
                    onCalendarCompleteListener.complete(mDate);
                }
                dismiss();
            }
        });
    }

    public void calendarClick(int row, int col, String dateFormat) {
        int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
        if (mKcalendar.getCalendarMonth() - month == 1 || mKcalendar.getCalendarMonth() - month == -11) {// ==1跨年跳转
            mKcalendar.lastMonth();
        } else if (month - mKcalendar.getCalendarMonth() == 1 || month - mKcalendar.getCalendarMonth() == -11) {// ==1跨年跳转
            mKcalendar.nextMonth();
        } else {
            mKcalendar.removeAllBgColor();
            mKcalendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
            mDate = dateFormat;//最后返回给全局 date
        }
    }

    public void setMonthYear(int year, int month) {
        mTvMonth.setText(year + "年" + month + "月");
    }

    public interface OnCalendarCompleteListener {
        void complete(String mDate);
    }
}
