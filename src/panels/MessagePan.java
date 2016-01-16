package panels;
import core.*;
/**
 *
 * @author UlTMATE
 */
import core.Home;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;

public class MessagePan extends JPanel implements ActionListener{
    
    public int pointer, msgID;
    ResultSet workingSet;
    JButton replyBut, forwardBut;
    
    public MessagePan(int rstLoc, ResultSet workingSet){
        pointer = rstLoc;
        this.workingSet = workingSet;
        createGUI();
    }
    
    public void createGUI(){
        setLayout(new BorderLayout());
        JPanel topPan = new JPanel(new BorderLayout());
//        topPan.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        JPanel topCentPan = new JPanel();
        JLabel titleLab = new JLabel();
        titleLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        topCentPan.add(titleLab);
        
        JPanel topRightPan = new JPanel();
//        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        replyBut = new JButton(new ImageIcon("src\\images\\replyIcon.png"));
        replyBut.setBorder(BorderFactory.createEmptyBorder());
        replyBut.addActionListener(this);
        replyBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        replyBut.setToolTipText("Reply");
        replyBut.setContentAreaFilled(false);
        replyBut.setRolloverEnabled(true);
        forwardBut = new JButton(new ImageIcon("src\\images\\forwardIcon.png"));
        forwardBut.setBorder(BorderFactory.createEmptyBorder());
        forwardBut.addActionListener(this);
        forwardBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forwardBut.setToolTipText("Forward");
        forwardBut.setContentAreaFilled(false);
        forwardBut.setRolloverEnabled(true);
        topRightPan.add(replyBut);
        topRightPan.add(forwardBut);
        topPan.add(topCentPan, "Center");
        topPan.add(topRightPan, "East");
        
        JPanel centPan = new JPanel(new BorderLayout());
            JPanel centTopPan = new JPanel(new BorderLayout());
            JPanel centTopLeftPan = new JPanel();
            JLabel fromLab = new JLabel();
            fromLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            JTextArea contentTA = new JTextArea();
//            JEditorPane contentTA = new JEditorPane(JEditorPane.W3C_LENGTH_UNITS,"");
            contentTA.setLineWrap(true);
            contentTA.setEditable(false);
            centTopLeftPan.add(fromLab);
            JPanel centTopRightPan = new JPanel();
            JLabel dateLab = new JLabel();
            dateLab.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            centTopRightPan.add(dateLab);
            centTopPan.add(centTopLeftPan, "West");
            centTopPan.add(centTopRightPan, "East");
        centPan.add(centTopPan, "North");
        centPan.add(new JScrollPane(contentTA), "Center");
        
        add(topPan, "North");
        add(centPan, "Center");
        
        try{
            workingSet.absolute(pointer+1);
            msgID = Home.bodyPan.msgID[pointer];
            titleLab.setText(workingSet.getString("subject"));
            fromLab.setText("From: "+workingSet.getString("mail_addresses"));
            dateLab.setText(workingSet.getString("sent_date"));
            contentTA.setText(workingSet.getString("content"));
        } catch (SQLException sqlExc){
            JOptionPane.showMessageDialog(this, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
        }
        Home.bodyPan.remove(Home.bodyPan.contentPan);
        Home.bodyPan.contentPan = new JScrollPane(this);
        Home.bodyPan.add(Home.bodyPan.contentPan);
        Home.home.homeFrame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent axnEve){
        Object obj = axnEve.getSource();
        if(obj==replyBut){
            try{
                if(Home.composeDial!=null && Home.composeDial.isShowing()){
                    
                } else {
                    Home.composeDial = new ComposeMailDialog(msgID, workingSet.getString("mail_addresses"), workingSet.getString("subject"));
                }
            } catch (SQLException sqlExc) {
                sqlExc.printStackTrace();
            }
        } else if(obj==forwardBut){
            try{
                if(Home.composeDial!=null && Home.composeDial.isShowing()){
                    
                } else {
                    Home.composeDial = new ComposeMailDialog(msgID, workingSet.getString("subject")+ "\n\n" + workingSet.getString("content"));
                }
            } catch (SQLException sqlExc) {
                sqlExc.printStackTrace();
            }
        }
    }
}
