package com.lmb_europa.campscout;

import android.animation.AnimatorSet;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import java.lang.Object;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.douglasjunior.androidSimpleTooltip.ArrowDrawable;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MainActivity extends AppCompatActivity {

    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }
    /**
     * The app version code (not the version name!) that was used on the last
     * start of the app.
     */
    private static final String LAST_APP_VERSION = "last_app_version";

    /**
     * Finds out started for the first time (ever or in the current version).<br/>
     * <br/>
     * Note: This method is <b>not idempotent</b> only the first call will
     * determine the proper result. Any subsequent calls will only return
     * {@link AppStart#NORMAL} until the app is started again. So you might want
     * to consider caching the result!
     *
     * @return the type of app start
     */
    public AppStart checkAppStart() {
        PackageInfo pInfo;
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        AppStart appStart = AppStart.NORMAL;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int lastVersionCode = sharedPreferences
                    .getInt(LAST_APP_VERSION, -1);
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);
            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).commit();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(SyncStateContract.Constants.DATA,
                    "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Log.w(SyncStateContract.Constants.DATA, "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Definitively assuming normal app start.");
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }

    private FloatingActionMenu menuRed;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();
    Boolean stuff = false;

    String sendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        //myToolbar.setTitleTextColor(getResources().getColor(R.color.titlecolor));

        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        fab1.setEnabled(true);
        fab2.setEnabled(true);
        fab3.setEnabled(true);
        fab4.setEnabled(true);

        menuRed.setClosedOnTouchOutside(true);

        menuRed.hideMenuButton(false);

        menus.add(menuRed);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        fab4.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }
        menuRed.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuRed.isOpened()) {
                    Toast.makeText(MainActivity.this, "Have fun!", Toast.LENGTH_SHORT).show();
                }
                menuRed.toggle(true);

            }
        });
        switch (checkAppStart()) {
            case NORMAL:
                sendData = "NORMAL";
                break;
            case FIRST_TIME_VERSION:
                // TODO show what's new
                break;
            case FIRST_TIME:
                sendData = "FIRST_TIME";
                View yourView = findViewById(R.id.menu_red);

                new SimpleTooltip.Builder(MainActivity.this)
                        .anchorView(yourView)
                        .text("Click me!")
                        .gravity(Gravity.BOTTOM)
                        .arrowDirection(ArrowDrawable.RIGHT)
                        .animated(true)
                        .build()
                        .show();
                break;
            default:
                break;
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    Intent i1 = new Intent(MainActivity.this, CalendarActivity.class);
                    i1.putExtra("checkApp", sendData);
                    startActivity(i1);
                    break;
                case R.id.fab2:
                    Intent i2 = new Intent(MainActivity.this, MapActivity.class);
                    i2.putExtra("checkApp", sendData);
                    startActivity(i2);
                    break;
                case R.id.fab3:
                    Intent i3 = new Intent(MainActivity.this, GaleryActivity.class);
                    startActivity(i3);
                    break;
                case R.id.fab4:
                    Intent i4 = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(i4);
                    break;
            }
        }
    };

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}
