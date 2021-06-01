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

@DatabaseTable(tableName = "ORDERDETAIL")
public class OrderDetail {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(columnName = "ORDERNO")
    private String orderno;
    @DatabaseField(columnName = "BARCODE")
    private String barcode;
    @DatabaseField(columnName = "ITEMNO")
    private String itemno; // 的系统需要 这个表示自定义的系统不重复序列号
    @DatabaseField(columnName = "ITEMSUBNO")
    private String itemsubno;
    @DatabaseField(columnName = "VALIDPRICE")
    private double validprice;
    @DatabaseField(columnName = "PRODUCEDATE")
    private String producedate;
    @DatabaseField(columnName = "VALIDDATE")
    private String validdate;
    @DatabaseField(columnName = "SUBAMT")
    private double subamt; // realqty * validprice
    @DatabaseField(columnName = "ORDERQTY")
    private double orderqty; // 引用单号的时候，需要这个订单数量
    @DatabaseField(columnName = "REALQTY")
    private double realqty; // 小包装数量
    @DatabaseField(columnName = "PACKAGEQTY")
    private double packageqty;// 大包装数量
    @DatabaseField(columnName = "GIVEQTY")
    private double giveqty; //赠送数量
    @DatabaseField(columnName = "UPLOADFLAG")
    private int uploadflag;
    @DatabaseField(columnName = "SCANTIME")
    private String scantime;
    @DatabaseField(columnName = "ITEMNAME")
    private String itemname;// 方便查询使用
    @DatabaseField(columnName = "STOCK")
    private double stock;   // 方便查询使用

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getItemsubno() {
        return itemsubno;
    }

    public void setItemsubno(String itemsubno) {
        this.itemsubno = itemsubno;
    }

    public double getValidprice() {
        return validprice;
    }

    public void setValidprice(double validprice) {
        this.validprice = validprice;
    }

    public String getProducedate() {
        return producedate;
    }

    public void setProducedate(String producedate) {
        this.producedate = producedate;
    }

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }

    public double getSubamt() {
        return subamt;
    }

    public void setSubamt(double subamt) {
        this.subamt = subamt;
    }

    public double getOrderqty() {
        return orderqty;
    }

    public void setOrderqty(double orderqty) {
        this.orderqty = orderqty;
    }

    public double getRealqty() {
        return realqty;
    }

    public void setRealqty(double realqty) {
        this.realqty = realqty;
    }

    public double getPackageqty() {
        return packageqty;
    }

    public void setPackageqty(double packageqty) {
        this.packageqty = packageqty;
    }

    public double getGiveqty() {
        return giveqty;
    }

    public void setGiveqty(double giveqty) {
        this.giveqty = giveqty;
    }

    public int getUploadflag() {
        return uploadflag;
    }

    public void setUploadflag(int uploadflag) {
        this.uploadflag = uploadflag;
    }

    public String getScantime() {
        return scantime;
    }

    public void setScantime(String scantime) {
        this.scantime = scantime;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }
}
