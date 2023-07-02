package jne.engine.events;

public class Event {

    private boolean isCanceled = false;
    public int value = 0;

    public boolean isCancelable()
    {
        return true;
    }

    public boolean isCanceled()
    {
        return isCanceled;
    }

    public void setCanceled(boolean cancel)
    {
        if (!isCancelable())
        {
            throw new UnsupportedOperationException(
                    "Attempted to call Event#setCanceled() on a non-cancelable event of type: "
                            + this.getClass().getCanonicalName()
            );
        }
        isCanceled = cancel;
    }

}
