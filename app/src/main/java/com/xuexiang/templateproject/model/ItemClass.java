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

@DatabaseTable(tableName = "ITEMCLS")

public  class ItemClass {
    @DatabaseField(generatedId = true)
    private Long Id; //主键
    @DatabaseField(columnName = "item_clsno")
    private String clsNo; //代码
    @DatabaseField(columnName = "Item_clsname")
    private String clsName; //名
    @DatabaseField(columnName = "up_clsno")
    private String up_clsno; //父类代码

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getClsNo() {
        return clsNo;
    }

    public void setClsNo(String clsNo) {
        this.clsNo = clsNo;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getUp_clsno() {
        return up_clsno;
    }

    public void setUp_clsno(String up_clsno) {
        this.up_clsno = up_clsno;
    }
}
