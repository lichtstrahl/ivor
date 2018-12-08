package root.ivatio.network.api;

import java.util.List;

import io.reactivex.Observable;
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
    @POST("/api/answers/replaceOrCreate")
    Observable<Answer> replaceAnswer(@Body Answer answer);
    @POST("/api/answers")
    Observable<Answer> insertAnswer(@Body PostContentDTO answer);


    @GET("/api/commands")
    Observable<List<Command>> loadCommands();
    @POST("api/commands/replaceOrCreate")
    Observable<Command> replaceCommand(@Body Command command);
    @POST("/api/commands")
    Observable<Command> insertCommand(@Body PostCommandDTO cmd);


    @GET("/api/communications")
    Observable<List<Communication>> loadCommunications();
    @POST("/api/communications/replaceOrCreate")
    Observable<Communication> replaceCommunication(@Body Communication communication);
    @POST("/api/communications")
    Observable<Communication> insertCommunication(@Body PostComDTO com);

    @GET("/api/communicationkeys")
    Observable<List<CommunicationKey>> loadCommunicationKeys();
    @POST("/api/communicationkeys/replaceOrCreate")
    Observable<CommunicationKey> replaceCommunicationKey(@Body CommunicationKey communicationKey);
    @POST("/api/communicationkeys")
    Observable<CommunicationKey> insertCommunicationKey(@Body PostComKeyDTO comKey);

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();
    @POST("/api/keywords/replaceOrCreate")
    Observable<KeyWord> replaceKeyWord(@Body KeyWord word);
    @POST("/api/keywords")
    Observable<KeyWord> insertKeyWord(@Body PostContentDTO word);

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
    @POST("/api/questions/replaceOrCreate")
    Observable<Question> replaceQuestion(@Body Question question);
    @POST("/api/questions")
    Observable<Question> insertQuestion(@Body PostContentDTO question);
}
