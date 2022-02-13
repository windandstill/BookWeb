package cn.aka.goods.utils.vcode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet("/VerifyCodeServlet")
public class VerifyCodeServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
		VerifyCode vc = new VerifyCode();
		//获取一次性验证码图片
		BufferedImage image = vc.getImage();
		// 该方法必须在getImage()方法之后来调用
        //System.out.println(vc.getText());//获取图片上的文本
		//把图片写到指定流中
		VerifyCode.output(image, response.getOutputStream());
		
		// 把文本保存到session中，为LoginServlet验证做准备
		request.getSession().setAttribute("vCode", vc.getText());
	}
}