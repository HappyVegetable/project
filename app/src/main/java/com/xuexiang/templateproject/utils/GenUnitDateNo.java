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


import java.text.DecimalFormat;

        import java.text.FieldPosition;

        import java.text.Format;

        import java.text.NumberFormat;

        import java.text.SimpleDateFormat;

        import java.util.Calendar;



public class GenUnitDateNo {
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
    /** This Format for format the data to special format. */
    private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS");
    /** This Format for format the number to special format. */
    private final static NumberFormat numberFormat = new DecimalFormat("0000");
    /** This int is the sequence number ,the default value is 0. */
    private static int seq = 0;
    private static final int MAX = 9999;

    /**

     * 时间格式生成序列

     * @return String

     */

    public static synchronized String generateSequenceNo() {

        Calendar rightNow = Calendar.getInstance();

        StringBuffer sb = new StringBuffer();

        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);

        numberFormat.format(seq, sb, HELPER_POSITION);

        if (seq == MAX) {

            seq = 0;

        } else {

            seq++;

        }

        return sb.toString();

    }

}
