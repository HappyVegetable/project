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

package com.xuexiang.templateproject.fragment.caigou;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.OnLineOrderMainAdapter;
import com.xuexiang.templateproject.core.BaseAdapterFragment;
import com.xuexiang.templateproject.model.OrderDetail;
import com.xuexiang.templateproject.model.OrderMain;
import com.xuexiang.templateproject.model.SupplierInfo;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.List;

import butterknife.BindView;

@Page(name = "在线订单", anim = CoreAnim.none)
public class CaiGouOrderOnLineFragment extends BaseAdapterFragment implements SmartViewHolder.OnItemClickListener {

    /**
     * 绑定数据列表
     */
    @BindView(R.id.swipeRecyclerView)
    SwipeRecyclerView mRecyclerView;

    private OnLineOrderMainAdapter outLineOrderMainAdapter;
    List<OrderMain> orderMainList; //订单集合
    DBService<OrderMain> orderMainDBService;
    DBService<OrderDetail> orderDetailDBService;
    DBService<SupplierInfo> supplierInfoDBService;

    String pageName;

    public static CaiGouOrderOnLineFragment newInstance(String page) {
        Bundle args = new Bundle();
        args.putString("pageName", page);
        CaiGouOrderOnLineFragment fragment = new CaiGouOrderOnLineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_caigou_order;
    }

    @Override
    protected void initViews() {
        orderMainDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderMain.class);
        orderDetailDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderDetail.class);
        supplierInfoDBService = ExternalDataBaseRepository.getInstance().getDataBase(SupplierInfo.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(outLineOrderMainAdapter = new OnLineOrderMainAdapter(R.layout.adapter_list_order));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Bundle bundle = getArguments();
        pageName = bundle.getString("pageName");
        switch (pageName) {
            case "在线采购订单":

                break;
            case "在线收货订单":

                break;
            default:

                break;
        }


    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initListeners() {
        outLineOrderMainAdapter.setOnItemClickListener(this);
    }

    public void reflash(Bundle bundle) {
        System.out.println("来到OnLine");
        XHttp.get("/getOnLineOrder")
                .syncRequest(false)
                .onMainThread(true)
                .execute(new SimpleCallBack<List<OrderMain>>(){
                    @Override
                    public void onSuccess(List<OrderMain> response) throws Throwable {

                        if (response != null && response.size() > 0) {
                            outLineOrderMainAdapter.refresh(response);

                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        XToastUtils.toast("发生了错误");
                    }
                });
       /* try {
            //按照时间顺序查询
            orderMainList = orderMainDBService.queryAllOrderBy("OPTIME", false);
            for (int i = 0; i < orderMainList.size(); i++) {
                orderMainList.get(i).setSup_name(!StringUtils.isEmptyTrim(orderMainList.get(i).getSupno()) ? supplierInfoDBService.queryForColumnFirst("SUPCUST_NO", orderMainList.get(i).getSupno()).getSupName() : "");
            }
            orderMainAdapter.refresh(orderMainList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/
    }


    @Override
    public void onItemClick(View itemView, int position) {
        if (pageName.equals("")) {

        } else if (pageName.equals("在线数据")) {

        }

    }
}
