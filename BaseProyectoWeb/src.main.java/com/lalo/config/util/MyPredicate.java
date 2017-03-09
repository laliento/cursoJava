package com.lalo.config.util;



import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.Predicate;

@SuppressWarnings("rawtypes")
public class MyPredicate implements Predicate{


	    private Object expected;
	    private String propertyName;

	    public MyPredicate(String propertyName, Object expected) {
	        super();
	        this.propertyName = propertyName;
	        this.expected = expected;
	    }

	    public boolean evaluate(Object object) {
	        try {
	        	
	        	if(expected != null) {
	        		return expected.equals(PropertyUtils.getProperty(object, propertyName));
	        	} else {
	        		return PropertyUtils.getProperty(object, propertyName) == null;
	        	}
	        	
	        } catch (Exception e) {
	            return false;
	        }
	    }

}
