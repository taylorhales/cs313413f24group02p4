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
    public void onAction() {
        // increment the runTimeCount and reset the Tick Count
        // also reset the 3 second timer
        sm.actionIncCount();
        sm.resetTickCount();
        sm.actionReset();
        if (sm.getRunCount() == 99) {
            sm.actionBeep(); // play beep notification before decrementing from max
            sm.toDecrementingState();
        }

    }

    @Override
    public void onTick() {
        // since clock is started onTick is now activated every second
        // increase the tickCount and check if 3 seconds has passed,
        // if yes then start decrementing
        sm.incTickCount();
        if (sm.getTickCount() > 3) {
            sm.actionBeep();  // play beep notification before decrementing
            sm.toDecrementingState();
        }
    }

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
