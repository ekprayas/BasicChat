package org.chatapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.chatapp.common.SentimentScore;
import org.chatapp.common.SentimentStore;

/**
 * Servlet implementation class SentimentScoreServlet
 */
@WebServlet("/SentimentScoreServlet")
public class SentimentScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SentimentScoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("userName");
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		
		SentimentScore senScore = null;
		if(username != null)
		{
			senScore = SentimentStore.getInstance().getSentimentStoreMap(username);
		}
		else
		{
			senScore = SentimentStore.getInstance().getPairSentimentStoreMap(from, to);
		}
		
		if(senScore == null) {
			senScore = new SentimentScore();
			senScore.setFrom(username);
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write("senscore#" + senScore.toString());
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
