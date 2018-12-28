package root.iavtio;

import org.junit.Before;
import org.junit.Test;

import root.ivatio.ivor.Ivor;
import root.ivatio.ivor.IvorPresenter;
import root.ivatio.ivor.IvorViewAPI;
import root.ivatio.util.ROLE;

import static org.mockito.Mockito.mock;

public class IvorPresenterUnitTests {
    private Ivor ivor;
    private IvorViewAPI api;
    private IvorPresenter presenter;
    private ROLE role = ROLE.STD;

    @Before
    public void init() {
        api = mock(IvorViewAPI.class);
        presenter = new IvorPresenter(ivor,api);
    }

    @Test
    public void test1() {

    }
}
