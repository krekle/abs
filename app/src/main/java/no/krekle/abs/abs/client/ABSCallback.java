package no.krekle.abs.abs.client;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by krekle on 02/03/16.
 */
public interface ABSCallback {

    public void success(Response response);

    public void failure(RetrofitError response);

    public void inProgress();

}
