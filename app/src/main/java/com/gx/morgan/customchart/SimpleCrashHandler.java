package com.gx.morgan.customchart;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.Vector;

/**
 * description：
 * <br>author：caowugao
 * <br>time： 2018/02/07 19:33
 */

public class SimpleCrashHandler implements UncaughtExceptionHandler {

    private volatile static SimpleCrashHandler instance;
    private UncaughtExceptionHandler mDefaultCrashHandler;
    private List<OnCrashListener> listeners;

    public interface OnCrashListener {
        void onCrash(Thread thread, Throwable t);
    }

    private SimpleCrashHandler() {
        listeners = new Vector<>();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 获取系统默认的异常处理器
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static SimpleCrashHandler getInstance() {
        if (null == instance) {
            synchronized (SimpleCrashHandler.class) {
                if (null == instance) {
                    instance = new SimpleCrashHandler();
                }
            }
        }
        return instance;
    }

    public void addOnCrashListener(OnCrashListener listener) {
        if (null != listener && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        handlerException(thread, t);
        if (null != mDefaultCrashHandler) {
            mDefaultCrashHandler.uncaughtException(thread, t);
        } else {
            // 用户原来设置了 Thread.setDefaultUncaughtExceptionHandler(null);时就直接退出应用
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    private void handlerException(Thread thread, Throwable t) {
        if (null == t) {
            return;
        }
        if (listeners.isEmpty()) {
            return;
        }
        for (OnCrashListener listener : listeners) {
            if (null != listener) {
                listener.onCrash(thread, t);
            }
        }
        listeners.clear();
    }
}
