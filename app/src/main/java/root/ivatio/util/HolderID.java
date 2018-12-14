package root.ivatio.util;

public class HolderID {
    private long selfID;
    private long serverID;

    public HolderID(long i0, long i1) {
        selfID = i0;
        serverID = i1;
    }

    public long getSelfID() {
        return selfID;
    }

    public long getServerID() {
        return serverID;
    }
}