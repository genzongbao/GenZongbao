package ydh.message.push;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PushExcecutor {
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	public static Future<?> submit(Runnable task) {
		return executor.submit(task);
	}
}
