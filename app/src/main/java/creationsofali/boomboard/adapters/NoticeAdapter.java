package creationsofali.boomboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.NoticeActivity;
import creationsofali.boomboard.datamodels.Notice;

/**
 * Created by ali on 5/1/17.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    List<Notice> noticeList;
    Context context;

    public NoticeAdapter(List<Notice> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textDateTime, textSubject, textMessage, textAuthorName;
        CardView cardNote;
        ImageView iconClip, imageAuthorDp;

        ViewHolder(View itemView) {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            textMessage = itemView.findViewById(R.id.textMessage);
            iconClip = itemView.findViewById(R.id.iconClip);
            iconClip.setVisibility(View.GONE);
            cardNote = itemView.findViewById(R.id.cardNote);
            textAuthorName = itemView.findViewById(R.id.textAuthorName);
            imageAuthorDp = itemView.findViewById(R.id.imageAuthorDp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open notice
                    Notice notice = noticeList.get(getAdapterPosition());
                    String gsonNote = new Gson().toJson(notice);
                    context.startActivity(new Intent(context, NoticeActivity.class)
                            .putExtra("notice", gsonNote)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notice_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textSubject.setText(noticeList.get(position).getSubject());

        holder.textAuthorName.setText(noticeList.get(position).getAuthor());

        if (noticeList.get(position).getMessage() != null) {
            // for notes with message
            String noteMessage = noticeList.get(position).getMessage();

            if (noteMessage.length() > 64) {
                // trim message
                holder.textMessage.setText(noteMessage.substring(0, 64));
                holder.textMessage.append("...");   // show continuation of story
            } else {
                // don't trim note message
                holder.textMessage.setText(noteMessage);
            }
        } else {
            // for notes with no message
            holder.textMessage.setText("This note has only attachment file.");
        }

        long dateTime = noticeList.get(position).getDate();
        // Jun 05 - 15:47
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd - kk:mm", Locale.ENGLISH);
        holder.textDateTime.setText(dateFormat.format(dateTime));

        // attachments
        if (noticeList.get(position).getDocUrl() != null ||
                noticeList.get(position).getImageUrl() != null) {
            // note contains attachment
            holder.iconClip.setVisibility(View.VISIBLE);
        } else {
            // no attachment
            holder.iconClip.setVisibility(View.GONE);
        }

        // author dp
        Glide.with(context).load(noticeList.get(position).getAuthorDpUrl()).into(holder.imageAuthorDp);
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
