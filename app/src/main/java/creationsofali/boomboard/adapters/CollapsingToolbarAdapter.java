package creationsofali.boomboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class CollapsingToolbarAdapter extends RecyclerView.Adapter<CollapsingToolbarAdapter.ViewHolder> {

    List<Notice> noticeList;
    Context context;

    public CollapsingToolbarAdapter(List<Notice> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textDateTime, textSubject;
        CardView cardNote;

        ViewHolder(View itemView) {
            super(itemView);
            textSubject = (TextView) itemView.findViewById(R.id.textSubject);
            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
            cardNote = (CardView) itemView.findViewById(R.id.cardNote);

            cardNote.setOnClickListener(new View.OnClickListener() {
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

    public CollapsingToolbarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_note, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CollapsingToolbarAdapter.ViewHolder holder, int position) {
        holder.textSubject.setText(noticeList.get(position).getSubject());
        long dateTime = noticeList.get(position).getDate();
        // Jun 05 - 15:47
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd - kk:mm", Locale.ENGLISH);
        holder.textDateTime.setText(dateFormat.format(dateTime));
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
