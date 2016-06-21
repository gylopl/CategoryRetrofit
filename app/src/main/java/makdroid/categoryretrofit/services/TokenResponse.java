package makdroid.categoryretrofit.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Grzecho on 19.06.2016.
 */
public class TokenResponse {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public int expiresIn;
    public String scope;
}