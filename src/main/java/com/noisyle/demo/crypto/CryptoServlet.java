package com.noisyle.demo.crypto;

import java.io.IOException;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

public class CryptoServlet extends HttpServlet {

	private static final long serialVersionUID = -7722722080232502772L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String str = req.getParameter("text");
		String key = req.getParameter("key");
		System.out.println("密文：" + str);
		System.out.println("密钥：" + key);

		try {
			byte[] bytes = str.getBytes("UTF-8");
			byte[] keyBytes = key.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			keyBytes = md.digest(keyBytes);
			
			bytes = Base64.decode(bytes);
			bytes = CryptoUtils.decryptAES(bytes, keyBytes, false, keyBytes);
			
			System.out.println("明文：" + new String(bytes, "UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect("");
	}

}
