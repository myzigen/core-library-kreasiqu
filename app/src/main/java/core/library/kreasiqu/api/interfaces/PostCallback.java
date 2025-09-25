package core.library.kreasiqu.api.interfaces;

public interface PostCallback<T> {
  public void onApiRequest();

  public void onApiValue(T data);

  public void onApiError(String error, boolean timeout);
}
