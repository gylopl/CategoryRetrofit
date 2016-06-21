package makdroid.categoryretrofit.model;

import java.util.ArrayList;
import java.util.List;

import makdroid.categoryretrofit.services.categoryResponse.ResponseCategory;
import makdroid.categoryretrofit.services.categoryResponse.Row;

/**
 * Created by Grzecho on 19.06.2016.
 */
public class CategoryMapper {

    public static List<Category> mapToCategory(ResponseCategory responseCategory) {
        List<Category> categoryList = new ArrayList<>();
        if (responseCategory != null && responseCategory.rows != null) {
            for (Row row : responseCategory.rows) {
                Category category = new Category();
                category.setName(row.name);
                if (row.image != null && row.image.filePath != null)
                    category.setImageUrl(row.image.filePath);
                if (row.image != null && row.image.filePathSmall != null)
                    category.setImageUrlSmall(row.image.filePathSmall);
                categoryList.add(category);
            }
        }
        return categoryList;
    }
}
