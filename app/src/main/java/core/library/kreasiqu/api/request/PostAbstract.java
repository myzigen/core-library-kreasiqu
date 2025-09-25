package core.library.kreasiqu.api.request;

import android.app.Activity;
import core.library.kreasiqu.api.connect.ApiConnected;
import core.library.kreasiqu.api.interfaces.PostCallback;
import core.library.kreasiqu.api.interfaces.PostInterfaces;
import retrofit2.Call;

public abstract class PostAbstract implements PostInterfaces {
  protected Activity activity;
  protected String idAgen;

  @Override
  public void postIdAgen(String idAgen) {
    this.idAgen = idAgen;
  }

  protected <T> void execute(Call<T> call, PostCallback callback) {
    new ApiConnected<T>(activity)
        .executeClient(
            call,
            new ApiConnected.Callback<T>() {
              @Override
              public void onApiRequest() {
                callback.onApiRequest();
              }

              @Override
              public void onApiValue(T data) {
                callback.onApiValue(data);
              }

              @Override
              public void onApiError(String error, boolean timeout) {
                callback.onApiError(error, timeout);
              }
            });
  }
}
