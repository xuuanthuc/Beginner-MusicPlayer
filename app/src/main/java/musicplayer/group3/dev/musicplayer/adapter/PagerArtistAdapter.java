package musicplayer.group3.dev.musicplayer.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import musicplayer.group3.dev.musicplayer.common.Const;
import musicplayer.group3.dev.musicplayer.fragment.AlbumOfAritstFragment;
import musicplayer.group3.dev.musicplayer.fragment.SongOfArtistFragment;


public class PagerArtistAdapter extends FragmentStatePagerAdapter {

    private final int numOfTab;

    public PagerArtistAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Const.DEFAULT_VALUE_INT_0:
                return SongOfArtistFragment.getInstance();
            case Const.DEFAULT_VALUE_INT_1:
                return AlbumOfAritstFragment.getInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
