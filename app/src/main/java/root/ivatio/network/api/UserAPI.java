package root.ivatio.network.api;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import root.ivatio.bd.users.User;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.ServerAnswerDTO;

public interface UserAPI {
    @GET("/api/clients")
    Call<List<User>> getUsers();

    @POST("/api/clients/insert")
    Single<User> postUser(@Body User user);

}
