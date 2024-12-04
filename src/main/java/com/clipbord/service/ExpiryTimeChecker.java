package com.clipbord.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class ExpiryTimeChecker {
	
	
	public void ExpiryTime(LocalDateTime experyTime) {
		
		// Example current time
        LocalDateTime now = LocalDateTime.now();

        // Example expiry timestamp (replace this with clipboard.getExpiryTimestamp())
        LocalDateTime expiryTimestamp = experyTime;

        // Check if the expiry timestamp is in the future
        if (expiryTimestamp.isAfter(now)) {
            // Calculate the remaining duration
            Duration duration = Duration.between(now, expiryTimestamp);

            long days = duration.toDays();
            long hours = duration.toHours() % 24; // Remaining hours after calculating days
            long minutes = duration.toMinutes() % 60; // Remaining minutes after calculating hours

            System.out.println("Time remaining:");
            System.out.println(days + " days");
            System.out.println(hours + " hours");
            System.out.println(minutes + " minutes");
        } else {
            System.out.println("The expiry time has already passed.");
        }
	}
  
        
    
}
