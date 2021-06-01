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

package com.xuexiang.templateproject.fragment.items;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.entity.NewGoods;
import com.xuexiang.templateproject.fragment.supplier.SearchSupplierFragment2;
import com.xuexiang.templateproject.model.ItemClass;
import com.xuexiang.templateproject.model.Items;
import com.xuexiang.templateproject.model.SupplierInfo;
import com.xuexiang.templateproject.utils.HzPyUtil;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.common.StringUtils;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


@Page(name = "增加新品", anim = CoreAnim.slide)
public class ItemAddFragment extends XPageFragment {

    private DBService<Items> mDBService;
    private DBService<SupplierInfo> sDBService;
    private DBService<ItemClass> cDBService;

    @BindView(R.id.stv_itemno)
    SuperTextView stv_itemno;  //货号
    @BindView(R.id.stv_itemsubno)
    SuperTextView stv_itemsubno; //自编码
    @BindView(R.id.stv_itemname)
    SuperTextView stv_itemname; //品名
    @BindView(R.id.stv_suppliername)
    SuperTextView stv_suppliername; //供应商名称
    @BindView(R.id.stv_clsname)
    SuperTextView stv_clsname;   //商品类别
    @BindView(R.id.stv_inprice)
    SuperTextView stv_inprice; //进价
    @BindView(R.id.stv_itemarea)
    SuperTextView stv_itemarea; //区域
    @BindView(R.id.stv_itemspec)
    SuperTextView stv_itemspec; //规格
    @BindView(R.id.stv_saleprice)
    SuperTextView stv_saleprice; //售价
    @BindView(R.id.stv_vipprice)
    SuperTextView stv_vipprice; //会员价
    @BindView(R.id.stv_itemunitno)
    SuperTextView stv_itemunitno; //单位

    private Items newGoods;
    Items modifyItems;
    private String type = "0";


    @Override
    protected TitleBar initTitleBar() {
      /*  TitleBar titleBar = super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToBack();
            }
        });*/
        // titleBar.setLeftVisible(false);  //隐藏左侧返回按钮

        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_item;
    }

