package com.gzsll.hupu.components.jockeyjs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Composes a group of JockeyHandlers into a single structure, <br>
 * When perform is invoked on this handler it will invoke it on all of the handlers.<br>
 * They will be invoked in FIFO order, however if any are asynchronous, no guarantees can be made
 * for
 * execution order.
 * <p/>
 * To maintain a single callback invariant, this handler will accumulate <br>
 * all the "complete" calls from the internal handlers.
 * <br><br>
 * Once all of the handlers have been called it this handler will signal completion.
 *
 * @author Paul
 */
public class CompositeJockeyHandler extends JockeyHandler {

    /**
     * Accumulates all the "completed" calls from the contained Handlers
     * Once all the handlers have completed this will signal completion
     *
     * @author Paul
     */
    private class AccumulatingListener implements OnCompletedListener {

        private int _size;
        private int _accumulated;

        private AccumulatingListener() {
            this._size = _handlers.size();
            this._accumulated = 0;
        }

        @Override
        public void onCompleted() {
            ++_accumulated;

            if (_accumulated >= _size) completed(_listener);
        }
    }

    private OnCompletedListener _listener;

    private List<JockeyHandler> _handlers = new ArrayList<JockeyHandler>();

    private OnCompletedListener _accumulator;

    public CompositeJockeyHandler(JockeyHandler... handlers) {
        add(handlers);
    }

    public void add(JockeyHandler... handler) {
        _handlers.addAll(Arrays.asList(handler));
    }

    public void clear(JockeyHandler handler) {
        _handlers.clear();
    }

    @Override
    public void perform(Map<Object, Object> payload, OnCompletedListener listener) {
        this._listener = listener;
        this._accumulator = new AccumulatingListener();
        doPerform(payload);
    }

    @Override
    protected void doPerform(Map<Object, Object> payload) {
        for (JockeyHandler handler : _handlers)
            handler.perform(payload, this._accumulator);
    }

    public static JockeyHandler compose(JockeyHandler... handlers) {
        return new CompositeJockeyHandler(handlers);
    }
}
