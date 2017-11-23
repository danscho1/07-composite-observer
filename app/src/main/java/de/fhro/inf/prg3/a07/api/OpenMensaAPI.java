package de.fhro.inf.prg3.a07.api;

import android.telecom.Call;

import java.util.List;

import de.fhro.inf.prg3.a07.model.Meal;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Peter Kurfer on 11/19/17.
 */

public interface OpenMensaAPI {
    // example request: GET /canteens/229/days/2017-11-22/meals
    @GET("canteens/229/days/{date}/meals")
    retrofit2.Call<List<Meal>> getMeals(@Path("date") String date);
}
