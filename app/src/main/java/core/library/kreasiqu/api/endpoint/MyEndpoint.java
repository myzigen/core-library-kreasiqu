package core.library.kreasiqu.api.endpoint;

import core.library.kreasiqu.api.response.read.ResImageSlide;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyEndpoint {
  // ----- Menampilkan Image Slider -----
  @GET("editor/read/list_image_slider.php")
  Call<ResImageSlide> endpointListImageSlider(@Query("id_agen") String idAgen);
}
