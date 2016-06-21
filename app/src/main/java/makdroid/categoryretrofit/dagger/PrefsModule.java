package makdroid.categoryretrofit.dagger;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import makdroid.categoryretrofit.database.prefs.BooleanPreference;
import makdroid.categoryretrofit.database.prefs.UserPrefMaster;


/**
 * Created by Grzecho on 24.05.2016.
 */
@Module
public class PrefsModule {

    private static String PREF_DATA_IS_DOWNLOADED = "PREF_DATA_IS_DOWNLOADED";

    @Provides
    @Singleton
    @PrefDataIsDownloaded
    BooleanPreference providePrefDataIsDownloaded(SharedPreferences preferences) {
        return new BooleanPreference(preferences, PREF_DATA_IS_DOWNLOADED);
    }

    @Provides
    @Singleton
    UserPrefMaster provideUserPrefMaster(@PrefDataIsDownloaded BooleanPreference userIsLogged) {
        return new UserPrefMaster(userIsLogged);
    }
}
