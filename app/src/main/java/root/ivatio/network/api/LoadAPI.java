package root.ivatio.network.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;

public interface LoadAPI {
    @GET("/api/answers")
    Observable<List<Answer>> loadAnswers();

    @GET("/api/commands")
    Observable<List<Command>> loadCommands();

    @GET("/api/communications")
    Observable<List<Communication>> loadCommunications();

    @GET("/api/communicationkeys")
    Observable<List<CommunicationKey>> loadCommunicationKeys();

    @GET("/api/keywords")
    Observable<List<KeyWord>> loadKeyWords();

    @GET("/api/questions")
    Observable<List<Question>> loadQuestions();
}
