package ydh.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ydh.customer.entity.Customer;

public class MyWebUtil
{
  public static HttpServletRequest getRequest()
  {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    return request;
  }

  public static Customer getCurrUserVO()
  {
    HttpServletRequest request =((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();;
    if (request == null) {
      return null;
    }
    HttpSession session = request.getSession();
    if (session == null) {
      return null;
    }
    Object o = session.getAttribute("loginCustomer");
    if (o == null) {
      return null;
    }
    if (Customer.class.equals(o.getClass())) {
      return (Customer)o;
    }

    return null;
  }
}