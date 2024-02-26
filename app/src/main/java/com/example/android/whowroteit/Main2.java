package com.example.android.whowroteit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Main2 extends AppCompatActivity {

    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize all the view variables
        mBookInput = (EditText)findViewById(R.id.bookInput);
        mTitleText = (TextView)findViewById(R.id.titleText);
        mAuthorText = (TextView)findViewById(R.id.authorText);
    }

    public void retrofitCall(String queryString){
        Retrofit retrofit = RetrofitClient.getClient();

        // Create the API interface
        BookApiService bookApi = retrofit.create(BookApiService.class);

        // Make the API call
        Call<JsonElement> call = bookApi.getBookInfo(queryString, 10, "books");

        // Execute the call asynchronously
        call.enqueue(new Callback<JsonElement>() {


            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement bookJSONString = response.body();
                Log.i("Nothing","Du lieu:"+bookJSONString);

                try {
                    String jsonString = bookJSONString.toString();
                    // Convert the response into a JSON object.
                    JSONObject jsonObject = new JSONObject(jsonString);
                    // Get the JSONArray of book items.
                    JSONArray itemsArray = jsonObject.getJSONArray("items");

                    // Initialize iterator and results fields.
                    int i = 0;
                    String title = null;
                    String authors = null;

                    // Look for results in the items array, exiting when both the title and author
                    // are found or when all items have been checked.
                    while (i < itemsArray.length() || (authors == null && title == null)) {
                        // Get the current item information.
                        JSONObject book = itemsArray.getJSONObject(i);
                        JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                        // Try to get the author and title from the current item,
                        // catch if either field is empty and move on.
                        try {
                            title = volumeInfo.getString("title");
                            authors = volumeInfo.getString("authors");
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        // Move to the next item.
                        i++;
                    }

                    // If both are found, display the result.
                    if (title != null && authors != null){
                        mTitleText.setText(title);
                        mAuthorText.setText(authors);
                        mBookInput.setText("");
                    } else {
                        // If none are found, update the UI to show failed results.
                        mTitleText.setText(R.string.no_results);
                        mAuthorText.setText("");
                    }

                } catch (Exception e){

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
                // Handle failure
                Log.e("ERROR","Loi call api:"+t.toString());
            }
        });
    }

    public void searchBooks(View view) {
        // Get the search string from the input field.
        String queryString = mBookInput.getText().toString();

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty,
        // add the search term to the arguments Bundle and start the loader.
        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
            mAuthorText.setText("");
            mTitleText.setText(R.string.loading);
            retrofitCall(queryString);
        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_search_term);
            } else {
                mAuthorText.setText("");
                mTitleText.setText(R.string.no_network);
            }
        }
    }
}
