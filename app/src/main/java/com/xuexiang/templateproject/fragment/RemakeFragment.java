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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.model.OrderMain;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.common.StringUtils;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

@Page(name="备注")
public class RemakeFragment extends BaseFragment {


    @BindView(R.id.btn_submit)
    SuperButton submit;
    @BindView(R.id.ed_order_remake)
    MultiLineEditText medittext;
    OrderMain newOrderMain;
    String orderMainNo;
    DBService<OrderMain> orderMainDBService;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_remake;
    }

    @Override
    protected void initViews() {
        orderMainDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderMain.class);
        Bundle bundle=getArguments();
        orderMainNo=bundle.getString("orderMainNo");
        try {
            newOrderMain = orderMainDBService.queryForColumnFirst("ORDERNO", orderMainNo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(!StringUtils.isEmptyTrim(newOrderMain.getUserremark())){
            medittext.setContentText(newOrderMain.getUserremark());
        }
    }

    @SingleClick
    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
       String edit=medittext.getContentText();
       newOrderMain.setUserremark(edit);
        try {
            orderMainDBService.updateData(newOrderMain);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        popToBack();
    }
}
