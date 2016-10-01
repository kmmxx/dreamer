
package com.dreamer.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public abstract class BaseActivity extends Activity {

    /**
     * set the layout 
     */
    public abstract void createView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        /*case R.id.menu_about:
            Intent intent = new Intent(BaseActivity.this,AboutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.small_2_big, R.anim.fade_out);
            return true;
        case R.id.menu_feedback:
            FeedbackAgent agent = new FeedbackAgent(this);
            agent.startFeedbackActivity();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        case R.id.menu_share:
            final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
            mController.openShare(BaseActivity.this, false);
             overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;*/
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
