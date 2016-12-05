package zuulFramework.worldofzuul;

/**
 * Implement this interface if you want to receive time events from the game
 * Such as get a call every 60 in game minute.
 */
public interface ITimeEventAble {
    /**
     * Should return the wanted in game time between callbacks
     */
    int getTimeBetweenEvents();

    /**
     * This method will be called whenever the time has been since last event happened.
     *
     * @param timeAt The time of the callback
     */
    void timeCallback(int timeAt, Game game);
}
