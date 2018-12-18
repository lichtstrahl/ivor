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

    public long insertQuestion(Question q) {
        q.id = App.getDB().getQuestionDao().getMaxID() + 1;
        return App.getDB().getQuestionDao().insert(q);
    }


    public List<KeyWord> getKeyWords() {
        return App.getDB().getKeyWordDao().getAll();
    }


    public long insertKeyWord(KeyWord word) {
        word.id = App.getDB().getKeyWordDao().getMaxID() + 1;
        return App.getDB().getKeyWordDao().insert(word);
    }

    public long insertAnswer(Answer a) {
        a.id = App.getDB().getAnswerDao().getMaxID() + 1;
        return App.getDB().getAnswerDao().insert(a);
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



    public List<CommunicationKey> getCommunicationKeysForAnswer(long aID) {
        return App.getDB().getCommunicationKeyDao().getCommunicationKeyForAnswer(aID);
    }
}
