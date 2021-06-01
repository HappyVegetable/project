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

package com.xuexiang.templateproject;

import android.app.Application;

import com.xuexiang.templateproject.xormlite.db.ExternalDataBase;
import com.xuexiang.xormlite.ExternalDataBaseRepository;
import com.xuexiang.xormlite.annotation.DataBase;
import com.xuexiang.xormlite.enums.DataBaseType;

/**
 * 本地数据库外置存储配置类
    @name 数据库名称
    @path 数据库存储地址
 */
@DataBase(name = "external", type = DataBaseType.EXTERNAL, path = "/storage/emulated/0/data/databases")
public class ExternalApp extends Application  {

    @Override
    public void onCreate() {
        super.onCreate();

        ExternalDataBaseRepository.getInstance()
                .setIDatabase(new ExternalDataBase(ExternalDataBaseRepository.DATABASE_PATH, ExternalDataBaseRepository.DATABASE_NAME, ExternalDataBaseRepository.DATABASE_VERSION))
                .init(this);

    }
}