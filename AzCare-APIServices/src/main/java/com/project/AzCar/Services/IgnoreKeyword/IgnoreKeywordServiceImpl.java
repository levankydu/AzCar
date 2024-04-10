package com.project.AzCar.Services.IgnoreKeyword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;
import com.project.AzCar.Repositories.IgnoreKeyword.IgnoreKeywordRepository;
@Service
public class IgnoreKeywordServiceImpl  implements IIgnoreKeywordService{
	@Autowired
	private IgnoreKeywordRepository ignoreRepo;
	
	@Override
	public IgnoreKeyword savekeyword(IgnoreKeyword s) {
		// TODO Auto-generated method stub
		return ignoreRepo.save(s);
	}

	@Override
	public IgnoreKeyword addkeyword(IgnoreKeyword s) {
		// TODO Auto-generated method stub
		return ignoreRepo.save(s);
	}

	@Override
	public List<IgnoreKeyword> listkeyword() {
		// TODO Auto-generated method stub
		return ignoreRepo.findAll();
	}

	@Override
	public IgnoreKeyword findByid(int id) {
		// TODO Auto-generated method stub
		return ignoreRepo.findById(id).get();
	}

	@Override
	public void deleteByid(int id) {
		// TODO Auto-generated method stub
		IgnoreKeyword s = ignoreRepo.findById(id).get();
		
		 ignoreRepo.delete(s);	
		
	}

	@Override
	public boolean isIgnore(String a, List<String> b) {
		
		// TODO Auto-generated method stub
		a = a.toLowerCase();
		for(String temp : b)
		{
			if(a.contains((String) temp))
			{
				return true;
				
			}
			
		}
		return false;
	}

}
