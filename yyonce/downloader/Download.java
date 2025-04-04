package yyonce.downloader;

import android.net.Uri;
import android.os.Environment;
import java.io.File;

final class Download {

  static final File rootDir;
  static {
    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    rootDir = new File(dir, "Downloader");    
  } 

  static Thread ing = null;
  static void apply(final State state, final String url, boolean download) {
    if (!download) { 
      if (ing != null) {
        ing.interrupt();
        ing = null;
      }
      state.set("Downloader", null);
    } else {
      final Uri uri = Uri.parse(url);
      final String dirName = uri.getHost();
      final String name = uri.getLastPathSegment(); 

      if (dirName == null || name == null) {
        state.set("Error", "Invalid name"); 
        return;
      }

      final File downloadDir = new File(rootDir, dirName);      
      final File file = new File(downloadDir, name); 
      final File cache = new File(downloadDir, "." + name);
      if (file.exists()) {
        state.set("Downloaded", name); 
        return;
      }

      state.set("Connect", name); 
      final Thread thread = new Thread() {
        @Override
        public void run() {   
          final Thread t = this;                                    
          try {
            new Downloadable(url, cache, file) {
              @Override
              public void onDownload(long count, long length, boolean done) {
                if (ing == t) { 
                  state.set(done ? "Downloaded" : "Downloading", done ? name : count + "/" + length);
                }
              }
            }.start();
          } catch (Throwable e) {
            if (ing == t) {
              state.set("Error", "Invalid download"); 
            }
          } 
        }
      }; 
      
      ing = thread;
      state.handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          if (ing == thread) {
            ing.start();
          }
        }
      }, 1000);
    }
  }

}
