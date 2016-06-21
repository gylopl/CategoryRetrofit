package makdroid.categoryretrofit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import makdroid.categoryretrofit.R;
import makdroid.categoryretrofit.model.Category;
import makdroid.categoryretrofit.utils.FileUtil;


/**
 * Created by Grzecho on 21.04.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MemeViewHolder> {
    private static final String BASE_URL_IMAGE = "http://demo.gopos.pl/";
    private List<Category> mCategoryCollection;
    private Context mContext;
    Picasso mPicasso;

    public CategoryAdapter(List<Category> categoryCollection, Context context) {
        this.mCategoryCollection = categoryCollection;
        this.mContext = context;
        mPicasso = new Picasso.Builder(mContext).downloader(new OkHttp3Downloader(FileUtil.getDiskCacheDir(mContext), Integer.MAX_VALUE)).build();
    }

    @Override
    public MemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_row,
                        parent,
                        false);
        return new MemeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MemeViewHolder holder, final int position) {
        final Category category = mCategoryCollection.get(position);
        holder.name.setText(category.getName());
        final String url = BASE_URL_IMAGE + category.getImageUrlSmall();
        mPicasso.setIndicatorsEnabled(true);
        mPicasso.load(url).networkPolicy(NetworkPolicy.OFFLINE)
                .resize(50, 50).into(holder.categoryImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.v("Picasso", "FROM CACHE DISK");
            }

            @Override
            public void onError() {
                mPicasso
                        .load(url).resize(50, 50)
                        .into(holder.categoryImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.v("Picasso", "FROM NETWORK");
                            }

                            @Override
                            public void onError() {
                                Log.v("Picasso", "Could not fetch image");
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryCollection.size();
    }

    public Category getItem(int position) {
        return mCategoryCollection.get(position);
    }

    final static class MemeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.category_name)
        TextView name;
        @Bind(R.id.imageView)
        ImageView categoryImage;

        public MemeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
