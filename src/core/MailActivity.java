package core;
import model.*;
import panels.*;
/**
 *
 * @author UlTMATE
 */
import java.util.*;
import java.util.regex.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class MailActivity implements ActionListener{
    
    private String username;
    private String password;
    private Properties receiveProps, sendProps;
    private Session sendSession;
    private Store store;
    private Folder inboxFolder;
    private boolean threadState;
    
    public MailActivity(){
        
    }
    
    public MailActivity(String username, String password){
        this.username = username;
        this.password = password;
        try {
            receiveProps = new Properties();
            receiveProps.put("mail.pop3.host", "pop.gmail.com");
            receiveProps.put("mail.pop3.port", "995");
            receiveProps.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            Session receiveSession = Session.getDefaultInstance(receiveProps, null);
            store = receiveSession.getStore("pop3");
            
            sendProps = new Properties();
            sendProps.put("mail.smtp.host", "smtp.gmail.com");
            sendProps.put("mail.smtp.port", "465"); // default port 25
            sendProps.put("mail.smtp.auth", "true");
            sendProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            sendProps.put("mail.debug", "false");
        } catch(NoSuchProviderException nspExc){
            Home.titlePan.disconnectNetwork();
            JOptionPane.showMessageDialog(null, nspExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
        } 
        
    }
    
    synchronized public void receiveMail(){
        try{
            store.connect(username, password);
            inboxFolder = store.getFolder("inbox");
            inboxFolder.open(Folder.READ_ONLY);
            Message[] arr = inboxFolder.getMessages();
            java.sql.Date sentDate; 
            System.out.println("No of Messages : " + arr.length);
            java.sql.Date date = Database.getLastMessageDate();
            for (int i = 0; i < arr.length; i++) {
//                System.out.println("\n--------------------------Message" + (i + 1) + "--------------------------");
//	arr[i].writeTo(System.out);
                Address[] from = arr[i].getFrom();
                String fromStr="";
//                System.out.println("From : " + from[0]);
//                System.out.println("Subject : " + arr[i].getSubject());
//                System.out.println("Date : " + arr[i].getSentDate());
//                System.out.println("Message : " + arr[i].getContent());
                for(int j=0; j<from.length; j++){
                    fromStr+=" "+from[j];
                }
//                Home.bodyPan.inboxSet.moveToInsertRow();
//                Home.bodyPan.inboxSet.updateString("mail_addresses", fromStr.trim());
//                Home.bodyPan.inboxSet.updateDate("sent_date", new java.sql.Date(arr[i].getSentDate().getTime()));
//                Home.bodyPan.inboxSet.updateString("subject", arr[i].getSubject());
//                Home.bodyPan.inboxSet.updateString("content", arr[i].getContent()+"");
//                Home.bodyPan.inboxSet.updateString("tag", "Inbox");
//                Home.bodyPan.inboxSet.updateString("username", username);
//                Home.bodyPan.inboxSet.first();
                sentDate = new java.sql.Date(arr[i].getSentDate().getTime());
                int dayValue = -1;
                int monValue = -1;
                int yearValue = -1;
                if (date != null) {
                    dayValue = date.getDate() - sentDate.getDate();
                    monValue = date.getMonth() - sentDate.getMonth();
                    yearValue = date.getYear() - sentDate.getYear();
                }
                if (yearValue <= 0 && monValue <= 0 && dayValue<0) {
                    String query = "INSERT INTO MESSAGES(mail_addresses, sent_date, subject, content, tag, username) VALUES('" + fromStr.trim().replaceAll("\'", "\\\\'") + "', '" + sentDate + "', '" + arr[i].getSubject().replaceAll("\'", "\\\\'") + "', '" + (arr[i].getContent() + "").replaceAll("\'", "\\\\'")
                            + "', 'Inbox', '" + username + "')";
                    Database.setData(query);
                }
//                System.out.println("\n***RST***\n from="+Home.bodyPan.inboxSet.getString("subject"));
            }
            FootPan.setMessage("All New Messages Downloaded");
        } catch(MessagingException msgExc){
            msgExc.printStackTrace();
            Home.titlePan.disconnectNetwork();
            JOptionPane.showMessageDialog(null, "No Internet Connection", "EXCEPTION", JOptionPane.ERROR_MESSAGE);
        } catch(IOException ioExc){
            Home.titlePan.disconnectNetwork();
            JOptionPane.showMessageDialog(null, ioExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            ioExc.printStackTrace();
        }finally {
            Home.titlePan.disconnectNetwork();
            threadState = true;
//            notifyAll();
        }
    }
    
    synchronized public void sendMail(String to, String cc, String bcc, String subject, String content, int msgID) {
        sendSession = Session.getInstance(sendProps, new SimpleMailAuthenticator(username, password));
        Pattern pat = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-_]+\\.[a-zA-Z]{2,4}");
        Matcher mat = pat.matcher(to);
//        System.out.println("To : " +to);
        if (to.equals("") || !mat.find()) {
            JOptionPane.showMessageDialog(null, "Invalid E-Mail_ID in TO field");
            return;
        }
        ArrayList ccList = null, bccList = null;
        if (cc.length() > 0) {
            StringTokenizer stroker = new StringTokenizer(cc);
            ccList = new ArrayList();
            while (stroker.hasMoreTokens()) {
                String tempCC = stroker.nextToken();
                mat = pat.matcher(tempCC);
                if (tempCC.equals("") || !mat.find()) {
                    JOptionPane.showMessageDialog(null, "Invalid E-Mail_ID(s) in CC field");
                    return;
                }
                ccList.add(cc);
            }

        }
        if (bcc.length() > 0) {
            StringTokenizer stroker = new StringTokenizer(bcc);
            bccList = new ArrayList();
            while (stroker.hasMoreTokens()) {
                String tempBCC = stroker.nextToken();
                mat = pat.matcher(tempBCC);
                if (tempBCC.equals("") || !mat.find()) {
                    JOptionPane.showMessageDialog(null, "Invalid E-Mail_ID(s) in BCC field");
                    return;
                }
                bccList.add(stroker.nextToken());
            }

        }
        try {
            Message msg = new MimeMessage(sendSession);
            msg.setFrom(new InternetAddress(username));

            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (ccList != null) {
                InternetAddress address[] = new InternetAddress[ccList.size()];
                ListIterator iter = ccList.listIterator();
                for (int i = 0; iter.hasNext(); i++) {
                    address[i] = new InternetAddress(iter.next() + "");
                }
                msg.setRecipients(Message.RecipientType.CC, address);
            }
            if (bccList != null) {
                InternetAddress address[] = new InternetAddress[bccList.size()];
                ListIterator iter = bccList.listIterator();
                for (int i = 0; iter.hasNext(); i++) {
                    address[i] = new InternetAddress(iter.next() + "");
                }
                msg.setRecipients(Message.RecipientType.BCC, address);
            }
            msg.setSubject(subject);
            msg.setText(content);
            Transport.send(msg);
            String mail_addresses = to + "," + cc + "," + bcc;
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            String query;
            if (msgID == 0) {
                query = "INSERT INTO MESSAGES(mail_addresses, sent_date, subject, content, tag, username) VALUES('" + mail_addresses + "', '" + sqlDate + "', '" + subject + "', '" + content
                        + "', 'SentMail', '" + Home.username + "')";
            } else {
                query = "UPDATE MESSAGES SET mail_addresses='" +mail_addresses+ "', sent_date='" +sqlDate+ "', subject='" +subject+"', "
                        + "content='" +content+"', tag='SentMail', username='" +username+ "' where msg_ID=" +msgID;
            }
            Database.setData(query);
            Database.close();
            FootPan.setMessage(FootPan.SENT_MESSAGE);
        } catch (MessagingException msgExc) {
            Home.titlePan.disconnectNetwork();
//            JOptionPane.showMessageDialog(null, "No Internet Connection", "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            String mail_addresses = to + "," + cc + "," + bcc;
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            String query;
            if (msgID == 0) {
                query = "INSERT INTO MESSAGES(mail_addresses, sent_date, subject, content, tag, username) VALUES('" + mail_addresses + "', '" + sqlDate + "', '" + subject + "', '" + content
                        + "', 'Outbox', '" + Home.username + "')";
            } else {
                query = "UPDATE MESSAGES SET mail_addresses='" +mail_addresses+ "', sent_date='" +sqlDate+ "', subject='" +subject+"', "
                        + "content='" +content+"', tag='Outbox', username='" +username+ "' where msgID=" +msgID;
            }
            Database.setData(query);
            Database.close();
            FootPan.setMessage(FootPan.OUTBOX_MESSAGE);
            msgExc.printStackTrace();
        }
    }
    
    public void closeConnections() {
        try {
            inboxFolder.close(true);
            store.close();
            Home.titlePan.disconnectNetwork();;
        } catch (MessagingException msgExc) {
            JOptionPane.showMessageDialog(null, msgExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            msgExc.printStackTrace();
        }
    }
    
    @Override
    synchronized public void actionPerformed(ActionEvent axnEve){
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!threadState) {
                    try {
                        wait();
                    } catch (InterruptedException interExc) {
                        interExc.printStackTrace();
                    }
                }
                threadState = false;
                receiveMail();
            }
        });
        receiveThread.start();
    }
}
