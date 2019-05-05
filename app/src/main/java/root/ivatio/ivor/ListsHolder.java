package root.ivatio.ivor;

import java.util.List;

import javax.annotation.Nullable;

import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;

public class ListsHolder {
    private List<Answer> answers;
    private List<Command> commands;
    private List<Communication> communications;
    private List<CommunicationKey> communicationKeys;
    private List<KeyWord> keyWords;
    private List<Question> questions;

    private ListsHolder(Builder builder) {
        this.answers = builder.answers;
        this.commands = builder.commands;
        this.communications = builder.communications;
        this.communicationKeys = builder.communicationKeys;
        this.keyWords = builder.keyWords;
        this.questions = builder.questions;
    }

    public static Builder getBuilder() {
        return new ListsHolder.Builder();
    }

    public static class Builder {
        private List<Answer> answers = null;
        private List<Command> commands = null;
        private List<Communication> communications = null;
        private List<CommunicationKey> communicationKeys = null;
        private List<KeyWord> keyWords = null;
        private List<Question> questions = null;

        public Builder buildAnswers(List<Answer> a) {
            this.answers = a;
            return this;
        }

        public Builder buildCommands(List<Command> c) {
            this.commands = c;
            return this;
        }

        public Builder buildCommunications(List<Communication> c) {
            this.communications = c;
            return this;
        }

        public Builder buildCommunicationKeys(List<CommunicationKey> c) {
            this.communicationKeys = c;
            return this;
        }

        public Builder buildKeyWords(List<KeyWord> w) {
            this.keyWords = w;
            return this;
        }

        public Builder buildQuestions(List<Question> q) {
            this.questions = q;
            return this;
        }

        @Nullable
        public ListsHolder build() {
            if (this.answers != null && this.commands != null && this.communications != null &&
                    this.communicationKeys != null && this.keyWords != null && this.questions != null) {
                return new ListsHolder(this);
            }
            else {
                return null;
            }
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<Communication> getCommunications() {
        return communications;
    }

    public List<CommunicationKey> getCommunicationKeys() {
        return communicationKeys;
    }

    public List<KeyWord> getKeyWords() {
        return keyWords;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
