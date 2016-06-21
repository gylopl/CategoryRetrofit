package makdroid.categoryretrofit.services;

import makdroid.categoryretrofit.services.categoryResponse.ResponseCategory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Grzecho on 14.06.2016.
 */
public interface RetrofitService {
    @Headers({
            "Accept: application/json"
    })
    @GET("oauth/token")
    Call<TokenResponse> getToken(@Query("username") String username, @Query("password") String password,
                                 @Query("client_id") String clientId, @Query("client_secret") String clientSecret,
                                 @Query("grant_type") String grantType);

    @GET("api/mobile/venue/5/productCategory")
    Call<ResponseCategory> getCategories(@Query("access_token") String accessToken);
}
