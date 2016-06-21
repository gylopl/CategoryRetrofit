package makdroid.categoryretrofit.otto;

/**
 * Created by Grzecho on 19.06.2016.
 */
public class DataDownloadSuccessEvent {
    private String message;

    public DataDownloadSuccessEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
