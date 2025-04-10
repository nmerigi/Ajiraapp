package com.example.ajiraapp;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override
    public boolean matchesSafely(Root root) {
        // Check if the window type is a toast
        int type = root.getWindowLayoutParams().get().type;
        // Check for both TYPE_APPLICATION_OVERLAY and TYPE_TOAST for compatibility
        return type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY || type == WindowManager.LayoutParams.TYPE_TOAST;
    }
}