package com.salmin.media.anr;

import android.widget.ImageView;
import android.widget.TextView;

public class MediaItemViewHolder {

    static final int STATE_INVALID = -1;
    static final int STATE_NONE = 0;
    static final int STATE_PLAYABLE = 1;
    static final int STATE_PAUSED = 2;
    static final int STATE_PLAYING = 3;

    ImageView mImageView;
    TextView mTitleView;
    TextView mDescriptionView;

}
