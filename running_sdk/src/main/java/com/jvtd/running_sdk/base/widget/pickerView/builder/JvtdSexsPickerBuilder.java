package com.jvtd.running_sdk.base.widget.pickerView.builder;

import android.content.Context;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.jvtd.running_sdk.R;
import com.jvtd.running_sdk.base.widget.pickerView.view.JvtdSexsPickerView;

public class JvtdSexsPickerBuilder extends JvtdOptionsPickerBuilder{
    public JvtdSexsPickerBuilder(Context context, OnOptionsSelectListener listener) {
        super(context, listener);
        setTitleText(context.getString(R.string.jvtd_picker_view_select_sexs));
    }

    public JvtdSexsPickerView build() {
        return new JvtdSexsPickerView(mPickerOptions);
    }
}
