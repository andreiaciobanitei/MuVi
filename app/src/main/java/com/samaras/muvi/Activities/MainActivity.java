package com.samaras.muvi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.samaras.muvi.Backend.ApiUtil;
import com.samaras.muvi.Backend.Models.MovieInfo;
import com.samaras.muvi.Backend.Models.MovieList;
import com.samaras.muvi.Backend.Models.Wishlist;
import com.samaras.muvi.Backend.MoviesAdapter;
import com.samaras.muvi.R;
import com.samaras.muvi.Backend.SpUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressDialog pDialog;
    private FirebaseUser user;
    MovieList movieList;
    Context context;
    Toolbar toolbar;
    private static MenuItem item;
    Drawer result;
    Bitmap[] images;
    String[] titles;
    String[] ids;
    String[] ratings;
    String[] descriptions;
    String[] genres;
    AccountHeader headerResult;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private RecyclerView rvMovies;


    public static void close(){
        item.collapseActionView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        item = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) item.getActionView();
        mSearchView.setQueryHint("Search for a movie");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String term = query.replaceAll(" ", "+");
                String url = ApiUtil.createURL("/search/movie") + "&query=\"" + term + "\"";
                new JSONAsyncTask(url, null).execute();
                setActionBarName("Search results for: \"" + term.replaceAll("\\+", " ") + "\"");
                close();
                return  true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("watchlist");
        movieList = new MovieList();
        rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        LinearLayoutManager  moviesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(moviesLayoutManager);



        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext())
                        .load(uri).placeholder(placeholder)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext())
                        .cancelRequest(imageView);
            }
        });
        Wishlist.getInstance();

        PrimaryDrawerItem trendingItem = new PrimaryDrawerItem().withIdentifier(1).withName("Trending")
                .withIcon(GoogleMaterial.Icon.gmd_trending_up)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        (new JSONAsyncTask(ApiUtil.createURL("/movie/now_playing"), null)).execute();
                        setActionBarName("Trending");
                        return false;
                    }
                });
        PrimaryDrawerItem topRatedItem = new PrimaryDrawerItem().withIdentifier(2).withName("Top Rated")
                .withIcon(GoogleMaterial.Icon.gmd_assessment)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ApiUtil.createURL("/movie/top_rated"), null).execute();
                        setActionBarName("Top Rated");
                        return false;
                    }
                });
        PrimaryDrawerItem searchItem = new PrimaryDrawerItem().withIdentifier(10).withName("Search")
                .withIcon(GoogleMaterial.Icon.gmd_search)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        setActionBarName("Search");
                        return false;
                    }
                });

        PrimaryDrawerItem wishlistItem = new PrimaryDrawerItem().withIdentifier(11).withName("Watchlist")
                .withIcon(GoogleMaterial.Icon.gmd_card_giftcard)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, final int position, IDrawerItem drawerItem) {
                        setActionBarName("Watchlist");

                        ArrayList<MovieInfo> movies = Wishlist.getInstance().list;
                        final MoviesAdapter adapter = new MoviesAdapter(context, movies);
                        adapter.setListener(new MoviesAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(MovieInfo item, final int position) {
                                final MovieInfo movie = item;
                                new MaterialDialog.Builder(MainActivity.this)
                                        .title(movie.title)
                                        .content(movie.description)
                                        .negativeText("Close")
                                        .positiveText("Remove from watchlist")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                mDatabaseReference.child(movie.getId()).removeValue();
                                                adapter.deleteMovie(position);
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), movie.title + " was removed from your watchlist!",
                                                        Toast.LENGTH_LONG).show();

                                            }})
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }})

                                        .negativeColor(getResources().getColor(R.color.md_red_700))
                                        .show();
                            }
                        });
                        rvMovies.setAdapter(adapter);
                        return false;
                    }
                });

        List<IDrawerItem> categorySubitems = new ArrayList<>();
        SecondaryDrawerItem catAction = new SecondaryDrawerItem().withIdentifier(3).withName("Action").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_flash_on)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ApiUtil.createURL("/genre/28/movies"), null).execute();
                        setActionBarName("Action");
                        return false;
                    }
                });
        SecondaryDrawerItem catComedy = new SecondaryDrawerItem().withIdentifier(4).withName("Comedy").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_mood)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ApiUtil.createURL("/genre/35/movies"), null).execute();
                        setActionBarName("Comedy");
                        return false;
                    }
                });;
        SecondaryDrawerItem catThriller = new SecondaryDrawerItem().withIdentifier(5).withName("Thriller").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_gesture)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ApiUtil.createURL("/genre/53/movies"), null).execute();
                        setActionBarName("Thriller");
                        return false;
                    }
                });
        SecondaryDrawerItem catHorror = new SecondaryDrawerItem().withIdentifier(6).withName("Horror").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_bug_report)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ApiUtil.createURL("/genre/27/movies"), null).execute();
                        setActionBarName("Horror");
                        return false;
                    }
                });
        categorySubitems.add(catAction);
        categorySubitems.add(catComedy);
        categorySubitems.add(catThriller);
        categorySubitems.add(catHorror);

        PrimaryDrawerItem categoriesItem = new PrimaryDrawerItem().withIdentifier(7)
                .withSubItems(categorySubitems).withName("Categories")
                .withIcon(GoogleMaterial.Icon.gmd_list)
                .withIcon(GoogleMaterial.Icon.gmd_arrow_drop_down);

        PrimaryDrawerItem profileItem = new PrimaryDrawerItem().withIdentifier(8).withName("Profile")
                .withIcon(GoogleMaterial.Icon.gmd_account_box)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent switchIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(switchIntent);
                        return false;
                    }
                });
        PrimaryDrawerItem logoutItem = new PrimaryDrawerItem().withIdentifier(9).withName("Logout")
                .withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Wishlist.getInstance().clearWishlist();
                        finish();
                        Intent switchIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(switchIntent);
                        SpUtil.setPreferenceString(context, "email", "");
                        SpUtil.setPreferenceString(context, "password", "");
                        FirebaseAuth.getInstance().signOut();
                        return false;
                    }
                });
        PrimaryDrawerItem chatItem = new PrimaryDrawerItem().withIdentifier(10).withName("Chat")
                .withIcon(GoogleMaterial.Icon.gmd_message)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Wishlist.getInstance().clearWishlist();
                        finish();
                        Intent switchIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(switchIntent);
                        return false;
                    }
                });

        user = FirebaseAuth.getInstance().getCurrentUser();
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(user.getDisplayName())
                                .withIcon(user.getPhotoUrl())
                                .withIdentifier(1)
                )
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .addDrawerItems(
                        trendingItem,
                        topRatedItem,
                        categoriesItem,
                        searchItem,
                        wishlistItem,
                        new DividerDrawerItem(),
                        chatItem,
                        profileItem,
                        logoutItem)
                .withAccountHeader(headerResult)
                .build();


        (new JSONAsyncTask(ApiUtil.createURL("/movie/now_playing"), null)).execute();
        setActionBarName("Trending");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
        updateUserProfile();

    }
    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateUserProfile(){
        if(user != null) {
            IProfile activeProfile = headerResult.getActiveProfile();
            activeProfile.withName(user.getDisplayName());
            activeProfile.withIcon(user.getPhotoUrl());
            headerResult.updateProfile(activeProfile);
        }
    }

    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    public void setActionBarName(String name) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);
    }

    public class JSONAsyncTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection;
        String URL_STRING;
        JSONObject jsonObject;
        View view;

        public JSONAsyncTask(String URL, View view) {
            this.URL_STRING = URL;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();

            try {
                URL url = new URL(URL_STRING);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                ArrayList<MovieInfo> movies = ApiUtil.getMoviesFromJson(result);

                MoviesAdapter adapter = new MoviesAdapter(context, movies);
                adapter.setListener(new MoviesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(MovieInfo item, int position) {
                        final MovieInfo movie = item;
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(movie.title)
                                .content(movie.description)
                                .negativeText("Close")
                                .positiveText("Add to watchlist")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if(!Wishlist.getInstance().containsMovie(movie))
                                            {
                                                mDatabaseReference.push().setValue(movie);
                                            }
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), movie.title + " was added to your watchlist!",
                                                Toast.LENGTH_LONG).show();
                                    }})
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }})

                                .negativeColor(getResources().getColor(R.color.md_red_700))
                                .show();
                    }
                });
                rvMovies.setAdapter(adapter);
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}