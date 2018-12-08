package root.ivatio.util;

import java.util.List;

import root.ivatio.bd.command.Command;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;

public class NetworkStorageAPI implements StorageAPI {
    @Override
    public void insertUser(User newUser) {

    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public User getUser(String login, String pass) {
        return null;
    }

    @Override
    public void selectionCommunication() {

    }

    @Override
    public void selectionCommunicationKey() {

    }

    @Override
    public List<Question> getQuestions() {
        return null;
    }

    @Override
    public Question getQuestion(long id) {
        return null;
    }

    @Override
    public void insertQuestion(Question q) {

    }

    @Override
    public List<KeyWord> getKeyWords() {
        return null;
    }

    @Override
    public KeyWord getKeyWord(long id) {
        return null;
    }

    @Override
    public void insertKeyWord(KeyWord word) {

    }

    @Override
    public long insertAnswer(Answer a) {
        return 0;
    }

    @Override
    public void insertCommunication(Communication c) {

    }

    @Override
    public void insertCommunicationKey(CommunicationKey c) {

    }

    @Override
    public List<Command> getCommands() {
        return null;
    }

    @Override
    public List<Answer> getAnswerForQuestion(long id) {
        return null;
    }

    @Override
    public Communication getCommunication(long qID, long aID) {
        return null;
    }

    @Override
    public List<Answer> getAnswerForKeyWord(long id) {
        return null;
    }

    @Override
    public CommunicationKey getCommunicationKey(long wID, long aID) {
        return null;
    }

    @Override
    public void updateCommunicationKey(CommunicationKey key) {

    }

    @Override
    public void updateCommunication(Communication com) {

    }

    @Override
    public long[] getKeyWordsID() {
        return new long[0];
    }

    @Override
    public long[] getQuestionsID() {
        return new long[0];
    }

    @Override
    public void deleteQuestion(Question q) {

    }

    @Override
    public void deleteCommunication(long id) {

    }

    @Override
    public void deleteCommunicationKey(long id) {

    }

    @Override
    public void deleteKeyWord(KeyWord word) {

    }
}
