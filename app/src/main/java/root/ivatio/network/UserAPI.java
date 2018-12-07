package root.ivatio.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import root.ivatio.activity.RegisterActivity;
import root.ivatio.bd.users.User;

public interface UserAPI {
    @GET("/api/clients")
    Call<List<User>> getUsers();

    @POST("/api/clients")
    Single<User> postUser(@Body RegisterActivity.PostUser user);
}
