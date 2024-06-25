package jhorlamide.BackendServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhorlamide.RequestLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseRequestHandler extends HttpServlet {
   List<String> store = new ArrayList<>();

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
      String responseMessage = "Replied with a hello message";

      res.setContentType("text/plain");
      res.getWriter().println(responseMessage);
      res.getWriter().flush();
      res.getWriter().close();

      RequestLogger.log(req, responseMessage);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
      store.add(getReqData(req));

      var responseMessage = "Data saved successfully";

      res.setContentType("application/json");
      res.getWriter().println("{\"message\": " + responseMessage + " data: " + store + "}");
      res.getWriter().flush();
      res.getWriter().close();

      RequestLogger.log(req, responseMessage);
   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
      var data = getReqData(req);
      String pathInfo = req.getPathInfo(); // pathInfo will be "/:id"
      String[] pathParts = pathInfo.split("/");
      String id = pathParts[1];

      res.setContentType("application/json");
      String responseMessage = "Put request received and processed";

      if (store.contains(id)) {
         res.getWriter().println("{\"message\": \"Record not found\" \"}");
         res.getWriter().flush();
         res.getWriter().close();
      }

      store.add(Integer.parseInt(id), data);

      res.getWriter().println("{\"message\": " + responseMessage + " data: " + store + "}");
      res.getWriter().flush();
      res.getWriter().close();

      RequestLogger.log(req, responseMessage);
   }

   @Override
   protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
      var dataKey = req.getRequestURI();
      var responseMessage = "DELETE request received and processed";

      store.remove(dataKey);

      res.setContentType("application/json");
      res.getWriter().println("{\"message\": " + responseMessage + "}");
      res.getWriter().flush();
      res.getWriter().close();

      RequestLogger.log(req, responseMessage);
   }

   private String getReqData(HttpServletRequest req) throws IOException {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(req.getReader());
      return jsonNode.toString();
   }
}
