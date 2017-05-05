package ydh;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import ydh.customer.entity.Customer;
import ydh.customer.realm.CustomerToken;
import ydh.layout.FrontLayout;
import ydh.upload.utils.UploadConfig;
import ydh.utils.ConfigTool;

/**
 * @author lxl
 *
 */
@Service
public class FrontLayoutImpl implements FrontLayout {
	
	@Override
	public ModelAndView layout(String contentView) {
		ModelAndView mav = new ModelAndView("layout");
		Customer loginCustomer = CustomerToken.loginCustomer();
		mav.addObject("contentView", contentView + ".vm");
		mav.addObject("logCus", loginCustomer);
		mav.addObject("imageUrlPrefix",ConfigTool.getString(UploadConfig.imageUrlPrefix));
		mav.addObject("fileUrlPrefix",ConfigTool.getString(UploadConfig.fileUrlPrefix));
		return mav;
	}
	
}
