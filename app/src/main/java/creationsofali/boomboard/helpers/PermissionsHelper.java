package creationsofali.boomboard.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import creationsofali.boomboard.datamodels.RequestCode;

/**
 * Created by ali on 4/3/17.
 */

public class PermissionsHelper {

    private static String TAG = "PermissionsHelper";

    private static final String
            CALL_PERMISSION = "android.permission.CALL_PHONE",
            READ_STORAGE_PERMISSION = "android.permission.READ_EXTERNAL_PERMISSION",
            WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_PERMISSION";


    public static boolean hasCallPermission(Context context) {
        int res = ContextCompat.checkSelfPermission(context, CALL_PERMISSION);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestCallPermission(final Activity activity, final View parentView) {

//         if (ActivityCompat.shouldShowRequestPermissionRationale(activity, CALL_PERMISSION)) {
//         show explanation for permission request
//        View dialogView = activity.getLayoutInflater().inflate(R.layout.layout_dialog_call_perm_req, null);
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
//        dialogBuilder.setView(dialogView);
//
//        final AlertDialog dialog = dialogBuilder.create();
//
//        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // request permission
//                ActivityCompat.requestPermissions(
//                        activity,
//                        new String[]{Manifest.permission.CALL_PHONE},
//                        RC_CALL_PERMISSION);
//                dialog.dismiss();
//            }
//        });
//
//        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // cancel request
//                dialog.dismiss();
//                Snackbar.make(
//                        parentView,
//                        activity.getApplicationContext().getString(R.string.sorry_cant_make_calls),
//                        Snackbar.LENGTH_LONG).show();
//            }
//        });
//        dialog.show();
        // } else {
        // request permission without explain anything
        //     ActivityCompat.requestPermissions(
        //             activity,
        //             new String[]{Manifest.permission.CALL_PHONE},
        //             RC_CALL_PERMISSION);
        // }
    }


    public static boolean hasReadStoragePermission(Context context) {
        int res = ContextCompat.checkSelfPermission(context, READ_STORAGE_PERMISSION);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasWriteStoragePermission(Context context) {
        int res = ContextCompat.checkSelfPermission(context, WRITE_STORAGE_PERMISSION);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                RequestCode.RC_READ_STORAGE);
    }

    public static void requestWriteStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                RequestCode.RC_WRITE_STORAGE);
    }

    public static void requestReadWriteStoragePermission(Activity activity) {
//        List<String> permReqList = new ArrayList<>();
//        permReqList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        permReqList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        ActivityCompat.requestPermissions(activity,
//                permReqList.toArray(new String[permReqList.size()]),
//                RequestCode.RC_READ_WRITE_STORAGE);

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                RequestCode.RC_READ_WRITE_STORAGE);
    }

}
