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

package com.xuexiang.templateproject.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class ProperUtils {

    private static String FilePath = "/storage/emulated/0/data/";
    private static String FileName = ".investigation_terminal.properties";
    /**
     * 得到properties配置文件中的所有配置属性
     *
     * @return Properties对象
     */
    public static String getConfigProperties(String key) {
        File mfile = new File(FilePath);
        if (!mfile.exists()) {
            mfile.mkdirs();
        }
        File mfileName = new File(FilePath+FileName);
        if (!mfileName.exists()) {
            return "";
        }
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(FilePath+FileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = props.getProperty(key);
        if (value==null) {
            value="";
        }
        return value;
    }


    /**
     * 给属性文件添加属性
     *
     * @param value
     * @author qiulinhe
     * @createTime 2016年6月7日 下午1:46:53
     */
    public static void writeDateToLocalFile( String key, String value) {
        File mfile = new File(FilePath);
        if (!mfile.exists()) {
            mfile.mkdirs();
        }
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream(FilePath+FileName);
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.put(key, value);
        OutputStream fos;
        try {
            fos = new FileOutputStream(FilePath+FileName);
            p.store(fos, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
