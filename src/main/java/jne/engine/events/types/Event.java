package jne.engine.events.types;

import jne.engine.events.EventListenerHelper;

public class Event {

    private boolean isCanceled = false;

    public boolean isCancelable() {
        return true;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean cancel) {
        if (!isCancelable()) {
            System.out.println(
                    "Attempted to call Event#setCanceled() on a non-cancelable event of type: "
                            + this.getClass().getCanonicalName()
            );
            return;
        }
        isCanceled = cancel;
    }

    public void post() {
        EventListenerHelper.post(this);
    }

}
