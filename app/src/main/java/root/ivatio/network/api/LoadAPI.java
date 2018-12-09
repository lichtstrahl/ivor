package root.ivatio.network.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.network.dto.PostComDTO;
import root.ivatio.network.dto.PostComKeyDTO;
import root.ivatio.network.dto.PostCommandDTO;
import root.ivatio.network.dto.PostContentDTO;

public interface LoadAPI {
    @GET("/api/answers")
    Observable<List<Answer>> loadAnswers();
    @POST("/api/answers/update")
    Observable<Answer> replaceAnswer(@Body Answer answer);
    @POST("/api/answers/insert")
    Observable<Answer> insertAnswer(@Body PostContentDTO answer);


    @GET("/api/commands")
    Observable<List<Command>> loadCommands();
    @POST("api/commands/update")
    Observable<Command> replaceCommand(@Body Command command);
    @POST("/api/commands/insert")
    Observable<Command> insertCommand(@Body PostCommandDTO cmd);


    @GET("/api/communications")
    Observable<List<Communication>> loadCommunications();
    @POST("/api/communications/update")
    Observable<Communication> replaceCommunication(@Body Communication communication);
    @POST("/api/communications/insert")
    Observable<Communication> insertCommunication(@Body PostComDTO com);
    @POST("/api/communications/delete")
    Observable<EmptyDTO> deleteCommunication(@Query("id") long id);

    @GET("/api/communicationkeys")
    Observable<List<CommunicationKey>> loadCommunicationKeys();
    @POST("/api/communicationkeys/update")
    Observable<CommunicationKey> replaceCommunicationKey(@Body CommunicationKey communicationKey);
    @POST("/api/communicationkeys/insert")
    Observable<CommunicationKey> insertCommunicationKey(@Body PostComKeyDTO comKey);
    @POST("/api/communicationkeys/delete")
    Observable<EmptyDTO> deleteCommunicationKey(@Query("id") long id);

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();
    @POST("/api/keywords/update")
    Observable<KeyWord> replaceKeyWord(@Body KeyWord word);
    @POST("/api/keywords/insert")
    Observable<KeyWord> insertKeyWord(@Body PostContentDTO word);
    @POST("/api/keywords/delete")
    Observable<EmptyDTO> deleteKeyWord(@Query("id") long id);

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
    @POST("/api/questions/update")
    Observable<Question> replaceQuestion(@Body Question question);
    @POST("/api/questions/insert")
    Observable<Question> insertQuestion(@Body PostContentDTO question);
    @POST("/api/questions/delete")
    Observable<EmptyDTO> deleteQuestion(@Query("id") long id);

    @POST("/api/selection")
    Observable<EmptyDTO> selection();
}
