package com.framgia.music_29.screen.search;

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
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHodel> {
    private List<Song> mSongs;
    private Context mContext;
    private OnClickItemListener mClickItemListener;

    public SearchAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel holder, int position) {
        holder.bindView(mContext, mSongs.get(position), mClickItemListener);
    }

    public void setClickItemListener(OnClickItemListener clickItemListener) {
        mClickItemListener = clickItemListener;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageSong;
        private TextView mTextSongName;
        private TextView mTextSongUser;
        private OnClickItemListener mClickItemListener;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            mImageSong = itemView.findViewById(R.id.image_song);
            mTextSongName = itemView.findViewById(R.id.text_song_name);
            mTextSongUser = itemView.findViewById(R.id.text_song_user);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickItemListener != null) {
                mClickItemListener.onClick(mSongs, getAdapterPosition());
            }
        }

        public void bindView(Context context, Song song, OnClickItemListener clickItemListener) {
            mTextSongUser.setText(song.getUserFullName());
            mTextSongName.setText(song.getTitle());
            Picasso.with(context).load(song.getArtworkUrl()).
                    placeholder(R.drawable.item_music).into(mImageSong);
            mClickItemListener = clickItemListener;
        }
    }
}
