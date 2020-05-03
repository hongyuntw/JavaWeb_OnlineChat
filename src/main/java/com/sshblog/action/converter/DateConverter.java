package com.sshblog.action.converter;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import com.sshblog.util.DateUtils;

public class DateConverter implements WebBindingInitializer {
	
	public void initBinder(WebDataBinder binder,WebRequest request) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.getFormat(DateUtils.YMD_DASH), true));
	}
	
}
