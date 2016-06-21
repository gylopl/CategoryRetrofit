package makdroid.categoryretrofit.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import makdroid.categoryretrofit.CategoryRetrofitApplication;
import makdroid.categoryretrofit.database.prefs.UserPrefMaster;
import makdroid.categoryretrofit.model.Category;
import makdroid.categoryretrofit.model.CategoryMapper;
import makdroid.categoryretrofit.otto.DataDownloadSuccessEvent;
import makdroid.categoryretrofit.services.categoryResponse.ResponseCategory;
import retrofit2.Call;

public class DownloadIntentService extends IntentService {
    private static final String USER = "test@gmail.com";
    private static final String PASSWORD = "test";
    private static final String CLIENT_ID = "06dff5d1-07b1-40f9-80bd-625db272f8da";
    private static final String CLIENT_SECRET = "b5847be0-caf5-483f-a234-757977fa7f37";
    private static final String GRANT_TYPE = "password";

    @Inject
    RetrofitService retrofitService;
    @Inject
    Dao<Category, Integer> categoryDao;
    @Inject
    UserPrefMaster userPrefMaster;
    @Inject
    Bus bus;

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CategoryRetrofitApplication application = (CategoryRetrofitApplication) getApplication();
        application.getAppComponent().inject(this);
        bus.register(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Call<TokenResponse> token = retrofitService.getToken(USER, PASSWORD, CLIENT_ID, CLIENT_SECRET, GRANT_TYPE);//synchroniczne, i tak jestesmy w innym watku
            TokenResponse tokenResponse = token.execute().body();
            Call<ResponseCategory> categories = retrofitService.getCategories(tokenResponse.accessToken);
            ResponseCategory responseCategory = categories.execute().body();
            List<Category> categoryList = CategoryMapper.mapToCategory(responseCategory);
            for (int i = 0; i <= 5; i++) {
                for (Category c : categoryList) {
                    categoryDao.create(c);
                }
            }
            userPrefMaster.setDataDownloaded();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    bus.post(new DataDownloadSuccessEvent("Data downloaded"));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
    }
}
