package com.project.AzCar.Services.UploadFiles;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.AzCar.Utilities.Constants;

@Service
public class FilesStorageServicesImpl implements FilesStorageServices {

	private final Path brandDir = Paths.get(Constants.ImgDir.BRAND_DIR);
	private final Path carDir = Paths.get(Constants.ImgDir.CAR_DIR);
	private final Path userDir = Paths.get(Constants.ImgDir.USER_DIR);

	@Override
	public void init() {
		try {
			Files.createDirectories(brandDir);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
		try {
			Files.createDirectories(carDir);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}

		try {
			Files.createDirectories(userDir);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}

	}

	@Override
	public void save(MultipartFile file, String dirName) {

		try {
			Files.copy(file.getInputStream(),Paths.get(dirName).resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}

	}

	@Override
	public Resource load(String filename,String dirName) {
		try {
		      Path file = Paths.get(dirName).resolve(filename);
		      Resource resource = new UrlResource(file.toUri());

		      if (resource.exists() || resource.isReadable()) {
		        return resource;
		      } else {
		        throw new RuntimeException("Could not read the file!");
		      }
		    } catch (MalformedURLException e) {
		      throw new RuntimeException("Error: " + e.getMessage());
		    }
	}

	@Override
	public boolean delete(String filename,String dirName) {
		try {
		      Path file = Paths.get(dirName).resolve(filename);
		      return Files.deleteIfExists(file);
		    } catch (IOException e) {
		      throw new RuntimeException("Error: " + e.getMessage());
		    }
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get("./UploadFiles").toFile());

	}

	@Override
	public Stream<Path> loadAll(String dirName) {
		try {
		      return Files.walk(Paths.get(dirName), 1).filter(path -> !path.equals(Paths.get(dirName))).map(Paths.get(dirName)::relativize);
		    } catch (IOException e) {
		      throw new RuntimeException("Could not load the files!");
		    }
		  }
	

}
