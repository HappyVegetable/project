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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.model.Items;
import com.xuexiang.templateproject.model.OrderDetail;
import com.xuexiang.templateproject.utils.SoftKeyboardUtil;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.InternalDataBaseRepository;
import com.xuexiang.xormlite.db.DBService;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.textview.label.LabelTextView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.data.DateUtils;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;


public class OrderDetailEditFragment extends DialogFragment {


    EditText ed_item_count;

    EditText ed_item_price;

    LabelTextView ed_item_producedte;

    TextView tvitemrealcount;

    TextView tvitemname;

    TextView tvitemno;

    TextView tvkucun;

    TextView tvspec;

    TextView tvunitno;

    TextView tvsaleprice;

    TextView submit;

    TextView cancel;


    private TimePickerView mDatePicker;
    Items itemsInfo;
    DBService<Items> itemsDBService;
    DBService<OrderDetail> orderDetailDBService;
    OrderDetail orderDetail;
    Integer count;
    Double price;
    String type = "0";//?????? 1??????   0??????   2??????????????????


    String orderMainNo; //????????????
    String itemNo; //????????????
    String orderDetailId;  //????????????ID


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_edit_itemdetail2, container);
        ed_item_count = view.findViewById(R.id.edit_item_count);
        ed_item_producedte = view.findViewById(R.id.edit_item_producedte);
        ed_item_price = view.findViewById(R.id.edit_item_price);
        tvitemrealcount = view.findViewById(R.id.tv_item_realcount);
        tvitemname = view.findViewById(R.id.tv_itemname);
        tvitemno = view.findViewById(R.id.tv_itemno);
        tvkucun = view.findViewById(R.id.tv_kucun);
        tvspec = view.findViewById(R.id.tv_spec);
        tvunitno = view.findViewById(R.id.tv_unitno);
        tvsaleprice = view.findViewById(R.id.tv_saleprice);
        submit = view.findViewById(R.id.btn_submit);
        cancel = view.findViewById(R.id.btn_cancel);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowParams = window.getAttributes();
        //windowParams.dimAmount = 0.0f;
        windowParams.gravity = Gravity.TOP;
        windowParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowParams.windowAnimations = R.style.winAnimation;
        window.setAttributes(windowParams);

         //** * ??????????????? ??????  ???????????????????????????????????????
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        itemsDBService = ExternalDataBaseRepository.getInstance().getDataBase(Items.class);
        orderDetailDBService = InternalDataBaseRepository.getInstance().getDataBase(OrderDetail.class);
        Bundle bundle = getArguments();
        orderMainNo = bundle.getString("orderMainNo");
        itemNo = bundle.getString("itemNo");

        fillText();//??????Item????????????

        orderDetailId = bundle.getString("orderDetailId");
        try {

            if (!StringUtils.isEmptyTrim(orderDetailId)) {   //?????? ??????
                orderDetail = orderDetailDBService.queryForColumnFirst("Id", orderDetailId);
                type = "1";
                submit.setText("??????");

            }else { //???????????????????????????????????????????????????
                orderDetail =orderDetailDBService.queryForColumnFirst("ORDERNO", orderMainNo, "ITEMNO", itemNo);
                if(orderDetail!=null) {
                    type="2";
                    ed_item_count.setImeOptions(EditorInfo.IME_ACTION_DONE); //????????????????????????
                    ed_item_price.setEnabled(false);
                    ed_item_producedte.setEnabled(false);
                    ed_item_producedte.setBackgroundResource(R.drawable.xui_config_color_edittext_disable);
                    submit.setText("??????");
                }
            }
            if(orderDetail!=null){
                //????????????????????????????????????
                int i = new Double(orderDetail.getRealqty()).intValue();
                ed_item_producedte.setText(orderDetail.getProducedate());
                ed_item_count.setText(String.valueOf(i));
                ed_item_price.setText(String.valueOf(orderDetail.getValidprice()));
                tvitemrealcount.setText(String.valueOf(i));
            }else{
                //???????????????????????????
                orderDetail = new OrderDetail();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ed_item_count.requestFocus();
   }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        submit.setOnClickListener((v1)-> {
                if (!StringUtils.isEmptyTrim(ed_item_count.getText().toString())
                        && !StringUtils.isEmptyTrim(ed_item_price.getText().toString())
                        && (!ed_item_price.getText().toString().equals("."))) {
                    count = Integer.valueOf(ed_item_count.getText().toString());
                    price = Double.valueOf(ed_item_price.getText().toString());
                    if (count == null || count <= 0) {
                        XToastUtils.toast("???????????????0");
                    } else if (price == null || price == 0) {
                        XToastUtils.toast("???????????????0");

                    } else {
                        if (price > itemsInfo.getSalePrice()) {
                            DialogLoader.getInstance().showConfirmDialog(
                                    getContext(),
                                    "??????????????????????????????????????????????",
                                    getString(R.string.lab_yes),
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                        saveOrderDetail();
                                    },
                                    getString(R.string.lab_no),
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                    }
                            );
                        } else {
                            saveOrderDetail();
                        }
                    }
                } else {
                    XToastUtils.toast("??????/??????????????????");
                }
        });
        cancel.setOnClickListener((v2)-> {
                //setResult();
                closeDialog();
        });
        if(!type.equals("2")){
            ed_item_producedte.setOnClickListener((v3)-> {
                    showDatePicker(); //????????????
            });
        }

    }


    private void fillText() {
        try {
            itemsInfo = itemsDBService.queryForColumnFirst("itemno", itemNo);
            tvitemname.setText(itemsInfo.getItemName());
            tvitemno.setText(itemsInfo.getItemNo());
            tvunitno.setText(itemsInfo.getUnitNo());
            tvspec.setText(itemsInfo.getSpec());
            tvsaleprice.setText(String.valueOf(itemsInfo.getSalePrice()));
            tvkucun.setText("0");
            //SoftKeyboardUtil.showKeyboard(getContext(),ed_item_count);//?????????????????????
        } catch (SQLException throwables) {
            XToastUtils.toast("????????????");
            throwables.printStackTrace();
        }
    }

    private void saveOrderDetail() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (type.equals("0")) {
            orderDetail.setOrderno(orderMainNo);
            orderDetail.setItemname(itemsInfo.getItemName());
            orderDetail.setItemno(itemsInfo.getItemNo());
            orderDetail.setItemsubno(itemsInfo.getItemSubNo());
            orderDetail.setScantime(simpleDateFormat.format(new Date()));
            orderDetail.setSubamt(Double.parseDouble(df.format(price * count)));
            orderDetail.setRealqty(count);
            orderDetail.setValidprice(price);
            //orderDetail.setProducedate(ed_item_producedte.getText().toString());
        }
        try {
            //????????????????????????????????????????????????????????????????????????????????????
            OrderDetail old = orderDetailDBService.queryForColumnFirst("ORDERNO", orderMainNo, "ITEMNO",itemNo);
            if (old != null) {
                if (type.equals("1")) {
                    old.setSubamt(Double.parseDouble(df.format(price * count)));
                    old.setRealqty(count);
                    old.setValidprice(price);
                    old.setProducedate(StringUtils.isEmptyTrim(orderDetail.getProducedate()) ? old.getProducedate() : orderDetail.getProducedate());
                } else if(type.equals("2")) {
                    Integer newCount = (int) old.getRealqty() + count;
                    old.setSubamt(Double.parseDouble(df.format(newCount * old.getValidprice())));
                    old.setRealqty(newCount);
                    old.setScantime(simpleDateFormat.format(new Date()));

                }
                orderDetailDBService.updateData(old);
            } else {
                orderDetailDBService.insert(orderDetail);
            }

            setResult();
            closeDialog();
           /* Bundle bundle=new Bundle();
            bundle.putString("resultCode","GETITEM");
            popToBack("??????????????????",bundle);*/
        } catch (SQLException throwables) {
            XToastUtils.toast("?????????");
            throwables.printStackTrace();

        }
    }

    // ??????????????????
    protected void setResult() {
        // ?????????????????????targetFragment
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra("RESPONSE_EVALUATE", "OK");
        getTargetFragment().onActivityResult(R.string.REQUEST_CODE_ADD_ORDERDETAIL,
                Activity.RESULT_OK, intent);

    }

    private void closeDialog() {
      /*  Fragment prev = getFragmentManager().findFragmentByTag("orderDetailDialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }*/
        List<View> list = new ArrayList<>();
        list.add(ed_item_price);
        list.add(ed_item_count);
        SoftKeyboardUtil.hideSoftKeyboard(getContext(), list);
        this.dismiss();

    }

    private void showDatePicker() {
        if (mDatePicker == null) {
            Calendar calendar = Calendar.getInstance();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            gc.roll(Calendar.YEAR, -10);
            gc.getTime();
            GregorianCalendar gc2 = new GregorianCalendar();
            gc2.setTime(new Date());
            gc2.add(Calendar.YEAR, 1);
            gc2.getTime();
            if (!StringUtils.isEmptyTrim(orderDetail.getProducedate())) {
                calendar.setTime(DateUtils.string2Date(orderDetail.getProducedate(), DateUtils.yyyyMMdd.get()));
            } else {
                calendar.setTime(DateUtils.getNowDate());
            }
            //

            mDatePicker = new TimePickerBuilder(getContext(), (date, v) ->
                    updateProduceDate(date)
                    //XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()))
            )
                    /*.setTimeSelectChangeListener(date ->
                            ed_item_producedte.setText(simpleDateFormat2.format(date))
                    )*/
                    .setTitleText("????????????")
                    .setType(TimePickerType.DEFAULT)
                    .setOutSideCancelable(true)
                    .setDate(calendar)
                    .setRangDate(gc, gc2)
                    .isDialog(true)
                    .build();
        }
        mDatePicker.show();
    }

    private void updateProduceDate(Date date) {
        orderDetail.setProducedate(simpleDateFormat2.format(date));
        ed_item_producedte.setText(simpleDateFormat2.format(date));
    }


}
