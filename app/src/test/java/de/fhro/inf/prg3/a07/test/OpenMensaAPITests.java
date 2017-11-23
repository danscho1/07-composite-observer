package de.fhro.inf.prg3.a07.test;

import android.text.format.DateUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import de.fhro.inf.prg3.a07.api.OpenMensaAPI;
import de.fhro.inf.prg3.a07.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by Peter Kurfer on 11/19/17.
 */

public class OpenMensaAPITests {

    private static final Logger logger = Logger.getLogger(OpenMensaAPITests.class.getName());
    private OpenMensaAPI openMensaAPI;

    @BeforeEach
    public void setup() {
        // use this to intercept all requests and output them to the logging facilities
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://openmensa.org/api/v2/")
                .client(client)
                .build();

        openMensaAPI = retrofit.create(OpenMensaAPI.class);
    }

    @Test
    public void testGetMeals() throws IOException {
        List<Meal> meals =  openMensaAPI.getMeals("2017-11-22").execute().body();

        assertNotNull(meals);
        assertNotEquals(0, meals.size());

        for(Meal m : meals){
            logger.info(m.toString());
        }
    }

}
