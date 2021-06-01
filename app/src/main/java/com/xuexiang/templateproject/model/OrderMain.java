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

package com.xuexiang.templateproject.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ORDERMAIN")
public class OrderMain {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(columnName = "ORDERNO")
    private String orderno;
    @DatabaseField(columnName = "REFSYSTEMNO")
    private String refsystemno; //引用的系统单号 比如采购订单号
    @DatabaseField(columnName = "REFRENCENO")
    private String refrenceno; // 系统返回的单号
    @DatabaseField(columnName = "ORDERTYPE")
    private String ordertype;
    @DatabaseField(columnName = "ORDERNAME")
    private String ordername;
    @DatabaseField(columnName = "BRANCHNO")
    private String branchno;
    @DatabaseField(columnName = "SUPNO")
    private String supno;
    @DatabaseField(columnName = "OPERNO")
    private String operno;
    @DatabaseField(columnName = "OPTIME")
    private String optime;
    @DatabaseField(columnName = "ORDER_STATUS")
    private int orderStatus; // 0 初始状态 1 返回完成已经
    @DatabaseField(columnName = "UPLOADFLAG")
    private int uploadflag; //0 未上传 1 上传成功
    @DatabaseField(columnName = "USERREMARK")
    private String userremark;
    @DatabaseField(columnName = "TOTALQTY")
    private double totalqty; // 总记录数
    @DatabaseField(columnName = "TOTALAMT")
    private double totalamt; // 总数量
    @DatabaseField(columnName = "TOTALMONEY")
    private double totalmoney;
    @DatabaseField(columnName = "MEMO")
    private String memo; // 备注
    @DatabaseField(columnName = "PAY_WAY")
    private String pay_way;
    private String sup_name;

    public String getSup_name() {
        return sup_name;
    }

    public void setSup_name(String sup_name) {
        this.sup_name = sup_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getRefsystemno() {
        return refsystemno;
    }

    public void setRefsystemno(String refsystemno) {
        this.refsystemno = refsystemno;
    }

    public String getRefrenceno() {
        return refrenceno;
    }

    public void setRefrenceno(String refrenceno) {
        this.refrenceno = refrenceno;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public String getBranchno() {
        return branchno;
    }

    public void setBranchno(String branchno) {
        this.branchno = branchno;
    }

    public String getSupno() {
        return supno;
    }

    public void setSupno(String supno) {
        this.supno = supno;
    }

    public String getOperno() {
        return operno;
    }

    public void setOperno(String operno) {
        this.operno = operno;
    }

    public String getOptime() {
        return optime;
    }

    public void setOptime(String optime) {
        this.optime = optime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getUploadflag() {
        return uploadflag;
    }

    public void setUploadflag(int uploadflag) {
        this.uploadflag = uploadflag;
    }

    public String getUserremark() {
        return userremark;
    }

    public void setUserremark(String userremark) {
        this.userremark = userremark;
    }

    public double getTotalqty() {
        return totalqty;
    }

    public void setTotalqty(double totalqty) {
        this.totalqty = totalqty;
    }

    public double getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(double totalamt) {
        this.totalamt = totalamt;
    }

    public double getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }
}
