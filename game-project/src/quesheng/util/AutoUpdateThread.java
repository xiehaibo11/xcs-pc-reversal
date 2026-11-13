/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutoUpdateThread {
    public String filename = "CS.exe";
    public String appname = "CS\u6253\u724c\u7aef";
    public String xiheportalURL = "";

    public String saveUrlAs(String url, String filePath, String method) {
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        File file = new File(filePath);
        try {
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            fileOut = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);
            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            fileOut.close();
            bis.close();
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("\u629b\u51fa\u5f02\u5e38\uff01\uff01");
        }
        return file.getAbsolutePath();
    }

    public String getArchName() {
        return System.getProperty("sun.arch.data.model");
    }
}

