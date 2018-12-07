package root.ivatio.util;

import java.util.List;

import root.ivatio.App;
import root.ivatio.bd.Command.Command;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;

public class StorageAPI {
    public static void insertUser(User newUser) {
        App.getDB().getUserDao().insert(newUser);
    }

    public static User getUser(long id) {
        return App.getDB().getUserDao().getUser(id);
    }

    public static User getUser(String login, String pass) {
        return App.getDB().getUserDao().getUser(login, pass);
    }

    public static void selectionCommunication() {
        App.getDB().getCommunicationDao().magicalDelete();
    }

    public static void selectionCommunicationKey() {
        App.getDB().getCommunicationKeyDao().magicalDelete();
    }

    public static List<Question> getQuestions() {
        return App.getDB().getQuestionDao().getAll();
    }

    public static Question getQuestion(long id) {
        return App.getDB().getQuestionDao().getQuestion(id);
    }

    public static void insertQuestion(Question q) {
        App.getDB().getQuestionDao().insert(q);
    }


    public static List<KeyWord> getKeyWords() {
        return App.getDB().getKeyWordDao().getAll();
    }

    public static KeyWord getKeyWord(long id) {
        return App.getDB().getKeyWordDao().getWord(id);
    }

    public static void insertKeyWord(KeyWord word) {
        App.getDB().getKeyWordDao().insert(word);
    }

    public static long insertAnswer(Answer a) {
        return App.getDB().getAnswerDao().insert(a);
    }

    public static void insertCommunication(Communication c) {
        App.getDB().getCommunicationDao().insert(c);
    }

    public static void insertCommunicationKey(CommunicationKey c) {
        App.getDB().getCommunicationKeyDao().insert(c);
    }

    public static List<Command> getCommands() {
        return App.getDB().getCommandDao().getAll();
    }

    public static List<Answer> getAnswerForQuestion(long id) {
        return App.getDB().getQuestionDao().getAnswerForQuestion(id);
    }

    public static Communication getCommunication(long qID, long aID) {
        return App.getDB().getCommunicationDao().getCommunication(qID, aID);
    }

    public static List<Answer> getAnswerForKeyWord(long id) {
        return App.getDB().getKeyWordDao().getAnswerForKeyWord(id);
    }

    public static CommunicationKey getCommunicationKey(long wID, long aID) {
        return App.getDB().getCommunicationKeyDao().getCommunicationKey(wID, aID);
    }

    public static void updateCommunicationKey(CommunicationKey key) {
        App.getDB().getCommunicationKeyDao().update(key);
    }

    public static void updateCommunication(Communication com) {
        App.getDB().getCommunicationDao().update(com);
    }

    public static long[] getKeyWordsID() {
        return App.getDB().getKeyWordDao().getAllID();
    }

    public static long[] getQuestionsID() {
        return App.getDB().getQuestionDao().getAllID();
    }

    public static void deleteQuestion(Question q) {
        App.getDB().getQuestionDao().delete(q);
    }

    public static void deleteCommunication(long id) {
        App.getDB().getCommunicationDao().delete(id);
    }

    public static void deleteCommunicationKey(long id) {
        App.getDB().getCommunicationKeyDao().delete(id);
    }

    public static void deleteKeyWord(KeyWord word) {
        App.getDB().getKeyWordDao().delete(word);
    }
}
