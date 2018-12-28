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
    @GET("/api/answers/byID")
    Observable<Answer> loadAnswerByID(@Query("id") long id);
    @POST("/api/answers/insert")
    Observable<Answer> insertAnswer(@Body PostContentDTO answer);


    @GET("/api/commands")
    Observable<List<Command>> loadCommands();

    @GET("/api/communications/communicationsForQuestion")
    Observable<List<Communication>> loadCommunicationsForQuestion(@Query("questionID") long id);
    @POST("/api/communications/update")
    Observable<Communication> replaceCommunication(@Body Communication communication);
    @POST("/api/communications/insert")
    Observable<Communication> insertCommunication(@Body PostComDTO com);
    @POST("/api/communications/delete")
    Observable<EmptyDTO> deleteCommunication(@Query("id") long id);

    @GET("/api/communicationkeys/communicationKeysForKeyWord")
    Observable<List<CommunicationKey>> loadCommunicationKeysForKeyWord(@Query("keyID") long id);
    @POST("/api/communicationkeys/update")
    Observable<CommunicationKey> replaceCommunicationKey(@Body CommunicationKey communicationKey);
    @POST("/api/communicationkeys/insert")
    Observable<CommunicationKey> insertCommunicationKey(@Body PostComKeyDTO comKey);
    @POST("/api/communicationkeys/delete")
    Observable<EmptyDTO> deleteCommunicationKey(@Query("id") long id);

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();
    @GET("/api/keywords/byID")
    Observable<KeyWord> loadKeyWordByID(@Query("id") long id);
    @POST("/api/keywords/insert")
    Observable<KeyWord> insertKeyWord(@Body PostContentDTO word);
    @POST("/api/keywords/delete")
    Observable<EmptyDTO> deleteKeyWord(@Query("id") long id);

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
    @GET("/api/questions/byID")
    Observable<Question> loadQuestionByID(@Query("id") long id);
    @POST("/api/questions/insert")
    Observable<Question> insertQuestion(@Body PostContentDTO question);
    @POST("/api/questions/delete")
    Observable<EmptyDTO> deleteQuestion(@Query("id") long id);

    @POST("/api/selection")
    Observable<EmptyDTO> selection();
}
