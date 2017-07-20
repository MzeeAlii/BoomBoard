package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by ali on 7/19/17.
 */

public class DirectoriesHelper {

    public static void openDirectory(Context context, String dirPath) {

        Uri folderUri = Uri.parse(dirPath);
        Intent openFolderIntent = new Intent(Intent.ACTION_VIEW);
        openFolderIntent.setDataAndType(folderUri, "resource/folder");

        if (openFolderIntent.resolveActivityInfo(context.getPackageManager(), 0) != null)
            context.startActivity(openFolderIntent);
        else
            Toast.makeText(context,
                    "File Manager unavailable or not configured properly!",
                    Toast.LENGTH_LONG).show();
    }
}
