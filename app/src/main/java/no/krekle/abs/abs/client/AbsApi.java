package no.krekle.abs.abs.client;

import android.util.Log;

import no.krekle.abs.abs.driver.DriveLog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.mime.TypedByteArray;

/**
 * Created by krekle on 02/03/16.
 */
public class AbsApi {
    // Base url
    private static String BASE_URL = "http://folk.ntnu.no/audunlib/bil";

    public interface Abs {
        @GET("/insert.php?user=1,2&trip=4,5&timestamp=333333333,444444444&speed=10,11")
        void test(Callback<Response> callback);

        @POST("/insert")
        void insert(
                @Body DriveLog log,
                Callback<Response> callback);
    }


    // Singleton fields
    private RestAdapter restAdapter;
    private Abs apiService;

    public void test(final ABSCallback callback) {

        // Service started callback
        callback.inProgress();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();
        apiService = restAdapter.create(Abs.class);
        apiService.test(new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                Log.v("CALLBACK", "Success");
                callback.success(response);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("CALLBACK", "Failure");
                callback.failure(error);
            }
        });
    }

    public void insert(DriveLog log, final ABSCallback callback) {

        // Service started callback
        callback.inProgress();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();
        apiService = restAdapter.create(Abs.class);
        apiService.insert(log, new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                Log.v("DATA", "TEST");
                Log.v("DATA", new String(((TypedByteArray) response.getBody()).getBytes()));


                callback.success(response);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.v("CALLBACK", "Failure");
                callback.failure(error);
            }
        });

    }
}
