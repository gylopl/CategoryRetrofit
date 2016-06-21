package makdroid.categoryretrofit.dagger;

import javax.inject.Singleton;

import dagger.Component;
import makdroid.categoryretrofit.ui.MainActivity;
import makdroid.categoryretrofit.database.prefs.UserPrefMaster;
import makdroid.categoryretrofit.services.DownloadIntentService;
import makdroid.categoryretrofit.ui.CategoryDetailsFragment;

/**
 * Created by Grzecho on 19.06.2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class, DataBaseModule.class, PrefsModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(DownloadIntentService service);

    void inject(CategoryDetailsFragment fragment);

    UserPrefMaster userPrefMaster();

}
