package study.spring.springmyshop.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import study.spring.springmyshop.helper.RegexHelper;
import study.spring.springmyshop.helper.UploadItem;
import study.spring.springmyshop.helper.WebHelper;
import study.spring.springmyshop.model.Carts;
import study.spring.springmyshop.model.Members;
import study.spring.springmyshop.service.CartsService;

@Controller
public class ShoppingController {
    /** WebHelper 주입 */
    @Autowired
    WebHelper webHelper;

    /** RegexHelper 주입 */
    @Autowired
    RegexHelper regexHelper;

    /** Service 패턴 구현체 주입 */
    @Autowired
    CartsService cartsService;

    @RequestMapping(value = "/shopping/cart", method=RequestMethod.GET)
    public ModelAndView cart(Model model, HttpSession session, @SessionAttribute(value = "member", required = false) Members members) {
        
        Carts input = new Carts();
        // 비회원인 경우 클라이언트를 식별하기 위한 JSESSION-ID -> 모든 브라우저마다 고유한 값으로 할당된다.
        input.setSessionId(session.getId());
        
        if (members != null) {
            input.setMemberId(members.getId());
        }
        
        List<Carts> output = null;
        
        try {
            output = cartsService.getCartsList(input);
        } catch (Exception e) {
            return webHelper.redirect(null, e.getLocalizedMessage());
        }
        
        if (output != null) {
            for (Carts item : output) {
                System.out.println(item.toString());
                UploadItem img = item.getTitleImg();
                System.out.println(img.toString());
                
                String filePath = img.getFilePath();
                img.setFileUrl(webHelper.getUploadPath(filePath));
                
                try {
                    String thumbnail = webHelper.createThumbnail(filePath, 150, 150, false);
                    img.setThumbnailUrl(webHelper.getUploadPath(thumbnail));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
        
        model.addAttribute("output", output);
        return new ModelAndView("shopping/cart");
    }
    
    @RequestMapping(value = "/shopping/order", method=RequestMethod.POST)
    public ModelAndView cart(Model model, HttpSession session, 
            @SessionAttribute(value = "member", required = false) Members members,
            @RequestParam(value = "cart_id[]") int[] cartId) {
        
        Carts input = new Carts();
        // 비회원인 경우 클라이언트를 식별하기 위한 JSESSION-ID -> 모든 브라우저마다 고유한 값으로 할당된다.
        input.setSessionId(session.getId());
        
        if (members != null) {
            input.setMemberId(members.getId());
        }
        
        input.setIdArr(cartId);
        
        List<Carts> output = null;
        
        try {
            output = cartsService.getCartsList(input);
        } catch (Exception e) {
            return webHelper.redirect(null, e.getLocalizedMessage());
        }
        
        if (output != null) {
            for (Carts item : output) {
                System.out.println(item.toString());
                UploadItem img = item.getTitleImg();
                System.out.println(img.toString());
                
                String filePath = img.getFilePath();
                img.setFileUrl(webHelper.getUploadPath(filePath));
                
                try {
                    String thumbnail = webHelper.createThumbnail(filePath, 150, 150, false);
                    img.setThumbnailUrl(webHelper.getUploadPath(thumbnail));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
        
        model.addAttribute("output", output);
        
        return new ModelAndView("shopping/order");
    }
}
