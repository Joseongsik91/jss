package com.oneplat.oap.mgmt.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * @author lee
 * @date 2016-12-30
 */
public class FileUtil {

    /**
     * <p>
     * 파일을 읽는다.
     * </p>
     *
     * @param file <code>File</code>
     * @return 결과 값
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readFile(File file) throws IOException {
        BufferedInputStream in =
                new BufferedInputStream(new FileInputStream(file));
        return readFileContent(in);
    }

    /**
     * <p>
     * String 형으로 파일의 내용을 읽는다.
     * </p>
     *
     * @param in the in
     * @return 파일 내용
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readFileContent(InputStream in) throws IOException {
        StringBuffer buf = new StringBuffer();

        for (int i = in.read(); i != -1; i = in.read()) {
            buf.append((char) i);
        }

        return buf.toString();
    }

    /**
     * <p>
     * String 영으로 파일의 내용을 읽는다.
     * </p>
     *
     * @param file <code>File</code>
     * @param encoding <code>String</code>
     * @return 파일 내용
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readFile(File file, String encoding)
            throws IOException {
        StringBuffer sb = new StringBuffer();

        List<String> lines = FileUtils.readLines(file, encoding);

        for (Iterator<String> it = lines.iterator();;) {
            sb.append(it.next());

            if (it.hasNext()) {
                sb.append("");
            } else {
                break;
            }
        }

        return sb.toString();
    }

    /**
     * <p>
     * 파일의 확장자를 가져온다.
     * </p>
     * @param filename
     *        <code>String</code>
     * @return 파일확장자
     */
    public static String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    /**
     * <p>
     * 파일의 존재여부를 확인한다.
     * </p>
     * @param filename
     *        <code>String</code>
     * @return 존재여부
     */
    public static boolean isExistsFile(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    /**
     * <p>
     * 디렉토리명을 제외한 파일명을 가져온다.
     * </p>
     *
     * @param filename <code>String</code>
     * @return the string
     */
    public static String stripFilename(String filename) {
        return FilenameUtils.getBaseName(filename);
    }

    /**
     * <p>
     * 파일의 크기를 가져온다.
     * </p>
     * @param filename
     *        <code>String</code>
     * @return 존재여부
     */
    public static long getFileSize(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            return Long.valueOf("0");
        }
        return file.length();
    }
}
