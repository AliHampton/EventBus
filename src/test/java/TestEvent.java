import net.ali.eventbus.api.IEvent;

import java.util.ArrayList;
import java.util.List;

public class TestEvent implements IEvent {

    public boolean success = false;
    public List<Integer> codes = new ArrayList<>();
}
