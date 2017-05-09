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

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import creationsofali.boomboard.R;
import creationsofali.boomboard.activities.NoteActivity;
import creationsofali.boomboard.datamodels.Note;

/**
 * Created by ali on 5/1/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    List<Note> noteList;
    Context context;

    public NotesAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textDateTime, textSubject, textMessage;
        CardView cardNote;
        ImageView iconClip;

        ViewHolder(View itemView) {
            super(itemView);
            textSubject = (TextView) itemView.findViewById(R.id.textSubject);
            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
            textMessage = (TextView) itemView.findViewById(R.id.textMessage);
            iconClip = (ImageView) itemView.findViewById(R.id.iconClip);
            iconClip.setVisibility(View.GONE);
            cardNote = (CardView) itemView.findViewById(R.id.cardNote);

            cardNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open note
                    Note note = noteList.get(getAdapterPosition());
                    String gsonNote = new Gson().toJson(note);
                    context.startActivity(new Intent(context, NoteActivity.class)
                            .putExtra("note", gsonNote)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textSubject.setText(noteList.get(position).getSubject());

        if (noteList.get(position).getMessage() != null) {
            // for notes with message
            String noteMessage = noteList.get(position).getMessage();

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

        long dateTime = noteList.get(position).getDate();
        // Jun 05 - 15:47
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd - kk:mm", Locale.ENGLISH);
        holder.textDateTime.setText(dateFormat.format(dateTime));

        // attachments
        if (noteList.get(position).getDocUrl() != null ||
                noteList.get(position).getImageUrl() != null) {
            // note contains attachment
            holder.iconClip.setVisibility(View.VISIBLE);
        } else {
            // no attachment
            holder.iconClip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
