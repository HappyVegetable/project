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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.OrderDetailAdapter;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.fragment.RemakeFragment;
import com.xuexiang.templateproject.fragment.ScanSettingFragment;
import com.xuexiang.templateproject.fragment.branch.SearchBranchFragment;
import com.xuexiang.templateproject.fragment.items.SearchItemFragment;
import com.xuexiang.templateproject.fragment.supplier.SearchSupplierFragment2;
import com.xuexiang.templateproject.model.Branch;
import com.xuexiang.templateproject.model.OrderDetail;
import com.xuexiang.templateproject.model.OrderMain;
import com.xuexiang.templateproject.model.SupplierInfo;
import com.xuexiang.templateproject.utils.DemoDataProvider;
import com.xuexiang.templateproject.utils.GenUnitDateNo;
import com.xuexiang.templateproject.utils.RandomUtils;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaImageButton;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.imageview.IconImageView;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.common.StringUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

@Page(name = "新建采购订单", anim = CoreAnim.present)
public class CaiGouAddFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener {


    @BindView(R.id.swipeRecyclerView)
    SwipeRecyclerView mRecyclerView;

    @BindView(R.id.stv_ordermainno)
    SuperTextView stv_ordermainno;

    @BindView(R.id.stv_suppliername)
    SuperTextView stv_suppliername;
    @BindView(R.id.stv_branchname)
    SuperTextView stv_branchname;

    @BindView(R.id.btn_add_order_item)
    XUIAlphaImageButton im_addorderitem;
   /* @BindView(R.id.btn_search_order_item)
    ImageView im_searchorderitem;*/

    @BindView(R.id.tv_itemCounts)
    TextView tv_itemCounts;
    @BindView(R.id.tv_realqtyCount)
    TextView tv_realqtyCount;
    @BindView(R.id.tv_totalMoney)
    TextView tv_totalMoney;

    //右上弹出选择框
    private XUISimplePopup mMenuPopup;

    DecimalFormat df = new DecimalFormat("#.00");
    String type = "0"; //判断是新增还是修改
    String selectItemNo; //选中的商品号
    String orderMainNo; //订单号
    OrderMain newOrderMain;
    String remake = "";  //订单备注
    Boolean showItemToast = true;  //默认扫描商品有弹窗修改数量
    Boolean default0 = true; //引用原单数量为0

