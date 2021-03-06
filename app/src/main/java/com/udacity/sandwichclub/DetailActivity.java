package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView image;
    private TextView origin_label;
    private TextView origin;
    private TextView alsoKnown_label;
    private TextView alsoKnown;
    private TextView ingredients;
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        image = findViewById(R.id.image_iv);
        origin_label = findViewById(R.id.origin_label);
        origin = findViewById(R.id.origin_tv);
        alsoKnown_label = findViewById(R.id.also_known_label);
        alsoKnown = findViewById(R.id.also_known_tv);
        ingredients = findViewById(R.id.ingredients_tv);
        description = findViewById(R.id.description_tv);

//       Creating intent to pass data in the activity view

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

//      if data is not found return error message

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            closeOnError();
            return;
        }

//      Populating UI base on .get info

        setTitle(sandwich.getMainName());
        populateUI(sandwich);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();

    }

//    Hiding the previous Picasso call

//    private void populateUI(Sandwich sandwich) {
//        Picasso.with(this)
//                .load(sandwich.getImage())
//                .into(image);

    //  Displaying error message when image url does not load

    private void populateUI(Sandwich sandwich) {
        Picasso.with(this).load(sandwich.getImage()).fit().centerCrop()
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher_round)
            .into(image);


//       Setting up visibility that displays data from JSON file

        if (sandwich.getPlaceOfOrigin().isEmpty()) {

            origin_label.setVisibility(View.GONE);
            origin.setVisibility(View.GONE);

//       Adding reviewer's suggestion to set text for data not available

            origin.setText(R.string.error_message);

        } else {
            origin.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getAlsoKnownAs().isEmpty()) {

            alsoKnown_label.setVisibility(View.GONE);
            alsoKnown.setVisibility(View.GONE);

            alsoKnown.setText(R.string.error_message);

        } else {
            List<String> aka = sandwich.getAlsoKnownAs();
            String aka_str = TextUtils.join(",", aka);
            alsoKnown.setText(aka_str);

        }

        description.setText(sandwich.getDescription());

        List<String> ing = sandwich.getIngredients();
        String ing_str = TextUtils.join(",", ing);
        ingredients.setText(ing_str);

    }
}


