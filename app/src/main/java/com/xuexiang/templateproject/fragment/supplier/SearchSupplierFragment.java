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

package com.xuexiang.templateproject.fragment.supplier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.SupplierAdapter;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.model.SupplierInfo;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.SimpleCallBack;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

@Page(name="供应商查询111")
public class SearchSupplierFragment extends BaseFragment  implements SmartViewHolder.OnItemLongClickListener, SmartViewHolder.OnItemClickListener {
    private static final int REQUEST_CODE_EDIT_USER = 1000;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_stateful)
    StatefulLayout mLlStateful;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private SupplierAdapter supplierAdapter;


    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_searchsupplier;
    }


    @Override
    protected void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(supplierAdapter = new SupplierAdapter(R.layout.adapter_list_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initListeners() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getAllItem(refreshLayout);
            }
        });
        supplierAdapter.setOnItemLongClickListener(this);  //添加长按列表的事件监听
        supplierAdapter.setOnItemClickListener(this);     //添加点击列表事件监听

        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        //*将选中的数据封装到Intent
        Intent intent=new Intent();
        intent.putExtra("supplierName",supplierAdapter.getItem(position).getSupName());
        intent.putExtra("supplierNo",supplierAdapter.getItem(position).getSupNo());
        setFragmentResult(RESULT_OK,intent);
        popToBack();
    }

    @Override
    public void onItemLongClick(View itemView, int position) {

    }
    /**
     * 获取商品信息
     *
     * @param refreshLayout
     */
    @SuppressLint("CheckResult")
    public void getAllItem(@NonNull final RefreshLayout refreshLayout){
        XHttp.get("/getSupplierList")
                .syncRequest(false)
                .onMainThread(true)
                .execute(new SimpleCallBack<List<SupplierInfo>>(){
                    @Override
                    public void onSuccess(List<SupplierInfo> response) throws Throwable {
                        refreshLayout.finishRefresh(true);
                        if (response != null && response.size() > 0) {
                            supplierAdapter.refresh(response);
                            mLlStateful.showContent();
                        } else {
                            mLlStateful.showEmpty();
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        refreshLayout.finishRefresh(false);
                        mLlStateful.showError(e.getMessage(), null);
                        XToastUtils.toast("发生了错误");
                    }
                });
    }
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_USER) {
            mRefreshLayout.autoRefresh();
        }
    }
}