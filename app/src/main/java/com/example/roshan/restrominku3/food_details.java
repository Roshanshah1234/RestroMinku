package com.example.roshan.restrominku3;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.roshan.restrominku3.Database.Database;
import com.example.roshan.restrominku3.Model.Food;
import com.example.roshan.restrominku3.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class food_details extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference food;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        //firebase
        database=FirebaseDatabase.getInstance();
        food=database.getReference("Food");

        //Init View
        numberButton=(ElegantNumberButton)findViewById(R.id.number_button);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                    foodId,
                    currentFood.getName(),
                    numberButton.getNumber(),
                    currentFood.getPrice(),
                    currentFood.getDiscount()
                ));
                Toast.makeText(food_details.this,"Added To Cart",Toast.LENGTH_SHORT).show();
            }
        });




        food_description=(TextView)findViewById(R.id.food_description);
        food_price=(TextView)findViewById(R.id.food_name);
        food_name=(TextView)findViewById(R.id.food_name);
        food_image=(ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get food Id from Intent

        if(getIntent() !=null)
            foodId=getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
            getDetailFood(foodId);
        }

    }

    private void getDetailFood(String foodId) {

        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentFood=dataSnapshot.getValue(Food.class);

                //Set Image

                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
