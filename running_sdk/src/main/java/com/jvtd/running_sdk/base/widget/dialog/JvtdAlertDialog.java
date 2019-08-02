package com.jvtd.running_sdk.base.widget.dialog;

import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.editText.JvtdClearEditText;
import com.jvtd.running_sdk.base.widget.toast.JvtdToast;
import com.jvtd.running_sdk.utils.JvtdEditTextUtils;
import com.jvtd.running_sdk.utils.JvtdKeyboardUtils;
import com.jvtd.running_sdk.utils.JvtdUiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class JvtdAlertDialog extends JvtdDialog implements View.OnClickListener {
    private static final float DIALOG_DIM_AMOUNT = 0.4f;

    @IntDef({LEFT, RIGHT,CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogBtn {}

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int CENTER = 3;

    private TextView mTitleTextView;
    private TextView mInfoTextView;
    private JvtdClearEditText mEditText;
    private TextView mLeftBtn;
    private TextView mRightBtn;
    private RelativeLayout mCenterView;
    private TextView mCenterBtn;
    private RelativeLayout mContentLayout;
    private View btnDivider;

    private OnClickListener mOnClickListener;
    private boolean isEdit = false;
    private boolean isShowContentLayout = true;
    private String editHint = "";
    private String editText = "";
    private String editEmptyText = "";//如果输入为空则使用
    private boolean isShowEmptyText = false;
    private String title = "";
    private String message = "";
    private String leftBtnText = "";
    private String rightBtnText = "";
    private String centerBtnText = "";
    private int maxNum = 0;
    private boolean showLeftBtn = true;
    private boolean cancelable = true;
    private boolean keyBackEnabled = true;
    private int resString;
    private int inputType = 0;

    @Override
    public void onClick(View view) {
        if (getContext() != null)
            JvtdKeyboardUtils.hideSoftInput(mEditText,getContext());
        if (mOnClickListener == null) return;
        boolean dismissDialog = true;
        int i = view.getId();
        String mText = "";
        if (mEditText.getText() != null)
        {
            mText = mEditText.getText().toString().trim();
        }
        int position = LEFT;
        if (i == R.id.alert_dialog_left_btn) {
            position = LEFT;
        } else if (i == R.id.alert_dialog_center_btn){
            position = CENTER;
        } else if (i == R.id.alert_dialog_right_btn) {
            if (isShowContentLayout && isEdit && TextUtils.isEmpty(mText)) {
                dismissDialog = false;
                JvtdToast toast = new JvtdToast(getActivity(), JvtdToast.FLAG_BOTTOM);
                toast.setScreenFront(true);
                if (!TextUtils.isEmpty(editEmptyText)) {
                    mText = editEmptyText;
                    dismissDialog = true;
                } else {
                    toast.showMessage(resString == 0 ? R.string.jvtd_alert_dialog_input_cannot_null : resString, Toast.LENGTH_SHORT);
                }
            }
            position = RIGHT;
        }
        if (dismissDialog)
        {
            mOnClickListener.onClick(position, mText);
            dismiss();
        }
    }

    /**
     * 触控方法
     *
     * @author Chenlei
     * created at 2018/9/17
     **/
    public interface OnClickListener {
        void onClick(@DialogBtn int position, String inputText);
    }

    @Override
    public void onDestroyView() {
        dismiss();
        super.onDestroyView();
    }

    @Override
    public boolean getCancelOutside() {
        return cancelable;
    }

    @Override
    public boolean getKeyBackEnabled() {
        return keyBackEnabled;
    }

    @Override
    public int getWidth() {
        if (getContext() != null)
            return (int) (JvtdUiUtils.windowWidth(getContext()) * 0.8);
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public float getDimAmount() {
        return DIALOG_DIM_AMOUNT;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.jvtd_alert_dialog_layout;
    }

    @Override
    public void bindView(View v) {
        mTitleTextView = v.findViewById(R.id.alert_dialog_title_text_view);
        mInfoTextView = v.findViewById(R.id.alert_dialog_info_text_view);
        mEditText = v.findViewById(R.id.alert_dialog_edit_text);
        mLeftBtn = v.findViewById(R.id.alert_dialog_left_btn);
        mRightBtn = v.findViewById(R.id.alert_dialog_right_btn);
        mContentLayout = v.findViewById(R.id.content_layout);
        btnDivider = v.findViewById(R.id.alert_dialog_btn_divider);
        mCenterView = v.findViewById(R.id.alert_dialog_center_view);
        mCenterBtn = v.findViewById(R.id.alert_dialog_center_btn);

        settingAlertDialog();
    }

    private void settingAlertDialog() {
        if (!TextUtils.isEmpty(title)) mTitleTextView.setText(title);

        if (isShowContentLayout)
        {
            mContentLayout.setVisibility(View.VISIBLE);
            if (isEdit)
            {
                mEditText.setVisibility(View.VISIBLE);
                if (maxNum > 0)
                {
                    JvtdEditTextUtils.setMaxLength(mEditText,maxNum);
                }
                mInfoTextView.setVisibility(View.GONE);
            } else
            {
                mEditText.setVisibility(View.GONE);
                mInfoTextView.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(message) && !isEdit) mInfoTextView.setText(message);
            if (isEdit)
            {
                if (inputType != 0) mEditText.setInputType(inputType);
                if (!TextUtils.isEmpty(editHint)) mEditText.setHint(editHint);
                String tempStr = "";
                if (!TextUtils.isEmpty(editText))
                {
                    tempStr = editText;
                }
                if (!TextUtils.isEmpty(editEmptyText) && isShowEmptyText)
                    tempStr = editEmptyText;
                if (!TextUtils.isEmpty(tempStr)){
                    mEditText.setText(tempStr);
                    if (tempStr.length() <= maxNum || maxNum == 0)
                        mEditText.setSelection(tempStr.length());// 将光标移至文字末尾
                }
            }
        } else
            mContentLayout.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(leftBtnText)) mLeftBtn.setText(leftBtnText);
        if (!TextUtils.isEmpty(rightBtnText)) mRightBtn.setText(rightBtnText);
        if (!TextUtils.isEmpty(centerBtnText)){
            mCenterBtn.setText(centerBtnText);
            mCenterView.setVisibility(View.VISIBLE);
            mCenterBtn.setOnClickListener(this);
        }else {
            mCenterView.setVisibility(View.GONE);
        }
        if (!showLeftBtn)
        {
            mLeftBtn.setVisibility(View.GONE);
            btnDivider.setVisibility(View.GONE);
        }

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
    }

    @Override
    public int getStyle() {
        return JvtdDialog.NORMAL;
    }

    /**
     * 是否点击外部可销毁
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setCanCancelable(boolean cancelable)
    {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 是否显示详细布局 （输入框及提醒消息）优先级最高
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setShowContentLayout(boolean showContentLayout)
    {
        isShowContentLayout = showContentLayout;
        return this;
    }

    /**
     * 是否开启输入模式
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setEdit(boolean edit)
    {
        isEdit = edit;
        return this;
    }

    /**
     * 输入hint
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setEditHint(String editHint)
    {
        this.editHint = editHint;
        return this;
    }

    /**
     * 输入最大长度
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setEditMaxNum(int maxNum)
    {
        this.maxNum = maxNum;
        return this;
    }

    /**
     * 标题
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setTitle(String title)
    {
        this.title = title;
        return this;
    }

    /**
     * 左侧按钮文字
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setLeftBtnText(String leftBtnText)
    {
        this.leftBtnText = leftBtnText;
        return this;
    }

    /**
     * 右侧按钮文字
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setRightBtnText(String rightBtnText)
    {
        this.rightBtnText = rightBtnText;
        return this;
    }

    /**
     * 中间按钮文字 如果为空中间按钮不显示
     *
     * @author Chenlei
     * created at 2018/9/25
     **/
    public JvtdAlertDialog setCenterBtnText(String centerBtnText){
        this.centerBtnText = centerBtnText;
        return this;
    }

    /**
     * 按钮点击事件
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setOnClickListener(OnClickListener onClickListener)
    {
        this.mOnClickListener = onClickListener;
        return this;
    }

    /**
     * 输入框输入文本
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setEditText(String editText)
    {
        this.editText = editText;
        return this;
    }

    /**
     * 输入为空时默认文本  如果设置则可以为空时点击右侧按钮
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setEditEmptyText(String editEmptyText) {
        this.editEmptyText = editEmptyText;
        return this;
    }

    /**
     * 是否直接展示空字符串 优先级大于edittext
     *
     * @author Chenlei
     * created at 2018/9/27
     **/
    public JvtdAlertDialog setShowEmptyText(boolean showEmptyText) {
        isShowEmptyText = showEmptyText;
        return this;
    }

    /**
     * 提示信息
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setMessage(String message)
    {
        this.message = message;
        return this;
    }

    /**
     * 是否显示左侧按钮
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setShowLeftBtn(boolean showLeftBtn)
    {
        this.showLeftBtn = showLeftBtn;
        return this;
    }

    /**
     * 是否返回键销毁
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setKeyBackEnabled(boolean keyBackEnabled)
    {
        this.keyBackEnabled = keyBackEnabled;
        return this;
    }

    /**
     * 输入为空时 提示信息
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setToastMessage(int resString)
    {
        this.resString = resString;
        return this;
    }

    /**
     * 输入类型
     *
     * @author Chenlei
     * created at 2018/9/18
     **/
    public JvtdAlertDialog setInputType(int type){
        this.inputType = type;
        return this;
    }
}
