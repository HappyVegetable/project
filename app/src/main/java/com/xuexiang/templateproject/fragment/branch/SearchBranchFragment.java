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

package com.xuexiang.templateproject.fragment.branch;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.j256.ormlite.dao.RawRowMapper;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.adapter.BranchAdapter;
import com.xuexiang.templateproject.adapter.ItemsClassAdapter;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.model.Branch;
import com.xuexiang.templateproject.utils.SoftKeyboardUtil;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

@Page(name = "仓库查询")
public class SearchBranchFragment extends BaseFragment implements  SmartViewHolder.OnItemClickListener , TextWatcher {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private BranchAdapter branchAdapter;
    private DBService<Branch> mDBService;

    /**
     * 查询条件
     */
    @BindView(R.id.tv_search_item)
    MaterialEditText clearEditText;


    @Override
    protected TitleBar initTitle() {
        return super.initTitle();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_public_search;
    }


    @Override
    protected void initViews() {
        mDBService = ExternalDataBaseRepository.getInstance().getDataBase(Branch.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(branchAdapter = new BranchAdapter(R.layout.adapter_list_item));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        try {
            List<Branch> itemList = mDBService.queryAll();
            branchAdapter.refresh(itemList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SoftKeyboardUtil.showKeyboard(getContext(),clearEditText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initListeners() {
        branchAdapter.setOnItemClickListener(this);     //添加点击列表事件监听
        clearEditText.addTextChangedListener(this);    //添加输入框文字变化监听
    }

    @Override
    public void onItemClick(View itemView, int position) {
        //*将选中的数据封装到Intent
        Intent intent=new Intent();
        intent.putExtra("branchName",branchAdapter.getItem(position).getBranch_name());
        intent.putExtra("branchNo",branchAdapter.getItem(position).getBranch_no());
        setFragmentResult(RESULT_OK,intent);
        popToBack();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchResult(s.toString());
    }

    private void searchResult(String param) {
        List<Branch> itemList=new ArrayList<>();
        try {

            StringBuilder sbsql=new StringBuilder();
            sbsql.append("select BRANCH_NO,BRANCH_NAME from BRANCHINFO");
            sbsql.append(" where BRANCH_NO like '%"+param+"%' ");
            sbsql.append(" or BRANCH_NAME like '%"+param+"%' ");
            itemList=mDBService.queryDataBySql(sbsql.toString(), new RawRowMapper<Branch>() {
                @Override
                public Branch mapRow(String[] columnNames, String[] resultColumns)  {
                    Branch items=new Branch();
                    items.setBranch_no(resultColumns[0]);
                    items.setBranch_name(resultColumns[1]);
                    return items;
                }
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        branchAdapter.refresh(itemList);

    }

}
