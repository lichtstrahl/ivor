package root.ivatio.network.api;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import root.ivatio.bd.users.User;
import root.ivatio.bd.users.UserPost;
import root.ivatio.network.dto.AnswerForQuestionDTO;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.EvaluationDTO;
import root.ivatio.network.dto.ContentDTO;
import root.ivatio.network.dto.ServerAnswerDTO;

public interface PythonServerAPI {
    @POST("/api/login")
    Single<ServerAnswerDTO<User>> login(@Body User user);

    @POST("/api/register")
    Single<ServerAnswerDTO> register(@Body UserPost user);

    @POST("/api/request")
    Single<ServerAnswerDTO<AnswerForQuestionDTO>> request(@Body ContentDTO request);

    @POST("/api/evaluation")
    Single<ServerAnswerDTO<EmptyDTO>> evaluation(@Body EvaluationDTO eval);

    @POST("/api/answers/insert")
    Single<ServerAnswerDTO<EmptyDTO>> insertAnswer(@Body ContentDTO answer);
}
