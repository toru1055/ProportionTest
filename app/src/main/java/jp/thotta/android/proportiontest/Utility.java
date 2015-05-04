package jp.thotta.android.proportiontest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by thotta on 15/05/04.
 */
public class Utility {
    public static AlertDialog makeMessageAlertDialog(
            String title, String message, final Activity context, final boolean isFinish) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isFinish) {
                            context.finish();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }

    public static AdRequest makeAdRequest(Context context) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(context.getString(R.string.ad_test_device_id))
                .addTestDevice(context.getString(R.string.ad_test_device_id2))
                .addKeyword("有意差検定")
                .addKeyword("統計解析")
                .addKeyword("データ分析")
                .addKeyword("ABテスト")
                .addKeyword("ネット広告")
                .addKeyword("バナー広告")
                .addKeyword("オンライン広告")
                .build();
        return adRequest;
    }
}
