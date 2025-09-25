package core.library.kreasiqu.api.connect;

import android.app.Activity;
import core.library.kreasiqu.api.endpoint.MyEndpoint;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConnected<T> {
  private Activity activity;
  private MyEndpoint endpoint;
  private OkHttpClient client;
  private HttpLoggingInterceptor interceptor;

  public ApiConnected(Activity activity) {
    this.activity = activity;
    interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
    client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://kreasi.qiospro.my.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .client(client)
            .build();

    endpoint = retrofit.create(MyEndpoint.class);
  }

  public void executeClient(Call<T> call, Callback<T> callback) {
    callback.onApiRequest();

    call.enqueue(
        new retrofit2.Callback<T>() {
          @Override
          public void onResponse(Call<T> call, Response<T> response) {
            new android.os.Handler(android.os.Looper.getMainLooper())
                .post(
                    () -> {
                      if (response.isSuccessful() && response.body() != null) {
                        callback.onApiValue(response.body());
                      } else {
                        callback.onApiError("Response gagal: " + response.code(), false);
                      }
                    });
          }

          @Override
          public void onFailure(Call<T> call, Throwable t) {
            new android.os.Handler(android.os.Looper.getMainLooper())
                .post(
                    () -> {
                      boolean isTimeout = t instanceof java.net.SocketTimeoutException;
                      callback.onApiError(t.getLocalizedMessage(), isTimeout);
                    });
          }
        });
  }

  public MyEndpoint getEndpoint() {
    return endpoint;
  }

  public void setLogLevel(HttpLoggingInterceptor.Level level) {
    interceptor.setLevel(level != null ? level : HttpLoggingInterceptor.Level.NONE);
  }

  public interface Callback<T> {
    void onApiRequest();

    void onApiValue(T data);

    void onApiError(String error, boolean timeout);
  }
}
