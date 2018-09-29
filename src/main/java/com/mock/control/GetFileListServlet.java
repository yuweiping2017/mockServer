package com.mock.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetFileListServlet extends HttpServlet{
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = System.getProperty("user.dir") + "\\upload";
		List<String> list = new ArrayList<>();
		File f = new File(path);
		 if (!f.exists()) {
	            System.out.println(path + " not exists");
	            return;
	        }

	        File fa[] = f.listFiles();
	        for (int i = 0; i < fa.length; i++) {
	            File fs = fa[i];
	            if(fs.isFile()) list.add(fs.getName());	           
	        }
	        
	        req.setAttribute("filelist", list);
	        req.getRequestDispatcher("index.jsp").forward(req, resp);;
	        
		
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
