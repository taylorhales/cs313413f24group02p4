package edu.luc.etl.cs313.android.simplestopwatch.model.state;

/**
 * The restricted view states have of their surrounding state machine.
 * This is a client-specific interface in Peter Coad's terminology.
 *
 * @author laufer
 */
interface StopwatchSMStateView {

    // transitions
    void toRunningState();
    void toStoppedState();
    void toLapRunningState();
    void toLapStoppedState();
    void toIncrementingState();
    void toDecrementingState();


    // actions
    void actionInit();
    void actionReset();
    void actionStart();
    void actionStop();
    void actionLap();
    void actionInc();
    void actionUpdateView();
    void actionIncCount();
    void actionRingTheAlarm();

    // state-dependent UI updates
    void updateUIRuntime();
    void updateUILaptime();

    //temp to be organized later
    int getRuntime();
    void decRunTime();
    void incTickCount();
    int getTickCount();
    void resetTickCount();
}
