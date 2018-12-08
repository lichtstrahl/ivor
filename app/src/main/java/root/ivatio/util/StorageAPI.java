package root.ivatio.util;

import java.util.List;

import root.ivatio.bd.command.Command;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;


public interface StorageAPI {
    void insertUser(User newUser);

    User getUser(long id);

    User getUser(String login, String pass);

    void selectionCommunication();

    void selectionCommunicationKey();

    List<Question> getQuestions();

    Question getQuestion(long id);

    void insertQuestion(Question q);

    List<KeyWord> getKeyWords();

    KeyWord getKeyWord(long id);

    void insertKeyWord(KeyWord word);

    long insertAnswer(Answer a);

    void insertCommunication(Communication c);

    void insertCommunicationKey(CommunicationKey c);

    List<Command> getCommands();

    List<Answer> getAnswerForQuestion(long id);

    Communication getCommunication(long qID, long aID);

    List<Answer> getAnswerForKeyWord(long id);

    CommunicationKey getCommunicationKey(long wID, long aID);

    void updateCommunicationKey(CommunicationKey key);

    void updateCommunication(Communication com);

    long[] getKeyWordsID();

    long[] getQuestionsID();

    void deleteQuestion(Question q);

    void deleteCommunication(long id);

    void deleteCommunicationKey(long id);

    void deleteKeyWord(KeyWord word);
}
