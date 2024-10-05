package utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class SendTestAutomationReport {
    static Logger log = LogManager.getLogger(SendTestAutomationReport.class);
    public static void main(String [] args){
        if(PropertiesFileManager.getPropertyValue("SEND_EMAIL_AFTER_EXECUTION").equalsIgnoreCase("Yes")){
            log.info("############# Sending Automation Report to the mail ########");
            sendMailAfterExecution();
        }else{
            log.info("Sending of Test Automation Report is not enabled. You can enable it from environment.properties");
        }
    }
     private static void sendMailAfterExecution(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "mailrelay.cloud");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        Session session = Session.getDefaultInstance(props);
        //String jiraExecutionLink= CONFIG.getProperty("Xray_ExecutionLink").toString()+CONFIG.getProperty("Xray_ExecutionKey").toString();
        String jiraExecutionLink= PropertiesFileManager.getPropertyValue("Xray_ExecutionLink").toString()+PropertiesFileManager.getPropertyValue("Xray_ExecutionKey").toString();
        String jiraDashboardLink= "https://jira.cloud/secure/Dashboard.jspa?selectPageId=XXXXX";
        File latestFile =null;
        String latestFileName ="";
        try {
            MimeMessage message = new MimeMessage(session);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            String executionEnvironment = System.getProperty("env").toUpperCase();
            message.setFrom(new InternetAddress("testAutomation@test.com"));
            InternetAddress[] toAddresses = {
                    new InternetAddress("ssssalunkhe@gmail.com")
            };
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(executionEnvironment+" - Test Automation Execution Result of Project Name Test Automation");
            messageBodyPart.setContent(
                    "<html><body><p style=\"font-family: Calibri, sans-serif; font-size: 16px;\"><br>Hi All,</br><br>Test automation execution is completed on the <b>"+executionEnvironment+"</b> Environment.</br><br> Attached the test results document for more details.</br>" +
                            "<br>Here is the Test execution link from JIRA:  "+jiraExecutionLink+"</br>"+
                            "<br>Here is the Dashboard link from JIRA:  "+jiraDashboardLink+"</br>"+
                            "<br>Thanks </br> QA Automation Team.</p>" +
                            "<br><p style=\"font-family: Calibri, sans-serif; font-size: 12px;\">This is an auto generated email. Please do not reply.</p></br></body></html>", "text/html");
            multipart.addBodyPart(messageBodyPart);
            try{
                String[] files = {System.getProperty("user.dir")+"/target/cucumber-reports.html",System.getProperty("user.dir")+"/test-output/HtmlReport/TestExecutionResultReport.html",System.getProperty("user.dir")+"/test-output/PdfReport/TestExecutionResult.pdf"};
                for (String file : files) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(file);
                    multipart.addBodyPart(attachmentPart);
                }
                message.setContent(multipart);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            Transport.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
