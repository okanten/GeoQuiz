package solutions.kanten.geoquiz;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import solutions.kanten.geoquiz.models.Capital;
import solutions.kanten.geoquiz.models.Country;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Capital> capitalList = new ArrayList<>();
    private ArrayList<Capital> selectedCapitals = new ArrayList<>();
    private Country correctCountry;
    private ArrayList<Capital> alreadyAnswered = new ArrayList<>();

    private TextView txtCountry;
    private ImageView imgFlag;

    private final static int ID_AFRICA = 1;
    private final static int ID_AMERICA = 2;
    private final static int ID_ASIA = 3;
    private final static int ID_AUSTRALIA = 4;
    private final static int ID_EUROPE = 5;
    private int intentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        recyclerView = findViewById(R.id.recyclerView);

        imgFlag = findViewById(R.id.imgCountry);

        intentID = getIntent().getIntExtra("id", 0);


        txtCountry = findViewById(R.id.txtCountry);
        txtCountry.setText(String.format(Locale.getDefault(), getString(R.string.txtCapitalOf), ""));
        loadJson();
        refreshList();
    }

    private void loadJson() {
        InputStream inputStream = getResources().openRawResource(R.raw.countries);

        Scanner scanner = new Scanner(inputStream);

        StringBuilder builder = new StringBuilder();

        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        parseJson(builder.toString());
    }

    private void parseJson(String s) {

        try {
            JSONArray root = new JSONArray(s);

            for (int i = 0; i <= root.length(); i++) {
                JSONObject jsonObject = root.getJSONObject(i);
                if (checkCountryForCode(jsonObject)) {
                    String countryCode = (jsonObject.getString("country_code").equals("DO") ? "dodo" : jsonObject.getString("country_code"));
                    Country country = new Country(jsonObject.getString("name"), countryCode.toLowerCase());
                    String c = (!jsonObject.getString("capital").equals("null")) ? jsonObject.getString("capital") : jsonObject.getString("name");
                    Capital capital = new Capital(c, false, country);
                    capitalList.add(capital);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean checkCountryForCode(JSONObject jsonObject) throws JSONException {
        String continent;
        switch(intentID) {
            case ID_AFRICA:
                continent = "Africa";
                break;
            case ID_AMERICA:
                continent = "America";
                break;
            case ID_ASIA:
                continent = "Asia";
                break;
            case ID_AUSTRALIA:
                continent = "Asia";
                break;
            case ID_EUROPE:
                continent = "Europe";
                break;
            default:
                return true;
        }

        String[] objectContinent = jsonObject.getJSONArray("timezones").get(0).toString().split("/");

        return objectContinent[0].equals(continent);
    }

    public void refreshList() {

        int max = capitalList.size();

        if (max == alreadyAnswered.size()) {
            Toast.makeText(this, "Congrats! You've answered every capital correctly", Toast.LENGTH_SHORT).show();
            selectedCapitals.clear();
            mAdapter.notifyDataSetChanged();
        } else {
            int r = new Random().nextInt(max);

            correctCountry = capitalList.get(r).getCountry();
            Capital capital = capitalList.get(r);
            while(alreadyAnswered.contains(capital)) {
                r = new Random().nextInt(max);
                capital = capitalList.get(r);
                correctCountry = capitalList.get(r).getCountry();
            }
            alreadyAnswered.add(capital);
            selectedCapitals.clear();
            selectedCapitals.add(capital);
            for(int i = 0; i < 3; i++) {
                int incorrectRandom = new Random().nextInt(max);
                Capital newCapital = capitalList.get(incorrectRandom);
                while(incorrectRandom == r || selectedCapitals.contains(newCapital)) {
                    incorrectRandom = new Random().nextInt(max);
                    newCapital = capitalList.get(incorrectRandom);
                }
                selectedCapitals.add(newCapital);
            }
            Collections.shuffle(selectedCapitals);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new RecyclerViewAdapter(selectedCapitals, this, correctCountry);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(mLayoutManager);
            txtCountry.setText(String.format(Locale.getDefault(), getString(R.string.txtCapitalOf), correctCountry.getCountryName() + " (" + alreadyAnswered.size() + ")"));

            int resID = getResources().getIdentifier(correctCountry.getCountryCode(), "raw", getPackageName());

            try {
                InputStream in = getResources().openRawResource(resID);
                Drawable d = Drawable.createFromStream(in, "flag");

                imgFlag.setImageDrawable(d);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}
