package makdroid.categoryretrofit.dagger;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import makdroid.categoryretrofit.database.DataBaseHelper;
import makdroid.categoryretrofit.model.Category;

/**
 * Created by Grzecho on 19.06.2016.
 */
@Module
public class DataBaseModule {


    @Provides
    @Singleton
    OrmLiteSqliteOpenHelper provideDatabaseHelper(DataBaseHelper databaseHelper) {
        return databaseHelper;
    }

    @Provides
    @Singleton
    Dao<Category, Integer> provideDaoCategory(OrmLiteSqliteOpenHelper databaseHelper) {
        try {
            return databaseHelper.getDao(Category.class);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create DAO!");
        }
    }

//    @Provides
//    @Singleton
//    DatabaseManager provideDatabaseManager(SQLiteOpenHelper databaseHelper) {
//        return new DatabaseManager(databaseHelper);
//    }
}
