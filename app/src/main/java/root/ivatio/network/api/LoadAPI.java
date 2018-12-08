package root.ivatio.network.api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.network.dto.PostComDTO;
import root.ivatio.network.dto.PostComKeyDTO;
import root.ivatio.network.dto.PostCommandDTO;
import root.ivatio.network.dto.PostContentDTO;

public interface LoadAPI {
    @GET("/api/answers")
    Observable<List<Answer>> loadAnswers();
    @POST("/api/answers")
    Single<Answer> postAnswer(@Body PostContentDTO answer);

    @GET("/api/commands")
    Observable<List<Command>> loadCommands();
    @POST("api/commands")
    Single<Command> postCommand(@Body PostCommandDTO command);


    @GET("/api/communications")
    Observable<List<Communication>> loadCommunications();
    @POST("/api/communications")
    Single<Communication> postCommunication(@Body PostComDTO communication);

    @GET("/api/communicationkeys")
    Observable<List<CommunicationKey>> loadCommunicationKeys();
    @POST("/api/communicationkeys")
    Single<CommunicationKey> postCommunicationKey(@Body PostComKeyDTO communicationKey);

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();
    @POST("/api/keywords")
    Single<KeyWord> postKeyWord(@Body PostContentDTO word);

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
    @POST("/api/questions")
    Single<Question> postQuestio(@Body PostContentDTO question);
}
