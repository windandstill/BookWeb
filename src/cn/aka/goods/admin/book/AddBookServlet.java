package cn.aka.goods.admin.book;

import cn.aka.goods.user.book.domain.Book;
import cn.aka.goods.user.book.service.BookService;
import cn.aka.goods.user.book.service.imp.BookServiceImp;
import cn.aka.goods.user.category.domain.Category;
import cn.itcast.commons.CommonUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/admin/addBookServlet")
public class AddBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        /**
         * 1.commons-fileupload上传三步
         */
        //创建工具 工厂类
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        /**
         * 2.创建解析器对象
         */
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        //设置单个上传文件大小为500KB
        servletFileUpload.setSizeMax(500*1024);
        /**
         * 3.request得到List<FileItem>
         */
        List<FileItem> fileItemList = null;
        try {
            fileItemList = servletFileUpload.parseRequest(request);
        } catch (FileUploadException e) {
            //如果出现异常说明单个文件超出500KB
            error("上传文件超过500KB",request,response);
            return;//程序打挺不继续执行了
        }
        /**
         * 4.把fileItemList封装到book对象中
         * 4.1首先把“普通表单字段”放到一个Map中，再把Map转换成Book和Category对象，再建立两者的关系
         */
        HashMap<String, Object> map = new HashMap<>();
        for (FileItem fileItem:fileItemList) {
            //如果是普通表单字段
            if (fileItem.isFormField()) {
                map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
            }
            //把Map中大部分数据封装到Book对象中
        }
            Book book = CommonUtils.toBean(map, Book.class);
            //把Map中cid封装到Category中
            Category category = CommonUtils.toBean(map, Category.class);
            book.setCategory(category);
        /**
         * 4.2把上传的图片保存起来
         * 1.获取文件名:截取
         * 2.给文件添加前缀:使用uuid前缀,也为避免文件同名现象
         * 3.校验文件的扩展名:只能是jpg
         * 4.校验图片尺寸
         * 5.指定图片的保存路径,需要使用ServletContext#getRealPath()
         * 6.保存
         * 7.把图片路径设置给Book对象
         */
        //获取大图 因为在表单项中第二项
        FileItem fileItem = fileItemList.get(1);
        String filename = fileItem.getName();
        // 截取文件名，因为部分浏览器上传的绝对路径
        int index = filename.lastIndexOf("\\");
        if(index != -1) {
            filename = filename.substring(index + 1);
        }
        // 给文件名添加uuid前缀，避免文件同名现象
        filename = CommonUtils.uuid() + "_" + filename;
        // 转小写 校验文件名称的扩展名
        if(!filename.toLowerCase().endsWith(".jpg")) {
            error("上传的图片扩展名必须是JPG", request, response);
            return;
        }
        // 校验图片的尺寸
        // 保存上传的图片，把图片new成图片对象：Image、Icon、ImageIcon、BufferedImage、ImageIO
         //1. 获取真实路径
        String savepath = this.getServletContext().getRealPath("/book_img");
         //2. 创建目标文件
        File destFile = new File(savepath, filename);
         //3. 保存文件
        try {
            //它会把临时文件重定向到指定的路径，再删除临时文件
            fileItem.write(destFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 校验尺寸
        // 1. 使用文件路径创建ImageIcon
        ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
        // 2. 通过ImageIcon得到Image对象
        Image image = icon.getImage();
        // 3. 获取宽高来进行校验
        if(image.getWidth(null) > 350 || image.getHeight(null) > 350) {
            error("您上传的图片尺寸超出了350*350！", request, response);
            destFile.delete();//删除图片
            return;
        }
        // 把图片的路径设置给book对象
        book.setImage_w("book_img/" + filename);

            // 获取文件名
            fileItem = fileItemList.get(2);//获取小图
            filename = fileItem.getName();
            // 截取文件名，因为部分浏览器上传的绝对路径
            index = filename.lastIndexOf("\\");
            if(index != -1) {
                filename = filename.substring(index + 1);
            }
            // 给文件名添加uuid前缀，避免文件同名现象
            filename = CommonUtils.uuid() + "_" + filename;
            // 校验文件名称的扩展名
            if(!filename.toLowerCase().endsWith(".jpg")) {
                error("上传的图片扩展名必须是JPG", request, response);
                return;
            }
            // 校验图片的尺寸
            // 保存上传的图片，把图片new成图片对象：Image、Icon、ImageIcon、BufferedImage、ImageIO
             //保存图片：1. 获取真实路径
            savepath = this.getServletContext().getRealPath("/book_img");
             //2. 创建目标文件
            destFile = new File(savepath, filename);
             //3. 保存文件
            try {
                fileItem.write(destFile);//它会把临时文件重定向到指定的路径，再删除临时文件
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 校验尺寸
            // 1. 使用文件路径创建ImageIcon
            icon = new ImageIcon(destFile.getAbsolutePath());
            // 2. 通过ImageIcon得到Image对象
            image = icon.getImage();
            // 3. 获取宽高来进行校验
            if(image.getWidth(null) > 350 || image.getHeight(null) > 350) {
                error("您上传的图片尺寸超出了350*350！", request, response);
                destFile.delete();//删除图片
                return;
            }
            // 把图片的路径设置给book对象
            book.setImage_b("book_img/" + filename);

            // 调用service完成保存
            book.setBid(CommonUtils.uuid());
            BookService bookService = new BookServiceImp();
            bookService.add(book);

            // 保存成功信息转发到msg.jsp
            request.setAttribute("msg", "添加图书成功！");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
        }
    private void error(String msg,HttpServletRequest request,HttpServletResponse response)
            throws ServletException ,IOException{
        request.setAttribute("msg",msg);
        request.getRequestDispatcher("adminjsps/admin/book/add.jsp").forward(request,response);
    }
}
