package musicplayer.group3.dev.musicplayer.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import musicplayer.group3.dev.musicplayer.R;
import musicplayer.group3.dev.musicplayer.adapter.PagerAdapter;
import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.item.ItemSong;
import musicplayer.group3.dev.musicplayer.media.MediaManager;
import musicplayer.group3.dev.musicplayer.service.MediaService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //Bottom Menu
    private View viewBottom;
    private ImageView imvPausePlay;
    private ImageView imvPrevious, imvNext;
    private ImageView imvImageSong;
    private TextView tvTitleSong, tvNameArtist;
    private LinearLayout llDetailTitleSong;
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private MediaManager mediaManager;
    private UpdatePlayNewSong updatePlayNewSong = new UpdatePlayNewSong();
    private IntentFilter intentFilter = new IntentFilter();
    private Handler handler = new Handler();
    private NavigationView navigationView;
    private ImageView imvMenu, imvSetting, imvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnClick();
        mediaManager = MediaManager.getInstance(this);
        intentFilter.addAction(Const.ACTION_SEND_DATA);
        registerReceiver(updatePlayNewSong, intentFilter);
        MainActivity.this.runOnUiThread(runnable);
        showBottomLayout(false);
    }



    private void setOnClick() {
        navigationView.setNavigationItemSelectedListener(this);
        imvMenu.setOnClickListener(this);
        imvSetting.setOnClickListener(this);
        imvSearch.setOnClickListener(this);
        llDetailTitleSong.setOnClickListener(this);
        imvImageSong.setOnClickListener(this);
        imvNext.setOnClickListener(this);
        imvPrevious.setOnClickListener(this);
        imvPausePlay.setOnClickListener(this);
    }

    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        imvMenu =  findViewById(R.id.imv_menu_drawer);
        imvSetting = findViewById(R.id.imv_setting);
        imvSearch =  findViewById(R.id.imv_search);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 3);
        TabLayout tabLayout =  findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //Bottom Menu
        viewBottom = (View) findViewById(R.id.bottom_menu);
        llDetailTitleSong = (LinearLayout) findViewById(R.id.ll_detail_title_song);
        imvImageSong = (ImageView) findViewById(R.id.imv_image_song);
        imvNext = (ImageView) findViewById(R.id.imv_next);
        imvPrevious = (ImageView) findViewById(R.id.imv_previous);
        imvPausePlay = (ImageView) findViewById(R.id.imv_pause_play);
        tvTitleSong = (TextView) findViewById(R.id.tv_bottom_title_song);
        tvTitleSong.setSelected(true);
        tvNameArtist = (TextView) findViewById(R.id.tv_bottom_name_artist);
        if (mediaManager == null) {
            mediaManager = MediaManager.getInstance(this);
        }
        if (mediaManager.getArrItemSong().size() > 0) {
            ItemSong song = mediaManager.getArrItemSong().get(mediaManager.getCurrentIndex());
            setInforBottomLayout(song.getDisplayName(), song.getArtist());
        }
    }

    public void showBottomLayout(boolean isShow) {
        if (isShow) {
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            viewBottom.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_song) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_albums) {
            viewPager.setCurrentItem(1);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imv_menu_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.imv_setting:
                showPopupMenu(v);
                break;
            case R.id.imv_search:
                actionSearch();
                break;
            case R.id.imv_next:
                intent = new Intent(Const.ACTION_NEXT);
                sendBroadcast(intent);
                break;
            case R.id.imv_previous:
                intent = new Intent(Const.ACTION_PREVIOUS);
                sendBroadcast(intent);
                break;
            case R.id.imv_pause_play:
                Intent intentPause = new Intent(Const.ACTION_PAUSE_SONG);
                sendBroadcast(intentPause);
                break;
            case R.id.imv_image_song:
                actionShowDetailSong(v);
                break;
            case R.id.ll_detail_title_song:
                actionShowDetailSong(v);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQUEST_CODE_ACTION_SEARCH_MAIN:
                if (resultCode == RESULT_OK) {
                    int position = getItemSong(data.getStringExtra(Const.KEY_ACTION_SEARCH_SONG_NAME));
                    mediaManager.setCurrentIndex(position);
                    mediaManager.play(true);
                    showBottomLayout(true);
                    setInforBottomLayout(mediaManager.getArrItemSong().get(position).getDisplayName(), mediaManager.getArrItemSong().get(position).getArtist());
                }
                break;
        }
    }

    private int getItemSong(String songName) {
        ArrayList<ItemSong> arr = mediaManager.getArrItemSong();
        int position = 0;
        for (ItemSong song : arr) {
            if (song.getDisplayName().equalsIgnoreCase(songName)) {
                return position;
            }
            position++;
        }
        return -1;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateInforBottomLayout();
            handler.postDelayed(this, 200);
        }
    };

    private void updateInforBottomLayout() {
        if (mediaManager.getmPlayer().isPlaying()) {
            imvPausePlay.setImageResource(R.drawable.pause);
        } else {
            imvPausePlay.setImageResource(R.drawable.play);
        }
    }

    class UpdatePlayNewSong extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Const.ACTION_SEND_DATA:
                    setInforBottomLayout(intent.getStringExtra(Const.KEY_TITLE_SONG), intent.getStringExtra(Const.KEY_NAME_ARTIST));
                    break;
            }
        }
    }

    private void actionSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE_ACTION_SEARCH_MAIN);
    }


    private void actionShowDetailSong(View view) {
        Intent intent = new Intent(this, DetailSongActivity.class);
        startActivity(intent);
  
    }

    private void showPopupMenu(final View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Snackbar.make(v, "Please input setting", Snackbar.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void setInforBottomLayout(String nameSong, String nameArtist) {
        tvTitleSong.setText(nameSong);
        tvNameArtist.setText(nameArtist);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        try {
            unbindService(serviceConnection);
            unregisterReceiver(updatePlayNewSong);
        } catch (Exception e) {
            Log.d("Error", "Error unbind service connect");
        }
        super.onDestroy();
    }
}
