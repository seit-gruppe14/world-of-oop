/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caoda
 */
public class Time implements ObservableStringValue {
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
	private List<ChangeListener<? super String>> listeners = new ArrayList<>();

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
	 *                 has happened.
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
		String before = getNiceFormattedTime();
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

		String formattetValue = getNiceFormattedTime();
		listeners.forEach(changeListener -> changeListener.changed(this, before, formattetValue));
	}

	/**
	 * Returns the current value of this {@code ObservableObjectValue<T>}.
	 *
	 * @return The current value
	 */
	@Override
	public String get() {
		return getNiceFormattedTime();
	}

	/**
	 * Adds a {@link ChangeListener} which will be notified whenever the value
	 * of the {@code ObservableValue} changes. If the same listener is added
	 * more than once, then it will be notified more than once. That is, no
	 * check is made to ensure uniqueness.
	 * <p>
	 * Note that the same actual {@code ChangeListener} instance may be safely
	 * registered for different {@code ObservableValues}.
	 * <p>
	 *
	 * @param listener The listener to register
	 * @throws NullPointerException if the listener is null
	 * @see #removeListener(ChangeListener)
	 */
	@Override
	public void addListener(ChangeListener<? super String> listener) {
		if (listener == null) throw new NullPointerException("listener");
		listeners.add(listener);
	}

	/**
	 * Removes the given listener from the list of listeners, that are notified
	 * whenever the value of the {@code ObservableValue} changes.
	 * <p>
	 * If the given listener has not been previously registered (i.e. it was
	 * never added) then this method call is a no-op. If it had been previously
	 * added then it will be removed. If it had been added more than once, then
	 * only the first occurrence will be removed.
	 *
	 * @param listener The listener to remove
	 * @throws NullPointerException if the listener is null
	 * @see #addListener(ChangeListener)
	 */
	@Override
	public void removeListener(ChangeListener<? super String> listener) {
		listeners.remove(listener);
	}

	/**
	 * Returns the current value of this {@code ObservableValue}
	 *
	 * @return The current value
	 */
	@Override
	public String getValue() {
		return get();
	}

	/**
	 * Adds an {@link InvalidationListener} which will be notified whenever the
	 * {@code Observable} becomes invalid. If the same
	 * listener is added more than once, then it will be notified more than
	 * once. That is, no check is made to ensure uniqueness.
	 * <p>
	 * Note that the same actual {@code InvalidationListener} instance may be
	 * safely registered for different {@code Observables}.
	 * <p>
	 *
	 * @param listener The listener to register
	 * @throws NullPointerException if the listener is null
	 * @see #removeListener(InvalidationListener)
	 */
	@Override
	public void addListener(InvalidationListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Removes the given listener from the list of listeners, that are notified
	 * whenever the value of the {@code Observable} becomes invalid.
	 * <p>
	 * If the given listener has not been previously registered (i.e. it was
	 * never added) then this method call is a no-op. If it had been previously
	 * added then it will be removed. If it had been added more than once, then
	 * only the first occurrence will be removed.
	 *
	 * @param listener The listener to remove
	 * @throws NullPointerException if the listener is null
	 * @see #addListener(InvalidationListener)
	 */
	@Override
	public void removeListener(InvalidationListener listener) {
		throw new UnsupportedOperationException();
	}

	public int getCurrentTime() {
		return time;
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
}

