package com.salmin.media.anr;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.media.MediaDescriptionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    static View setupView(Activity activity, View convertView, ViewGroup parent, MediaDescriptionCompat description, int state) {
        MediaItemViewHolder viewHolder;
        Integer cachedState = STATE_INVALID;

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.media_list_item, parent, false);
            viewHolder = new MediaItemViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.play_eq);
            viewHolder.mTitleView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mDescriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MediaItemViewHolder) convertView.getTag();
            cachedState = (Integer) convertView.getTag(R.id.tag_mediaitem_state_cache);
        }

        viewHolder.mTitleView.setText(description.getTitle());
        viewHolder.mDescriptionView.setText(description.getSubtitle());

        // If the state of convertView is different, we need to adapt the view to the
        // new state.
        if (cachedState == null || cachedState != state) {
            switch (state) {
                case STATE_PLAYABLE:
                    viewHolder.mImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_shortcut_play_arrow));
                    viewHolder.mImageView.setVisibility(View.VISIBLE);
                    break;
                case STATE_PLAYING:
                    AnimationDrawable animation = (AnimationDrawable) activity.getResources().getDrawable(R.drawable.ic_equalizer_animation);
                    viewHolder.mImageView.setImageDrawable(animation);
                    viewHolder.mImageView.setVisibility(View.VISIBLE);
                    animation.start();
                    break;
                case STATE_PAUSED:
                    viewHolder.mImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_equalizer1_white_36dp));
                    viewHolder.mImageView.setVisibility(View.VISIBLE);
                    break;
                default:
                    viewHolder.mImageView.setVisibility(View.GONE);
            }
            convertView.setTag(R.id.tag_mediaitem_state_cache, state);
        }

        return convertView;
    }

}
