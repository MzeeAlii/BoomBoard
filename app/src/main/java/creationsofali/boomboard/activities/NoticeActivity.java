package creationsofali.boomboard.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import creationsofali.boomboard.R;
import creationsofali.boomboard.appfonts.MyCustomAppFont;
import creationsofali.boomboard.datamodels.Notice;

public class NoticeActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView textSubject, textMessage, textAttachmentName;
    ImageView imageAttachmentPreview, iconAttachment, iconDownload;
    CardView layoutAttachmentFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
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
        final Notice notice = new Gson().fromJson(gsonNote, Notice.class);

        textSubject = (TextView) findViewById(R.id.textSubject);
        textMessage = (TextView) findViewById(R.id.textMessage);
        textAttachmentName = (TextView) findViewById(R.id.textAttachment);
        iconAttachment = (ImageView) findViewById(R.id.iconAttachment);
        iconDownload = (ImageView) findViewById(R.id.iconDownload);
        imageAttachmentPreview = (ImageView) findViewById(R.id.imagePreview);
        layoutAttachmentFile = (CardView) findViewById(R.id.layoutAttachmentFile);

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
                Toast.makeText(NoticeActivity.this, "Downloading file...", Toast.LENGTH_LONG).show();
            }
        });


        // set my custom app font
        View rootView = findViewById(android.R.id.content);
        new MyCustomAppFont(getApplicationContext(), rootView);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void onCreateNotice(Notice notice) {
        textSubject.setText(notice.getSubject());
        String[] label = notice.getSubject().split(" ");

        if (notice.getMessage() != null)
            textMessage.setText(notice.getMessage());
        else {
            textMessage.setText("Download the attachment file below.");
            textMessage.setTextColor(ContextCompat.getColor(NoticeActivity.this, R.color.color_secondary_text));
        }


        // for attachment file
        if (notice.getDocUrl() == null && notice.getImageUrl() == null) {
            // no attachment
            layoutAttachmentFile.setVisibility(View.GONE);

        } else if (notice.getDocUrl() != null) {
            // doc attached
            iconAttachment.setImageResource(R.drawable.ic_file_pdf);
            iconAttachment.setColorFilter(ContextCompat.getColor(
                    NoticeActivity.this, R.color.color_red));
            imageAttachmentPreview.setImageResource(R.drawable.image_docs);

            textAttachmentName.setText(label[0]);
            if (label.length > 1)
                textAttachmentName.append("_" + label[1]);
            if (label.length > 2)
                textAttachmentName.append("_" + label[2]);
            if (label.length > 3)
                textAttachmentName.append("_" + label[3]);
            if (label.length > 4)
                textAttachmentName.append("_" + label[4]);

            // add extension at the end of file name
            textAttachmentName.append(".pdf");

        } else if (notice.getImageUrl() != null) {
            // image attached
            iconAttachment.setImageResource(R.drawable.ic_file_image);
            iconAttachment.setColorFilter(ContextCompat.getColor(
                    NoticeActivity.this, R.color.color_green));

            textAttachmentName.setText(label[0]);
            if (label.length > 1)
                textAttachmentName.append("_" + label[1]);
            if (label.length > 2)
                textAttachmentName.append("_" + label[2]);
            if (label.length > 3)
                textAttachmentName.append("_" + label[3]);
            if (label.length > 4)
                textAttachmentName.append("_" + label[4]);

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
}


