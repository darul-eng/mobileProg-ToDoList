package com.Final.todolist.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.Final.todolist.R;
import com.Final.todolist.data.Note;
import com.Final.todolist.ui.detail.DetailActivity;
import com.Final.todolist.utils.NoteDiffCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {
    private final ArrayList<Note> listNotes = new ArrayList<>();
    private final Activity activity;

    NoteAdapter(Activity activity) {
        this.activity = activity;

    }

    void setListNotes(List<Note> listNotes) {
        final NoteDiffCallback diffCallback = new NoteDiffCallback(this.listNotes, listNotes);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.listNotes.clear();
        this.listNotes.addAll(listNotes);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, int position) {
        holder.tvTitle.setText(listNotes.get(position).getTitle());
        holder.tvDate.setText(listNotes.get(position).getDate());
        holder.tvDescription.setText(listNotes.get(position).getDesc());
        holder.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POSITION, holder.getAdapterPosition());
                intent.putExtra(DetailActivity.EXTRA_NOTE, listNotes.get(holder.getAdapterPosition()));
                activity.startActivityForResult(intent, DetailActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Note> filteredList = new ArrayList<>();

            filteredListNotes : if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listNotes);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Note note : listNotes){
                    if (note.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(note);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listNotes.clear();
            listNotes.addAll((Collection<? extends Note>) results.values);
            notifyDataSetChanged();
        }
    };

    class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription, tvDate;
        final CardView cvNote;

        NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_desc);
            tvDate = itemView.findViewById(R.id.tv_item_date);
//            tv_item_datepicker = itemView.findViewById(R.id.tv_item_datepicker);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}