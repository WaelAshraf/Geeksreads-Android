package com.example.geeksreads;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NotificationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Context mContext;

    /* SideBar Views */
    ImageView userPhoto;
    TextView userName;
    TextView followersCount;
    TextView booksCount;
    MenuItem FollowItem;
    MenuItem BookItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mContext = this;

        /* ToolBar and SideBar Setups */
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Notifications");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /* Getting All views by id from SideBar Layout */
        Menu menu = navigationView.getMenu();
        MenuItem itemFollower = menu.findItem(R.id.Followers);
        followersCount = (TextView) itemFollower.getActionView();
        FollowItem = menu.findItem(R.id.Followers);
        followersCount.setTextColor(getResources().getColor(R.color.white));
        MenuItem itemBook = menu.findItem(R.id.MyBooks);
        booksCount = (TextView) itemBook.getActionView();
        booksCount.setTextColor(getResources().getColor(R.color.white));
        BookItem = menu.findItem(R.id.MyBooks);
        /* Get Header Items */
        View mHeader = navigationView.getHeaderView(0);
        userName = mHeader.findViewById(R.id.UserNameTxt);
        userPhoto = mHeader.findViewById(R.id.UserPhoto);
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(NotificationActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(NotificationActivity.this, Profile.class);
                startActivity(mIntent);
            }
        });
        JSONObject JSON = new JSONObject();
        String UrlSideBar = "http://geeksreads.000webhostapp.com/Fatema/SideBar.php";
        GetSideBarDetails getSideBarDetails = new GetSideBarDetails();
        getSideBarDetails.execute(UrlSideBar,JSON.toString());


        final JSONObject jsonObject = new JSONObject();
        try {
            JSON.put("id", "value");
        }catch (JSONException e) {
            e.printStackTrace();
        }

        final String UrlService = "http://geeksreads.000webhostapp.com/Shrouk/BookDetails.php";

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GetNotificationsList performBackgroundTask = new GetNotificationsList();
                                performBackgroundTask.execute(UrlService,jsonObject.toString());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        MenuItem item = menu.findItem(R.id.menunotification);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search books");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.Home) {

            Intent myIntent = new Intent(NotificationActivity.this, SideBarActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.Followers) {
            Intent myIntent = new Intent(NotificationActivity.this, FollowActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.MyBooks) {
            Intent myIntent = new Intent(NotificationActivity.this, BookActivity.class);
            startActivity(myIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Class that get the data from host and Add it to its views.
     *  The Parameters are host Url and toSend Data.
     */
    public class GetNotificationsList extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
            //progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result){
            //progress.setVisibility(View.GONE);
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                dialog.setMessage("Done");
                //dialog.show();
                ListView notificationList = findViewById(R.id.NotificationList);
                notificationList.setAdapter(new Notification_JSONAdapter(mContext, new JSONArray(result)));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }


    /**
     * Class that get sidebar profile pic. from server
     */
    private class GetUserPicture extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String photoUrl = params[0];
                URL url = new URL(photoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                // Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            userPhoto.setImageBitmap(result);

        }
    }

    /**
     * Class that get sidebar Data from server
     */
    private class GetSideBarDetails extends AsyncTask<String, Void, String> {
        static final String REQUEST_METHOD = "GET";
        //public static final int READ_TIMEOUT = 3000;
        //public static final int CONNECTION_TIMEOUT = 3000;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(mContext).create();
            dialog.setTitle("Connection Status");
        }

        @Override
        protected String doInBackground(String... params){
            String UrlString = params[0];
            String JSONString = params[1];
            String result= "";

            try {
                //Create a URL object holding our url
                URL url = new URL(UrlString);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(REQUEST_METHOD);
                http.setDoInput(true);
                http.setDoOutput(true);

                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                String data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(JSONString,"UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                ops.close();

                //Create a new InputStreamReader
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
                String line ="";
                while ((line = reader.readLine()) != null)
                {
                    result += line;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;

            } catch (MalformedURLException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            return result;
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result){
            if(result==null) {
                Toast.makeText(mContext,"Unable to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                FollowItem.setTitle("Followers   " + jsonObject.getString("Followers"));
                BookItem.setTitle("My Books   " + jsonObject.getString("CountBooks"));
                userName.setText(jsonObject.getString("UserName"));
                GetUserPicture Pic = new GetUserPicture();
                Pic.execute(jsonObject.getString("photourl"));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

    }


}
