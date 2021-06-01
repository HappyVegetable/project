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

package com.xuexiang.templateproject.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codbking.calendar.CalendarDate;
import com.codbking.calendar.CalendarDateView;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xutil.common.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import static android.app.Activity.RESULT_OK;

/**
 * @author xuexiang
 * @since 2019-05-29 22:49
 */
@Page(name = "日期选择",anim = CoreAnim.fade)
public class MaterialDesignCalendarFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;

    //开始日期Str
    String beginDateStr;
    Date beginDate;
    //开始日期Str
    String endDateStr;
    Date endDate;


    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_design_calendar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        Bundle bundle=getArguments();
        beginDateStr=bundle.getString("beginDateStr");
        endDateStr=bundle.getString("endDateStr");
        calendarDateView.setAdapter((convertView, parentView, calendarDate) -> {
            TextView textView;
            if (convertView == null) {
                convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.adapter_calendar_item, null);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtils.dp2px(48), DensityUtils.dp2px(48));
                convertView.setLayoutParams(params);
            }

            textView = convertView.findViewById(R.id.tv_text);
            textView.setBackgroundResource(R.drawable.bg_calendar_material_design_item);

            textView.setText(String.valueOf(calendarDate.day));

            if (calendarDate.monthFlag != 0) {
                textView.setTextColor(0xFF9299A1);
            } else {
                if (calendarDate.isToday() && calendarDate.equals(calendarDateView.getSelectCalendarDate())) {
                    textView.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
                } else {
                    textView.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_content_text));
                }
            }
            return convertView;
        });

        calendarDateView.setOnCalendarSelectedListener((view, postion, calendarDate) -> validDate(calendarDate.formatDate()) );

        calendarDateView.setOnTodaySelectStatusChangedListener((todayView, isSelected) -> {
            TextView view = todayView.findViewById(R.id.tv_text);
            if (isSelected) {
                view.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.xui_config_color_content_text));
            } else {
                view.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.colorAccent));
            }
        });

        calendarDateView.setOnMonthChangedListener((view, position, date) -> tvTitle.setText(String.format("%d年%d月", date.year, date.month)));

        CalendarDate date = CalendarDate.get(new Date());
        tvTitle.setText(String.format("%d年%d月", date.year, date.month));
    }

    private void validDate(String selectDateStr) {
        Date selectDate;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            selectDate=sdf.parse(selectDateStr);
            if(!StringUtils.isEmptyTrim(beginDateStr)){ //选择了截止日期
                beginDate=sdf.parse(beginDateStr);
                if(beginDate.after(selectDate)){
                    XToastUtils.toast("截止日期不能小于起始日期");
                }else{
                    returnDate(selectDateStr);
                }
            }else if(!StringUtils.isEmptyTrim(endDateStr)) { //选择了起始日期
                endDate=sdf.parse(endDateStr);
                if(endDate.before(selectDate)){
                    XToastUtils.toast("起始日期不能大于截止日期");
                }else{
                    returnDate(selectDateStr);
                }
            }else {
                returnDate(selectDateStr);
            }
        } catch (ParseException e) {
            XToastUtils.toast("日期转换错误");
            e.printStackTrace();
        }
    }

    private void  returnDate(String date){
        Intent intent=new Intent();
        intent.putExtra("selectDate",date);
        setFragmentResult(RESULT_OK,intent);
        popToBack();
    }

    @OnClick({R.id.iv_previous, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_previous:
                calendarDateView.previous();
                break;
            case R.id.iv_next:
                calendarDateView.next();
                break;
            default:
                break;
        }
    }
}
