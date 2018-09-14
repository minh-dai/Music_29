package com.framgia.music_29.screen.home.online;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.utils.OnClickItemListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Song> mSongs;
    private Context mContext;
    private OnClickItemListener mClickItemListener;

    public RecyclerViewAdapter(Context context) {
        mContext = context;
        mSongs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_online, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(mContext, mSongs.get(position), mSongs, mClickItemListener);
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
        notifyDataSetChanged();
    }

    public void setClickItemListener(OnClickItemListener clickItemListener) {
        mClickItemListener = clickItemListener;
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageSong;
        private TextView mTextSongName;
        private OnClickItemListener mClickItemListener;
        private List<Song> mSongs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageSong = itemView.findViewById(R.id.image_song_player);
            mTextSongName = itemView.findViewById(R.id.text_song);
            itemView.setOnClickListener(this);
        }

        public void bindView(Context context, Song song, List<Song> songs,
                OnClickItemListener clickItemListener) {
            mClickItemListener = clickItemListener;
            mTextSongName.setText(song.getTitle().trim());
            Picasso.with(context).load(song.getArtworkUrl()).
                    placeholder(R.drawable.item_music).into(mImageSong);
            mSongs = songs;
        }

        @Override
        public void onClick(View v) {
            mClickItemListener.onClick(mSongs , getAdapterPosition());
        }
    }
}
