package jp.thotta.android.proportiontest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.apache.commons.math3.stat.interval.BinomialConfidenceInterval;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.stat.interval.NormalApproximationInterval;

import info.hoang8f.widget.FButton;


public class ABTestResultActivity extends ActionBarActivity {
    private static final double CONFIDENCE_LEVEL = 0.95;
    private static final double SIGNIFICANCE_LEVEL = 0.05;
    private static final String TAG = "ABTestResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abtest_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        int sampleCountA = intent.getIntExtra("sampleCountA", 0);
        int positiveCountA = intent.getIntExtra("positiveCountA", 0);
        int sampleCountB = intent.getIntExtra("sampleCountB", 0);
        int positiveCountB = intent.getIntExtra("positiveCountB", 0);

        try {
            IndependenceTest independenceTest = new IndependenceTest(sampleCountA, positiveCountA, sampleCountB, positiveCountB);
            FButton fButton = (FButton) findViewById(R.id.textSignificance);
            if (independenceTest.isSignificantlyDifferent(SIGNIFICANCE_LEVEL)) {
                fButton.setText("有意差あり！");
                fButton.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
            } else {
                fButton.setText("有意差なし。");
                fButton.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
            }
            double ctrA = (double) positiveCountA / sampleCountA;
            double ctrB = (double) positiveCountB / sampleCountB;
            BinomialConfidenceInterval binomialConfidenceInterval = new NormalApproximationInterval();
            ConfidenceInterval ciA = binomialConfidenceInterval.createInterval(sampleCountA, positiveCountA, CONFIDENCE_LEVEL);
            ConfidenceInterval ciB = binomialConfidenceInterval.createInterval(sampleCountB, positiveCountB, CONFIDENCE_LEVEL);
            setTextView(R.id.textCtrA, ctrA);
            setTextView(R.id.textCtrB, ctrB);
            setTextView(R.id.textCtrLowerA, ciA.getLowerBound());
            setTextView(R.id.textCtrUpperA, ciA.getUpperBound());
            setTextView(R.id.textCtrLowerB, ciB.getLowerBound());
            setTextView(R.id.textCtrUpperB, ciB.getUpperBound());

            GraphView graph = (GraphView)findViewById(R.id.graph);
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"", "Type A", "Type B", ""});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getGridLabelRenderer().setHighlightZeroLines(false);
            graph.getGridLabelRenderer().setVerticalAxisTitle("確率(%)");
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1, toPercent(ctrA)),
                    new DataPoint(2, toPercent(ctrB))
            });
            series.setShape(PointsGraphSeries.Shape.POINT);
            series.setSize(10);
            graph.addSeries(series);
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1, toPercent(ciA.getLowerBound())),
                    new DataPoint(1, toPercent(ciA.getUpperBound()))
            });
            graph.addSeries(series2);
            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(2, toPercent(ciB.getLowerBound())),
                    new DataPoint(2, toPercent(ciB.getUpperBound()))
            });
            graph.addSeries(series3);
            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(0, toPercent(ctrA)),
                    new DataPoint(3, toPercent(ctrB))
            });
            series4.setSize(0);
            graph.addSeries(series4);
        } catch(Exception e) {
            //TODO: show dialog
            Log.v(TAG, e.getMessage());
            Utility.makeMessageAlertDialog(
                    "入力エラー",
                    "0以上の数値を入力してください。" +
                            "試行回数 > 成功回数にしてください。",
                    this, true).show();
        }

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = Utility.makeAdRequest(this);
        mAdView.loadAd(adRequest);

    }

    private void setTextView(int textViewId, double ratio) {
        TextView tv = (TextView)findViewById(textViewId);
        tv.setText(toPercentString(ratio));
    }

    private static double toPercent(double ratio) {
        return ratio * 100;
    }

    private static String toPercentString(double ratio) {
        return String.format("%.2f", toPercent(ratio));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_abtest_result, menu);
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
