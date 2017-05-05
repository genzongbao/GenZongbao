package ydh.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import ydh.customer.entity.Customer;
import ydh.customer.realm.CustomerToken;
import ydh.customer.service.AfterCustomerFreezed;
import ydh.listener.UserSessionListener;
@Service
public class AfterFreezedImpl implements AfterCustomerFreezed {

	@Override
	public void afterCustomerFreezed(Customer customer) {
		List<HttpSession> sessions = UserSessionListener.getSessions();
		for (HttpSession httpSession : sessions) {
			Customer loginCustomer = (Customer)httpSession.getAttribute(CustomerToken.SESSION_KEY);
			if(loginCustomer != null && loginCustomer.getCusPhone().equals(customer.getCusPhone())){
				httpSession.invalidate();
			}
		}
	}

}
