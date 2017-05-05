package ydh.mvc;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;


public class CicadaWebBindingInitializer implements WebBindingInitializer {
	private String dateFormat = "yyyy-MM-dd";
	private String datetimeFormat = "yyyy-MM-dd HH:mm:ss";
	private LocalValidatorFactoryBean validator;
	
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		PropertyEditor propertyEditor = new CicadaDateEditor(dateFormat, datetimeFormat);
		binder.registerCustomEditor(Date.class, propertyEditor);
		// 字符串处理
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		if (this.validator != null) {
			binder.setValidator(validator);
		}
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		//枚举处理
//		binder.registerCustomEditor(Enum.class, new EnumEditor());
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDatetimeFormat() {
		return datetimeFormat;
	}

	public void setDatetimeFormat(String datetimeFormat) {
		this.datetimeFormat = datetimeFormat;
	}

	public LocalValidatorFactoryBean getValidator() {
		return validator;
	}

	public void setValidator(LocalValidatorFactoryBean validator) {
		this.validator = validator;
	}

}
