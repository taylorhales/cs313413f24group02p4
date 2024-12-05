package edu.luc.etl.cs313.android.simplestopwatch.model.state;

//import android.util.Log;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    private final TimeModel timeModel;

    private final ClockModel clockModel;

    public int runCount = 0;

    private int tickCount = 0;

    //temp to be organized
    @Override
    public int getRuntime() {
        return timeModel.getRuntime();
    }

    @Override
    public void decRunTime() {
        timeModel.decRunTime();
    }

    @Override
    public void incTickCount() {
        tickCount++;
    }

    @Override
    public int getTickCount() {
        return tickCount;
    }

    @Override
    public void resetTickCount() {
        tickCount = 0;
    }

    @Override
    public int getRunCount() { return runCount; }

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    protected void setState(final StopwatchState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    private StopwatchModelListener listener;

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized void onStartStop() { state.onStartStop(); }
    @Override public synchronized void onLapReset()  { state.onLapReset(); }
    @Override public synchronized void onTick()      { state.onTick(); }
    @Override public synchronized void onAction()    { state.onAction(); }
    @Override public synchronized void onDecrement() { /*Log.d("DEBUG", "onDecrement called, current state: " + state.getClass().getSimpleName()); */ state.onDecrement(); }

    @Override public void updateUIRuntime() { listener.onTimeUpdate(timeModel.getRuntime()); }
    @Override public void updateUILaptime() { listener.onTimeUpdate(timeModel.getLaptime()); }

    // known states
    private final StopwatchState STOPPED      = new StoppedState(this);
    private final StopwatchState ALARMING      = new AlarmingState(this);
    private final StopwatchState LAP_RUNNING  = new LapRunningState(this);
    private final StopwatchState LAP_STOPPED  = new LapStoppedState(this);
    private final StopwatchState INCREMENTING = new IncrementingState(this);
    private final StopwatchState DECREMENTING = new DecrementingState(this);

    // transitions
    @Override public void toAlarmingState()      { setState(ALARMING); }
    @Override public void toStoppedState()      { setState(STOPPED); }
    @Override public void toLapRunningState()   { setState(LAP_RUNNING); }
    @Override public void toLapStoppedState()   { setState(LAP_STOPPED); }
    @Override public void toIncrementingState() { setState(INCREMENTING);  }
    @Override public void toDecrementingState() { setState(DECREMENTING); }

    // actions
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime();} // deleted updated view, checking to just use timer for the 3 second
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }
    @Override public void actionLap()        { timeModel.setLaptime(); }
    @Override public void actionInc()        { timeModel.incRuntime(); } // same thing here
    @Override public void actionUpdateView() { /* Log.d("DEBUG", "actionUpdateView called"); */ state.updateView(); }
    @Override public void actionIncCount()   { runCount++; timeModel.setRunCount(runCount); actionUpdateView(); } // increment the runCount
    @Override public void actionDecCount()   { runCount--; timeModel.setRunCount(runCount); actionUpdateView(); } // begin to Decrement the count
    @Override public void actionResetRunCount() {runCount = 0; timeModel.setRunCount(runCount); actionUpdateView(); }

    // notify listener to play the alarm sound
    @Override public void actionRingTheAlarm() {listener.playDefaultNotification();}

    // notify listener to play beep tone
    @Override public void actionBeep() {listener.playBeep();}
}
