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
import creationsofali.boomboard.datamodels.Note;

/**
 * Created by ali on 5/1/17.
 */

public class CollapseNotesAdapter extends RecyclerView.Adapter<CollapseNotesAdapter.ViewHolder> {

    List<Note> noteList;
    Context context;

    public CollapseNotesAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
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
                    // open note
                    Note note = noteList.get(getAdapterPosition());
                    String gsonNote = new Gson().toJson(note);
                    context.startActivity(new Intent(context, NoticeActivity.class)
                            .putExtra("note", gsonNote)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }

    }

    public CollapseNotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_note, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CollapseNotesAdapter.ViewHolder holder, int position) {
        holder.textSubject.setText(noteList.get(position).getSubject());
        long dateTime = noteList.get(position).getDate();
        // Jun 05 - 15:47
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd - kk:mm", Locale.ENGLISH);
        holder.textDateTime.setText(dateFormat.format(dateTime));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
