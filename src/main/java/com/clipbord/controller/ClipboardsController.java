package com.clipbord.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clipbord.beans.Clipboards;
import com.clipbord.repository.ClipboardsRepository;
import com.clipbord.service.ExpiryTimeChecker;
import com.clipbord.service.QRCodeService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ClipboardsController {


    @Autowired
    private ClipboardsRepository clipboardRepository;

    @Autowired
    private ExpiryTimeChecker expiryTimeChecker;


    @PostMapping("/{uniqueId}")
    public String saveClipboard(
            @PathVariable("uniqueId") String uniqueId,
            @RequestParam("content") String content,
            @RequestParam("expiryTime") long expiryTime,
            Model model,
            HttpServletRequest request) { // Add HttpServletRequest parameter

        System.out.println("I am in save method with uniqueId: " + uniqueId);

        LocalDateTime expiryTimestamp = LocalDateTime.now().plusMinutes(expiryTime);

        Clipboards clipboard = new Clipboards(uniqueId, content, expiryTimestamp);
        clipboardRepository.save(clipboard);
        String baseUrl="";

        try {
            // Construct dynamic base URL
            baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
            String qrCodeImage = QRCodeService.generateQRCode(baseUrl, uniqueId);

            model.addAttribute("qrCode", qrCodeImage);
        }
        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to generate QR Code");
        }
        model.addAttribute("link", baseUrl+""+uniqueId);
        return "success";
    }


    @GetMapping("/success")
    public String success() {
        return "success";
    }

//    @GetMapping("/{uniqueId}")
//    public String getClipboard(@PathVariable String uniqueId, Model model) {
//        Optional<Clipboards> optionalClipboard = clipboardRepository.findById(uniqueId);
//        System.out.println("uniqueId : "+uniqueId);
//        boolean timeLimit= true;
//
//        if (optionalClipboard.isPresent()) {
//            Clipboards clipboard = optionalClipboard.get();
//            if (clipboard.getExpiryTimestamp().isAfter(LocalDateTime.now())) {
//            	 timeLimit=false;
//            	 model.addAttribute("timeLimit",timeLimit);
//            	 model.addAttribute("copyButton","copyButton");
//            	 
//            	 LocalDateTime expiryday= clipboard.getExpiryTimestamp();
//            	 expiryTimeChecker.ExpiryTime(expiryday);
//            	 
//                model.addAttribute("content", clipboard.getContent());
//                return "code"; 
//            } else {
//                clipboardRepository.deleteById(uniqueId); 
//            }
//        }
//        model.addAttribute("timeLimit",timeLimit);
//        return "code"; 
//    }

    @GetMapping("/{uniqueId}")
    public String getClipboard(@PathVariable String uniqueId, Model model) {
        Optional<Clipboards> optionalClipboard = clipboardRepository.findById(uniqueId);
        System.out.println("uniqueId : " + uniqueId);
        boolean timeLimit = true;

        if (optionalClipboard.isPresent()) {
            Clipboards clipboard = optionalClipboard.get();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTimestamp = clipboard.getExpiryTimestamp();

            if (expiryTimestamp.isAfter(now)) {
                timeLimit = false;

                // Calculate remaining time
                Duration duration = Duration.between(now, expiryTimestamp);
                long days = duration.toDays();
                long hours = duration.toHoursPart();
                long minutes = duration.toMinutesPart();
                long seconds  = duration.toSecondsPart();

                // Determine the most relevant unit for display
                String remainingTime;
                if (days > 0) {
                    remainingTime = days + (days == 1 ? " day" : " days");
                } else if (hours > 0) {
                    remainingTime = hours + (hours == 1 ? " hour" : " hours");
                } else if (minutes > 0) {
                    remainingTime = minutes + (minutes == 1 ? " minute" : " minutes");
                } else {
                    remainingTime = seconds  + (seconds  == 1 ? " second" : " seconds ");
                }

                // Pass values to the model
                model.addAttribute("timeLimit", timeLimit);
                model.addAttribute("remainingTime", remainingTime);
                model.addAttribute("copyButton", "copyButton");
                model.addAttribute("content", clipboard.getContent());

                return "code";
            } else {
                clipboardRepository.deleteById(uniqueId); // Delete expired clipboard
            }
        }

        model.addAttribute("timeLimit", timeLimit);
        return "code";
    }




