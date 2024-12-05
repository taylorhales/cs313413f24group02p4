package edu.luc.etl.cs313.android.simplestopwatch.model.state;

//import android.util.Log;
import edu.luc.etl.cs313.android.simplestopwatch.R;

class DecrementingState implements StopwatchState {

    private final DefaultStopwatchStateMachine sm;

    public DecrementingState(final DefaultStopwatchStateMachine sm) {
        this.sm = sm;
    }

    @Override
    public void onStartStop() {
        sm.actionStop();
        sm.toStoppedState();
    }

    @Override
    public void onLapReset() {
        // No action required for Lap/Reset in DecrementingState
    }

    @Override
    public void onDecrement() {
        //
    }

    @Override
    public void onAction() {
        // if onIncrement button is pressed, runCount is resetted and
        // sent to stopped state
        sm.actionResetRunCount();
        sm.actionStop();
        sm.toStoppedState();
    }

    @Override
    public void onTick() {
        // once in decrement state, since clock is still going we now
        // decrement the runTime count until it reaches 0, then we go to
        // stopped state when 0 is reached
        sm.actionDecCount();
        if (sm.runCount == 0) {
            sm.toAlarmingState();
        }
    }


    @Override
    public void updateView() {
        //Log.d("DEBUG", "Updating UI runtime to: " + sm.getRuntime());
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.DECREMENTING;
    }
}