package core;

/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.Database;
import panels.FootPan;

public class ComposeMailDialog extends JDialog implements ActionListener {
    
    String to, subject, content;
    JPanel glassPan, mailPan;
    JButton closeBut, sendBut, saveBut;
    JTextField toTf, ccTf, bccTf, subjectTf;
    JTextArea contentTA;
    int msgID;
    
    public ComposeMailDialog(int msgID) {
        this(msgID, "", "", "");
    }

    public ComposeMailDialog(int msgID, String content) {
        this(msgID, "", "", content);
    }

    public ComposeMailDialog(int msgID, String to, String subject) {
        this(msgID, to, subject, "");
    }

    public ComposeMailDialog(int msgID, String to, String subject, String content) {
        super(Home.home.homeFrame, "Compose", false);
        this.msgID = msgID;
        this.to = to;
        this.subject = subject;
        this.content = content;
        createGUI();
    }
    
    public void createGUI() {
        Font fnt = new Font(Font.SANS_SERIF, Font.BOLD, 13);
        setUndecorated(true);
        mailPan = new JPanel(new BorderLayout());
        
        JPanel topPan = new JPanel(new BorderLayout());
        topPan.setBackground(Color.darkGray);
        topPan.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 0));
        JLabel titleLab = new JLabel("Compose");
        titleLab.setForeground(Color.white);
        titleLab.setFont(fnt);
        closeBut = new JButton("X");
        closeBut.addActionListener(this);
        closeBut.setBackground(Color.red);
        closeBut.setForeground(Color.white);
        topPan.add(titleLab, "Center");
        topPan.add(closeBut, "East");
        add(topPan, "North");
        
        JPanel topMailPan = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
//        JPanel topMailPan = new JPanel();
//        topMailPan.setLayout(new BoxLayout(topMailPan, BoxLayout.Y_AXIS));
        JLabel toLab = new JLabel("To :");
        toLab.setForeground(Color.blue);
        toLab.setFont(fnt);
        toTf = new JTextField("", 25);
        JLabel ccLab = new JLabel("CC :");
        ccLab.setForeground(Color.blue);
        ccLab.setFont(fnt);
        ccTf = new JTextField("", 25);
        ccTf.setToolTipText("Separate Mail_IDs with a Whitespace");
        JLabel bccLab = new JLabel("BCC :");
        bccLab.setForeground(Color.blue);
        bccLab.setFont(fnt);
        bccTf = new JTextField("", 25);
        bccTf.setToolTipText("Separate Mail_IDs with a Whitespace");
        JLabel subjectLab = new JLabel("Subject :");
        subjectLab.setForeground(Color.blue);
        subjectLab.setFont(fnt);
        subjectTf = new JTextField(subject, 25);
        
        String str[] = to.split(",", 3);
        for(int i=0; i<str.length; i++){
            if(i==0){
                toTf.setText(str[i]);
            } else if(i==1){
                ccTf.setText(str[i]);
            } else if(i==2){
                bccTf.setText(str[i]);
            }
        }
