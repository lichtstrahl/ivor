package root.ivatio.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import root.ivatio.bd.users.User;

public interface UserAPI {
    @GET("/api/clients")
    Call<List<User>> getUsers();
}
