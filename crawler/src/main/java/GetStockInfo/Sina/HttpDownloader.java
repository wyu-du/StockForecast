package GetStockInfo.Sina;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {
 
    public static void download(String remoteFileUrl, String localFilePath) throws IOException {
    		try {
                URL url = new URL(remoteFileUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5 * 1000); // 5000 毫秒内没有连接上 则放弃连接
                httpURLConnection.connect(); // 连接
                
                try (DataInputStream dis = new DataInputStream(httpURLConnection.getInputStream());
                            FileOutputStream fos = new FileOutputStream(localFilePath)) {
                        byte[] buf = new byte[20*1024];
                        for (int readSize; (readSize = dis.read(buf)) >0;) {
                            fos.write(buf, 0, readSize);
                        }
                        //System.out.println("下载完毕~");
                 } catch (IOException ex) {
                        ex.printStackTrace();
                 }
                 httpURLConnection.disconnect();
            } catch (IOException ex) {
                System.out.println("URL 不存在或者连接超时");
            }
    	}
}