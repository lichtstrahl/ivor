package ivor.action;

import java.util.List;

public interface ActionAPI {
    String next();
    void put(String param);
    List<String> getParam();
}