    @Override
    protected void initViews() {

        newGoods = new Items();
        mDBService = ExternalDataBaseRepository.getInstance().getDataBase(Items.class);
        sDBService = ExternalDataBaseRepository.getInstance().getDataBase(SupplierInfo.class);
        cDBService = ExternalDataBaseRepository.getInstance().getDataBase(ItemClass.class);

        Bundle param = getArguments();
        int requestCode = param.getInt("requestCode");
        switch (requestCode) {
            case R.string.REQUEST_CODE_GET_UPDATE:
                String itemNo = param.getString("itemNo");
                type = "1";
                try {
                    TitleBar titleBar = super.initTitleBar();
                    titleBar.setTitle("商品变更");
                    modifyItems = mDBService.queryForColumnFirst("itemno", itemNo);
                    stv_itemno.setCenterEditString(modifyItems.getItemNo());
                    stv_itemsubno.setCenterEditString(modifyItems.getItemSubNo());
                    stv_itemname.setCenterEditString(modifyItems.getItemName());

                    stv_itemunitno.setCenterEditString(modifyItems.getUnitNo());
                    stv_itemspec.setCenterEditString(modifyItems.getSpec());
                    stv_itemarea.setCenterEditString(modifyItems.getArea());
                    stv_inprice.setCenterEditString(String.valueOf(modifyItems.getInPrice()));
                    stv_saleprice.setCenterEditString(String.valueOf(modifyItems.getSalePrice()));
                    stv_vipprice.setCenterEditString(String.valueOf(modifyItems.getVipPrice()));
                    SupplierInfo supplierInfo = sDBService.queryForColumnFirst("SUPCUST_NO", modifyItems.getSup());
                    stv_suppliername.setCenterString(String.format("[%s]%s", supplierInfo.getSupNo(), supplierInfo.getSupName()));
                    ItemClass itemClass = cDBService.queryForColumnFirst("item_clsno", modifyItems.getCls());
                    stv_clsname.setCenterString(String.format("[%s]%s", itemClass.getClsNo(), itemClass.getClsName()));

                    modifyItems.setCls(itemClass.getClsNo());
                    modifyItems.setSup(supplierInfo.getSupNo());

                    stv_itemspec.getCenterEditText().setEnabled(false);
                    stv_itemarea.getCenterEditText().setEnabled(false);
                    stv_itemunitno.getCenterEditText().setEnabled(false);
                    stv_itemsubno.getCenterEditText().setEnabled(false);
                    stv_itemno.getCenterEditText().setEnabled(false);
                    stv_itemname.getCenterEditText().setEnabled(false);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            default:
                TitleBar titleBar = super.initTitleBar();
                titleBar.setTitle("增加新品");
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SingleClick
    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_submit:
                if (type.equals("1")) {
                    if (stv_inprice.getCenterEditValue().trim().length() == 0
                            || stv_saleprice.getCenterEditValue().trim().length() == 0
                            || stv_vipprice.getCenterEditValue().trim().length() == 0
                            ||stv_inprice.getCenterEditValue().trim().equals(".")
                            ||stv_saleprice.getCenterEditValue().trim().equals(".")
                            ||stv_vipprice.getCenterEditValue().trim().equals(".")

                    ) {
                        XToastUtils.toast("进价/售价/会员价/不能为空.");
                        return;
                    }

                    Double inPrice = Double.parseDouble(stv_inprice.getCenterEditValue().trim());
                    Double salePrice = Double.parseDouble(stv_saleprice.getCenterEditValue().trim());
                    Double vipPrice = Double.parseDouble(stv_vipprice.getCenterEditValue().trim());
                    modifyItems.setInPrice(inPrice);
                    modifyItems.setSalePrice(salePrice);
                    modifyItems.setVipPrice(vipPrice);

                    try {
                        mDBService.updateData(modifyItems);
                        XToastUtils.toast("上传数据成功");
                        popToBack();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        XToastUtils.toast(throwables.getMessage());
                    }

                } else if (type.equals("0")) {
                    String itemNo = stv_itemno.getCenterEditValue().trim();
                    String itemSubNo = stv_itemsubno.getCenterEditValue().trim();
                    String itemName = stv_itemname.getCenterEditValue().trim();
                    String spec = stv_itemspec.getCenterEditValue().trim();
                    String area = stv_itemarea.getCenterEditValue().trim();
                    String unitNo = stv_itemunitno.getCenterEditValue().trim();

                    if (itemNo.length() == 0 || itemName.length() == 0) {
                        XToastUtils.toast("货号/品名不能为空.");
                        return;
                    }
                    String py = new HzPyUtil().getAllFirstLetter(itemName);

                    if (stv_inprice.getCenterEditValue().trim().length() == 0
                            || stv_saleprice.getCenterEditValue().trim().length() == 0
                            ||stv_inprice.getCenterEditValue().trim().equals(".")
                            ||stv_saleprice.getCenterEditValue().trim().equals(".")) {
                        XToastUtils.toast("进价/售价/不能为空.");
                        return;
                    }
                    if (StringUtils.isEmptyTrim(newGoods.getSup())) {
                        XToastUtils.toast("供应商不能为空.");
                        return;
                    }
                    if (StringUtils.isEmptyTrim(newGoods.getCls())) {
                        XToastUtils.toast("请选择商品的类别.");
                        return;
                    }
                    Double inPrice = Double.parseDouble(stv_inprice.getCenterEditValue().trim());
                    Double salePrice = Double.parseDouble(stv_saleprice.getCenterEditValue().trim());
                    Double vipPrice;
                    if (stv_vipprice.getCenterEditValue().trim().length() == 0
                            ||stv_vipprice.getCenterEditValue().trim().equals(".")) {
                        vipPrice = salePrice;
                    } else {
                        vipPrice = Double.parseDouble(stv_vipprice.getCenterEditValue().trim());
                    }

                    newGoods.setItemNo(itemNo);
                    newGoods.setItemSubNo(itemSubNo);
                    newGoods.setItemName(itemName);
                    newGoods.setSpec(spec);
                    newGoods.setInPrice(inPrice);
                    newGoods.setSalePrice(salePrice);
                    newGoods.setVipPrice(vipPrice);
                    newGoods.setPy(py);
                    newGoods.setArea(area);
                    newGoods.setUnitNo(unitNo);
                    submitItem();
                }
                break;
        }
    }


    private void submitItem() {

        try {
            mDBService.insert(newGoods);
            XToastUtils.toast("上传数据成功");
        } catch (SQLException e) {
            e.printStackTrace();
            XToastUtils.toast(e.getMessage());
        }

        newGoods = new Items();
        stv_itemno.setCenterEditString("");
        stv_itemsubno.setCenterEditString("");
        stv_itemname.setCenterEditString("");
        stv_itemspec.setCenterEditString("");
        stv_inprice.setCenterEditString("");
        stv_saleprice.setCenterEditString("");
        stv_vipprice.setCenterEditString("");
        stv_itemarea.setCenterEditString("");
        stv_itemunitno.setCenterEditString("");
        stv_suppliername.setCenterString("");
        stv_clsname.setCenterString("");

    }

    @Override
    protected void initListeners() {
        /*
        添加图片点击监听事件
         */
  /*      stv_suppliername.setRightImageViewClickListener(imageView -> {
            openPageForResult(false, "供应商查询2", null, CoreAnim.none,R.string.REQUEST_CODE_GET_SUPPLIER);

        }).setCenterEditTextFocusChangeListener((v, hasFocus)->{
            if(hasFocus==true){
                openPageForResult(false, "供应商查询2", null, CoreAnim.none,R.string.REQUEST_CODE_GET_SUPPLIER);

            }

        });*/
        if (type.equals("0")) {
            stv_suppliername.setOnSuperTextViewClickListener((v1) -> {
                openPageForResult(SearchSupplierFragment2.class, null, R.string.REQUEST_CODE_GET_SUPPLIER);
            });
            stv_clsname.setOnSuperTextViewClickListener((v2) -> {
                openPageForResult(SearchItemsClassFragment.class, null, R.string.REQUEST_CODE_GET_CLS);
            });
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogLoader.getInstance().showConfirmDialog(
                    getContext(),
                    getString(R.string.tip_back_permission),
                    getString(R.string.lab_yes),
                    (dialog, which) -> {
                        dialog.dismiss();
                        popToBack();
                    },
                    getString(R.string.lab_no),
                    (dialog, which) -> {
                        dialog.dismiss();

                    }
            );
        }
        return false;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case R.string.REQUEST_CODE_GET_SUPPLIER:
                    stv_suppliername.setCenterString((String.format("[%s]%s", data.getStringExtra("supplierName").trim(), data.getStringExtra("supplierNo").trim())));
                    newGoods.setSup(data.getStringExtra("supplierNo").trim());
                    break;
                case R.string.REQUEST_CODE_GET_CLS:
                    stv_clsname.setCenterString(String.format("[%s]%s", data.getStringExtra("clsName").trim(), data.getStringExtra("clsNo").trim()));
                    newGoods.setCls(data.getStringExtra("clsNo").trim());
                    break;
                default:

                    break;
            }
        }
    }


}
