package creationsofali.boomboard.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import creationsofali.boomboard.activities.NoticeActivity;
import creationsofali.boomboard.datamodels.Notice;

/**
 * Created by ali on 6/3/17.
 */

public class DownloadTaskHelper {

    private static final String TAG = "DownloadTaskHelper";
    private StorageReference storageReference;
    private Context context;
    private Notice notice;
    private ProgressDialog dialog;
    private static boolean isDownloadTaskStarted = false;
    private NoticeActivity activity;

    private static final long ONE_MEGABYTE = 1024 * 1024, ONE_KILOBYTE = 1024;

    public DownloadTaskHelper(Context context, Notice notice) {
        this.context = context;
        this.activity = (NoticeActivity) context;
        this.notice = notice;
        this.dialog = new ProgressDialog(context);
        this.dialog.setCancelable(false);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        if (notice.getDocUrl() != null)
            storageReference = firebaseStorage.getReferenceFromUrl(notice.getDocUrl());
        else
            storageReference = firebaseStorage.getReferenceFromUrl(notice.getImageUrl());
    }

    public void downloadAttachmentFile() {

        File boomBoardFolder = getBoomBoardFolder();
        // File newFile = null;

        if (notice.getDocUrl() != null) {
            onCreateNewFile(0, ".pdf", boomBoardFolder);

        } else if (notice.getImageUrl() != null) {
            onCreateNewFile(0, ".png", boomBoardFolder);
        }
        // finally
        // if (newFile != null)
        //   downloadTask(newFile);
    }

    private File getBoomBoardFolder() {
        String boomBoardPath;
        File boomBoardFolder;

        if (notice.getDocUrl() != null) {
            // doc folder required
            boomBoardPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/BoomBoard/Docs";

            boomBoardFolder = new File(boomBoardPath);
            // check if folder exists, if not create
            if (boomBoardFolder.exists() || boomBoardFolder.mkdirs())
                // save file in the folder
                return boomBoardFolder;

        } else if (notice.getImageUrl() != null) {
            // image folder required
            boomBoardPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/BoomBoard/Images";

            boomBoardFolder = new File(boomBoardPath);
            // check if folder exists, if not create
            if (boomBoardFolder.exists() || boomBoardFolder.mkdirs())
                // save file in the folder
                return boomBoardFolder;
        }
        // else
        return null;
    }

    private void onCreateNewFile(int dupCount, String extension, File boomBoardFolder) {
        String newFileName;

        if (dupCount == 0)
            newFileName = notice.getDate() + extension;
        else
            newFileName = notice.getDate() + "-dup-" + dupCount + extension;

        File newFile = new File(boomBoardFolder, newFileName);
        try {
            if (newFile.createNewFile()) {
                // download file and save to storage
                Log.d(TAG, "onCreateNewFile: file created " + newFileName);

                if (!isDownloadTaskStarted)
                    downloadTask(newFile);

                // return newFile;
                // return true;
            } else if (newFile.exists()) {
                // recursion to create dup file
                dupCount++;
                // Toast.makeText(context, "File already exists.", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCreateNewFile: dupCount = " + dupCount);
                // self-calling
                onCreateNewFile(dupCount, extension, boomBoardFolder);
            }

        } catch (IOException e) {
            Log.d(TAG, "onCreateNewFile: err = " + e.getMessage());
            //  return null;
        }

        // return null;
    }

    private void downloadTask(File newFile) {
        isDownloadTaskStarted = true;
        Log.d(TAG, "downloadTask");
        dialog.setTitle("Downloading File");
        dialog.setMessage("Please wait...");
        dialog.show();

        storageReference.getFile(newFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // show succession of task
                Log.d(TAG, "downloadTask#onSuccess: file saved to storage");
                isDownloadTaskStarted = false;
                dialog.dismiss();
                activity.showSnackbar("Successful, file saved to storage.");
                Toast.makeText(context, "Successful!", Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "downloadTask#onFailure:  " + e.getMessage());
                dialog.dismiss();
                isDownloadTaskStarted = false;
                activity.showSnackbar("Failed, please try again.");

                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
            }

        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                long totalByteCount = taskSnapshot.getTotalByteCount();
                Log.d(TAG, "downloadTask#onProgress: byteCount = " + totalByteCount);

                if (totalByteCount > ONE_MEGABYTE) {
                    // greater than 1 MB
                    if (totalByteCount > 0)
                        dialog.setTitle("Downloading File " + totalByteCount / ONE_MEGABYTE + " MB");

                    int progress = (int) (taskSnapshot.getBytesTransferred() / totalByteCount) * 100;
                    // update progress percent
                    dialog.setMessage("Downloaded " + progress + "%");

                } else {
                    // less than 1 MB
                    if (totalByteCount > 0)
                        dialog.setTitle("Downloading File " + totalByteCount / ONE_KILOBYTE + " KB");

                    int progress = (int) (taskSnapshot.getBytesTransferred() / totalByteCount) * 100;
                    // update progress percent
                    dialog.setMessage("Downloaded " + progress + "%");
                }

                if (!dialog.isShowing())
                    dialog.show();
            }
        });
    }
}
