package yyonce.downloader;

import android.app.ActionBar;
import android.os.*;

final class State implements Runnable {

  final Handler handler = new Handler(Looper.getMainLooper());
  final ActionBar actionBar;
  String title, subTitle;

  State(ActionBar ab) {
    actionBar = ab;
  }

  @Override
  public void run() {
    actionBar.setTitle(title); 
    actionBar.setSubtitle(subTitle);
  } 
  
  void set(String a, String b) {
    title = a;
    subTitle = b;
    handler.post(this);
  }
  
}
