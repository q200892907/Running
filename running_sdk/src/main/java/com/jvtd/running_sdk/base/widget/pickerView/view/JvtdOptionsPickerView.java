package com.jvtd.running_sdk.base.widget.pickerView.view;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.List;

public class JvtdOptionsPickerView<T> extends OptionsPickerView {
    public JvtdOptionsPickerView(PickerOptions pickerOptions) {
        super(pickerOptions);
    }

    public void show(List optionsItems) {
        super.setPicker(optionsItems);
        show();
    }

    public void show(List options1Items, List options2Items) {
        super.setPicker(options1Items, options2Items);
        show();
    }

    public void show(List options1Items, List options2Items, List options3Items) {
        super.setPicker(options1Items, options2Items, options3Items);
        show();
    }
}
