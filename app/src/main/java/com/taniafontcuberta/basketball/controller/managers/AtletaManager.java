package com.taniafontcuberta.basketball.controller.managers;

import android.util.Log;

import com.taniafontcuberta.basketball.controller.services.AtletaService;
import com.taniafontcuberta.basketball.model.Atleta;
import com.taniafontcuberta.basketball.util.CustomProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AtletaManager {
    private static AtletaManager ourInstance;
    private List<Atleta> atletas;
    private Retrofit retrofit;
    private AtletaService athleteService;

    private AtletaManager() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        retrofit = new Retrofit.Builder()
                .baseUrl(CustomProperties.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))

                .build();

        athleteService = retrofit.create(AtletaService.class);
    }

    public static AtletaManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new AtletaManager();
        }

        return ourInstance;
    }


    public synchronized void getAllAthletes(final AtletaCallback athleteCallback) {
        Call<List<Atleta>> call = athleteService.getAllAthletes(UserLoginManager.getInstance().getBearerToken());

        call.enqueue(new Callback<List<Atleta>>() {
            @Override
            public void onResponse(Call<List<Atleta>> call, Response<List<Atleta>> response) {
                atletas = response.body();

                int code = response.code();

                if (response.isSuccess()) {
                    athleteCallback.onSuccess(atletas);
                } else {
                    athleteCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Atleta>> call, Throwable t) {
                Log.e("AthleteManager->", "getAllAthletes()->ERROR: " + t);

                athleteCallback.onFailure(t);
            }
        });
    }

    public Atleta getAthlete(String id) {
        for (Atleta atleta : atletas) {
            if (id.equals(atleta.getId().toString())) {
                return atleta;
            }
        }

        return null;
    }

    /* POST - CREATE PLAYER */

    public synchronized void createAthlete(final AtletaCallback athleteCallback,Atleta atleta) {
        Call<Atleta> call = athleteService.createAthlete(UserLoginManager.getInstance().getBearerToken(), atleta);
        call.enqueue(new Callback<Atleta>() {
            @Override
            public void onResponse(Call<Atleta> call, Response<Atleta> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    //playerCallback.onSuccess1(apuestas1x2);
                    Log.e("Atleta->", "createAthlete: OK" + 100);

                } else {
                    athleteCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Atleta> call, Throwable t) {
                Log.e("AthleteManager->", "createAthlete: " + t);
                athleteCallback.onFailure(t);
            }
        });
    }

    /* PUT - UPDATE Athlete */
    public synchronized void updateAthlete(final AtletaCallback athleteCallback, Atleta atleta) {
        Call <Atleta> call = athleteService.updateAthlete(UserLoginManager.getInstance().getBearerToken() ,atleta);
        call.enqueue(new Callback<Atleta>() {
            @Override
            public void onResponse(Call<Atleta> call, Response<Atleta> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    Log.e("Atleta->", "updateAthlete: OOK" + 100);

                } else {
                    athleteCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Atleta> call, Throwable t) {
                Log.e("PlayerManager->", "updatePlayer: " + t);

                athleteCallback.onFailure(t);
            }
        });
    }

    /* DELETE - DELETE PLAYER */
    public synchronized void deleteAthlete(final AtletaCallback athleteCallback, Long id) {
        Call <Void> call = athleteService.deleteAthlete(UserLoginManager.getInstance().getBearerToken() ,id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int code = response.code();

                if (code == 200 || code == 201) {
                    Log.e("Player->", "Deleted: OK");

                } else {
                    athleteCallback.onFailure(new Throwable("ERROR" + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AthleteManager->", "deleteAthlete: " + t);

                athleteCallback.onFailure(t);
            }
        });
    }

}
