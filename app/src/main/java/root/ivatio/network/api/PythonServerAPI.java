package root.ivatio.network.api;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import root.ivatio.bd.users.User;
import root.ivatio.network.dto.AnswerDTO;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.RequestDTO;
import root.ivatio.network.dto.ServerAnswerDTO;

public interface PythonServerAPI {
    @POST("/api/login")
    Single<ServerAnswerDTO<User>> login(@Body User user);

    @POST("/api/register")
    Single<ServerAnswerDTO> register(@Body User user);

    @POST("/api/request")
    Single<ServerAnswerDTO<AnswerDTO>> request(@Body RequestDTO request);
}
