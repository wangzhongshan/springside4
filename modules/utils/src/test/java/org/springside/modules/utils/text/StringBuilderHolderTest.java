package org.springside.modules.utils.text;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

public class StringBuilderHolderTest {

	private static ThreadLocal<StringBuilderHolder> stringBuilderHolder = new ThreadLocal<StringBuilderHolder>() {
		@Override
		protected StringBuilderHolder initialValue() {
			return new StringBuilderHolder(512);
		}
	};

	@Test
	public void test() throws InterruptedException {
		
		final CountDownLatch countdown = new CountDownLatch(10);
		final CyclicBarrier barrier = new CyclicBarrier(10);
		
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				try {
					barrier.await();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringBuilder builder = stringBuilderHolder.get().stringBuilder();
				builder.append(Thread.currentThread().getName()+"-1");
				System.out.println(builder.toString());
				
				builder = stringBuilderHolder.get().stringBuilder();
				builder.append(Thread.currentThread().getName()+"-2");
				System.out.println(builder.toString());
				
				countdown.countDown();
			}
		};
		
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(runnable);
			thread.start();
		}
		
		countdown.await();
		
		
	}

}
