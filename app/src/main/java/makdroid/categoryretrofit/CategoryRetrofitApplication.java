package makdroid.categoryretrofit;

import android.app.Application;

import makdroid.categoryretrofit.dagger.AppComponent;
import makdroid.categoryretrofit.dagger.AppModule;
import makdroid.categoryretrofit.dagger.DaggerAppComponent;
import makdroid.categoryretrofit.dagger.DataBaseModule;
import makdroid.categoryretrofit.dagger.NetModule;
import makdroid.categoryretrofit.dagger.PrefsModule;

/**
 * Created by Grzecho on 19.06.2016.
 */
public class CategoryRetrofitApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .prefsModule(new PrefsModule())
                .dataBaseModule(new DataBaseModule())
                .netModule(new NetModule("http://demo.gopos.pl"))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}