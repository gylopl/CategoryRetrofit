package makdroid.categoryretrofit.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.sql.SQLException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import makdroid.categoryretrofit.CategoryRetrofitApplication;
import makdroid.categoryretrofit.R;
import makdroid.categoryretrofit.model.Category;

public class CategoryDetailsFragment extends Fragment {
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String FRAGMENT_TAG = "CategoryDetailsFragment";
    private static final String BASE_URL_IMAGE = "http://demo.gopos.pl/";

    @Inject
    Dao<Category, Integer> categoryDao;
    private Category category;

    @Bind(R.id.category_name)
    TextView mTvCategoryName;
    @Bind(R.id.category_description)
    EditText mEtCategoryDescription;
    @Bind(R.id.image_category_avatar)
    ImageView mCategoryAvatar;

    public CategoryDetailsFragment() {
    }

    public static CategoryDetailsFragment newInstance(int categoryId) {
        CategoryDetailsFragment fragment = new CategoryDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDependencyInjector();
        if (getArguments() != null) {
            int categoryId = getArguments().getInt(EXTRA_CATEGORY_ID);
            try {
                category = categoryDao.queryForId(categoryId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDependencyInjector() {
        CategoryRetrofitApplication application = (CategoryRetrofitApplication) getActivity().getApplication();
        application.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (category != null) {
            mTvCategoryName.setText(category.getName());
            mEtCategoryDescription.setText(category.getDescription());
            loadImageFromUrl();
        }
    }

    @OnClick(R.id.btn_save)
    public void saveCategory() {
        category.setDescription(mEtCategoryDescription.getText().toString());
        try {
            categoryDao.createOrUpdate(category);
            Toast.makeText(getActivity(), R.string.message_saved, Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromUrl() {
        final String url = BASE_URL_IMAGE + category.getImageUrl();
//        Picasso.with(getActivity()).load(url).memoryPolicy(MemoryPolicy.NO_CACHE).into(mTarget);
        Picasso.with(getActivity()).load(url).into(mTarget);
    }

    final Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mCategoryAvatar.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.v("BITMAP", "LOAD FAILED");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
