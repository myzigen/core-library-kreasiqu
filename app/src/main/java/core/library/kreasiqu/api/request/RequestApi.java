package core.library.kreasiqu.api.request;

import android.app.Activity;
import core.library.kreasiqu.api.connect.ApiConnected;
import core.library.kreasiqu.api.interfaces.PostCallback;
import core.library.kreasiqu.api.response.read.ResImageSlide;
import retrofit2.Call;


public class RequestApi extends PostAbstract {

  public RequestApi(Activity activity) {
    this.activity = activity;
  }

  // ----- Request Tampilkan ImageSlider
  public void getReadImageSlide(PostCallback callback) {
    ApiConnected<ResImageSlide> api = new ApiConnected<>(activity);
    Call<ResImageSlide> call = api.getEndpoint().endpointListImageSlider(idAgen);
    execute(call, callback);
  }
}
