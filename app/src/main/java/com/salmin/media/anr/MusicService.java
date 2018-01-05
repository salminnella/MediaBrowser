package com.salmin.media.anr;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.util.List;

public class MusicService extends MediaBrowserService{

    private MediaSessionCompat mSession;
    private PlaybackManager mPlayback;

    final MediaSessionCompat.Callback mCallback =
            new MediaSessionCompat.Callback() {
                @Override
                public void onPlayFromMediaId(String mediaId, Bundle extras) {
                    mSession.setActive(true);
                    MediaMetadataCompat metadata =
                            MusicLibrary.getMetadata(MusicService.this, mediaId);
                    mSession.setMetadata(metadata);
                    mPlayback.play(metadata);
                }

                @Override
                public void onPlay() {
                    if (mPlayback.getCurrentMediaId() != null) {
                        onPlayFromMediaId(mPlayback.getCurrentMediaId(), null);
                    }
                }

                @Override
                public void onPause() {
                    mPlayback.pause();
                }

                @Override
                public void onStop() {
                    stopSelf();
                }

                @Override
                public void onSkipToNext() {
                    onPlayFromMediaId(
                            MusicLibrary.getNextSong(mPlayback.getCurrentMediaId()), null);
                }

                @Override
                public void onSkipToPrevious() {
                    onPlayFromMediaId(
                            MusicLibrary.getPreviousSong(mPlayback.getCurrentMediaId()), null);
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();

        // Start a new MediaSession.
        mSession = new MediaSessionCompat(this, "MusicService");
        mSession.setCallback(mCallback);
        mSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        setSessionToken(mSession.getSessionToken());

        // TODO: Uncomment the following line to show a notification.
        final MediaNotificationManager mediaNotificationManager =
                new MediaNotificationManager(this);

        mPlayback =
                new PlaybackManager(
                        this,
                        new PlaybackManager.Callback() {
                            @Override
                            public void onPlaybackStatusChanged(PlaybackStateCompat state) {
                                mSession.setPlaybackState(state);

                                // TODO: Uncomment the following line to show a notification.
                                mediaNotificationManager.update(
                                        mPlayback.getCurrentMedia(), state, getSessionToken());
                            }
                        });
    }

    @Override
    public void onDestroy() {
        mPlayback.stop();
        mSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new MediaBrowserServiceCompat.BrowserRoot(MusicLibrary.getRoot(), null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowser.MediaItem>> result) {
        result.sendResult(MusicLibrary.getMediaItems());
    }
}
