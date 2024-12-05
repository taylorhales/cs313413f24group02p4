package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class StoppedState implements StopwatchState {

    public StoppedState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onStartStop() {
        sm.actionStart();
        sm.toRunningState();
    }

    @Override
    public void onLapReset() {
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onAction() {
        // When onIncrement button is pressed, start the clock model for 3 second timer
        // while switching to incrementing state and increase the count
        sm.actionStart();
        sm.actionIncCount();
        sm.toIncrementingState();
    }

    @Override
    public void onTick() {throw new UnsupportedOperationException("onTick");}

    @Override
    public void onDecrement() {
        sm.toDecrementingState(); // Transition to DecrementingState when ticking starts
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}
