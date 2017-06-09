package creationsofali.boomboard.activities;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

import creationsofali.boomboard.R;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Notice;
import creationsofali.boomboard.datamodels.RequestCode;
import creationsofali.boomboard.helpers.DownloadTaskHelper;
import creationsofali.boomboard.helpers.PermissionsHelper;

public class NoticeActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView textSubject, textMessage, textAttachmentName, textStaffName, textStaffTitle, textWhen;
    ImageView imageAttachmentPreview, iconAttachment, iconDownload, imageStaffDp;
    CardView layoutAttachmentFile;

    Notice notice;

    private static final String TAG = "NoticeActivity",
            WRITE_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_PERMISSION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }


        String gsonNote = getIntent().getStringExtra("notice");
        notice = new Gson().fromJson(gsonNote, Notice.class);

        textSubject = (TextView) findViewById(R.id.textSubject);
        textMessage = (TextView) findViewById(R.id.textMessage);
        textAttachmentName = (TextView) findViewById(R.id.textAttachment);
        iconAttachment = (ImageView) findViewById(R.id.iconAttachment);
        iconDownload = (ImageView) findViewById(R.id.iconDownload);
        imageAttachmentPreview = (ImageView) findViewById(R.id.imagePreview);
        layoutAttachmentFile = (CardView) findViewById(R.id.layoutAttachmentFile);
        textStaffName = (TextView) findViewById(R.id.textStaffName);
        textStaffTitle = (TextView) findViewById(R.id.textStaffTitle);
        textWhen = (TextView) findViewById(R.id.textWhen);
        imageStaffDp = (ImageView) findViewById(R.id.imageStaffDp);

        // setting values for notice
        onCreateNotice(notice);

        imageAttachmentPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open attachment without download to device storage
                if (notice.getImageUrl() != null)
                    // show image preview
                    showImagePreview(notice.getImageUrl());
            }
        });

        iconDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // download attachment
                boolean hasWriteStoragePerm = PermissionsHelper.hasWriteStoragePermission(NoticeActivity.this);

                if (hasWriteStoragePerm) {
                    // download file
                    Log.d(TAG, "onClick: saving to storage");
                    //DownloadTaskHelper.saveFileToStorage(notice, NoticeActivity.this);
//                    if (notice.getDocUrl() != null) {
//                        // download doc file
//                        if (DownloadTaskHelper.saveFileToStorage(notice)
//                            showSnackbar("Successful saved to storage.");
//
//                    } else if (notice.getImageUrl() != null) {
//                        // download image file
//                        if (DownloadTaskHelper.saveFileToStorage("image"))
//                            showSnackbar("Successful saved to storage.");
                } else
                    PermissionsHelper.requestWriteStoragePermission(NoticeActivity.this);
            }
        });


        // set my custom app font
        View rootView = findViewById(android.R.id.content);
        new

                MyCustomAppFont(getApplicationContext(), rootView);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_notice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.optionBookmark:
                // save a notice as a bookmark
                Toast.makeText(NoticeActivity.this,
                        "Notice saved in your bookmarks.",
                        Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_bookmark_check);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            switch (requestCode) {
                case RequestCode.RC_WRITE_STORAGE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // DownloadTaskHelper.saveFileToStorage(notice, NoticeActivity.this);
                        DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper(NoticeActivity.this, notice);
                        downloadTaskHelper.downloadAttachmentFile();
                        Log.d(TAG, "onRequestPermissionsResult: permission granted");
                        // showSnackbar("Successful! Permission granted.");
                    } else {
                        Log.d(TAG, "onRequestPermissionsResult: permission denied");
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void onCreateNotice(Notice notice) {
        textSubject.setText(notice.getSubject());
        textStaffName.setText(notice.getAuthor());
        textStaffTitle.setText(notice.getAuthorEmail());

        //05 Jun '17 - 15:47
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM ''yy - kk:mm", Locale.ENGLISH);
        textWhen.setText(dateFormat.format(notice.getDate()));

        Glide.with(NoticeActivity.this).load(notice.getAuthorDpUrl()).into(imageStaffDp);

        if (notice.getMessage() != null)
            textMessage.setText(notice.getMessage());
        else {
            textMessage.setText("Download the attachment file below.");
            textMessage.setTextColor(ContextCompat.getColor(NoticeActivity.this, R.color.color_secondary_text));
        }

        // for attachment file
        String[] label = notice.getSubject().split(" ");

        if (notice.getDocUrl() == null && notice.getImageUrl() == null) {
            // no attachment
            layoutAttachmentFile.setVisibility(View.GONE);

        } else {
            textAttachmentName.setText(label[0]);
            if (label.length > 1)
                textAttachmentName.append("_" + label[1]);
            if (label.length > 2)
                textAttachmentName.append("_" + label[2]);
            if (label.length > 3)
                textAttachmentName.append("_" + label[3]);
            if (label.length > 4)
                textAttachmentName.append("_" + label[4]);
        }

        if (notice.getDocUrl() != null) {
            // doc attached
            iconAttachment.setImageResource(R.drawable.ic_file_pdf);
            iconAttachment.setColorFilter(ContextCompat.getColor(
                    NoticeActivity.this, R.color.color_red));
            imageAttachmentPreview.setImageResource(R.drawable.image_docs);

            // add extension at the end of file name
            textAttachmentName.append(".pdf");

        } else if (notice.getImageUrl() != null) {
            // image attached
            iconAttachment.setImageResource(R.drawable.ic_file_image);
            iconAttachment.setColorFilter(ContextCompat.getColor(
                    NoticeActivity.this, R.color.color_green));

            // add extension at the end of file name
            textAttachmentName.append(".png");

            Glide.with(NoticeActivity.this).load(notice.getImageUrl()).into(imageAttachmentPreview);
        }
    }

    private void showImagePreview(String imageUrl) {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_preview_image, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NoticeActivity.this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        ImageView imagePreview = (ImageView) dialogView.findViewById(R.id.imagePreviewDialog);
        Glide.with(NoticeActivity.this).load(imageUrl).into(imagePreview);
    }


    public void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.noticeCoordinator),
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setDuration(8000)
                .show();
    }
}


