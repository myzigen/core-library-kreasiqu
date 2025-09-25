package core.library.kreasiqu.api.response.read;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResImageSlide {
  @SerializedName("status")
  private String status;

  @SerializedName("data")
  private List<ItemSlider> data;

  public String getStatus() {
    return status;
  }

  public List<ItemSlider> getData() {
    return data;
  }

  public static class ItemSlider {
    @SerializedName("id_image")
    private int id;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    private String action_type;

    private String action_value;

    public int getIdImage() {
      return id;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public String getTitle() {
      return title;
    }

    public String getDescription() {
      return description;
    }

    public String getActionType() {
      return action_type;
    }

    public String getActionValue() {
      return action_value;
    }
  }
}
