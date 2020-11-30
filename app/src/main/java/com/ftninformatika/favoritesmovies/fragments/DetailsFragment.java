package com.ftninformatika.favoritesmovies.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.ftninformatika.favoritesmovies.MainActivity;
import com.ftninformatika.favoritesmovies.R;
import com.ftninformatika.favoritesmovies.model.Movie;
import com.ftninformatika.favoritesmovies.webservice.RESTService;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {
    //1. Odredimo atribute
    private String ImdbID;

    private TextView etTitle, etDirector, etActors, etWriter;
    private ImageView imageView;

    private Movie movie;


    public static final int NOTIF_ID = 101;
    public static final String NOTIF_CHANNEL_ID = "nas_notif_kanal";


    public DetailsFragment() {
    }

    public void setImdbID(String imdbID) {
        ImdbID = imdbID;
    }


    private void showMovies(Movie movie) {

        this.movie=movie;

        etTitle.setText(movie.getTitle());
        etDirector.setText(movie.getDirector());
        etWriter.setText(movie.getWriter());
        etActors.setText(movie.getActors());

        Picasso.get().load(movie.getPoster()).into(imageView);
    }

    private void getMoviesByID() {
        HashMap<String, String> query = new HashMap<>();
        query.put("apikey", RESTService.API_KEY);
        query.put("i", ImdbID);

        Call<Movie> call = RESTService.apiInterface().getMoviesDetails(query);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if (response.code() == 200) {

                    showMovies(response.body());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, "Nas Notif Kanal", importance);
            channel.setDescription("Opis naseg kanala");
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        setHasOptionsMenu(true);

        createNotificationChannel();

        etTitle = view.findViewById(R.id.textView_Title);
        etWriter = view.findViewById(R.id.textView_Writer);
        etDirector = view.findViewById(R.id.textView_Director);
        etActors = view.findViewById(R.id.textView_Actors);


        imageView = view.findViewById(R.id.imageView_Poster);

        getMoviesByID();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                doAddElement();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNotification(boolean exist) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), NOTIF_CHANNEL_ID);
        if (exist) {
            builder.setContentTitle(getString(R.string.Favorites))
                    .setContentText(getString(R.string.add_favorites_unsuccess))
                    .setSmallIcon(R.drawable.save_notification);
        } else {
            builder.setContentTitle(getString(R.string.Favorites))
                    .setContentText(getString(R.string.add_favorites_success))
                    .setSmallIcon(R.drawable.save_notification);
        }
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID, builder.build());
    }

    private void doAddElement() {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean enabledNotifications = sharedPreferences.getBoolean("notification_settings", true);

            List<Movie> moviesindb = ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().queryForAll();
            boolean exist = false;
            for (int i = 0; i < moviesindb.size(); i++) {
                if (moviesindb.get(i).getImdbID().equals(ImdbID)) {
                    exist = true;
                    break;
                }
            }

            if (enabledNotifications) {
                //ovde obvezno pozovemo showNotification
                showNotification(exist);
            }

            if (exist) {
                if (!enabledNotifications) {
                    Toast.makeText(getActivity(), getString(R.string.add_favorites_unsuccess), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!enabledNotifications) {
                    Toast.makeText(getActivity(), getString(R.string.add_favorites_success), Toast.LENGTH_SHORT).show();
                }
                ((MainActivity) getActivity()).getDatabaseHelper().getMovieDao().create(movie);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}