package com.project.AzCar.Services.IgnoreKeyword;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.AzCar.Entities.IgnoreKeyword.IgnoreKeyword;

@Service
public interface IIgnoreKeywordService {
	public IgnoreKeyword savekeyword(IgnoreKeyword s);
	public IgnoreKeyword addkeyword(IgnoreKeyword s);
	List<IgnoreKeyword> listkeyword();
	IgnoreKeyword findByid(int id);
	void deleteByid(int id);
	boolean isIgnore(String a, List<String> b);
	}
