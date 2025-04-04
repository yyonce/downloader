package yyonce.downloader;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;

public final class Activity extends android.app.Activity {

  @Override
  protected void onCreate(Bundle bundle) { 
    super.onCreate(bundle);
    requestPermissions(new String[] {
      android.Manifest.permission.READ_EXTERNAL_STORAGE,
      android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }, 0);
    new Ui(this);
  }

  @Override
  public void onRequestPermissionsResult(int code, String[] permission, int[] state) {
    super.onRequestPermissionsResult(code, permission, state);
    if (code == 0) {
      for (int i : state) {
        if (i != PackageManager.PERMISSION_GRANTED) {
          System.exit(0);
        }
      } 
    }
  }

  @Override
  public boolean onKeyDown(int k, KeyEvent ke) {
    if (k == KeyEvent.KEYCODE_BACK) {
      moveTaskToBack(true);
      return true;
    }
    return super.onKeyDown(k, ke);
  }

} 
