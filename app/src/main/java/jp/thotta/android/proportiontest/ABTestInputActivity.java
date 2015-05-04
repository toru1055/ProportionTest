package jp.thotta.android.proportiontest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ABTestInputActivity extends ActionBarActivity {
    private static final String TAG = "ABTestInputActivity";
    private View.OnClickListener onExecTestClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editTextSampleA = (EditText)findViewById(R.id.sampleCountA);
            EditText editTextPositiveA = (EditText)findViewById(R.id.positiveCountA);
            EditText editTextSampleB = (EditText)findViewById(R.id.sampleCountB);
            EditText editTextPositiveB = (EditText)findViewById(R.id.positiveCountB);
            Intent intent = new Intent(ABTestInputActivity.this, ABTestResultActivity.class);
            try {
                intent.putExtra("sampleCountA", Integer.parseInt(editTextSampleA.getText().toString()));
                intent.putExtra("positiveCountA", Integer.parseInt(editTextPositiveA.getText().toString()));
                intent.putExtra("sampleCountB", Integer.parseInt(editTextSampleB.getText().toString()));
                intent.putExtra("positiveCountB", Integer.parseInt(editTextPositiveB.getText().toString()));
                startActivity(intent);
            } catch(Exception e) {
                //TODO: show dialog
                Log.v(TAG, e.getMessage());
                Utility.makeMessageAlertDialog(
                        "入力エラー",
                        "全てに数値を入力してください。数値は約21億までにしてください。",
                        ABTestInputActivity.this,
                        false).show();
            }
        }
    };



    private View.OnClickListener onResetClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText editTextSampleA = (EditText)findViewById(R.id.sampleCountA);
            EditText editTextPositiveA = (EditText)findViewById(R.id.positiveCountA);
            EditText editTextSampleB = (EditText)findViewById(R.id.sampleCountB);
            EditText editTextPositiveB = (EditText)findViewById(R.id.positiveCountB);
            editTextSampleA.setText("");
            editTextPositiveA.setText("");
            editTextSampleB.setText("");
            editTextPositiveB.setText("");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abtest_input);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button resetButton = (Button)findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(onResetClick);
        Button execTestButton = (Button)findViewById(R.id.buttonExecTest);
        execTestButton.setOnClickListener(onExecTestClick);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = Utility.makeAdRequest(this);
        mAdView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_abtest_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
