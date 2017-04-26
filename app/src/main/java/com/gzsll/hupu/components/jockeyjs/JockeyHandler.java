/*******************************************************************************
 * Copyright (c) 2013,  Paul Daniels
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.gzsll.hupu.components.jockeyjs;

import java.util.Map;

public abstract class JockeyHandler {

    public interface OnCompletedListener {
        public void onCompleted();
    }

    /**
     * Executes this handler with a given payload
     */
    public void perform(Map<Object, Object> payload) {
        perform(payload, null);
    }

    /**
     * Executes this handler with a given payload and
     * a listener that will be notified when this operation is complete
     */
    public void perform(Map<Object, Object> payload, OnCompletedListener listener) {
        doPerform(payload);
        completed(listener);
    }

    protected void completed(OnCompletedListener listener) {
        if (listener != null) listener.onCompleted();
    }

    protected abstract void doPerform(Map<Object, Object> payload);
}
