package com.framgia.music_29.screen.genre;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Song> mSongs;
    private Context mContext;
    private onClickItemListener mClickItemListener;
    private boolean mLocal;
    private final int mViewTypeItem = 0, mViewTypeLoading = 1;

    public GenreAdapter(Context context) {
        mContext = context;
        mSongs = new ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == mViewTypeItem) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recycler_view, parent, false);
            return new ItemViewHolder(view, mSongs);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_loading, parent, false);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.bindView(mContext, mSongs.get(position), mLocal, mClickItemListener);
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.mProgressBar.setIndeterminate(true);
        }
    }

    public void setSongs(List<Song> songs, Boolean local) {
        mLocal = local;
        mSongs = songs;
        notifyDataSetChanged();
    }

    // anh xem em loi nay nua duojc k ??
    //loi gi nua e
    //man hinh do o dau

    @Override
    public int getItemViewType(int position) {
        return mSongs.get(position) == null ? mViewTypeLoading : mViewTypeItem;
    }

    protected void setClickItemListener(onClickItemListener clickItemListener) {
        mClickItemListener = clickItemListener;
    }

    public void setSongs(List<Song> songs) {
        mSongs.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    protected static class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        protected ImageView mImageSong;
        protected TextView mTextSongName;
        protected TextView mTextSongUser;
        private List<Song> mSongs;
        protected onClickItemListener mClickItemListener;

        public ItemViewHolder(@NonNull View itemView, List<Song> songs) {
            super(itemView);
            mSongs = songs;
            mImageSong = itemView.findViewById(R.id.image_song);
            mTextSongName = itemView.findViewById(R.id.text_song_name);
            mTextSongUser = itemView.findViewById(R.id.text_song_user);
            itemView.setOnClickListener(this);
        }

        public void bindView(Context context, Song song,boolean local,  onClickItemListener clickItemListener) {
            mTextSongUser.setText(song.getUserFullName().trim());
            mTextSongName.setText(song.getTitle().trim());

            if (!local) {
                Picasso.with(context).load(song.getArtworkUrl()).
                        placeholder(R.drawable.item_music).into(mImageSong);
            }else {
                setImageLocal(song);
            }

            mClickItemListener = clickItemListener;
        }

        private void setImageLocal(Song song) {
            if(song.getUriImage() != null) {
                mImageSong.setImageBitmap(BitmapFactory.decodeByteArray(song.getUriImage(), 0,
                        song.getUriImage().length));
            }else {
                mImageSong.setImageResource(R.drawable.item_music);
            }
        }

        @Override
        public void onClick(View v) {
            if (mClickItemListener != null) {
                mClickItemListener.onClick(mSongs, getAdapterPosition());
            }
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        public LoadingViewHolder(View view) {
            super(view);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    interface onClickItemListener {
        void onClick(List<Song> songs, int position);

        void onLoadMore();
    }
}
