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
        // Increment tick count
        sm.incTickCount();

        // Decrement running time after 3 seconds or if runtime equals 99
        if (sm.getTickCount() >= 3 || sm.getRuntime() == 99) {
            sm.decRunTime();
            sm.resetTickCount(); // Reset tick count after decrement

            // Check if the time has reached zero
            if(sm.getRuntime() <= 0){
                sm.actionStop(); // Stop the timer
                sm.actionRingTheAlarm(); // Start the alarm
                sm.toStoppedState(); // Change to stopped state
                return;
            }
        }

        // Update the view to reflect the new time
        //Log.d("DEBUG", "Runtime decremented to: " + sm.getRuntime());
        sm.actionUpdateView();
    }

    @Override
    public void onIncrement() {
        sm.toIncrementingState();
    }

    @Override
    public void onTick() {
        throw new UnsupportedOperationException("onTick");
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