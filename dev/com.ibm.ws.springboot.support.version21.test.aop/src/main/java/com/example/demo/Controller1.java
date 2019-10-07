package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public /*final*/ class Controller1 {

	
	public static String backTrace() {
		String retVal = "BackTrace: ";
		Exception ex = new Exception();
		StackTraceElement[] st = ex.getStackTrace();
		for (int x=1; x<11; x++) {
			retVal += "\n  " + st[x];
		}
        return retVal;        				
	}
	
	@Autowired
    Inner inner;
	
	@LogExecutionTime 
    @GetMapping(value="/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {
        return "This is Home page:\n"  +  reflect(this.getClass())	+ "\n"	+ backTrace() + "\n" +
        		"Inner message: " + inner.message() + "\n" +
        		reflect(inner.getClass());
    }
	

	private String reflect(@SuppressWarnings("rawtypes") Class clazz) {
		return "Object: " + clazz.getName() + "\n" +
		       "Is final: " +
		         (((clazz.getModifiers() & java.lang.reflect.Modifier.FINAL) >0 ) ? "TRUE" : "FALSE") + "\n" + 
		       "Is private: " + 
		        (((clazz.getModifiers() & java.lang.reflect.Modifier.PRIVATE) >0 ) ? "TRUE" : "FALSE");			       
	}
	
}