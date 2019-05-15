package root.ivatio.network.api;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import root.ivatio.bd.users.User;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.ServerAnswerDTO;

public interface IvorJavaServerAPI {
    @POST("/api/login")
    Single<ServerAnswerDTO<User>> login(@Body User user);

    @POST("/api/register")
    Single<ServerAnswerDTO<EmptyDTO>> register(@Body User user);
}
