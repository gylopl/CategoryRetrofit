package makdroid.categoryretrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import makdroid.categoryretrofit.R;
import makdroid.categoryretrofit.utils.FragmentUtils;

public class CategoryDetailsActivity extends AppCompatActivity {
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadCategory(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadCategory(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        int categoryId = extras.getInt(EXTRA_CATEGORY_ID);
        if (savedInstanceState == null) {
            FragmentUtils.setFragment(this, CategoryDetailsFragment.newInstance(categoryId), R.id.mainViewCategory, CategoryDetailsFragment.FRAGMENT_TAG);
        }
    }

    public static void start(Context context, int categoryId) {
        Intent detailsCardVotingItent = new Intent(context, CategoryDetailsActivity.class);
        detailsCardVotingItent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        context.startActivity(detailsCardVotingItent);
    }

}
