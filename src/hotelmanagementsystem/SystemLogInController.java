/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hotelmanagementsystem;

import UserCreditial.UserCreditialModel;
import companysetup.CompanySetupController;
import companysetup.CompanySetupModel;
import companysetup.CompanySetupView;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import reusableClass.CyptoAES;
import reusableClass.DisplayMessages;
import reusableClass.Function;

/**
 *
 * @author SUSHIL
 */
public class SystemLogInController  extends UserCreditialModel{
    SystemLogInView inView ;
    MainFrameModel model;
    MainFrameView mainview;
    public SystemLogInController(SystemLogInView view, MainFrameModel model,MainFrameView mainview){
        inView = view;
        this.model = model;
        this.mainview = mainview;
        
        inView.addLogInListener(new ValidateUserListener());
        inView.addCancelListener(new ValidateUserListener());
        inView.addTxtListener(new TxtValidateUserListener());
        inView.addWindowListener(new CloseAdapter());
        
    }
     public void RegisterHandler(){
        try{
           if(model.getCompanyRegister()){
            mainview.setCompany(model.getCompanyDetail());
           }
           else{
//              executeCompanySetup  companysetup = new executeCompanySetup(mainview, true); 
              //executing the companysetup files
             
                 CompanySetupModel cmodel ;
                 CompanySetupView cview ;
                 CompanySetupController ccontrol;
     
   
                cmodel = new CompanySetupModel();
                cview = new CompanySetupView(mainview,true);
                cview.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
               ccontrol = new CompanySetupController(cview,cmodel,mainview);
               cview.setVisible(true);
             
              
             
//              companysetup.cview.removeWindowListener(null);
//             companysetup.cview.addWindowCloseListener(new WindowAdapter() {
//                 
//             @Override
//             public void windowClosing(WindowEvent se){
//                 
//              int choice = JOptionPane.showConfirmDialog(mainview, "Do You Want To Close Whole System?", "System Close Window  ",JOptionPane.YES_NO_CANCEL_OPTION);
//              if(choice == JOptionPane.YES_OPTION){
//                  System.exit(0);
//              }
//             }   
//             });
           }
           
        }
        catch(Exception re){
            JOptionPane.showMessageDialog(mainview, re+"From Registerhandler");
        }
    }
    public class ValidateUserListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       try{
           if(e.getActionCommand().equalsIgnoreCase("login")){
               String pass = new String(inView.getPassword());
               String encyptedPassword = CyptoAES.encrypt(pass);
               String username = inView.getUserName();
               if(model.ValidateUser(username, encyptedPassword)){
//                   inView.setModal(false);
                   inView.setVisible(false);
                   mainview.setUserName(username);
                   //check for register
                   RegisterHandler();
                   
                   
                   
                   mainview.setLogOutVisibleTrue();
                   mainview.setChangePasswordVisible(true);
                   mainview.setUserId(model.getUserIdFromUserName(mainview.getUserName()));
                   model.DisplayMenuItemAsCreditial(getUserCreditial(mainview.getUserId()),mainview.getAllMenuItemText(),mainview.getAllToolBar());
                     DateFormat dateformat = new SimpleDateFormat("EEE, MMM  d ,Y");
                    //check it there is date then only  update the date again the point of sale system date
                    if(Function.checkSystemDateExist()){  
                   //check the date of the computer and pointofsale system and state status both doesnot coincide
                    Date posDate = Function.returnSystemDate();
                    Date SystemDate = new Date();
                    if(!dateformat.format(posDate).equals(dateformat.format(SystemDate))){
//                        JOptionPane.showMessageDialog(mainview, posDate.getTime()+"\n"+SystemDate.getTime());
                        DisplayMessages.displayInfo(mainview, "Computer Date( "+dateformat.format(SystemDate)+" ) and Our Software System Date( "+dateformat.format(posDate)+" )  Doesnot Match.\n Please Make Sure You are Running of the Current Date", " Date Info");
                        
                    }
                    }
                    else{
                        DisplayMessages.displayInfo(inView, "Please Setup the Date In order to Perform  Transaction", "System Date Info");
                    }
                   
               }
               else{
                   JOptionPane.showMessageDialog(inView, "Enter Valid UserName and Password");
               }
               
           }
           if(e.getActionCommand().equalsIgnoreCase("Cancel")){
             if(DisplayMessages.displayInputYesNo(inView, "This will Close The System","Exit System")){
              
                  System.exit(0);
              }
           }
           
       }
       catch(HeadlessException ve){
           JOptionPane.showMessageDialog(inView, ve+"from ValidateUserListener");
       }
        }
        
    }
    public class TxtValidateUserListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try{
            inView.returnLogInBtn().doClick();
//               String encyptedPassword = new String(inView.getPassword());
//               String username = inView.getUserName();
//               if(model.ValidateUser(username, encyptedPassword)){
//                    inView.setVisible(false);
////                   inView.setModal(false);
//                   //check for register
//                   RegisterHandler();
//                   
//                  
//                   mainview.setUserName(username);
//                   mainview.setLogOutVisibleTrue();
//                   mainview.setUserId(model.getUserIdFromUserName(mainview.getUserName()));
//                   model.DisplayMenuItemAsCreditial( model.getUserCreditial(mainview.getUserId()),mainview.getAllMenuItemText(),mainview.getAllToolBar());
//        }
//                else{
//                   JOptionPane.showMessageDialog(inView, "Enter Valid UserName and Password");
//               }
        }
        catch(Exception te){
            JOptionPane.showMessageDialog(mainview, te+"from TxtValidateUserlIstener");
        }
        }
        
    }
    
    public class CloseAdapter extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent we){
              int choice = JOptionPane.showConfirmDialog(inView, "This will Close The System","Exit System",JOptionPane.YES_NO_CANCEL_OPTION);
              if(choice == JOptionPane.YES_OPTION){
                  System.exit(0);
              }
        }
    }
}
