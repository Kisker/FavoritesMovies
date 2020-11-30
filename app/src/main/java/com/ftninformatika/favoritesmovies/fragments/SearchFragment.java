package com.ftninformatika.favoritesmovies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ftninformatika.favoritesmovies.R;
import com.ftninformatika.favoritesmovies.adapters.SearchListViewAdapter;
import com.ftninformatika.favoritesmovies.model.Result;
import com.ftninformatika.favoritesmovies.model.Search;
import com.ftninformatika.favoritesmovies.webservice.RESTService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText etSearch;
    private Button bSearch;
    private ListView lvMovies;

    private onListItemClickListener listener;

    public SearchFragment() {
    }

    private void showMovies(List<Search> movies) {
        if (movies != null) {
            final SearchListViewAdapter adapter = new SearchListViewAdapter(getActivity(), movies);
            lvMovies.setAdapter(adapter);
            lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onListItemClicked(adapter.getItem(i).getImdbID());
                }
            });
        }
    }

    private void getMoviesByTitle(String title) {
        HashMap<String, String> query = new HashMap<>();
        query.put("apikey", RESTService.API_KEY);
        query.put("s", title);

        Call<Result> call = RESTService.apiInterface().getMoviesByTitle(query);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 200) {
                    showMovies(response.body().getSearch());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.editText_Search);
        bSearch = view.findViewById(R.id.buttonSearch);
        lvMovies = view.findViewById(R.id.listMovies);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSearch.getText().toString().equals("")) {
                    getMoviesByTitle(etSearch.getText().toString().trim());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.search_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onListItemClickListener) {
            listener = (onListItemClickListener) context;
        } else {
            Toast.makeText(getActivity(), "Morate implementirati interface", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface onListItemClickListener {
        void onListItemClicked(String ImdbID);
    }

}
