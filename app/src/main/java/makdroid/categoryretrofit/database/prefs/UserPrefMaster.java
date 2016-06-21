package makdroid.categoryretrofit.database.prefs;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Grzecho on 24.05.2016.
 */
@Singleton
public class UserPrefMaster {

    private final BooleanPreference dataDownloaded;

    @Inject
    public UserPrefMaster(BooleanPreference dataDownloaded) {
        this.dataDownloaded = dataDownloaded;
    }

    public void setDataDownloaded() {
        this.dataDownloaded.set(true);
    }
    
    public boolean isDataDownloaded() {
        return dataDownloaded.get();
    }

}
