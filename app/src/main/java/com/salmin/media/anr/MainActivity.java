package com.salmin.media.anr;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BrowseAdapter mBrowserAdapter;
    private ImageButton mPlayPause;
    private TextView mTitle;
    private TextView mSubtitle;
    private ImageView mAlbumArt;
    private ViewGroup mPlaybackControls;

    private MediaMetadataCompat mCurrentMetadata;
    private PlaybackStateCompat mCurrentState;

    private MediaBrowserCompat mMediaBrowser;

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    mMediaBrowser.subscribe(mMediaBrowser.getRoot(), mSubscriptionCallback);
                    try {
                        MediaControllerCompat mediaController =
                                new MediaControllerCompat(
                                        MainActivity.this, mMediaBrowser.getSessionToken());
                        updatePlaybackState(mediaController.getPlaybackState());
                        updateMetadata(mediaController.getMetadata());
                        mediaController.registerCallback(mMediaControllerCallback);
                        MediaControllerCompat.setMediaController(
                                MainActivity.this, mediaController);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

    private final MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    super.onChildrenLoaded(parentId, children);
                }
            };

    // Receives callbacks from the MediaController and updates the UI state,
    // i.e.: Which is the current item, whether it's playing or paused, etc.
    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onSessionDestroyed() {
                    super.onSessionDestroyed();
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                }
            };

    // Displays list of browsed MediaItems.
    private class BrowseAdapter extends ArrayAdapter<MediaBrowserCompat.MediaItem> {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));

    }
}
