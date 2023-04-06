package de.unistuttgart.iste.ese.api.toDos;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import de.unistuttgart.iste.ese.api.assignees.Assignee;


@Component
@PropertySource("classpath:application.properties")
//@ConfigurationProperties(prefix="mail")
public class ToDoService {
    @Value("${mail.username}")
    String username;
    @Value("${mail.password}")
    String password;
    @Value("${mail.address}")
    String senderAddress;
    @Autowired
    ToDoRepository repository;
    
    
    
    public ByteArrayInputStream loadCSV() {
        List<ToDo> toDos = (List<ToDo>) repository.findAll();
        ByteArrayInputStream in = toDosToCSV(toDos);
        return in;
      }
    
    
    public static ByteArrayInputStream toDosToCSV(List<ToDo> toDos) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
        	List<String> headline=Arrays.asList("id","title","description","finished","assignees","createdDate","dueDate","finishedDate");
        	csvPrinter.printRecord(headline);
          for (ToDo toDo : toDos) {
            List<String> data = new ArrayList<String>(Arrays.asList(
                  String.valueOf(toDo.getId()),
                  toDo.getTitle(),
                  toDo.getDescription(),
                  String.valueOf(toDo.isFinished())    
                ));
            //create name list of assignees
            String assigneeNames="";
            if (!toDo.getAssigneeList().isEmpty()) {
            	int i=0; //count the loop iteration for "+"
            	for (Assignee assignee: toDo.getAssigneeList()) {
                	assigneeNames=assigneeNames+assignee.getPrename()+" "+ assignee.getName();
                	if(i<toDo.getAssigneeList().size()-1) {
                		assigneeNames=assigneeNames+"+";
                	}
                	i++;
                }
            data.add(assigneeNames);
            }else {
            	data.add("");
            }
            data.add(new SimpleDateFormat("yyyy-MM-dd").format(toDo.getCreatedDate()));
            if(toDo.getDueDate().getTime()!=0) {
            	data.add(new SimpleDateFormat("yyyy-MM-dd").format(toDo.getDueDate()));
            }else {
            	data.add("");
            }
            if(toDo.isFinished()) {
            	data.add(new SimpleDateFormat("yyyy-MM-dd").format(toDo.getFinishedDate()));
            }else {
            	data.add("");
            }
            //System.out.println(data);
            csvPrinter.printRecord(data);
          }
          
          csvPrinter.flush();
          return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
          throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
      }
    
    
    
	 public boolean sendMail(String recipientAddress, String text) {

    // instantiate SMTP properties (taken from
    // https://www.tik.uni-stuttgart.de/en/support/service-manuals/e-mail/)
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.uni-stuttgart.de");
    props.put("mail.smtp.port", "587");

    // get a Session object with the properties
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    try {
        // create a default MimeMessage object and fill its attributes
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderAddress));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipientAddress));
        message.setSubject("You have been added to a new ToDo!");
        message.setText(text);

        // send message
        Transport.send(message);

        //System.out.println("Sent message successfully....");
        return true;

    } catch (MessagingException e) {
    	//System.out.println("Something went wrong....");
    	//System.out.println(e);
        return false;
        
    }
	 }
}
