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

package com.xuexiang.templateproject.adapter;

import android.widget.ImageView;

import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.model.OrderMain;

/**
 * 订单类适配器
 */
public class OutLineOrderMainAdapter extends SmartRecyclerAdapter<OrderMain> {
    public OutLineOrderMainAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, OrderMain model, int position) {
        if (model != null) {
            holder.text(R.id.order_info_supname, model.getSup_name());
            holder.text(R.id.order_info_no,model.getOrderno());
            holder.text(R.id.order_info_date,model.getOptime());
            holder.text(R.id.order_info_total,String.valueOf(model.getTotalmoney()));
            if(model.getOrderStatus()==0){
                ImageView im=holder.findViewById(R.id.order_info__status);
                im.setImageResource(R.drawable.ic_review_no);
            }else if(model.getOrderStatus()==1){
                ImageView im=holder.findViewById(R.id.order_info__status);
                im.setImageResource(R.drawable.ic_review_in);
            }else if(model.getOrderStatus()==2){
                ImageView im=holder.findViewById(R.id.order_info__status);
                im.setImageResource(R.drawable.ic_review_yes);
            }

        }
    }
}
