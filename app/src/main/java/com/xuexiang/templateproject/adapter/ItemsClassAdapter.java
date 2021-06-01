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

import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.model.ItemClass;
import com.xuexiang.templateproject.utils.HzPyUtil;
import com.xuexiang.xutil.common.StringUtils;

public class ItemsClassAdapter   extends SmartRecyclerAdapter<ItemClass> {
    public ItemsClassAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, ItemClass model, int position) {
        if (model != null) {
            holder.text(R.id.item_info_no, "编    码：" + model.getClsNo());
            holder.text(R.id.item_info_name,  StringUtils.getString(model.getClsName()));
            //holder.text(R.id.item_info_rem,"助记码："+new HzPyUtil().getAllFirstLetter(model.getClsName()));
        }
    }
}
