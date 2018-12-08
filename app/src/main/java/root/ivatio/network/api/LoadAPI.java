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

public interface LoadAPI {
    @GET("/api/answers")
    Observable<List<Answer>> loadAnswers();
    @POST("/api/answers/replaceOrCreate")
    Observable<Answer> postAnswer(@Body Answer answer);

    @GET("/api/commands")
    Observable<List<Command>> loadCommands();
    @POST("api/commands/replaceOrCreate")
    Observable<Command> postCommand(@Body Command command);


    @GET("/api/communications")
    Observable<List<Communication>> loadCommunications();
    @POST("/api/communications/replaceOrCreate")
    Observable<Communication> postCommunication(@Body Communication communication);

    @GET("/api/communicationkeys")
    Observable<List<CommunicationKey>> loadCommunicationKeys();
    @POST("/api/communicationkeys/replaceOrCreate")
    Observable<CommunicationKey> postCommunicationKey(@Body CommunicationKey communicationKey);

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();
    @POST("/api/keywords/replaceOrCreate")
    Observable<KeyWord> postKeyWord(@Body KeyWord word);

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
    @POST("/api/questions/replaceOrCreate")
    Observable<Question> postQuestio(@Body Question question);
}
