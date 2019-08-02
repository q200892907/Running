package com.jvtd.running_sdk.base.widget.pickerView.builder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.contrarywind.view.WheelView;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.pickerView.view.JvtdTimePickerView;

import java.util.Calendar;

public class JvtdTimePickerBuilder {
    private PickerOptions mPickerOptions;
    public JvtdTimePickerBuilder(Context context, OnTimeSelectListener listener) {
        mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        mPickerOptions.context = context;
        mPickerOptions.timeSelectListener = listener;
        setType(new boolean[]{true, true, true, false, false, false});// 默认全部显示
        setCancelText(context.getString(R.string.jvtd_picker_view_cancel_title));//取消按钮文字
        setSubmitText(context.getString(R.string.jvtd_picker_view_ok_title));//确认按钮文字
        setTitleSize(16);//标题文字大小
        setTitleText(context.getString(R.string.jvtd_picker_view_select_time));//标题文字
        setOutSideCancelable(true);//点击屏幕，点在控件外部范围时，是否取消显示
        isCyclic(false);//是否循环滚动
        setTitleColor(ContextCompat.getColor(context, R.color.jvtd_picker_view_title_color));//标题文字颜色
        setSubmitColor(ContextCompat.getColor(context, R.color.jvtd_picker_view_btn_color));//确定按钮文字颜色
        setCancelColor(ContextCompat.getColor(context, R.color.jvtd_picker_view_btn_color));//取消按钮文字颜色
        setTitleBgColor(ContextCompat.getColor(context, R.color.jvtd_picker_view_title_bg_color));//标题背景颜色
        setBgColor(ContextCompat.getColor(context, R.color.jvtd_picker_view_info_bg_color));//滚轮背景颜色
        setTextColorCenter(ContextCompat.getColor(context, R.color.jvtd_picker_view_current_text_color));//当前字体颜色
        setTextColorOut(ContextCompat.getColor(context, R.color.jvtd_picker_view_other_text_color));//其他字体颜色
        setSubCalSize(14);//按钮文字大小
        setContentTextSize(14);//内容文字大小
        setLineSpacingMultiplier(2.4f);
//        setRangDate(startDate, endDate);//起始终止年月日设定
        setLabel(context.getString(R.string.jvtd_picker_view_year),
                context.getString(R.string.jvtd_picker_view_month),
                context.getString(R.string.jvtd_picker_view_day),
                context.getString(R.string.jvtd_picker_view_hour),
                context.getString(R.string.jvtd_picker_view_minute),
                context.getString(R.string.jvtd_picker_view_seconds));//默认设置为年月日时分秒
        isCenterLabel(true); //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        isDialog(false);//是否显示为对话框样式
    }

    //Option
    public JvtdTimePickerBuilder setGravity(int gravity) {
        mPickerOptions.textGravity = gravity;
        return this;
    }


    /**
     * new boolean[]{true, true, true, false, false, false}
     * control the "year","month","day","hours","minutes","seconds " display or hide.
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
     *
     * @param type 布尔型数组，长度需要设置为6。
     * @return TimePickerBuilder
     */
    public JvtdTimePickerBuilder setType(boolean[] type) {
        mPickerOptions.type = type;
        return this;
    }

    public JvtdTimePickerBuilder setSubmitText(String textContentConfirm) {
        mPickerOptions.textContentConfirm = textContentConfirm;
        return this;
    }

    public JvtdTimePickerBuilder isDialog(boolean isDialog) {
        mPickerOptions.isDialog = isDialog;
        return this;
    }

    public JvtdTimePickerBuilder setCancelText(String textContentCancel) {
        mPickerOptions.textContentCancel = textContentCancel;
        return this;
    }

    public JvtdTimePickerBuilder setTitleText(String textContentTitle) {
        mPickerOptions.textContentTitle = textContentTitle;
        return this;
    }

    public JvtdTimePickerBuilder setSubmitColor(int textColorConfirm) {
        mPickerOptions.textColorConfirm = textColorConfirm;
        return this;
    }

    public JvtdTimePickerBuilder setCancelColor(int textColorCancel) {
        mPickerOptions.textColorCancel = textColorCancel;
        return this;
    }

    /**
     * ViewGroup 类型的容器
     *
     * @param decorView 选择器会被添加到此容器中
     * @return TimePickerBuilder
     */
    public JvtdTimePickerBuilder setDecorView(ViewGroup decorView) {
        mPickerOptions.decorView = decorView;
        return this;
    }

    public JvtdTimePickerBuilder setBgColor(int bgColorWheel) {
        mPickerOptions.bgColorWheel = bgColorWheel;
        return this;
    }

    public JvtdTimePickerBuilder setTitleBgColor(int bgColorTitle) {
        mPickerOptions.bgColorTitle = bgColorTitle;
        return this;
    }

