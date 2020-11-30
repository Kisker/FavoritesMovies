package com.ftninformatika.favoritesmovies.ORMLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ftninformatika.favoritesmovies.model.Movie;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Movie, String> movieDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Movie.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Movie.class, true);
            TableUtils.createTable(connectionSource, Movie.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Dao<Movie, String> getMovieDao() throws SQLException {
        if (movieDao == null) {
            movieDao = getDao(Movie.class);
        }

        return movieDao;
    }
    //obavezno prilikom zatvarnja rada sa bazom osloboditi resurse. I uvek ovako ide.
    @Override
    public void close() {
        movieDao = null;

        super.close();
    }
}
