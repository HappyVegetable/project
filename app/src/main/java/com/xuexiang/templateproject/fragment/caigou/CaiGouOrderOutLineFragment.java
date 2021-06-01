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

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.OutLineOrderMainAdapter;
import com.xuexiang.templateproject.core.BaseAdapterFragment;
import com.xuexiang.templateproject.model.OrderDetail;
import com.xuexiang.templateproject.model.OrderMain;
import com.xuexiang.templateproject.model.SupplierInfo;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xutil.common.StringUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;

@Page(name = "离线订单", anim = CoreAnim.none)
public class CaiGouOrderOutLineFragment extends BaseAdapterFragment implements SmartViewHolder.OnItemClickListener {
    /**
     * 绑定数据列表
     */
    @BindView(R.id.swipeRecyclerView)
    SwipeRecyclerView mRecyclerView;

    private OutLineOrderMainAdapter outLineOrderMainAdapter;
    List<OrderMain> orderMainList; //订单集合
    DBService<OrderMain> orderMainDBService;
    DBService<OrderDetail> orderDetailDBService;
    DBService<SupplierInfo> supplierInfoDBService;

    String pageName;

    public static CaiGouOrderOutLineFragment newInstance(String page) {
        Bundle args = new Bundle();
        args.putString("pageName", page);
        CaiGouOrderOutLineFragment fragment = new CaiGouOrderOutLineFragment();
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


        //必须在setAdapter之前调用
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //必须在setAdapter之前调用
        mRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(outLineOrderMainAdapter = new OutLineOrderMainAdapter(R.layout.adapter_list_order));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Bundle bundle = getArguments();
        pageName = bundle.getString("pageName");
        switch (pageName) {
            case "离线采购订单":
                reflash(null);
                break;
            case "离线收货订单":


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
        try {
            //按照时间顺序查询
            orderMainList = orderMainDBService.queryAllOrderBy("OPTIME", false);
            for (int i = 0; i < orderMainList.size(); i++) {
                orderMainList.get(i).setSup_name(!StringUtils.isEmptyTrim(orderMainList.get(i).getSupno()) ? supplierInfoDBService.queryForColumnFirst("SUPCUST_NO", orderMainList.get(i).getSupno()).getSupName() : "");
            }
            outLineOrderMainAdapter.refresh(orderMainList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public  void getRequestOrder(){


    }


    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

        // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
        // 2. 指定具体的高，比如80;
        // 3. WRAP_CONTENT，自身高度，不推荐;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_red)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = (menuBridge, position) -> {
        menuBridge.closeMenu();
        int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            OrderMain orderMain = orderMainList.get(position);
            try {
                orderMainDBService.deleteData(orderMain);
                List<OrderDetail> detailList = orderDetailDBService.queryByColumn("ORDERNO", orderMain.getOrderno());
                if (detailList != null && detailList.size() != 0) {
                    orderDetailDBService.deleteDatas(detailList);
                }
                XToastUtils.toast("删除成功");
                reflash(null);

            } catch (SQLException throwables) {
                XToastUtils.toast("删除失败");
                throwables.printStackTrace();
            }

        }
    };

    @Override
    public void onItemClick(View itemView, int position) {
        if(pageName.equals("离线数据")){
            OrderMain orderMain = outLineOrderMainAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString("orderMainNo", orderMain.getOrderno());
            openPageForResult(CaiGouAddFragment.class, bundle, R.string.REQUEST_CODE_ADD_ORDERMAIN);
        }else if(pageName.equals("在线数据")){

        }

    }
}
