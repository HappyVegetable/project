

package com.xuexiang.templateproject.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "supplierInfo")
public class SupplierInfo {
    @DatabaseField(generatedId = true)
    private Long Id; //主键
    @DatabaseField(columnName = "SUPCUST_NO")
    private String supNo; //商品吗
    @DatabaseField(columnName = "SUPCUST_NAME")
    private String supName; //品名
    @DatabaseField(columnName = "PY_STR")
    private String py; //拼音
    @DatabaseField(columnName = "MOBILE")
    private String mobile; //拼音

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSupNo() {
        return supNo;
    }

    public void setSupNo(String supNo) {
        this.supNo = supNo;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    @Override
    public String toString() {
        return "SupplierInfo{" +
                "Id=" + Id +
                ", supNo='" + supNo + '\'' +
                ", supName='" + supName + '\'' +
                ", py='" + py + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
