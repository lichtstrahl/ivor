package root.ivatio.ivor.presenter;

import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorViewAPI;

public class Presenter {
    protected Ivor model;
    protected IvorViewAPI viewAPI;
    public Presenter(Ivor ivor, IvorViewAPI api) {
        model = ivor;
        viewAPI = api;
    }
}
