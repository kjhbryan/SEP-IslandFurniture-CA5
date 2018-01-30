/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.CountryEntity;
import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Bryan K
 */
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private FacilityManagementBeanLocal facilityManagementBean;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();
            Member m = new Member();
            m.setName(request.getParameter("name"));
            m.setPhone(request.getParameter("phone"));
            m.setCity(request.getParameter("country"));
            m.setAddress(request.getParameter("address"));
            m.setSecurityQuestion(Integer.parseInt(request.getParameter("securityQuestion")));
            m.setSecurityAnswer(request.getParameter("securityAnswer"));
            m.setAge(Integer.parseInt(request.getParameter("age")));
            m.setIncome(Integer.parseInt(request.getParameter("income")));
            m.setEmail((String)session.getAttribute("memberEmail"));
            String updateStatus = memberEditProfile(m);
            if(updateStatus != null){
                List<CountryEntity> countries = facilityManagementBean.getListOfCountries();
                session.setAttribute("countries",countries);
                
                session.setAttribute("member", m);
                response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?goodMsg=Account updated successfully.");
            }
        }
        catch(Exception ex){
            out.println("\n\n "+ ex.getMessage());
        }
    }
    
    public String memberEditProfile(Member member)
    {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                .path("memberEditProfile")
                .queryParam("name",member.getName())
                .queryParam("phone",member.getPhone())
                .queryParam("country",member.getCity())
                .queryParam("address",member.getAddress())
                .queryParam("securityQuestion",member.getSecurityQuestion())
                .queryParam("securityAnswer",member.getSecurityAnswer())
                .queryParam("age",member.getAge())
                .queryParam("income",member.getIncome())
                .queryParam("email", member.getEmail());
       Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
       Response response = invocationBuilder.post(null);
       
       if(response.getStatus() != 200){
           return null;
       }
       return "Ok";
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
