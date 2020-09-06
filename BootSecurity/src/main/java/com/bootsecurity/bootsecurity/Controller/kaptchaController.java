package com.bootsecurity.bootsecurity.Controller;

import com.bootsecurity.bootsecurity.config.auth.ImageCodeIdentify.KaptchaImageVO;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class kaptchaController {
    @Resource
    DefaultKaptcha captchaProducer;

    @GetMapping(value = "/kaptcha")
    public void getKaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        String text = captchaProducer.createText();
        session.setAttribute("kaptcha_key",new KaptchaImageVO(text,2*60));

        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
        response.addHeader("Cache-Control","post-check=0,pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/jpeg");

        try(ServletOutputStream outputStream = response.getOutputStream()){
            BufferedImage image = captchaProducer.createImage(text);
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        }


    }

}
