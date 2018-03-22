package com.example.vaibhavmitaliitian.etoll;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Vaibhav Mital IITian on 06-03-2018.
 */


public class enablePermission {
    public static void startInstalledAppDetailsActivity(final Activity context) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle("Please Enable Permissions");
        helpBuilder.setMessage("Click Yes to continue.");
        helpBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context == null) {
                    return;
                }
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + context.getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(i);

            }
        });
        AlertDialog helpDialog = helpBuilder.create();

        helpDialog.show();

    }

    public static void showSimplePopUp(final Activity context, String title, String message) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle(title);
        helpBuilder.setMessage(message);
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}
