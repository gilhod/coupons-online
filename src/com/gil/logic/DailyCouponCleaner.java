package com.gil.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import com.gil.dao.CouponDao;
import com.gil.exceptions.ApplicationException;



public class DailyCouponCleaner {
	
	private static DailyCouponCleaner instance = null;
	private static Object mutex = new Object();
	
	public static DailyCouponCleaner getInstance() throws ApplicationException {

		synchronized (mutex) {
			if (instance == null) {
				instance = new DailyCouponCleaner();
			}
		}
		return instance;
	}
	
	// parameters of the daily task.
	// the task should take place every day at 4:30 AM.
	
	private static final int EXECUTE_HOUR = 4;
	private static final int EXECUTE_MINUTES = 3;
	private static final int FREQUENCY = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

	Timer timer;
	DailyCouponCleanerTask task;
	

	private DailyCouponCleaner() {
		timer = new Timer();
		task = new DailyCouponCleanerTask();
		long delay = getTimeDelay();
		
		timer.scheduleAtFixedRate(task, delay , FREQUENCY);
	}

	class DailyCouponCleanerTask extends TimerTask {

		@Override
		public void run() {
			new DailyCouponCleanerThread().start();
		}
	}

	class DailyCouponCleanerThread extends Thread {

		@Override
		public void run() {

			CouponDao couponDao = new CouponDao();
			try {
				couponDao.deletePurchasesOfExpiredCoupons();
				couponDao.deleteExpiredCoupons();
			} 
			catch (ApplicationException e) {
				//Run() cannot throw exceptions. Catch and write to logger 
			}
		}
	}
	
	public void stopTask(){
		timer.cancel();
	}

	// This method calculate the time (in milliseconds) until first time the
	// task should be executed
	private long getTimeDelay() {

		// getting current hour and minutes
		Calendar calendar = new GregorianCalendar();
		int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
		int minutesNow = calendar.get(Calendar.MINUTE);

		// expressing times in minutes for comparison
		int timeNowInMinutes = hourNow * 60 + minutesNow;
		int taskTimeInMinutes = EXECUTE_HOUR * 60 + EXECUTE_MINUTES;

		// check if the time for task is now. If yes, return delay is zero.
		if (taskTimeInMinutes == timeNowInMinutes) {
			return 0;
		}

		// check if destination time is today or tomorrow. If tomorrow, add one
		// day to destination date.
		if (taskTimeInMinutes < timeNowInMinutes) {
			calendar.add(Calendar.DATE, 1);
		}

		// generate the destination time for first execution of the task
		Calendar destinationDate = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE), EXECUTE_HOUR, EXECUTE_MINUTES);

		// calculate the time delay from now to first execution time in
		// milliseconds.
		long delay = destinationDate.getTimeInMillis() - System.currentTimeMillis();

		return delay;

	}

}
