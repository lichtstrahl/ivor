package root.ivatio.util;

import java.util.List;

import root.ivatio.App;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;

public class LocalStorageAPI {

    public void insertUser(User newUser) {
        App.getDB().getUserDao().insert(newUser);
    }

    public User getUser(long id) {
        return App.getDB().getUserDao().getUser(id);
    }

    public User getUser(String login, String pass) {
        return App.getDB().getUserDao().getUser(login, pass);
    }

    public void selectionCommunication() {
        App.getDB().getCommunicationDao().magicalDelete();
    }

    public void selectionCommunicationKey() {
        App.getDB().getCommunicationKeyDao().magicalDelete();
    }

    public List<Question> getQuestions() {
        return App.getDB().getQuestionDao().getAll();
    }

    public Question getQuestion(long id) {
        return App.getDB().getQuestionDao().getQuestion(id);
    }

    public void insertQuestion(Question q) {
        App.getDB().getQuestionDao().insert(q);
    }


    public List<KeyWord> getKeyWords() {
        return App.getDB().getKeyWordDao().getAll();
    }

    public KeyWord getKeyWord(long id) {
        return App.getDB().getKeyWordDao().getWord(id);
    }

    public void insertKeyWord(KeyWord word) {
        App.getDB().getKeyWordDao().insert(word);
    }

    public long insertAnswer(Answer a) {
        return App.getDB().getAnswerDao().insert(a);
    }

    public List<Answer> getAnswers() {
        return App.getDB().getAnswerDao().getAll();
    }

    public void insertCommunication(Communication c) {
        App.getDB().getCommunicationDao().insert(c);
    }

    public void insertCommunicationKey(CommunicationKey c) {
        App.getDB().getCommunicationKeyDao().insert(c);
    }

    public List<Command> getCommands() {
        return App.getDB().getCommandDao().getAll();
    }

    public List<Answer> getAnswerForQuestion(long id) {
        return App.getDB().getQuestionDao().getAnswerForQuestion(id);
    }

    public Communication getCommunication(long qID, long aID) {
        return App.getDB().getCommunicationDao().getCommunication(qID, aID);
    }

    public List<Communication> getCommunications() {
        return App.getDB().getCommunicationDao().getAll();
    }

    public List<Answer> getAnswerForKeyWord(long id) {
        return App.getDB().getKeyWordDao().getAnswerForKeyWord(id);
    }

    public CommunicationKey getCommunicationKey(long wID, long aID) {
        return App.getDB().getCommunicationKeyDao().getCommunicationKey(wID, aID);
    }

    public List<CommunicationKey> getCommunicationKeys() {
        return App.getDB().getCommunicationKeyDao().getAll();
    }

    public void updateCommunicationKey(CommunicationKey key) {
        App.getDB().getCommunicationKeyDao().update(key);
    }

    public void updateCommunication(Communication com) {
        App.getDB().getCommunicationDao().update(com);
    }

    public long[] getKeyWordsID() {
        return App.getDB().getKeyWordDao().getAllID();
    }

    public long[] getQuestionsID() {
        return App.getDB().getQuestionDao().getAllID();
    }

    public void deleteQuestion(Question q) {
        App.getDB().getQuestionDao().delete(q);
    }

    public void deleteCommunication(long id) {
        App.getDB().getCommunicationDao().delete(id);
    }

    public void deleteCommunicationKey(long id) {
        App.getDB().getCommunicationKeyDao().delete(id);
    }

    public void deleteKeyWord(KeyWord word) {
        App.getDB().getKeyWordDao().delete(word);
    }
}
