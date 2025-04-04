package yyonce.downloader;

import android.app.Activity;
import android.widget.*;

final class Ui implements CompoundButton.OnCheckedChangeListener {
  
  final State state;
      
  final EditText et;
  final CheckBox cb;

  Ui(Activity activity) { 
    state = new State(activity.getActionBar());
        
    et = new EditText(activity); 
    et.setHint("url");     
    cb = new CheckBox(activity);
    cb.setOnCheckedChangeListener(this);

    LinearLayout ll = new LinearLayout(activity);
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.addView(et);
    ll.addView(cb);
    activity.setContentView(ll);
  }

  @Override
  public void onCheckedChanged(CompoundButton cb, boolean checked) { 
    et.setEnabled(!checked);
    Download.apply(state, et.getText().toString(), checked); 
  }
   
}
