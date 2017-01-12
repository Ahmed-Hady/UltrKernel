package com.ultrakernel.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.ultrakernel.R;
import com.ultrakernel.fragment.CPUFragment;
import com.ultrakernel.fragment.KernelFragment;
import com.ultrakernel.fragment.Creditsfragement;
import com.ultrakernel.fragment.SystemInfo_fragement;

import me.drakeet.materialdialog.MaterialDialog;

import static com.ultrakernel.util.Config.UpdaterUrl;
import static com.ultrakernel.util.ShellCommands.RAM_IMP;
import static com.ultrakernel.util.ShellCommands.boost_system;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Creditsfragement mCredits;
    private SystemInfo_fragement mSystemInfo;
    private KernelFragment mKernel;
    private CPUFragment mCpu;
    private AppUpdater mAppUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Fragments*/
        mCredits=new Creditsfragement();
        mSystemInfo=new SystemInfo_fragement();
        mKernel=new KernelFragment();
        mCpu=new CPUFragment();

        /*default fragment*/
        updateFragment(this.mSystemInfo);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.menu_fab);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(190);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        Updater();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void Updater(){
        mAppUpdater=new AppUpdater(this);
        mAppUpdater
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML(UpdaterUrl)
                .setDisplay(Display.DIALOG)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
                .setButtonDismiss("Maybe later")
                .setButtonDoNotShowAgain("Huh, not interested");
        mAppUpdater.start();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_systemInfo) {
            final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.menu_fab);
            fabMenu.setVisibility(FloatingActionsMenu.VISIBLE);
            updateFragment(mSystemInfo);
        } else if (id == R.id.nav_kernel_tweaks) {
            final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.menu_fab);
            fabMenu.setVisibility(FloatingActionsMenu.GONE);
            updateFragment(mKernel);
        } else if (id == R.id.nav_cpu_tweaks) {
            final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.menu_fab);
            fabMenu.setVisibility(FloatingActionsMenu.GONE);
            updateFragment(mCpu);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_credits) {
            updateFragment(mCredits);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void updateFragment(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);

        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    public void boost(View v){
        if(getPreferences_bool("bb") == true) {
            boost_system(this);
        }else{
            mMaterialDialog.show();
        }
    }

    public void kill_drains(View v){
        RAM_IMP(this);
    }

    MaterialDialog mMaterialDialog = new MaterialDialog(this)
            .setTitle("Error!")
            .setMessage("BusyBox Not Found.\n")
            .setPositiveButton("INSTALL", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = "stericson.busybox";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    mMaterialDialog.dismiss();
                }
            })
            .setNegativeButton("NO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();
                }
            });

    //********************************* Getting & Setting Info ***********************************
    public void PutStringPreferences(String Name,String Function){
        SharedPreferences settings = getSharedPreferences(Name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Name, Function);
        editor.commit();
    }

    public String getStringPreferences(String Name){
        String o;
        SharedPreferences settings = getSharedPreferences(Name, 0); // 0 - for private mode
        o=settings.getString(Name,null);
        return o;
    }

    public void PutBooleanPreferences(String Name,Boolean Function){
        SharedPreferences settings = getSharedPreferences(Name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Name, Function);
        editor.commit();
    }

    public boolean getPreferences_bool(String Name){
        SharedPreferences settings = getSharedPreferences(Name, 0); // 0 - for private mode
        return settings.getBoolean(Name, Boolean.parseBoolean(null));
    }

    public void RemovePreferences(String Name){
        SharedPreferences settings = getSharedPreferences(Name, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(Name);
        editor.commit();
    }
    //********************************************************************************************

}

