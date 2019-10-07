package com.example.demo;

import org.springframework.stereotype.Component;

@Component
class Inner {
	/**
	 * 
	 */
	private final Controller1 controller1;

	private final String trace;
	/**
	 * @param controller1
	 */
	Inner(Controller1 controller1) {
		this.controller1 = controller1;
		trace = Controller1.backTrace();
	}

	@LogExecutionTime
	public String message() {
		
		return "A message from inner.\n" + this.controller1.backTrace() + "\n Construction backtrace:\n" + trace ;
	}
	
}