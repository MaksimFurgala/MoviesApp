package com.example.moviesapp.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesapp.Adapters.ActorListAdapter;
import com.example.moviesapp.Adapters.CategoryEachFilmAdapter;
import com.example.moviesapp.Domains.Film;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.ActivityFilmDetailBinding;

import eightbitlab.com.blurview.RenderScriptBlur;

public class FilmDetailActivity extends AppCompatActivity {

    private ActivityFilmDetailBinding binding;
    private static final float radiusBlur = 10f;
    private static final String filmByIntent = "film";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilmDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Парсинг данных из intent, настройка imageView для постера, настройка blurView,
        recyclerView для списка жанров и актеров*/
        parseFilmDetailsSetupListeners();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * Парсинг данных из intent'а.
     */
    private void parseFilmDetailsSetupListeners() {
        // Получаем объект фильм из параметров intent'а
        Film film = (Film) getIntent().getSerializableExtra(filmByIntent);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new GranularRoundedCorners(0, 0, 0, 50));

        Glide.with(this)
                .load(film.getPoster())
                .apply(requestOptions)
                .into(binding.filmPoster);

        binding.titleTxt.setText(film.getTitle());
        binding.kinopoiskTxt.setText(String.format(getString(R.string.kinopoisk_rate), film.getKinopoisk()));
        binding.filmTimesTxt.setText(String.format(getString(R.string.film_times), film.getYear(), film.getTime()));
        binding.filmSummary.setText(film.getDescription());

        // Добавление слушателей для кнопок.
        setupListeners(film);

        // Получаем задний план окна для использования его в blurView.
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        binding.blurView.setupWith(rootView, new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radiusBlur);
        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView.setClipToOutline(true);

        //region Настройка recyclerView для жанров и актеров если есть соответствующие коллекции.
        if (film.getGenre() != null) {
            binding.genreView.setAdapter(new CategoryEachFilmAdapter(film.getGenre()));
            binding.genreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        if (film.getCasts() != null) {
            binding.actorsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.actorsView.setAdapter(new ActorListAdapter(film.getCasts()));
        }
        //endregion
    }

    /**
     * Добавление слушателей для кнопок на карточке фильма.
     *
     * @param film - фильм
     */
    private void setupListeners(Film film) {
        // Просмотр трейлера на YouTube через приложение или сайт.
        binding.watchTrailerBtn.setOnClickListener(v -> {
            String idTrailer = film.getTrailer().replace(getString(R.string.youtube_url), "");
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vnd_youtube) + idTrailer));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(film.getTrailer()));

            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        });

        // Назад к списку фильмов (в главный экран).
        binding.backImg.setOnClickListener(v -> finish());
    }
}