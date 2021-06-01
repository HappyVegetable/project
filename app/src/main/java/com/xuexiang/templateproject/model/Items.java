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

@DatabaseTable(tableName = "items")
public class Items {



    @DatabaseField(generatedId = true)
    private Long Id; //主键

    @DatabaseField(columnName = "itemno")
    private String ItemNo; //商品吗
    @DatabaseField(columnName = "itemsubno")
    private String ItemSubNo; //自编码
    @DatabaseField(columnName = "itemname")
    private String ItemName; //品名
    @DatabaseField(columnName = "py")
    private String Py;    //拼音
    @DatabaseField(columnName = "cls")
    private String Cls;  //类别
    @DatabaseField(columnName = "sup")
    private String Sup;  //供应商
    @DatabaseField(columnName = "inprice")
    private double InPrice;     //进价
    @DatabaseField(columnName = "saleprice")
    private double SalePrice;  //售价
    @DatabaseField(columnName = "vipprice")
    private double VipPrice;      //会员价
    @DatabaseField(columnName = "spec")
    private String Spec;            //规格
    @DatabaseField(columnName = "saleway")
    private String Saleway;     //销售渠道
    @DatabaseField(columnName = "area")
    private String Area;   //地区
    @DatabaseField(columnName = "unitno")
    private String UnitNo;  //单位




    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getItemSubNo() {
        return ItemSubNo;
    }

    public void setItemSubNo(String itemSubNo) {
        ItemSubNo = itemSubNo;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getPy() {
        return Py;
    }

    public void setPy(String py) {
        Py = py;
    }

    public String getCls() {
        return Cls;
    }

    public void setCls(String cls) {
        Cls = cls;
    }

    public String getSup() {
        return Sup;
    }

    public void setSup(String sup) {
        Sup = sup;
    }

    public double getInPrice() {
        return InPrice;
    }

    public void setInPrice(double inPrice) {
        InPrice = inPrice;
    }

    public double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(double salePrice) {
        SalePrice = salePrice;
    }

    public double getVipPrice() {
        return VipPrice;
    }

    public void setVipPrice(double vipPrice) {
        VipPrice = vipPrice;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public String getSaleway() {
        return Saleway;
    }

    public void setSaleway(String saleway) {
        Saleway = saleway;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    @Override
    public String toString() {
        return "Items{" +
                "Id=" + Id +
                ", ItemNo='" + ItemNo + '\'' +
                ", ItemSubNo='" + ItemSubNo + '\'' +
                ", ItemName='" + ItemName + '\'' +
                ", Py='" + Py + '\'' +
                ", Cls='" + Cls + '\'' +
                ", Sup='" + Sup + '\'' +
                ", InPrice=" + InPrice +
                ", SalePrice=" + SalePrice +
                ", VipPrice=" + VipPrice +
                ", Spec='" + Spec + '\'' +
                ", Saleway='" + Saleway + '\'' +
                ", Area='" + Area + '\'' +
                ", UnitNo='" + UnitNo + '\'' +
                '}';
    }
}
