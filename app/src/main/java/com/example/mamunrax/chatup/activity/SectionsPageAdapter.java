package com.example.mamunrax.chatup.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mamunrax.chatup.fragment.ChatsFragment;
import com.example.mamunrax.chatup.fragment.FriendsFragment;
import com.example.mamunrax.chatup.fragment.RequestsFragment;

class SectionsPageAdapter extends FragmentPagerAdapter{

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position){
//            case 0:
//
//                return ;
//            case 1:
//                return null;
//            case 2:
//                return null;
//            default:
//                return null;
//        }
//    }
}
