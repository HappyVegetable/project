/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.templateproject.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.utils.ProperUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;

import butterknife.BindView;

@Page(name = "扫描设置")
public class ScanSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.sb_showItemToast)
    SwitchButton showItemToast; //扫描商品弹出数量修改窗口
    @BindView(R.id.sb_default)
    SwitchButton default0;  //引用原单数量为0
    String checked1;
    String checked2;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scansetting;
    }

    @Override
    protected void initViews() {
        checked1 =   ProperUtils.getConfigProperties("showItemToast");
        checked2 = ProperUtils.getConfigProperties("default0");
        if (checked1 != null&&checked1.equals("true")) {
            showItemToast.setChecked(true);
        }else{
            showItemToast.setChecked(false);
        }
        if (checked2 != null&&checked2.equals("true")) {
            default0.setChecked(true);
        }else{
            default0.setChecked(false);
        }

    }

    @Override
    protected void initListeners() {
        showItemToast.setOnCheckedChangeListener(this);
        default0.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == showItemToast.getId()) {
            if (isChecked) {
                ProperUtils.writeDateToLocalFile("showItemToast", "true");
            } else {
                ProperUtils.writeDateToLocalFile("showItemToast", "false");
            }
        } else if (buttonView.getId() == default0.getId()) {

            if (isChecked) {
                ProperUtils.writeDateToLocalFile("default0", "true");
            } else {
                ProperUtils.writeDateToLocalFile("default0", "false");
            }
        }
    }
}
