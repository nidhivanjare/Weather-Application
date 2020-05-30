package android.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView cityName ;
    Button searchButton;
    TextView result;

    class Weather extends AsyncTask<String , Void , String> {// 1. String -> URL , 2. void -> nothing , 3. String ->return type will be string




        @Override
        protected String doInBackground(String... address) {
//String means address can be sent . It acts as an array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Establish connection with address
                connection.connect();

                //retrieve data from url
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //Retrieve data and return it as string
                int data = isr.read();
                String content =  "";
                char ch;
                while (data!= -1)
                {
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void search(View view){

        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);

        String cName = cityName.getText().toString();

        String content ;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="
                    +cName+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();

            //check data is retrieved properly or not
            Log.i("connect",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");// not part of weather array
            String mainPressure = jsonObject.getString("main");
            String mainHumidity = jsonObject.getString("main");
            Double visibility;

            Log.i("weatherData",weatherData);


            //weather data is in array
            JSONArray array = new JSONArray(weatherData);

            String main = "";
            String description = "";
            String temperature = "";
            String pressure = "";
            String humidity = "";


            for (int i = 0 ; i<array.length() ; i++)
            {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description=weatherPart.getString("description");

            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");

            JSONObject mainPart1 = new JSONObject(mainPressure);
            pressure = mainPart1.getString("pressure");

            JSONObject mainPart2 = new JSONObject(mainHumidity);
            humidity = mainPart2.getString("humidity");


            visibility = Double.parseDouble(jsonObject.getString("visibility"));

               Log.i("Temperature",temperature);
               Log.i("Pressure",pressure);
               Log.i("Humidity",humidity);

               // Log.i("main",main);
           //  Log.i("main",description);




            result.setText("Weather : "+main+"\nDescription : "+description+"\nTemperature : "
                    +temperature+" °c"+"\nVisibility : "+visibility+" m"+"\nPressure : "+pressure+" mb"+"\nHumidity : "+humidity+" %");

            //show result on screen


        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





    }
}
