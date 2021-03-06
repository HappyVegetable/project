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

@Page(name = "??????????????????", anim = CoreAnim.present)
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

    //?????????????????????
    private XUISimplePopup mMenuPopup;

    DecimalFormat df = new DecimalFormat("#.00");
    String type = "0"; //???????????????????????????
    String selectItemNo; //??????????????????
    String orderMainNo; //?????????
    OrderMain newOrderMain;
    String remake = "";  //????????????
    Boolean showItemToast = true;  //???????????????????????????????????????
    Boolean default0 = true; //?????????????????????0

    List<OrderDetail> detailList = new ArrayList<>();
    DBService<OrderMain> orderMainDBService;
    DBService<OrderDetail> orderDetailDBService;
    DBService<Branch> branchDBService;
    DBService<SupplierInfo> supplierInfoDBService;
    private OrderDetailAdapter orderDetailAdapter;
    int itemCount;  //???????????????
    Double realqtyCount = 0d;  //????????????
    Double totalMoney = 0d; //????????????
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_caigou;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //?????????Fragment???onPause()
        } else {
            // ?????????Fragment???onResume()
            if (StringUtils.isEmptyTrim(selectItemNo)) { //?????????????????????????????????????????????
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

        initMenuPopup();//???????????????

        //?????????setAdapter????????????
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //?????????setAdapter????????????
        mRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(orderDetailAdapter = new OrderDetailAdapter(R.layout.adapter_list_detail_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        orderMainDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderMain.class);
        orderDetailDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderDetail.class);
        supplierInfoDBService = ExternalDataBaseRepository.getInstance().getDataBase(SupplierInfo.class);
        branchDBService = ExternalDataBaseRepository.getInstance().getDataBase(Branch.class);


        Bundle bundle = getArguments();

        //?????????0 ????????????1
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
                reflash(false);//?????????????????????
                remake = newOrderMain.getUserremark();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            newOrderMain = new OrderMain();
            newOrderMain.setOrdername("????????????");
            newOrderMain.setOrdertype("12");
            orderMainNo = "CGDD-" + simpleDateFormat2.format(new Date());
            newOrderMain.setOrderno(orderMainNo);
            newOrderMain.setOptime(simpleDateFormat.format(new Date()));
            newOrderMain.setOperno("?????????");
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
        //????????????
        orderDetailAdapter.setOnItemClickListener(this);
    }

    @SingleClick
    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                //saveOrderMain();
                //XToastUtils.toast("??????????????????");

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
     * ??????????????????????????????????????????
     *
     * @param title
     */
    private void openRemakeOrSetting(String title) {
        Bundle bundle = new Bundle();
        if (title.equals("??????")) {
            bundle.putString("orderMainNo", orderMainNo);
            openPageForResult(RemakeFragment.class, bundle, R.string.REQUEST_CODE_ADD_ORDERMAIN_REMAKE);

        } else if (title.equals("??????")) {
            openPageForResult(ScanSettingFragment.class, bundle, R.string.REQUEST_CODE_ADD_ORDERMAIN_SETTING);

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (detailList.size() != 0) {
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        "???????????????????????????????????????",
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
                        "?????????????????????",
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
            XToastUtils.toast("??????????????????");
            return false;
        } else if (StringUtils.isEmptyTrim(newOrderMain.getBranchno())) {
            XToastUtils.toast("???????????????");
            return false;
        } else {
            return true;
        }
    }

    private void deleteAllOrderDetail() {
        try {
            orderMainDBService.deleteData(newOrderMain);//????????????
            orderDetailDBService.deleteDatas(detailList);//????????????
        } catch (SQLException throwables) {
            XToastUtils.toast("???????????????");
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
                //??????????????????????????????????????????
                OrderMain orderMain = orderMainDBService.queryForColumnFirst("ORDERNO", orderMainNo);
                orderMain.setTotalmoney(Double.parseDouble(df.format(totalMoney)));
                orderMain.setTotalamt(realqtyCount);
                orderMain.setTotalqty(itemCount);
                orderMainDBService.updateData(orderMain);
            }
        } catch (SQLException throwables) {
            XToastUtils.toast("?????????????????????");
            throwables.printStackTrace();
        }
        orderDetailAdapter.refresh(detailList);
    }

    /**
     * ?????????????????????Item?????????????????????????????????
     */
    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

        // 1. MATCH_PARENT ???????????????????????????Item?????????;
        // 2. ???????????????????????????80;
        // 3. WRAP_CONTENT???????????????????????????;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        // ??????????????????????????????????????????????????????????????????
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.menu_selector_red)
                    .setText("??????")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// ????????????????????????
        }
    };
    /**
     * RecyclerView???Item???Menu???????????????
     */
    private OnItemMenuClickListener mMenuItemClickListener = (menuBridge, position) -> {
        menuBridge.closeMenu();
        int direction = menuBridge.getDirection(); // ???????????????????????????
        int menuPosition = menuBridge.getPosition(); // ?????????RecyclerView???Item??????Position???
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
        reflash(true);//Dialog??????????????????onFragmentResult??????????????????
    }


    private void openPage(Bundle bundle) {
        OrderDetailEditFragment dialog = new OrderDetailEditFragment();
        dialog.setTargetFragment(this, R.string.REQUEST_CODE_ADD_ORDERDETAIL);
        dialog.setArguments(bundle);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "orderDetailDialog");
    }
}