    public JvtdTimePickerBuilder setTitleColor(int textColorTitle) {
        mPickerOptions.textColorTitle = textColorTitle;
        return this;
    }

    public JvtdTimePickerBuilder setSubCalSize(int textSizeSubmitCancel) {
        mPickerOptions.textSizeSubmitCancel = textSizeSubmitCancel;
        return this;
    }

    public JvtdTimePickerBuilder setTitleSize(int textSizeTitle) {
        mPickerOptions.textSizeTitle = textSizeTitle;
        return this;
    }

    public JvtdTimePickerBuilder setContentTextSize(int textSizeContent) {
        mPickerOptions.textSizeContent = textSizeContent;
        return this;
    }

    /**
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     *
     * @param date
     * @return TimePickerBuilder
     */
    public JvtdTimePickerBuilder setDate(Calendar date) {
        mPickerOptions.date = date;
        return this;
    }

    public JvtdTimePickerBuilder setLayoutRes(int res, CustomListener customListener) {
        mPickerOptions.layoutRes = res;
        mPickerOptions.customListener = customListener;
        return this;
    }


    /**
     * 设置起始时间
     * 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
     */

    public JvtdTimePickerBuilder setRangDate(Calendar startDate, Calendar endDate) {
        mPickerOptions.startDate = startDate;
        mPickerOptions.endDate = endDate;
        return this;
    }


    /**
     * 设置间距倍数,但是只能在1.0-4.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public JvtdTimePickerBuilder setLineSpacingMultiplier(float lineSpacingMultiplier) {
        mPickerOptions.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public JvtdTimePickerBuilder setDividerColor(int dividerColor) {
        mPickerOptions.dividerColor = dividerColor;
        return this;
    }

    /**
     * 设置分割线的类型
     *
     * @param dividerType
     */
    public JvtdTimePickerBuilder setDividerType(WheelView.DividerType dividerType) {
        mPickerOptions.dividerType = dividerType;
        return this;
    }

    /**
     * //显示时的外部背景色颜色,默认是灰色
     *
     * @param backgroundId
     */

    public JvtdTimePickerBuilder setBackgroundId(int backgroundId) {
        mPickerOptions.outSideColor = backgroundId;
        return this;
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public JvtdTimePickerBuilder setTextColorCenter(int textColorCenter) {
        mPickerOptions.textColorCenter = textColorCenter;
        return this;
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public JvtdTimePickerBuilder setTextColorOut(int textColorOut) {
        mPickerOptions.textColorOut = textColorOut;
        return this;
    }

    public JvtdTimePickerBuilder isCyclic(boolean cyclic) {
        mPickerOptions.cyclic = cyclic;
        return this;
    }

    public JvtdTimePickerBuilder setOutSideCancelable(boolean cancelable) {
        mPickerOptions.cancelable = cancelable;
        return this;
    }

    public JvtdTimePickerBuilder setLunarCalendar(boolean lunarCalendar) {
        mPickerOptions.isLunarCalendar = lunarCalendar;
        return this;
    }


    public JvtdTimePickerBuilder setLabel(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        mPickerOptions.label_year = label_year;
        mPickerOptions.label_month = label_month;
        mPickerOptions.label_day = label_day;
        mPickerOptions.label_hours = label_hours;
        mPickerOptions.label_minutes = label_mins;
        mPickerOptions.label_seconds = label_seconds;
        return this;
    }

    /**
     * 设置X轴倾斜角度[ -90 , 90°]
     *
     * @param x_offset_year    年
     * @param x_offset_month   月
     * @param x_offset_day     日
     * @param x_offset_hours   时
     * @param x_offset_minutes 分
     * @param x_offset_seconds 秒
     * @return
     */
    public JvtdTimePickerBuilder setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day,
                                                int x_offset_hours, int x_offset_minutes, int x_offset_seconds) {
        mPickerOptions.x_offset_year = x_offset_year;
        mPickerOptions.x_offset_month = x_offset_month;
        mPickerOptions.x_offset_day = x_offset_day;
        mPickerOptions.x_offset_hours = x_offset_hours;
        mPickerOptions.x_offset_minutes = x_offset_minutes;
        mPickerOptions.x_offset_seconds = x_offset_seconds;
        return this;
    }

    public JvtdTimePickerBuilder isCenterLabel(boolean isCenterLabel) {
        mPickerOptions.isCenterLabel = isCenterLabel;
        return this;
    }

    /**
     * @param listener 切换item项滚动停止时，实时回调监听。
     * @return
     */
    public JvtdTimePickerBuilder setTimeSelectChangeListener(OnTimeSelectChangeListener listener) {
        mPickerOptions.timeSelectChangeListener = listener;
        return this;
    }

    public JvtdTimePickerView build() {
        return new JvtdTimePickerView(mPickerOptions);
    }
}
