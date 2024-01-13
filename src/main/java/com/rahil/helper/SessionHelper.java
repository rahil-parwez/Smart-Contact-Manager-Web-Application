package com.rahil.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Component(value = "sessionHelper")
public class SessionHelper {

	public void removeMessageFromSession() {
		
		try {
			
			 // Get the current request attributes
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		
			if (requestAttributes != null) {
				
				
			
				// Specify the scope from which you want to remove the attribute
	            // For example, RequestAttributes.SCOPE_REQUEST or RequestAttributes.SCOPE_SESSION
	            int scope = RequestAttributes.SCOPE_REQUEST;
	            
	            // Remove the attribute
	            requestAttributes.removeAttribute("message", scope);
	            
	            // Optionally, check if the attribute was removed successfully
	            Object removedAttributeValue = requestAttributes.getAttribute("message", scope);
	            
	            if (removedAttributeValue == null) {
	                System.out.println("Attribute removed successfully!");
	            } else {
	                System.out.println("Attribute removal failed!");
	            }
			
			}else {
	            // Handle the case when there is no request context
	            System.out.println("No active request context");
	        }
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}
