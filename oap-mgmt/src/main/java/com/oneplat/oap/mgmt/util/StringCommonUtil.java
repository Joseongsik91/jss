package com.oneplat.oap.mgmt.util;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author lee
 * @date 2016-08-09
 */
public final class StringCommonUtil {
    //자릿수 체크후 ... 처리
    public static String ellipsis(String text, int length){
        String ellipsisString = "...";
        String outputString = text;

        if(text.length()>0 && length>0){
            if(text.length()>length){
                outputString = text.substring(0, length);
                outputString += ellipsisString;
            }
        }
        return outputString;
    }

    /**
     * 오늘 년월일자 가져오기
     *
     * @param
     * @return yyyy-MM-dd
     * @see
     */
    public static String getDate() {

        String rtnStr = null;

        // 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
        String pattern = "yyyy-MM-dd";

        try {
            SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
            Timestamp ts = new Timestamp(System.currentTimeMillis());

            rtnStr = sdfCurrent.format(ts.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtnStr;
    }

    /**
     * 파일 업로드 디렉토리 년/월 추가
     *
     * @param folderTypePath
     * @return filepath/yyyy/dd
     * @see
     */
    public static String getDateFolderPath(String folderTypePath) {
        String date = getDate();
        return folderTypePath + File.separator + date.split("-")[0]+ File.separator +date.split("-")[1];
    }
}
