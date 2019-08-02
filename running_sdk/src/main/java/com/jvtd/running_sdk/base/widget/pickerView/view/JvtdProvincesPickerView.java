package com.jvtd.running_sdk.base.widget.pickerView.view;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.jvtd.running_sdk.bean.JvtdProvincesBean;

import java.util.List;

public class JvtdProvincesPickerView extends JvtdOptionsPickerView<JvtdProvincesBean> {
    public JvtdProvincesPickerView(PickerOptions pickerOptions) {
        super(pickerOptions);
    }

    public void showProvinces(List<JvtdProvincesBean> optionsItems) {
        super.show(optionsItems);
    }

    public void showProvincesCity(List<JvtdProvincesBean> options1Items, List<List<JvtdProvincesBean>> options2Items) {
        super.show(options1Items, options2Items);
    }

    public void showProvincesCityArea(List<JvtdProvincesBean> options1Items, List<List<JvtdProvincesBean>> options2Items,List<List<List<JvtdProvincesBean>>> options3Items) {
        super.show(options1Items, options2Items,options3Items);
    }
}
