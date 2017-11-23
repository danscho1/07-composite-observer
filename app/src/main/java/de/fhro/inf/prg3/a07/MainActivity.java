package de.fhro.inf.prg3.a07;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import de.fhro.inf.prg3.a07.api.OpenMensaAPI;
import de.fhro.inf.prg3.a07.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private OpenMensaAPI openMensaAPI;
    final String errmsg = "ERROR! Learn to use technology";
    List<Meal> retrievedMeals = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this will inflate the layout from res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        final ArrayAdapter mealArrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.meal_entry
        );

        final ListView lv = findViewById(R.id.mealsListView);
        lv.setAdapter(mealArrayAdapter);

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

        Button btn = findViewById(R.id.button_refresh);
        final CheckBox veggie = findViewById(R.id.checkbox_vegetarian);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMensaAPI.getMeals(sdf.format(new Date())).enqueue(new Callback<List<Meal>>() {
                    @Override
                    public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                        if(response.isSuccessful()){
                            mealArrayAdapter.clear();
                            retrievedMeals = response.body();
                            if(veggie.isChecked()){
                                List<Meal> vegetarian = new LinkedList<>();
                                for (Meal meal : retrievedMeals) if (!meal.toString().contains("fleisch")) vegetarian.add(meal);
                                mealArrayAdapter.addAll(vegetarian);
                            } else mealArrayAdapter.addAll(retrievedMeals);
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    errmsg,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Meal>> call, Throwable t) {
                        Toast.makeText(
                                MainActivity.this,
                                errmsg,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        });
        veggie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Meal> vegetarian = new LinkedList<>();
                for(Meal meal : retrievedMeals) if(!meal.toString().contains("fleisch")) vegetarian.add(meal);
                mealArrayAdapter.clear();
                mealArrayAdapter.addAll(vegetarian);
            }
        });
    }
}
