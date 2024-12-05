package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class IncrementingState implements StopwatchState {

    private final StopwatchSMStateView sm;

    public IncrementingState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    @Override
    public void onStartStop() {
        sm.actionStop();
        sm.toStoppedState();
    }

    @Override
    public void onLapReset() {
        sm.toStoppedState();
        sm.actionReset();
    }

    @Override
    public void onIncrement() {
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
        return R.string.INCREMENTING;
    }
}
