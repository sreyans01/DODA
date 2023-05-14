package com.sreyans.discussondrawings.helper;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UIUtils {

    public static void  showBottomSheet(FragmentActivity activity, BottomSheet bottomSheet) {
        showBottomSheet(activity, bottomSheet, null);
    }

    public static void showBottomSheet(FragmentActivity activity, BottomSheet bottomSheet, Bundle args) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager fm = activity.getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(bottomSheet.getTagName());
            if (fragment != null) {
                // remove any BottomSheet already shown
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
            if (args != null) {
                bottomSheet.setArguments(args);
            }
            fm.beginTransaction().add(bottomSheet, bottomSheet.getTagName()).commitAllowingStateLoss();
        }
    }

    public static void replaceFragment(AppCompatActivity context, Class<? extends Fragment> T, Bundle arguments, String TAG, int containerViewId, boolean addToBackStack) {
        replaceFragment(context, T, arguments, TAG, containerViewId, addToBackStack, 0, 0);
    }

    public static void replaceFragment(AppCompatActivity context, Class<? extends Fragment> T, String TAG, int containerViewId) {
        replaceFragment(context, T, null, TAG, containerViewId, false);
    }

    public static void replaceFragment(AppCompatActivity context, Class<? extends Fragment> T, Bundle arguments, String TAG, int containerViewId, boolean addToBackStack, int startTransition, int endTransition) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            try {
                fragment = T.newInstance();
                fragment.setArguments(arguments);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(startTransition, endTransition);
        fragmentTransaction.replace(containerViewId, fragment, TAG);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(TAG);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public static boolean removeFragmentsByTag(AppCompatActivity context, String... fragmentTags) {
        boolean isAnyFragmentRemoved = false;
        if (context != null) {
            FragmentManager fragmentManager = context.getSupportFragmentManager();
            for (String tag : fragmentTags) {
                Fragment fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment != null) {
                    isAnyFragmentRemoved = true;
                    fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
            }
        }
        return isAnyFragmentRemoved;
    }

    public static void showDialog(AppCompatActivity activity, DialogFragment dialog, Bundle args, String tag) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager fm = activity.getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null) {
                // remove any TopSheet already shown
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
            if (args != null) {
                dialog.setArguments(args);
            }
            fm.beginTransaction().add(dialog, tag).commitAllowingStateLoss();
        }
    }

    public static void showToast(String message) {}

}
