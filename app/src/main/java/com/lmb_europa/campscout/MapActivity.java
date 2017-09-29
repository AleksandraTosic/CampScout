package com.lmb_europa.campscout;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.*;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import io.github.douglasjunior.androidSimpleTooltip.ArrowDrawable;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;

import static java.util.concurrent.TimeUnit.*;

public class MapActivity extends AppCompatActivity {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //private Button b;
    private LocationManager locationManager;
    private LocationManager locManager;
    private LocationListener listener;
    private ImageView map;
    private ImageView iv;
    //ImageButton floatButton;

    private FloatingActionMenu menuGreen;
    private FloatingActionButton fab1;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    double longitudeGPS, latitudeGPS;
    double longitudeNetwork, latitudeNetwork;
    boolean network_manager;

    double lonGPS, latGPS;
    double lonNetwork, latNetwork;

    public final static double OneEightyDeg = 180.0d;
    public static double ImageSizeW, ImageSizeH;

    Canvas canvas;
    Bitmap scaledBitmap, scaledBitmap1;

    Location upper, lower, currentNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //click = gpsClass.getLocationStatus();

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.titlecolor));*/

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        /*map = (ImageView) findViewById(R.id.map_to_show_places);

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(getApplicationContext(), "Touch coordinates : " +
                            String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });*/

        map = (ImageView) findViewById(R.id.map_to_show_places);
        iv = (ImageView) findViewById(R.id.point_to_show_places);

        /*Display display = getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.imagep, options);
        int width = options.outWidth;
        if (width > displayWidth) {
            int widthRatio = Math.round((float) width / (float) displayWidth);
            options.inSampleSize = widthRatio;
        }
        options.inJustDecodeBounds = false;
        scaledBitmap =  BitmapFactory.decodeResource(getResources(),
                R.drawable.imagep, options);
        largeImage.setImageBitmap(scaledBitmap);*/

        /*ImageView smolImage = (ImageView) findViewById(R.id.point_to_show_places);
        Display display1 = getWindowManager().getDefaultDisplay();
        int displayWidth1 = display1.getWidth();
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.pin_black, options1);
        int width1 = options1.outWidth;
        if (width1 > displayWidth1) {
            int widthRatio1 = Math.round((float) width1 / (float) displayWidth1);
            options1.inSampleSize = widthRatio1;
        }
        options1.inJustDecodeBounds = false;
        scaledBitmap1 =  BitmapFactory.decodeResource(getResources(),
                R.drawable.pin_black, options1);
        smolImage.setImageBitmap(scaledBitmap1);*/

        menuGreen = (FloatingActionMenu)findViewById(R.id.menu_green);
        menuGreen.hideMenuButton(false);

        menus.add(menuGreen);

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

        createCustomAnimation();
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);

        fab1.setOnClickListener(clickListener);

        String title = getIntent().getStringExtra("checkApp");

        if (title.equals("FIRST_TIME")) {
            View yourView = findViewById(R.id.menu_green);


            new SimpleTooltip.Builder(this)
                    .anchorView(yourView)
                    .text("Press to see position")
                    .gravity(Gravity.TOP)
                    .animated(true)
                    .build()
                    .show();
        }

        getNetworkStatus();

        if (network_manager)
        {
            toggleNetworkUpdates();
            setNetworkStatus(false);
        }
        else {
            toggleGPSUpdates();
            setNetworkStatus(false);
        }
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.drawable.ic_close : R.drawable.ic_star);
            }
        });
        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);
    }

    public int getX()
    {
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;

        return maxX;
    }

    public int getY()
    {
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxY = mdispSize.y;
        return maxY;
    }

    public double getWidthSizesFromBitmap() {
        ImageView d = (ImageView) findViewById(R.id.map_to_show_places);
        ImageSizeW = d.getWidth();

        return ImageSizeW;
    }

    public double getHeightSizesFromBitmap() {
        ImageView d = (ImageView) findViewById(R.id.map_to_show_places);
        ImageSizeH = d.getHeight();

        return ImageSizeH;
    }

    public void setNetworkStatus(boolean status)
    {
        network_manager = status;
    }

    public void getNetworkStatus() //ovo mozda lici na getersku funkciju ali sam vrlo sigurna da nije...takodje nisam 100% zasto ovo stoji ovde, ali ne sumnjam da mi je imalo vrlo smisla kad sam dosla na ideju da ga stavim ovde
    {
        if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            setNetworkStatus(true);
        }
    }

    public double getLongitudeGPS() { return lonGPS; }

    public void setLongitudeGPS (double longGPS) { lonGPS = longGPS; }

    public double getLatitudeGPS() { return latGPS; }

    public void setLatitudeGPS (double latitGPS) { latGPS = latitGPS; }

    public double getLongitudeNetwork() { return lonNetwork; }

    public void setLongitudeNetwork (double longNet) { lonNetwork = longNet; }

    public double getLatitudeNetwork() { return latNetwork; }

    public void setLatitudeNetwork (double latitNet) { latNetwork = latitNet; }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this feature")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void toggleGPSUpdates(/*View view*/) {
        if (!checkLocation())
            return;
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, locationListenerGPS);
        //Toast.makeText(this, "GPS provider started running", Toast.LENGTH_LONG).show();

    }

    public void toggleNetworkUpdates(/*View view*/) {
        if (!checkLocation())
            return;

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 2 * 60 * 1000, 10, locationListenerNetwork);
            //Toast.makeText(this, "Network provider started running", Toast.LENGTH_LONG).show();

    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            //Toast.makeText(MapActivity.this, String.valueOf(longitudeGPS) + " " + String.valueOf(latitudeGPS), Toast.LENGTH_LONG).show();

            setLongitudeGPS(longitudeGPS);
            setLatitudeGPS(latitudeGPS);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
            //Toast.makeText(MapActivity.this, String.valueOf(longitudeNetwork) + " " + String.valueOf(latitudeNetwork), Toast.LENGTH_LONG).show();

            setLongitudeNetwork(longitudeNetwork);
            setLatitudeNetwork(latitudeNetwork);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private Location getUpper()
    {
        upper = new Location(Context.LOCATION_SERVICE);

        upper.setLongitude(13.5806036);
        upper.setLatitude(45.2617178);

        return upper;
    }

    private Location getLower()
    {
        lower = new Location(Context.LOCATION_SERVICE);

        lower.setLongitude(13.58476639);
        lower.setLatitude(45.25186953);

        return lower;
    }

    private Location getCurrent()
    {
        currentNow = new Location(Context.LOCATION_SERVICE);

        //currentNow.setLongitude(13.583892);
        //currentNow.setLatitude(45.254736);

        currentNow.setLongitude(getLongitudeGPS());
        currentNow.setLatitude(getLatitudeGPS());

        return currentNow;
    }

    /*ovo ce da sluzi za crtanje na mapi...fora je da se mapa pretvori u Canvas...nemoj to da zaboravis sad da odradis - vise nije neophodno to da se uradi*/
    public double getCurrentPixelY(Location upperLeft, Location lowerRight, Location current) {
        double hypotenuse = upperLeft.distanceTo(current);
        double bearing = upperLeft.bearingTo(current);
        double currentDistanceY = Math.cos(bearing * Math.PI / OneEightyDeg) * hypotenuse;
        //                           "percentage to mark the position"
        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
        double totalDistanceY = totalHypotenuse * Math.cos(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
        double currentPixelY = currentDistanceY / totalDistanceY * getHeightSizesFromBitmap();

        //Log.v("ytvtydxcrsdjcvdyd", String.valueOf(currentPixelY));

        return currentPixelY;
    }

    public double getCurrentPixelX(Location upperLeft, Location lowerRight, Location current) {
        double hypotenuse = upperLeft.distanceTo(current);
        double bearing = upperLeft.bearingTo(current);
        double currentDistanceX = Math.cos(bearing * Math.PI / OneEightyDeg) * hypotenuse;
        //                           "percentage to mark the position"
        double totalHypotenuse = upperLeft.distanceTo(lowerRight);
        double totalDistanceX = totalHypotenuse * Math.cos(upperLeft.bearingTo(lowerRight) * Math.PI / OneEightyDeg);
        double currentPixelX = currentDistanceX / totalDistanceX * getWidthSizesFromBitmap();

        //Log.v("ytvtydxc rsdj cvdyd", String.valueOf(currentPixelX));

        //koristi samo poslednji deo koordinata, nemoj da koristis celu koordinatu za pomeraj, i ovako na sestoj poziciji tek imas promenu

        return currentPixelX;
    }

    public void alert()
    {
        Log.v("dhgghggjghiiloigh", String.valueOf(getWidthSizesFromBitmap()) + " x " + String.valueOf(getHeightSizesFromBitmap()));
    }

    public void alert(float X, float Y)
    {
        Log.v("dhgghggjghiiloigh", String.valueOf(X) + " x " + String.valueOf(Y));
    }

    public void alertTouch()
    {
        map = (ImageView) findViewById(R.id.map_to_show_places);
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(getApplicationContext(), "Touch coordinates : " +
                            String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            beepForAnHour();
            menuGreen.close(true);
            getNetworkStatus();

            //Bitmap mutableBitmap = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
            //canvas = new Canvas(mutableBitmap);
            //canvas.drawBitmap(scaledBitmap1, 1000, 1000, null);

            /*ImageView smolImage = (ImageView) findViewById(R.id.point_to_show_places);
            Display display1 = getWindowManager().getDefaultDisplay();
            int displayWidth1 = display1.getWidth();
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), R.drawable.pin_black, options1);
            int width1 = options1.outWidth;
            if (width1 > displayWidth1) {
                int widthRatio1 = Math.round((float) width1 / (float) displayWidth1);
                options1.inSampleSize = widthRatio1;
            }
            options1.inJustDecodeBounds = false;
            scaledBitmap1 =  BitmapFactory.decodeResource(getResources(),
                    R.drawable.pin_black, options1);
            smolImage.setImageBitmap(scaledBitmap1);*/

            /*if (network_manager)
            {
                toggleNetworkUpdates();
                //setNetworkStatus(false);

                float coordX = (float)(getCurrentPixelX(getUpper(), getLower(), getCurrent()));
                float coordY = (float)(getCurrentPixelY(getUpper(), getLower(), getCurrent()));

                iv.setVisibility(View.VISIBLE);
                iv.setX(coordX);
                iv.setY(coordY);*/

                //alert();
                //alert(coordX, coordY);
                //alertTouch();

                /*Bitmap resultBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas c = new Canvas(resultBitmap);

                c.drawBitmap(scaledBitmap, 0, 0, null);

                Paint p = new Paint();
                p.setAlpha(255);

                c.drawBitmap(scaledBitmap1, (int)getCurrentPixelX(getUpper(), getLower(), getCurrent()), (int)getCurrentPixelY(getUpper(), getLower(), getCurrent()), null);*/

                //double coordX = (927.6671 * getLatitudeNetwork()) / 13.5764017;
                //double coordY = (827.71277 * getLongitudeNetwork()) / 45.2570237;
            /*}
            else
            {*/
                toggleGPSUpdates();
                //setNetworkStatus(false);

                float coordX = (float)(getCurrentPixelX(getUpper(), getLower(), getCurrent()));
                float coordY = (float)(getCurrentPixelY(getUpper(), getLower(), getCurrent()));

                iv.setVisibility(View.VISIBLE);
                iv.setX(coordX);
                iv.setY(coordY);

                //alert();
                //alert(coordX, coordY);
                //alertTouch();

                /*Bitmap resultBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas c = new Canvas(resultBitmap);

                //c.drawBitmap(scaledBitmap, 0, 0, null);

                Paint p = new Paint();
                p.setAlpha(255);

                c.drawBitmap(scaledBitmap1, (int)getCurrentPixelX(getUpper(), getLower(), getCurrent()), (int)getCurrentPixelY(getUpper(), getLower(), getCurrent()), null); //ovde treba da se menjaju koordinate*/

                //double coordX = (927.6671 * getLatitudeGPS()) / 13.5764017;
                //double coordY = (827.71277 * getLongitudeGPS()) / 45.2570237;
            //}
            //radi, sto je super...stvarno preslikava koordinate na piksel koji treba da se postavi na mapi, sto je fenomenalno da budem iskrena!
            //dok sam radila ovo, vrlo sam neraspolozena i sve je grozno pa je i ova funkcija grozna. Ali sad stvarno radi ono sto treba, iz ne znam kog razloga...
        }
    };

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() {
                if (network_manager) {

                    toggleNetworkUpdates();

                    float coordX = (float) (getCurrentPixelX(getUpper(), getLower(), getCurrent()));
                    float coordY = (float) (getCurrentPixelY(getUpper(), getLower(), getCurrent()));

                    iv.setVisibility(View.VISIBLE);
                    iv.setX(coordX);
                    iv.setY(coordY);
                }
                else {

                    toggleGPSUpdates();

                    float coordX = (float) (getCurrentPixelX(getUpper(), getLower(), getCurrent()));
                    float coordY = (float) (getCurrentPixelY(getUpper(), getLower(), getCurrent()));

                    iv.setVisibility(View.VISIBLE);
                    iv.setX(coordX);
                    iv.setY(coordY);
                }
            }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 1, 1, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
            }
        }, 60 * 60, SECONDS);
    }
    //TODO: ishendluj bug, onaj sto te tera dva puta da se klikne da bi se poromenila lokacija /recimo da je ovo odradjeno, ali nisam sigurna da je odradjeno kako valja, ali radi to sto treba
    //TODO: ubaciti uslov za default slucaj i u odnosu na default slucaj mrdaj koordinate...u suprotnom, greska ce da ti bude ogromna
    //TODO: ubaciti pomeraje za slucaje kad se uveca slika
    //TODO: bonus, ako uspes implementiraj automatsku promenu pozicije
}