//    @PostMapping("/{uniqueId}")
//	public String saveClipboard(
//	        @PathVariable("uniqueId") String uniqueId,
//	        @RequestParam("content") String content,
//	        @RequestParam("expiryTime") long expiryTime, 
//	        Model model) {
//
//	    System.out.println("I am in save method with uniqueId: " + uniqueId);
//
//	    LocalDateTime expiryTimestamp = LocalDateTime.now().plusMinutes(expiryTime);
//	    
//	    Clipboards clipboard = new Clipboards(uniqueId, content, expiryTimestamp);
//	    clipboardRepository.save(clipboard);
//
//	    try {
//	        String qrCodeImage = QRCodeService.generateQRCode(uniqueId);
//	        model.addAttribute("qrCode", qrCodeImage); 
//	    } 
//	    catch (Exception e) {
//	        e.printStackTrace();
//	        model.addAttribute("error", "Failed to generate QR Code");
//	    }
//	    model.addAttribute("link", uniqueId);
//	    return "success";
//	}

//  private String extractDynamicUrl(HttpServletRequest request) {
//  String requestUri = request.getRequestURI(); // Get the full URI
//  System.out.println("user : "+requestUri);
//  return requestUri.startsWith("/") ? requestUri.substring(1) : requestUri; // Remove leading "/"
//}


//    // Save clipboard data
//    @PostMapping("/save")
//    public String saveClipboard(
//            @RequestParam("content") String content,
//            @RequestParam("expiryTime") long expiryTime, 
//            Model model, HttpServletRequest request) {
//
//    	System.out.println("I am in save method");
//    	String uniqueId = extractDynamicUrl(request);
//    	 
//        // Create unique ID and expiry time
//        //String uniqueId = UUID.randomUUID().toString();
//        LocalDateTime expiryTimestamp = LocalDateTime.now().plusMinutes(expiryTime);
//
//        // Save to database
//        Clipboards clipboard = new Clipboards(uniqueId, content, expiryTimestamp);
//        clipboardRepository.save(clipboard);
//
//        // Send unique link to the user
//        model.addAttribute("link", "/" + uniqueId);
//        return "redirect:"+uniqueId;
//    }

//    @PostMapping("/{uniqueId}")
//    public String saveClipboard(
//            @PathVariable("uniqueId") String uniqueId,
//            @RequestParam("content") String content,
//            @RequestParam("expiryTime") long expiryTime, 
//            Model model) {
//
//        System.out.println("I am in save method with uniqueId: " + uniqueId);
//
//        // Check if the uniqueId already exists in the database
//        Optional<Clipboards> existingClipboard = clipboardRepository.findById(uniqueId);
//
//        if (existingClipboard.isPresent()) {
//            Clipboards clipboard = existingClipboard.get();
//
//            // If the content exists and hasn't expired, update the content
//            if (clipboard.getExpiryTimestamp().isAfter(LocalDateTime.now())) {
//                clipboard.setContent(content);
//                clipboard.setExpiryTimestamp(LocalDateTime.now().plusMinutes(expiryTime));
//                clipboardRepository.save(clipboard);
//
//                model.addAttribute("link", "/" + uniqueId);
//                return "redirect:" + uniqueId;
//            } else {
//                // If expired, delete the old record
//                clipboardRepository.deleteById(uniqueId);
//            }
//        }
//
//        // Create a new clipboard entry if it doesn't exist
//        LocalDateTime expiryTimestamp = LocalDateTime.now().plusMinutes(expiryTime);
//        Clipboards clipboard = new Clipboards(uniqueId, content, expiryTimestamp);
//        clipboardRepository.save(clipboard);
//
//        model.addAttribute("link", "/" + uniqueId);
//        return "redirect:" + uniqueId;
//    }





}
