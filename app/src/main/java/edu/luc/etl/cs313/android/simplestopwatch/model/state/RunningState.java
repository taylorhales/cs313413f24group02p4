package edu.luc.etl.cs313.android.simplestopwatch.model.state;

//import android.util.Log;
import edu.luc.etl.cs313.android.simplestopwatch.R;

class RunningState implements StopwatchState {

    public RunningState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;



    @Override
    public void onStartStop() {
        sm.actionStop();
        sm.toStoppedState();

    }

    @Override
    public void onLapReset() {
        sm.actionLap();
        sm.toLapRunningState();
    }

    @Override
    public void onTick() {
        sm.actionRingTheAlarm(); // trigger alarm action while in StoppedState
    }

    @Override
    public void onDecrement() {
        //Log.d("DEBUG", "DecrementingState onDecrement invoked");
        sm.toDecrementingState(); // Transition to DecrementingState when ticking starts
    }


    @Override
    public void onAction() {
        sm.actionStop();
        sm.toStoppedState();
    }


    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.RUNNING;
    }
}
