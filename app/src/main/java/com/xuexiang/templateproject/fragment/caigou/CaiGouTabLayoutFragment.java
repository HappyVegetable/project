/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseAdapterFragment;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.fragment.searchbar.SearchBarFragment;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


@Page(name = "采购订单")
public class CaiGouTabLayoutFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    FragmentAdapter<BaseAdapterFragment> adapter ;
    TitleBar titleBar;

    private List<BaseFragment> mFragments = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_caigou_tablayout_;
    }

    @Override
    protected void initViews() {

        // 固定数量的Tab,关联ViewPager

        adapter = new FragmentAdapter(getChildFragmentManager(),mFragments);
        adapter.addFragment(CaiGouOrderOutLineFragment.newInstance("离线采购订单"),"离线订单");
        adapter.addFragment(CaiGouOrderOnLineFragment.newInstance("在线采购订单"),"在线订单");

        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        WidgetUtils.setTabLayoutTextFont(mTabLayout);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getText().equals("离线订单")){
            titleBar.removeAllActions();
            titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_add_white_32) {
                @Override
                @SingleClick
                public void performAction(View view) {
                    openPage(CaiGouAddFragment.class);
                }
            });
        }else if(tab.getText().equals("在线订单")){
            titleBar.removeAllActions();
            titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_search_white_32dp) {
                @Override
                @SingleClick
                public void performAction(View view) {
                    openPageForResult(SearchBarFragment.class,1111);
                }
            });
        }

    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle=new Bundle();
        bundle.putString("beginDateStr",data.getStringExtra("beginDateStr"));
        bundle.putString("endDateStr",data.getStringExtra("endDateStr"));
        adapter.getItem(1).reflash(bundle);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause()
        } else {
            /**
             *重要！！！！！！！ 刷新离线数据的adpater
             */
            adapter.getItem(0).reflash(null);

       }
    }

    @Override
    protected TitleBar initTitle() {
         titleBar = super.initTitle();

        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_add_white_32) {
            @Override
            @SingleClick
            public void performAction(View view) {
                openPage(CaiGouAddFragment.class);
            }
        });
        return titleBar;
    }


}
