

package com.xuexiang.templateproject.fragment.searchbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.commonsdk.debug.I;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.calendar.MaterialDesignCalendarFragment;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.tabbar.TabControlView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.common.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

@Page(name = "搜索",anim = CoreAnim.none)
public class SearchBarFragment extends BaseFragment {

    @BindView(R.id.tcv_select)
    TabControlView mTabControlView;

    @BindView(R.id.btn_date_begin)
    SuperButton beginBtn;
    @BindView(R.id.btn_date_end)
    SuperButton endBtn;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    GregorianCalendar gc=new GregorianCalendar();
    Date todayDate;
    String todayStr;  //当天
    String beginDateStr;//开始日期
    String endDateStr;//截止日期
    String type="0"; //第一个选择框
    String queryType="0";  //设置查询类型
    String queryDate1;  //查询条件1
    String queryDate2;  //查询条件2

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_bar;
    }

    @Override
    protected void initViews() {
        todayDate=new Date();
        todayStr=sdf.format(new Date());
        try {
            todayDate=sdf.parse(todayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initTabControlView();

    }

    private void initTabControlView() {
        try {
            mTabControlView.setItems(ResUtils.getStringArray(R.array.course_param_option), ResUtils.getStringArray(R.array.course_param_value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTabControlView.setOnTabSelectionChangedListener((title, value) -> changeDate(value));
    }
    private void changeDate(String type){
        switch (type){
            case "1":
                queryType="0";
                beginDateStr=sdf.format(todayDate);

                break;
            case "2":
                queryType="1";
                gc.setTime(todayDate);
                gc.roll(Calendar.DATE,-7);
                beginDateStr=sdf.format(gc.getTime());
                break;
            case "3":
                queryType="1";
                gc.setTime(todayDate);
                gc.roll(Calendar.MONTH,-1);
                beginDateStr=sdf.format(gc.getTime());
                break;
            case "4":
                queryType="1";
                gc.setTime(todayDate);
                gc.set(Calendar.DAY_OF_MONTH, 1);
                beginDateStr=sdf.format(gc.getTime());
                break;
            case "5":
                queryType="1";
                gc.setTime(todayDate);
                gc.roll(Calendar.YEAR,-1);
                beginDateStr=sdf.format(gc.getTime());
                break;
            default:
                break;
        }
        endDateStr=sdf.format(todayDate);
        beginBtn.setText(beginDateStr);
        endBtn.setText(endDateStr);
    }

    @OnClick({R.id.btn_date_begin, R.id.btn_date_end,R.id.btn_submit})
    public void onViewClicked(View view) {
        Bundle bundle=new Bundle();
        switch (view.getId()) {
            case R.id.btn_date_begin:
                type="0";
                bundle.putString("endDateStr",endDateStr);
                openPageForResult(MaterialDesignCalendarFragment.class,bundle,0);
                break;
            case R.id.btn_date_end:
                type="1";
                bundle.putString("beginDateStr",beginDateStr);
                openPageForResult(MaterialDesignCalendarFragment.class,bundle,0);
                break;
            case R.id.btn_submit:
                //查询
                if (!StringUtils.isEmptyTrim(beginDateStr)&&!StringUtils.isEmptyTrim(endDateStr)){
                    // XToastUtils.toast("你选择了"+beginDateStr+"--"+endDateStr);
                    Intent intent=new Intent();
                    intent.putExtra("beginDateStr",beginDateStr);
                    intent.putExtra("endDateStr",endDateStr);
                    setFragmentResult(RESULT_OK,intent);
                    popToBack();
                }else {
                    XToastUtils.toast("请选择日期");
                }
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            String selectDate=data.getStringExtra("selectDate");
            if(!StringUtils.isEmptyTrim(selectDate)){
                if(type.equals("0")){
                    beginDateStr=selectDate;
                    beginBtn.setText(selectDate);
                }else {
                    endDateStr=selectDate;
                    endBtn.setText(selectDate);
                }
            }
        }else {
            //未操作日期
        }
    }
}
