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
import com.xuexiang.templateproject.adapter.entity.BriefInfo;
import com.xuexiang.templateproject.model.Items;
import com.xuexiang.xutil.common.StringUtils;

public class BriefAdapter  extends SmartRecyclerAdapter<BriefInfo> {
    public BriefAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, BriefInfo model, int position) {
        if (model != null) {
            holder.text(R.id.item_info_no, "编    码：" + model.getNo());
            holder.text(R.id.item_info_name,  StringUtils.getString(model.getName()));
            //holder.text(R.id.item_info_rem,"助记码："+model.getRem());
        }
    }
}
