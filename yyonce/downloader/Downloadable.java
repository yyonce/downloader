package yyonce.downloader;

import java.io.*;
import java.net.*;

abstract class Downloadable {

  private final String url;
  private final File cache;
  private final File file;

  Downloadable(String url, File cache, File file) {
    this.url = url;
    this.cache = cache;
    this.file = file; 
  }

  abstract void onDownload(long count, long length, boolean done);

  void start() throws IOException {
    final long start = cache.length();

    HttpURLConnection connect = null;
    InputStream in = null;
    FileOutputStream out = null;

    try {
      connect = (HttpURLConnection) new URL(url).openConnection();
      connect.setConnectTimeout(1000 * 10);
      connect.setReadTimeout(1000 * 10);
      
      if (start > 0) {
        connect.addRequestProperty("Range", "bytes=" + start + "-");
      }
      connect.connect();

      final long code = connect.getResponseCode();
      if (code != HttpURLConnection.HTTP_OK
      && code != HttpURLConnection.HTTP_PARTIAL) {
        throw new IOException("Code " + code);
      }

      final long len = connect.getContentLengthLong(); // API 24
      final long length = len == -1 ? -1 : len + start;
      in = connect.getInputStream();
      cache.getParentFile().mkdirs(); 
      out = new FileOutputStream(cache, true);

      final byte[] buffer = new byte[1024 * 1024];
      int read;
      long count = start;
      while (!Thread.interrupted()) {
        read = in.read(buffer);
        if (read == -1) {
          cache.renameTo(file);                 
          onDownload(count, length, true);   
          return;
        }           
        out.write(buffer, 0, read);
        count += read;        
        onDownload(count, length, false);     
      }
    } finally {
      try {
        out.close();
      } catch (Throwable e) {
      }
      try {
        in.close();
      } catch (Throwable e) {
      }
      try {
        connect.disconnect();
      } catch (Throwable e) {
      }
    }
  }

}