    List<OrderDetail> detailList = new ArrayList<>();
    DBService<OrderMain> orderMainDBService;
    DBService<OrderDetail> orderDetailDBService;
    DBService<Branch> branchDBService;
    DBService<SupplierInfo> supplierInfoDBService;
    private OrderDetailAdapter orderDetailAdapter;
    int itemCount;  //共计项目数
    Double realqtyCount = 0d;  //共计件数
    Double totalMoney = 0d; //总金额数
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_caigou;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause()
        } else {
            // 相当于Fragment的onResume()
            if (StringUtils.isEmptyTrim(selectItemNo)) { //判断是否是选择商品后返回该页面
                reflash(false);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("itemNo", selectItemNo);
                bundle.putString("orderMainNo", orderMainNo);
                selectItemNo = null;
                openPage(bundle);
            }

        }
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_navigation_more) {
            @Override
            @SingleClick
            public void performAction(View view) {
                mMenuPopup.showDown(view);
                /*if(checkData()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("resultCode", "ORDER_SELECT_ITEM");
                    openPageForResult(SearchItemFragment.class, bundle, R.string.REQUEST_CODE_GET_ORDER_SELECT_ITEM);
                }*/
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {

        initMenuPopup();//初始化菜单

        //必须在setAdapter之前调用
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //必须在setAdapter之前调用
        mRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(orderDetailAdapter = new OrderDetailAdapter(R.layout.adapter_list_detail_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        orderMainDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderMain.class);
        orderDetailDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderDetail.class);
        supplierInfoDBService = ExternalDataBaseRepository.getInstance().getDataBase(SupplierInfo.class);
        branchDBService = ExternalDataBaseRepository.getInstance().getDataBase(Branch.class);


        Bundle bundle = getArguments();

        //新建：0 ；更改：1
        String requestOrderMainNo = bundle.getString("orderMainNo");
        if (!StringUtils.isEmptyTrim(requestOrderMainNo)) {
            type = "1";
            orderMainNo = requestOrderMainNo;
            try {

                newOrderMain = orderMainDBService.queryForColumnFirst("ORDERNO", orderMainNo);
                if (!StringUtils.isEmptyTrim(newOrderMain.getSupno())) {
                    SupplierInfo supplierInfo = supplierInfoDBService.queryForColumnFirst("SUPCUST_NO", newOrderMain.getSupno());
                    stv_suppliername.setCenterString(String.format("[%s]%s", supplierInfo.getSupNo(), supplierInfo.getSupName()));
                }
                if (!StringUtils.isEmptyTrim(newOrderMain.getBranchno())) {
                    Branch branch = branchDBService.queryForColumnFirst("BRANCH_NO", newOrderMain.getBranchno());
                    stv_branchname.setCenterString(String.format("[%s]%s", branch.getBranch_no(), branch.getBranch_name()));
                }
                reflash(false);//加载订单子项目
                remake = newOrderMain.getUserremark();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            newOrderMain = new OrderMain();
            newOrderMain.setOrdername("采购订单");
            newOrderMain.setOrdertype("12");
            orderMainNo = "CGDD-" + simpleDateFormat2.format(new Date());
            newOrderMain.setOrderno(orderMainNo);
            newOrderMain.setOptime(simpleDateFormat.format(new Date()));
            newOrderMain.setOperno("管理员");
            newOrderMain.setOrderStatus(RandomUtils.getRandom(0, 3));
            newOrderMain.setPay_way("A");
            newOrderMain.setUserremark("");
            try {
                orderMainDBService.insert(newOrderMain);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        stv_ordermainno.setCenterString(orderMainNo);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        stv_suppliername.setOnSuperTextViewClickListener((v1) -> {
            openPageForResult(SearchSupplierFragment2.class, null, R.string.REQUEST_CODE_GET_SUPPLIER);
        });
        stv_branchname.setOnSuperTextViewClickListener((v2) -> {
            openPageForResult(SearchBranchFragment.class, null, R.string.REQUEST_CODE_GET_BRANCH);
        });
        im_addorderitem.setOnClickListener((v3) -> {
            if (checkData()) {
                Bundle bundle = new Bundle();
                bundle.putString("resultCode", "ORDER_SELECT_ITEM");
                openPageForResult(SearchItemFragment.class, bundle, R.string.REQUEST_CODE_GET_ORDER_SELECT_ITEM);
            }
        });
        //列表点击
        orderDetailAdapter.setOnItemClickListener(this);
    }

    @SingleClick
    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                //saveOrderMain();
                //XToastUtils.toast("模拟提交订单");

                popToBack();
                break;
        }
    }

    private void initMenuPopup() {
        mMenuPopup = new XUISimplePopup(getContext(), DemoDataProvider.menuItems)
                .create((adapter, item, position) ->

                        openRemakeOrSetting(item.getTitle().toString())

                );
    }

    /**
     * 根据标题判断打开哪个设置页面
     *
     * @param title
     */
    private void openRemakeOrSetting(String title) {
        Bundle bundle = new Bundle();
        if (title.equals("备注")) {
            bundle.putString("orderMainNo", orderMainNo);
            openPageForResult(RemakeFragment.class, bundle, R.string.REQUEST_CODE_ADD_ORDERMAIN_REMAKE);

        } else if (title.equals("设置")) {
            openPageForResult(ScanSettingFragment.class, bundle, R.string.REQUEST_CODE_ADD_ORDERMAIN_SETTING);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (detailList.size() != 0) {
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        "订单还未提交，是否要提交？",
                        getString(R.string.lab_yes),
                        (dialog, which) -> {

                            dialog.dismiss();
                            popToBack();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> {

                            dialog.dismiss();
                            popToBack();
                        }
                );
            } else {
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        "空单是否删除？",
                        getString(R.string.lab_yes),
                        (dialog, which) -> {
                            dialog.dismiss();
                            deleteAllOrderDetail();
                            popToBack();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> {
                            dialog.dismiss();
                            popToBack();
                        }
                );
            }
        }
        return false;
    }

    private boolean checkData() {
        if (StringUtils.isEmptyTrim(newOrderMain.getSupno())) {
            XToastUtils.toast("请选择供应商");
            return false;
        } else if (StringUtils.isEmptyTrim(newOrderMain.getBranchno())) {
            XToastUtils.toast("请选择仓库");
            return false;
        } else {
            return true;
        }
    }

    private void deleteAllOrderDetail() {
        try {
            orderMainDBService.deleteData(newOrderMain);//删除主单
            orderDetailDBService.deleteDatas(detailList);//删除子单
        } catch (SQLException throwables) {
            XToastUtils.toast("删除出错了");
            throwables.printStackTrace();
        }
    }

    private void reflash(boolean updateMain) {
        try {
            detailList = orderDetailDBService.queryByColumn("ORDERNO", orderMainNo);

            if (detailList != null && detailList.size() > 0) {
                itemCount = detailList.size();
                realqtyCount = 0d;
                totalMoney = 0d;
                for (int i = 0; i < detailList.size(); i++) {
                    OrderDetail orderDetail = detailList.get(i);
                    realqtyCount += orderDetail.getRealqty();
                    totalMoney += orderDetail.getSubamt();
                }

                tv_itemCounts.setText(String.valueOf(itemCount));
                tv_realqtyCount.setText(realqtyCount.toString());
                tv_totalMoney.setText(String.valueOf(Double.parseDouble(df.format(totalMoney))));
            } else {
                tv_itemCounts.setText("0");
                tv_realqtyCount.setText("0");
                tv_totalMoney.setText("0");
            }
            if (updateMain) {
                //刷新主单的金额，数量，项目数
                OrderMain orderMain = orderMainDBService.queryForColumnFirst("ORDERNO", orderMainNo);
                orderMain.setTotalmoney(Double.parseDouble(df.format(totalMoney)));
                orderMain.setTotalamt(realqtyCount);
                orderMain.setTotalqty(itemCount);
                orderMainDBService.updateData(orderMain);
            }
        } catch (SQLException throwables) {
            XToastUtils.toast("查询列表出错了");
            throwables.printStackTrace();
        }
        orderDetailAdapter.refresh(detailList);
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
            OrderDetail orderDetail = detailList.get(position);
            try {
                orderDetailDBService.deleteData(orderDetail);
                reflash(true);
            } catch (SQLException throwables) {
                XToastUtils.error(throwables);
                throwables.printStackTrace();
            }
        }
    };

    @Override
    public void onItemClick(View itemView, int position) {
        OrderDetail orderDetail = orderDetailAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("orderDetailId", String.valueOf(orderDetail.getId()));
        bundle.putString("itemNo", orderDetail.getItemno());
        bundle.putString("orderMainNo", orderMainNo);

        openPage(bundle);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case R.string.REQUEST_CODE_GET_SUPPLIER:
                    stv_suppliername.setCenterString(String.format("[%s]%s", data.getStringExtra("supplierNo").trim(), data.getStringExtra("supplierName").trim()));


                    try {
                        newOrderMain = orderMainDBService.queryForColumnFirst("ORDERNO", orderMainNo);
                        newOrderMain.setSupno(data.getStringExtra("supplierNo").trim());
                        orderMainDBService.updateData(newOrderMain);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case R.string.REQUEST_CODE_GET_BRANCH:
                    stv_branchname.setCenterString(String.format("[%s]%s", data.getStringExtra("branchNo").trim(), data.getStringExtra("branchName").trim()));

                    try {
                        newOrderMain = orderMainDBService.queryForColumnFirst("ORDERNO", orderMainNo);
                        newOrderMain.setBranchno(data.getStringExtra("branchNo").trim());
                        orderMainDBService.updateData(newOrderMain);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    break;
                case R.string.REQUEST_CODE_GET_ORDER_SELECT_ITEM:
                    selectItemNo = data.getStringExtra("itemNo");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reflash(true);//Dialog弹窗不能使用onFragmentResult获取返回结果
    }


    private void openPage(Bundle bundle) {
        OrderDetailEditFragment dialog = new OrderDetailEditFragment();
        dialog.setTargetFragment(this, R.string.REQUEST_CODE_ADD_ORDERDETAIL);
        dialog.setArguments(bundle);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "orderDetailDialog");
    }
}
