package root.ivatio.util;

import java.util.List;

import root.ivatio.App;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;

public class LocalStorageAPI {
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

    public long insertQuestion(Question q) {
        q.id = App.getDB().getQuestionDao().getMaxID() + 1;
        return App.getDB().getQuestionDao().insert(q);
    }


    public List<KeyWord> getKeyWords() {
        return App.getDB().getKeyWordDao().getAll();
    }

    public KeyWord getKeyWord(long id) {
        return App.getDB().getKeyWordDao().getWord(id);
    }

    public long insertKeyWord(KeyWord word) {
        word.id = App.getDB().getKeyWordDao().getMaxID() + 1;
        return App.getDB().getKeyWordDao().insert(word);
    }

    public long insertAnswer(Answer a) {
        a.id = App.getDB().getAnswerDao().getMaxID() + 1;
        return App.getDB().getAnswerDao().insert(a);
    }

    public List<Answer> getAnswers() {
        return App.getDB().getAnswerDao().getAll();
    }

    public long insertCommunication(Communication c) {
        c.id = App.getDB().getCommunicationDao().getMaxID() + 1;
        return App.getDB().getCommunicationDao().insert(c);
    }

    public long insertCommunicationKey(CommunicationKey c) {
        c.id = App.getDB().getCommunicationKeyDao().getMaxID() + 1;
        return App.getDB().getCommunicationKeyDao().insert(c);
    }

    public List<Communication> getCommunicationsForAnswer(long aID) {
        return App.getDB().getCommunicationDao().getCommunicationsForAnswer(aID);
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

    public List<CommunicationKey> getCommunicationKeysForAnswer(long aID) {
        return App.getDB().getCommunicationKeyDao().getCommunicationKeyForAnswer(aID);
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
