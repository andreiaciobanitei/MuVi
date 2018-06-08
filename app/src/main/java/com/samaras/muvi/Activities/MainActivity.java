package com.samaras.muvi.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.samaras.muvi.Backend.ClientHTTP;
import com.samaras.muvi.Backend.Models.CustomList;
import com.samaras.muvi.Backend.Models.MovieInfo;
import com.samaras.muvi.Backend.Models.MovieList;
import com.samaras.muvi.Backend.Models.Wishlist;
import com.samaras.muvi.R;
import com.samaras.muvi.Backend.SpUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressDialog pDialog;
    private ListView lv;
    private FirebaseUser user;
    MovieList movieList;
    Context context;
    Toolbar toolbar;
    ConstraintLayout searchbox;
    Button searchButton;
    EditText searchTerm;
    SearchView searchView;
    ArrayList<HashMap<String, String>> movieInfos;
    private static MenuItem item;
    Drawer result;
    Bitmap[] images;
    String[] titles;
    String[] ratings;
    String[] descriptions;
    String[] genres;
    String[] original_descriptions;
    int current_index = 0;
    AccountHeader headerResult;

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
                String url = ClientHTTP.createURL("/search/movie") + "&query=\"" + term + "\"";
                new JSONAsyncTask(url, null).execute();
                setActionBarName("Search results for: \"" + term.replaceAll("\\+", " ") + "\"");
                lv.setVisibility(View.VISIBLE);
                searchbox.setVisibility(View.INVISIBLE);
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

        movieList = new MovieList();
        movieInfos = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        searchbox = (ConstraintLayout) findViewById(R.id.searchbox);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchTerm = (EditText) findViewById(R.id.searchTerm);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = searchTerm.getText().toString();
                term = term.replaceAll(" ", "+");
                String url = ClientHTTP.createURL("/search/movie") + "&query=\"" + term + "\"";
                new JSONAsyncTask(url, null).execute();
                setActionBarName("Search results for: \"" + term.replaceAll("\\+", " ") + "\"");
                lv.setVisibility(View.VISIBLE);
                searchbox.setVisibility(View.INVISIBLE);
            }
        });


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


        PrimaryDrawerItem trendingItem = new PrimaryDrawerItem().withIdentifier(1).withName("Trending")
                .withIcon(GoogleMaterial.Icon.gmd_trending_up)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        (new JSONAsyncTask(ClientHTTP.createURL("/movie/now_playing"), null)).execute();
                        setActionBarName("Trending");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
        PrimaryDrawerItem topRatedItem = new PrimaryDrawerItem().withIdentifier(2).withName("Top Rated")
                .withIcon(GoogleMaterial.Icon.gmd_assessment)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ClientHTTP.createURL("/movie/top_rated"), null).execute();
                        setActionBarName("Top Rated");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
        PrimaryDrawerItem searchItem = new PrimaryDrawerItem().withIdentifier(10).withName("Search")
                .withIcon(GoogleMaterial.Icon.gmd_search)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        setActionBarName("Search");
                        lv.setVisibility(View.INVISIBLE);
                        searchbox.setVisibility(View.VISIBLE);
                        return false;
                    }
                });

        PrimaryDrawerItem wishlistItem = new PrimaryDrawerItem().withIdentifier(11).withName("Watchlist")
                .withIcon(GoogleMaterial.Icon.gmd_card_giftcard)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        setActionBarName("Watchlist");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);

                        HashSet<MovieInfo> list = Wishlist.getInstance().list;

                        titles = new String[list.size()];
                        images = new Bitmap[list.size()];
                        descriptions = new String[list.size()];
                        ratings = new String[list.size()];
                        genres = new String[list.size()];

                        int i = 0;
                        for(MovieInfo movie : Wishlist.getInstance().list) {
                            titles[i] = movie.title;
                            descriptions[i] = movie.description;
                            ratings[i] = movie.rating;
                            genres[i] = movie.genres;
                            images[i] = movie.bitmap;
                            i++;
                        }

                        CustomList adapter = new CustomList(MainActivity.this, titles, images, descriptions, ratings, genres);
                        lv.setAdapter(adapter);

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> av, View v, final int i, long l) {
                                new MaterialDialog.Builder(v.getContext())
                                        .title(titles[i])
                                        .content(original_descriptions[i])
                                        .negativeText("Close")
                                        .positiveText("Remove from watchlist")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                HashSet<MovieInfo> list = Wishlist.getInstance().list;
                                                for(MovieInfo movie : list) {
                                                    if(movie.title == titles[i]) {
                                                        list.remove(movie);
                                                        titles = new String[list.size()];
                                                        images = new Bitmap[list.size()];
                                                        descriptions = new String[list.size()];
                                                        ratings = new String[list.size()];
                                                        genres = new String[list.size()];

                                                        int i = 0;
                                                        for (MovieInfo movie2 : Wishlist.getInstance().list) {
                                                            titles[i] = movie2.title;
                                                            descriptions[i] = movie2.description;
                                                            ratings[i] = movie2.rating;
                                                            genres[i] = movie2.genres;
                                                            images[i] = movie2.bitmap;
                                                            i++;
                                                        }
                                                        CustomList adapter = new CustomList(MainActivity.this, titles, images, descriptions, ratings, genres);
                                                        lv.setAdapter(adapter);
                                                        break;
                                                    }
                                                }
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), titles[i] + " was removed from your watchlist!",
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
                        return false;
                    }
                });

        List<IDrawerItem> categorySubitems = new ArrayList<>();
        SecondaryDrawerItem catAction = new SecondaryDrawerItem().withIdentifier(3).withName("Action").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_flash_on)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ClientHTTP.createURL("/genre/28/movies"), null).execute();
                        setActionBarName("Action");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
        SecondaryDrawerItem catComedy = new SecondaryDrawerItem().withIdentifier(4).withName("Comedy").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_mood)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ClientHTTP.createURL("/genre/35/movies"), null).execute();
                        setActionBarName("Comedy");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });;
        SecondaryDrawerItem catThriller = new SecondaryDrawerItem().withIdentifier(5).withName("Thriller").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_gesture)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ClientHTTP.createURL("/genre/53/movies"), null).execute();
                        setActionBarName("Thriller");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
        SecondaryDrawerItem catHorror = new SecondaryDrawerItem().withIdentifier(6).withName("Horror").withLevel(2)
                .withIcon(GoogleMaterial.Icon.gmd_bug_report)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        new JSONAsyncTask(ClientHTTP.createURL("/genre/27/movies"), null).execute();
                        setActionBarName("Horror");
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
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
                        finish();
                        Intent switchIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(switchIntent);
                        lv.setVisibility(View.VISIBLE);
                        searchbox.setVisibility(View.INVISIBLE);
                        SpUtil.setPreferenceString(context, "email", "");
                        SpUtil.setPreferenceString(context, "password", "");
                        FirebaseAuth.getInstance().signOut();
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
                        profileItem,
                        logoutItem)
                .withAccountHeader(headerResult)
                .build();


        (new JSONAsyncTask(ClientHTTP.createURL("/movie/now_playing"), null)).execute();
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
            current_index = 0;
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
            System.out.println(result);
            try {
                jsonObject = new JSONObject(result);
                JSONArray movies = jsonObject.getJSONArray("results");
                titles = new String[movies.length()];
                images = new Bitmap[movies.length()];
                descriptions = new String[movies.length()];
                ratings = new String[movies.length()];
                genres = new String[movies.length()];
                original_descriptions = new String[movies.length()];

                for (int i = 0; i < movies.length(); i++) {
                    JSONObject obj = movies.getJSONObject(i);
                    String title = obj.getString("original_title");
                    String description = obj.getString("overview");
                    Double rating = obj.getDouble("vote_average");
                    Double popularity = obj.getDouble("popularity");
                    String path_to_jpg = obj.getString("poster_path");
                    Integer id = obj.getInt("id");
                    JSONArray genre_ids = obj.getJSONArray("genre_ids");
                    String photoURLString = ClientHTTP.createPhotoURL(path_to_jpg);
                    System.out.println(photoURLString);


                    HashMap<String, String> movieInfo = new HashMap<>();
                    movieInfo.put("title", title);
                    movieInfo.put("description", description);
                    movieInfo.put("rating", "Average rating: " + Double.toString(rating));
                    movieInfos.add(movieInfo);
                    titles[i] = title;

                    original_descriptions[i] = new String(description);

                    int description_length = description.length();
                    int stop_index = 170;
                    if(description_length >= 170)
                        for(int j = 150; j < 170; j++)
                            if(description.charAt(j) == ' ' || description.charAt(j) == '.')
                                stop_index = j;

                    if(description_length > 170) {
                        description = description.substring(0, stop_index);
                        descriptions[i] = description + " [...]";
                    } else
                        descriptions[i] = description;


                    ratings[i] = "Rating: " + Double.toString(rating);
                    genres[i] = "";
                    for (int j = 0; j < Math.min(genre_ids.length(), 3); j++) {
                        genres[i] += movieList.getGenre(genre_ids.getInt(j));
                        if (j != Math.min(genre_ids.length(), 3) - 1)
                            genres[i] += ", ";
                    }

                    new ImageAsync(photoURLString).execute();

                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public class ImageAsync extends AsyncTask<String, Void, Bitmap> {
            String photoURLString;

            public ImageAsync(String photoURLString) {
                this.photoURLString = photoURLString;
            }

            @Override
            protected Bitmap doInBackground(String... urls) {
                try {
                    java.net.URL photoURL = new java.net.URL(photoURLString);
                    HttpURLConnection connection = (HttpURLConnection) photoURL
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                images[current_index] = result;
                current_index++;

                CustomList adapter = new CustomList(MainActivity.this, titles, images, descriptions, ratings, genres);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> av, View v, final int i, long l) {
                        new MaterialDialog.Builder(v.getContext())
                                .title(titles[i])
                                .content(original_descriptions[i])
                                .negativeText("Close")
                                .positiveText("Add to watchlist")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        MovieInfo movie = new MovieInfo(titles[i], descriptions[i], ratings[i], genres[i], images[i]);
                                        Wishlist.getInstance().addMovie(movie);
                                        System.out.println(Wishlist.getInstance().size());

                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), titles[i] + " was added to your watchlist!",
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

                if(pDialog.isShowing())
                    pDialog.dismiss();

            }

        }
    }

}