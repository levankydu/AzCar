package com.project.AzCar.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.AzCar.Entity.OcrResult.OcrResult;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OcrService {
	@Autowired
	private Tesseract tesseract;

	public OcrResult ocr(MultipartFile file) throws IOException, TesseractException {
		File convFile = convert(file);
		String text = tesseract.doOCR(convFile);
		OcrResult ocrResult = new OcrResult();
		ocrResult.setResult(text);
		return ocrResult;
	}

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
}
