package makdroid.categoryretrofit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import makdroid.categoryretrofit.adapters.CategoryAdapter;
import makdroid.categoryretrofit.adapters.EndlessRecyclerViewOnScrollListener;
import makdroid.categoryretrofit.adapters.RecyclerItemClickListener;
import makdroid.categoryretrofit.database.prefs.UserPrefMaster;
import makdroid.categoryretrofit.model.Category;
import makdroid.categoryretrofit.otto.AddCategoryFromDbEvent;
import makdroid.categoryretrofit.otto.DataDownloadSuccessEvent;
import makdroid.categoryretrofit.services.DownloadIntentService;
import makdroid.categoryretrofit.ui.CategoryDetailsActivity;
import makdroid.categoryretrofit.utils.AlertDialogBuilder;
import makdroid.categoryretrofit.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Inject
    Dao<Category, Integer> categoryDao;
    @Inject
    Bus bus;
    @Inject
    UserPrefMaster userPrefMaster;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private int visibleThreshold = 5;
    private long mCountOfCategory;
    private int mPageLimit;

    private CategoryAdapter adapter;
    private List<Category> categoriesDb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeDependencyInjector();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    private void initializeDependencyInjector() {
        CategoryRetrofitApplication application = (CategoryRetrofitApplication) getApplication();
        application.getAppComponent().inject(this);
    }

    private void init() {
        if (userPrefMaster.isDataDownloaded())
            initRecyclerView();
        else
            startDownload();
    }

    private void startDownload() {
        if (!NetworkUtils.checkNetworkConnection(this)) {
            String error = getString(R.string.error_no_internet);
            AlertDialogBuilder.createErrorDialog(error, this).show();
        } else {
            Intent intent = new Intent(this, DownloadIntentService.class);
            startService(intent);
        }
    }

    private void initRecyclerView() {
        countOfCategory();
        countPageLimit();
        initCategoriesFromDb();
        adapter = new CategoryAdapter(categoriesDb, getApplicationContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                int curSize = adapter.getItemCount();
                if (curSize < mCountOfCategory)
                    new DownloadCategoriesFromDbTask().execute(curSize);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext()) {
            @Override
            public void onCategoryClicked(MotionEvent e) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildLayoutPosition(view);
                startCategoryDetailsActivity(adapter.getItem(position).getId());
            }
        });
    }

    private void initCategoriesFromDb() {
        List<Category> query = new ArrayList<>();
        try {
            Long limitFirstDownload = Long.valueOf(mPageLimit + visibleThreshold);
            QueryBuilder<Category, Integer> limit = categoryDao.queryBuilder().limit(Long.valueOf(limitFirstDownload));
            query.addAll(categoryDao.query(limit.prepare()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        categoriesDb = new ArrayList<>(query);
    }

    private class DownloadCategoriesFromDbTask extends AsyncTask<Integer, Void, Void> {
        List<Category> query = new ArrayList<>();
        int curSize;

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                QueryBuilder<Category, Integer> queryBuilder =
                        categoryDao.queryBuilder();
                curSize = params[0];
                queryBuilder.offset(Long.valueOf(curSize)).limit(Long.valueOf(mPageLimit));
                query.addAll(categoryDao.query(queryBuilder.prepare()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            bus.post(new AddCategoryFromDbEvent(query, curSize));
        }
    }

    @Subscribe
    public void onDownloadCategoriesFromDbResult(AddCategoryFromDbEvent event) {
        Toast.makeText(this, "Lista zaktualizowanaa", Toast.LENGTH_LONG).show();
        categoriesDb.addAll(event.getCategoryList());
        adapter.notifyItemRangeInserted(event.getCurSize(), categoriesDb.size() - 1);
    }

    @Subscribe
    public void onServiceSuccessResult(DataDownloadSuccessEvent event) {
        initRecyclerView();
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void countOfCategory() {
        try {
            this.mCountOfCategory = categoryDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*obliczenie ile rekordow mozna wyswietlic na ekranie, wysokosc jednego rekordu 70dp*/
    private void countPageLimit() {
        int px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics()));
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        int margin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        this.mPageLimit = (height - actionBarHeight - margin) / px;
    }

    @NonNull
    private void startCategoryDetailsActivity(int categoryId) {
        CategoryDetailsActivity.start(this, categoryId);
    }

}
