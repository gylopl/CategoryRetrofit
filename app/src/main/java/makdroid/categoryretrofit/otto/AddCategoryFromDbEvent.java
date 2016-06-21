package makdroid.categoryretrofit.otto;

import java.util.List;

import makdroid.categoryretrofit.model.Category;

/**
 * Created by Grzecho on 20.06.2016.
 */
public class AddCategoryFromDbEvent {
    private List<Category> categoryList;
    private int curSize;

    public AddCategoryFromDbEvent(List<Category> categoryList, int curSize) {
        this.categoryList = categoryList;
        this.curSize = curSize;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public int getCurSize() {
        return curSize;
    }
}
