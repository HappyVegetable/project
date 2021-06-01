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

package com.xuexiang.templateproject.entity;

public class NewGoods {
    private Long id;
    private String itemno; //商品吗
    private String itemsubno; //自编码
    private String itemname; //品名
    private String py;    //拼音
    private String cls;  //类别
    private String sup;  //供应商
    private double inprice;     //进价
    private double saleprice;  //售价
    private double vipprice;      //会员价
    private String spec;            //规格
    private String saleway;     //销售渠道
    private String area;   //地区
    private String unitno;  //单位
    public NewGoods() {}

    public NewGoods(Long id, String itemno, String itemsubno, String itemname, String py, String cls, String sup, double inprice, double saleprice, double vipprice, String spec, String saleway, String area, String unitno) {
        this.id = id;
        this.itemno = itemno;
        this.itemsubno = itemsubno;
        this.itemname = itemname;
        this.py = py;
        this.cls = cls;
        this.sup = sup;
        this.inprice = inprice;
        this.saleprice = saleprice;
        this.vipprice = vipprice;
        this.spec = spec;
        this.saleway = saleway;
        this.area = area;
        this.unitno = unitno;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getSup() {
        return sup;
    }

    public void setSup(String sup) {
        this.sup = sup;
    }

    public double getInprice() {
        return inprice;
    }

    public void setInprice(double inprice) {
        this.inprice = inprice;
    }

    public double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(double saleprice) {
        this.saleprice = saleprice;
    }

    public double getVipprice() {
        return vipprice;
    }

    public void setVipprice(double vipprice) {
        this.vipprice = vipprice;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSaleway() {
        return saleway;
    }

    public void setSaleway(String saleway) {
        this.saleway = saleway;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }
}
