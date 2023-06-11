package wang.ultra.my_utilities.common.runnable;

import wang.ultra.my_utilities.common.utils.DateConverter;

public class TestRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Test Time = " + DateConverter.getTime());
        System.out.println("\n");
    }
}