//        topMailPan.add(toLab);
//        topMailPan.add(toTf);
//        topMailPan.add(ccLab);
//        topMailPan.add(ccTf);
//        topMailPan.add(bccLab);
//        topMailPan.add(bccTf);
//        topMailPan.add(subjectLab);
//        topMailPan.add(subjectTf);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        topMailPan.add(toLab, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.EAST;
        topMailPan.add(toTf, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        topMailPan.add(ccLab, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.EAST;
        topMailPan.add(ccTf, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        topMailPan.add(bccLab, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.EAST;
        topMailPan.add(bccTf, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        topMailPan.add(subjectLab, gbc);
        gbc.gridx = 4;
        gbc.gridwidth = 10;
        topMailPan.add(subjectTf, gbc);
        
        contentTA = new JTextArea(content);
        contentTA.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(contentTA);
        jsp.setPreferredSize(new Dimension(400, 250));
        jsp.setBorder(BorderFactory.createMatteBorder(0,1,0,1,Color.darkGray));
        mailPan.add(new JScrollPane(topMailPan), "North");
        mailPan.add(jsp, "Center");
        mailPan.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        mailPan.setBackground(Color.lightGray);
        add(mailPan, "Center");
        
        JPanel botPan = new JPanel(new BorderLayout());
        botPan.setBackground(Color.white);
        botPan.setBorder(BorderFactory.createMatteBorder(1, 0, 8, 0, Color.darkGray));
        sendBut = new JButton(new ImageIcon(this.getClass().getResource("/images/sendIcon.png")));
        sendBut.setRolloverEnabled(true);
        sendBut.setRolloverIcon(new ImageIcon(this.getClass().getResource("/images/sendIconRoll.png")));
        sendBut.setContentAreaFilled(false);
        sendBut.addActionListener(this);
        sendBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBut.setToolTipText("Send");
        sendBut.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        saveBut = new JButton(new ImageIcon(this.getClass().getResource("/images/saveIcon.png")));
        saveBut.setRolloverEnabled(true);
        saveBut.setRolloverIcon(new ImageIcon(this.getClass().getResource("/images/saveIconRoll.png")));
        saveBut.setContentAreaFilled(false);
        saveBut.addActionListener(this);
        saveBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBut.setToolTipText("Save As Draft");
        saveBut.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        botPan.add(sendBut, "West");
        botPan.add(saveBut, "East");
        add(botPan, "South");
//        setOpacity(0.95f);
//        setSize(500,450);
        pack();
        setLocation(Home.home.homeFrame.getWidth() - 70 - getWidth(), Home.home.homeFrame.getHeight() - getHeight() - 5);
        setVisible(true);
//        mailPan.setPreferredSize(new Dimension(600,500));
//        
//        topMailPan.setOpaque(false);
//        mailPan.setOpaque(false);
//        Home.footPan.composeBut.setVisible(false);
//        glassPan.setOpaque(true);
//        glassPan.setBackground(new Color(0, 0, 0,180));
//        glassPan.add(mailPan, "North");
//        Home.home.homeFrame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent axnEve) {
        Object obj = axnEve.getSource();
        if (obj == closeBut) {
            this.setVisible(false);
            this.dispose();
            removeAll();
//            Home.home.homeFrame.setVisible(true);
        } else if (obj == sendBut) {
            to = toTf.getText()+","+ccTf.getText()+","+bccTf.getText();
            String str[] = to.split(",", 3);
            String toStr="", ccStr="", bccStr="";
            for (int i = 0; i < str.length; i++) {
                if (i == 0) {
                    toStr = str[i].trim();
                    toStr = toStr.replaceAll("\'", "\\\\'");
                } else if (i == 1) {
                    ccStr = str[i].trim();
                    ccStr = ccStr.replaceAll("\'", "\\\\'");
                } else if (i == 2) {
                    bccStr = str[i].trim();
                    bccStr = bccStr.replaceAll("\'", "\\\\'");
                }
            }
            this.setVisible(false);
            this.dispose();
//            if(Home.titlePan.connectBut.getText().equals("Connect")){
//                JOptionPane.showMessageDialog(null, "Internet Connection is Disabled", "ERROR", JOptionPane.ERROR_MESSAGE);
//            } else {
                MailActivity macti = new MailActivity(Home.username, Home.password);
                macti.sendMail(toStr, ccStr, bccStr, subjectTf.getText().replaceAll("\'", "\\\\'"), contentTA.getText().replaceAll("\'", "\\\\'"), msgID);
//            }
        } else if (obj == saveBut) {
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            String mail_addresses = toTf.getText().trim()+","+ccTf.getText().trim()+","+bccTf.getText().trim();
            mail_addresses = mail_addresses.replaceAll("\'", "\\\\'");
            String query = "INSERT INTO MESSAGES(mail_addresses, sent_date, subject, content, tag, username) VALUES('" + mail_addresses + "', '" + sqlDate + "', '" + subjectTf.getText().replaceAll("\'", "\\\\'") + "', '" + contentTA.getText().replaceAll("\'", "\\\\'")
                            + "', 'Draft', '" + Home.username + "')";
            Database.setData(query);
            Database.close();
            FootPan.setMessage(FootPan.SAVED_MESSAGE);
        }
    }
}
