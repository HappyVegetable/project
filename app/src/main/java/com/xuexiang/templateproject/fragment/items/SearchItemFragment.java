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

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.RawRowMapper;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.ItemsAdapter;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.core.qrcode.CustomCaptureActivity;
import com.xuexiang.templateproject.model.Items;
import com.xuexiang.templateproject.utils.SoftKeyboardUtil;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xqrcode.XQRCode;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.common.StringUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

@Page(name = "商品查询", anim = CoreAnim.none)
public class SearchItemFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener, TextWatcher {

    /**
     * 定制化扫描界面Request Code
     */
    public static final int REQUEST_CODE_GET_CAMERA = 113;
    public static final int REQUEST_CODE_GET_DB = 114;
    /**
     * 绑定数据列表
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    /**
     * 查询条件
     */
    @BindView(R.id.tv_search_item)
    MaterialEditText clearEditText;

    /**
     * 扫描按钮
     */
    @BindView(R.id.btn_scan)
    ImageButton superButton;

    private ItemsAdapter itemsAdapter;
    private DBService<Items> mDBService;
    String type="0";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_searchitem;
    }

    @Override
    protected TitleBar initTitle() {
        return super.initTitle();
    }

    @Override
    protected void initViews() {
        mDBService = ExternalDataBaseRepository.getInstance().getDataBase(Items.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(itemsAdapter = new ItemsAdapter(R.layout.adapter_list_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Bundle bundle=getArguments();
        //获取itemNo
        if(!StringUtils.isEmptyTrim(bundle.getString("resultCode"))){
            type="1";
        }
        SoftKeyboardUtil.showKeyboard(getContext(),clearEditText);


    }

    @Override
    protected void initListeners() {
        superButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
        itemsAdapter.setOnItemClickListener(this);     //添加点击列表事件监听
        clearEditText.addTextChangedListener(this);    //添加输入框文字变化监听

    }


    @Override
    public void onItemClick(View itemView, int position) {

        if(type.equals("0")){
            //查询商品信息页面
            Bundle bundle = new Bundle();
            bundle.putInt("requestCode", R.string.REQUEST_CODE_GET_UPDATE);
            bundle.putString("itemNo", itemsAdapter.getItem(position).getItemNo());
            openPage(ItemAddFragment.class, bundle);
        }else if(type.equals("1")){
            //返回到采购等获取结果的页面
            String itemNo=itemsAdapter.getItem(position).getItemNo();
            Intent intent=new Intent();
            intent.putExtra("itemNo",itemNo);
            setFragmentResult(RESULT_OK,intent);
            popToBack();

        }


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence itemNo, int start, int before, int count) {
       /* System.out.println(itemNo.toString());
        searchResult(itemNo.toString(), REQUEST_CODE_GET_DB);*/
    }

    @Override
    public void afterTextChanged(Editable itemNo) {
        searchResult(itemNo.toString(), REQUEST_CODE_GET_DB);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE_GET_CAMERA && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            handleScanResult(data);
        }
    }


    /**
     * 处理二维码扫描结果
     *
     * @param data
     */
    private void handleScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_SUCCESS) {
                    String result = bundle.getString(XQRCode.RESULT_DATA);
                    searchResult(result, REQUEST_CODE_GET_CAMERA);

                } else if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_FAILED) {
                    XToastUtils.toast("解析二维码失败", Toast.LENGTH_LONG);
                }
            }
        }
    }

    /**
     * 启动扫描
     */
    private void startScan() {
        CustomCaptureActivity.start(this, REQUEST_CODE_GET_CAMERA, R.style.XQRCodeTheme_Custom);
    }

    private void searchResult(String param, int searchType) {
        List<Items> itemList=new ArrayList<>();
        try {
            switch (searchType) {
                case REQUEST_CODE_GET_DB:
                    StringBuilder sbsql=new StringBuilder();
                    sbsql.append("select id,itemno,itemname,py from items");
                    sbsql.append(" where itemno like '%"+param+"%' ");
                    sbsql.append(" or itemname like '%"+param+"%' ");
                    //sbsql.append(" or py like '%"+param+"%' ");
                    itemList=mDBService.queryDataBySql(sbsql.toString(), new RawRowMapper<Items>() {
                        @Override
                        public Items mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                            Items items=new Items();
                            items.setId(Long.parseLong(resultColumns[0]));
                            items.setItemNo(resultColumns[1]);
                            items.setItemName(resultColumns[2]);
                            //items.setPy(resultColumns[3]);
                            return items;
                        }
                    });
                    //itemList = mDBService.indistinctQueryForColumn("itemno",StringUtils.getStringTrim(itemno));
                    if (itemList != null || itemList.size() == 0) {
                        itemsAdapter.refresh(itemList);
                    }
                    break;
                case REQUEST_CODE_GET_CAMERA:
                    itemList = mDBService.queryByColumn("itemno", StringUtils.getStringTrim(param));
                    if (itemList == null || itemList.size() == 0) {

                    } else if (itemList != null && itemList.size() == 1) {
                        if(type.equals("1")){
                            //返回到采购等获取结果的页面
                            String itemNo=param;
                            Intent intent=new Intent();
                            intent.putExtra("itemNo",itemNo);
                            setFragmentResult(RESULT_OK,intent);
                            popToBack();
                        }else {
                            Items items = itemList.get(0);
                            Bundle bundle = new Bundle();
                            bundle.putInt("requestCode", R.string.REQUEST_CODE_GET_UPDATE);
                            bundle.putString("itemNo", items.getItemNo());
                            openPage(ItemAddFragment.class, bundle);
                        }
                    }
                    break;
                default:
                    itemList = mDBService.queryAll();
                    break;
            }
        } catch (SQLException throwables) {
            XToastUtils.toast(throwables.getMessage());
            throwables.printStackTrace();
        }
        itemsAdapter.refresh(itemList);

    }
}
