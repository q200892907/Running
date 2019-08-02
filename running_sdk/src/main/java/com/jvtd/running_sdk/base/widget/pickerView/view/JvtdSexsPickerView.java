package com.jvtd.running_sdk.base.widget.pickerView.view;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.jvtd.running_sdk.utils.JvtdArrayUtils;

import java.util.List;

public class JvtdSexsPickerView extends JvtdOptionsPickerView<String> {
    public JvtdSexsPickerView(PickerOptions pickerOptions) {
        super(pickerOptions);
    }

    public void showSexs() {
        showSexs(JvtdArrayUtils.SEXS);
    }

    public void showSexs(List<String> sexsList){
        super.show(sexsList);
    }
}
