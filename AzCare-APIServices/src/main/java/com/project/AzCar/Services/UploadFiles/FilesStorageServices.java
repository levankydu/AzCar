package com.project.AzCar.Services.UploadFiles;


import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageServices {

	public void init();

	  public void save(MultipartFile file,String dirName) throws Exception;

	  public Resource load(String filename,String dirName);

	  public boolean delete(String filename,String dirName);
	  
	  public void deleteAll();

	  public Stream<Path> loadAll(String dirName);
}
