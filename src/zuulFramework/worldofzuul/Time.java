/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Caoda
 */
public class Time {
    private Game game;
    
    /**
     * The current ingame time in minutes since the start of the day at 10
     * o'clock Starts as 10:00
     */
    private int time = 60 * 10; // 60 minutes times 10 hours

    private int lastTime = -1;

    /**
     * All the callbacks that should be done according with different times.
     */
    private List<TimeCallback> callbacks;

    public Time(Game game) {
        this.game = game;
        this.callbacks = new ArrayList<TimeCallback>();
	
    }
    
    public List<TimeCallback> getList() {
        return callbacks;
    }

    /**
     * Gets the current time nicely formatted as a string
     *
     * @return A string like "13:37"
     */
    public String getNiceFormattedTime() {
        // Calculate the hours
        String hours = time / 60 + "";
        String minutes = time % 60 + "";
        if (hours.length() != 2) {
            hours = "0" + hours;
        }
        if (minutes.length() != 2) {
            minutes = "0" + minutes;
        }
	

        return String.format("%2s:%2s", hours, minutes);
    }

    /**
     * This method is called each time the play event happens, and should be
     * used to hook into things that should happen based on time.
     */
    private void doTimeEvent() {
        //Run through all the timecallbacks 
        List<TimeCallback> callbacks1 = this.callbacks;
        for (int i = 0; i < callbacks1.size(); i++) {
            TimeCallback callback = callbacks1.get(i);
            // Get callbacks and save it in the variable event, 
            // type ITimeEventAble
            ITimeEventAble event = callback.getCallback();

            // If the time since last time callback is bigger or equals the 
            // time between events then use the method timeCallback to call time
            // and player and then set the timeSinceLastCallback to 0
            if (callback.getTimeSinceLastCallback() >= event.getTimeBetweenEvents()) {
                callback.setTimeSinceLastCallback(0);
                event.timeCallback(this.time, game);
            }
        }
    }

    /**
     * Add a callback to time based events.
     *
     * @param callback The callback that should be called when ever a time event
     * has happened.
     */
    public void addTimeEvent(ITimeEventAble callback) {
        this.callbacks.add(new TimeCallback(callback));
    }

    /**
     * Removes a time callback from the list. Call this method before you remove
     * something that is in the callback list, otherwise it cannot be garbage
     * collected.
     *
     * @param callback The callback to remove
     */
    public void removeTimeEvent(ITimeEventAble callback) {
        this.callbacks = this.callbacks.stream().filter(timeCallback -> timeCallback.getCallback() != callback).collect(Collectors.toList());
    }

    /**
     * This method updates time for each time a player spends time in a room
     *
     * @param timeDif The amound of time that has changed
     */
    public void updateTime(int timeDif) {
        this.lastTime = this.time;
        // Go through every minute and add 1 minute each time, timeDif
        for (int i = 0; i < timeDif; i++) {
            this.time++;
            // Call a method doTimeEvent
            doTimeEvent();

        }

        // Run through all the timecallbacks
        for (TimeCallback callback : callbacks) {
            // set time since last callback, by using get time since last
            // callback and add timeDif
            callback.setTimeSinceLastCallback(callback.getTimeSinceLastCallback() + timeDif);
        }
    }

    /**
     * Stored specific callbacks that needs to happen at specific time.
     */
    private class TimeCallback {

        /**
         * The time since the callback was last called
         */
        private int timeSinceLastCallback = 0;

        /**
         * The callback itself to call
         */
        private ITimeEventAble callback;

        /**
         * Creates a callback store.
         *
         * @param callback
         */
        public TimeCallback(ITimeEventAble callback) {
            this.callback = callback;
        }

        /**
         * Gets the time since the callback was last called
         *
         * @return
         */
        public int getTimeSinceLastCallback() {
            return timeSinceLastCallback;
        }

        /**
         * Sets the time since the callback was last called
         *
         * @param timeSinceLastCallback
         */
        public void setTimeSinceLastCallback(int timeSinceLastCallback) {
            this.timeSinceLastCallback = timeSinceLastCallback;
        }

        /**
         * Gets the callback that should be made
         *
         * @return
         */
        public ITimeEventAble getCallback() {
            return callback;
        }

    }
    public int getCurrentTime(){
	return time;
    }
}

