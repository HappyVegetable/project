/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.templateproject.fragment.news;

import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutHelperFinder;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.base.broccoli.BroccoliSimpleDelegateAdapter;
import com.xuexiang.templateproject.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.templateproject.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.templateproject.adapter.base.delegate.XDelegateAdapter;
import com.xuexiang.templateproject.adapter.entity.NewInfo;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.utils.DemoDataProvider;
import com.xuexiang.templateproject.utils.Utils;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XRecyclerAdapter;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.ViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import me.samlss.broccoli.Broccoli;

/**
 * ????????????
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
public class NewsFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.card_view)
    CardView cardView;



    /**
     * @return ????????? null????????????????????????
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * ???????????????id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }


    /**
     * ??????????????????--->????????????
     * @param item
     */
    private void openGridLayoutPage(AdapterItem item){
        openNewPage(item.getTitle().toString());
    }

    private SimpleDelegateAdapter createAdapter(List<AdapterItem> list){
        //???????????????
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 0);
        gridLayoutHelper.setVGap(10);
        gridLayoutHelper.setHGap(0);
        gridLayoutHelper.setAutoExpand(false);
        gridLayoutHelper.setBgColor(5);
        gridLayoutHelper.setZIndex(999);
        //gridLayoutHelper.setMargin(20,0,20,0);
      /*  Iterator iterator=list.iterator();
        while (iterator.hasNext()){
            AdapterItem adapterItem= (AdapterItem) iterator.next();
            if(adapterItem.getTitle().equals("???????????????")){
                iterator.remove();
            }
        }*/
        SimpleDelegateAdapter<AdapterItem> procurementAdapter = new SimpleDelegateAdapter<AdapterItem>(R.layout.adapter_common_grid_item, gridLayoutHelper, list) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
                if (item != null) {

                    RadiusImageView imageView = holder.findViewById(R.id.riv_item);
                    imageView.setCircle(true);
                    ImageLoader.get().loadImage(imageView, item.getIcon());
                    holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));
                    holder.text(R.id.tv_sub_title, item.getTitle());
                    //????????????????????????
                    holder.click(R.id.ll_container, v ->
                            openGridLayoutPage(item)
                    );
                }
            }
        };
        return procurementAdapter;
    }

    private  SingleDelegateAdapter createAdapterTitle(String titleName){
        //???????????????
        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, titleName);
            }
        };
        return titleAdapter;
    }
    /**
     * ???????????????
     */
    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);

        //?????????
        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
                banner.setSource(DemoDataProvider.getBannerList())
                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
            }
        };

        //??????
        SimpleDelegateAdapter simpleDelegateAdapter= createAdapter(DemoDataProvider.getProcurementItems(getContext()));
        simpleDelegateAdapter.onCreateLayoutHelper().bindLayoutView(cardView);
        simpleDelegateAdapter.onCreateLayoutHelper().setZIndex(-1);
        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(createAdapterTitle("??????"));
        delegateAdapter.addAdapter(simpleDelegateAdapter);
        delegateAdapter.addAdapter(createAdapterTitle("??????"));
        delegateAdapter.addAdapter(createAdapter(DemoDataProvider.getWholesaleItems(getContext())));
        delegateAdapter.addAdapter(createAdapterTitle("??????"));
        delegateAdapter.addAdapter(createAdapter(DemoDataProvider.getWarehouseItems(getContext())));
        delegateAdapter.addAdapter(createAdapterTitle("??????"));
        delegateAdapter.addAdapter(createAdapter(DemoDataProvider.getReportItems(getContext())));
        delegateAdapter.addAdapter(createAdapterTitle("??????"));
        delegateAdapter.addAdapter(createAdapter(DemoDataProvider.getOtherItems(getContext())));

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //????????????
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            // TODO: 2020-02-25 ?????????????????????????????????
            refreshLayout.getLayout().postDelayed(() -> {
                refreshLayout.finishRefresh();
            }, 1000);
        });
        //????????????
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            // TODO: 2020-02-25 ?????????????????????????????????
            refreshLayout.getLayout().postDelayed(() -> {
                refreshLayout.finishLoadMore();
            }, 1000);
        });
        refreshLayout.autoRefresh();//????????????????????????????????????????????????
    }
}
