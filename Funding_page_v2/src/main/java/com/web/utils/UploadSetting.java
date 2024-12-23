package com.web.utils;

public class UploadSetting {

    /**
     * 업로드 디렉토리 경로 보정
     * @param uploadDir
     * @return
     */
    public static String directory(String uploadDir) {
        return uploadDir.endsWith("\\") ? uploadDir : uploadDir + "\\";
    }
    
    
}
