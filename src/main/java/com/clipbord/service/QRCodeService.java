package com.clipbord.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeService {
	
	public static String generateQRCode(String baseUrl, String uniqueId) throws WriterException, IOException {
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();

	    String url = baseUrl + uniqueId;
	    // Generate QR code as BitMatrix
	    BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);

	    // Convert BitMatrix to a byte array
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream); 
	    byte[] qrBytes = outputStream.toByteArray();

	    // Convert byte array to Base64 string and return
	    return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrBytes);
	}
	
	
    // Method to generate QR Code as Base64-encoded PNG image
//  public static String generateQRCode(String uniqueId) throws WriterException, IOException {
//      QRCodeWriter qrCodeWriter = new QRCodeWriter();
//      
//      String baseUrl="http://192.168.221.160:7171/"+uniqueId;
//      // Generate QR code as BitMatrix
//      BitMatrix bitMatrix = qrCodeWriter.encode(baseUrl, BarcodeFormat.QR_CODE, 200, 200);
//
//      // Convert BitMatrix to a byte array
//      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);  // Writing to stream as PNG
//      byte[] qrBytes = outputStream.toByteArray();
//
//      // Convert byte array to Base64 string and return
//      return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrBytes);
//  }

}


