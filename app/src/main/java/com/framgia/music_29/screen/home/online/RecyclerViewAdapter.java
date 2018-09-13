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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHodel> {
    private static List<Song> mSongs;
    private static Context mContext;
    private OnClickItemListener mClickItemListener;

    public RecyclerViewAdapter(Context context) {
        mContext = context;
        mSongs = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_online, parent, false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel holder, int position) {
        holder.bindView(mSongs.get(position) , mClickItemListener);
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

    public static class ViewHodel extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageSong;
        private TextView mTextSongName;
        private OnClickItemListener mClickItemListener;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            mImageSong = itemView.findViewById(R.id.image_song_player);
            mTextSongName = itemView.findViewById(R.id.text_song);
            itemView.setOnClickListener(this);
        }

        public void bindView(Song song , OnClickItemListener clickItemListener) {
            mClickItemListener = clickItemListener;
            mTextSongName.setText(song.getTitle().trim());
            Picasso.with(mContext).load(song.getArtworkUrl()).
                    placeholder(R.drawable.item_music).into(mImageSong);
        }

        @Override
        public void onClick(View v) {
            mClickItemListener.onClick(mSongs , getAdapterPosition());
        }
    }
}
