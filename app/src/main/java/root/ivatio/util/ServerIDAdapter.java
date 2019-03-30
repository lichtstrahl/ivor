package root.ivatio.util;

import java.util.List;

import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;

public class ServerIDAdapter {
    private ServerIDAdapter() {}

    public static void adapterQuestionID(Communication c, List<HolderID> holders) {
        for (HolderID holder : holders) {
            if (holder.getSelfID() == c.questionID) {
                c.questionID = holder.getServerID();
                break;
            }
        }
    }

    public static void adapterKeyID(CommunicationKey c, List<HolderID> holders) {
        for (HolderID holder : holders) {
            if (holder.getSelfID() == c.keyID) {
                c.keyID = holder.getServerID();
                break;
            }
        }
    }

    public static void adapterID(CommunicationKey c, List<HolderID> holders) {
        for (HolderID holder : holders) {
            if (c.id == holder.getSelfID()) {
                c.id = holder.getServerID();
                break;
            }
        }
    }

    public static void adapterID(Communication c, List<HolderID> holders) {
        for (HolderID holder : holders) {
            if (c.id == holder.getSelfID()) {
                c.id = holder.getServerID();
                break;
            }
        }
    }

    public static void adapterID(KeyWord word, List<HolderID> holders) {
        for (HolderID holder : holders) {
            if (word.id == holder.getSelfID()) {
                word.id = holder.getServerID();
                break;
            }
        }
    }

    public static void adapterID(Question q, List<HolderID> holders) {
        for (HolderID holder : holders) {
            if (q.id == holder.getSelfID()) {
                q.id = holder.getServerID();
                break;
            }
        }
    }
}
