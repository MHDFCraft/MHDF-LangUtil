package cn.chengzhiya.langutil.manager.network;

import cn.chengzhiya.langutil.exception.DownloadException;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class HttpManager {
    /**
     * 获取最快的地址
     *
     * @param urlList 地址列表
     * @return 延迟最低的地址(如果全部无法访问则返回第一个)
     */
    public String getFastUrl(List<String> urlList) {
        String minDelayUrl = urlList.get(0);
        long minDelay = Long.MAX_VALUE;

        for (String url : urlList) {
            long delay = getConnectingDelay(url);
            if (delay != -1 && delay < minDelay) {
                minDelay = delay;
                minDelayUrl = url;
            }
        }

        return minDelayUrl;
    }

    /**
     * 通过URL地址打开URL连接
     *
     * @param urlString URL地址
     * @return URL连接
     */
    public URLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(20));
        connection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(20));
        return connection;
    }

    /**
     * 获取指定URL地址的连接延迟
     *
     * @param urlString URL地址
     * @return 连接延迟
     */
    public long getConnectingDelay(String urlString) {
        long startTime = System.currentTimeMillis();
        try {
            URLConnection urlConnection = openConnection(urlString);
            urlConnection.connect();
        } catch (IOException e) {
            return -1;
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * 通过URL连接下载文件
     *
     * @param connection URL连接
     * @return 文件数据
     */
    public byte[] downloadFile(URLConnection connection) throws DownloadException {
        try {
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = ByteStreams.toByteArray(in);
                if (bytes.length == 0) {
                    throw new DownloadException("无可下载文件");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DownloadException(e);
        }
    }

    /**
     * 通过URL连接下载并保存文件
     *
     * @param connection URL连接
     * @param savePath   保存目录
     */
    public void downloadFile(URLConnection connection, Path savePath) throws DownloadException {
        try {
            Files.write(savePath, downloadFile(connection));
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
