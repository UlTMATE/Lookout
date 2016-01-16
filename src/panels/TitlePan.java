package panels;

import core.*;
/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TitlePan extends JPanel implements ActionListener{

    public JButton toggleCatBut, logoutBut, connectBut;
    private JLabel selectedCatLab;
    Timer timer;
    Thread connectThread;
    private boolean isHidden=false;
    public TitlePan() {
        super(new BorderLayout());

        JPanel leftPan = new JPanel();
        leftPan.setLayout(new BoxLayout(leftPan, BoxLayout.X_AXIS));
        toggleCatBut = new JButton(new ImageIcon("src\\images\\menuIcon.png"));
        toggleCatBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleCatBut.addActionListener(this);
        toggleCatBut.setFont(new Font("arial", Font.BOLD, 17));
        toggleCatBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleCatBut.setContentAreaFilled(false);
        toggleCatBut.setForeground(Color.ORANGE);
        toggleCatBut.setBackground(Color.black);
        toggleCatBut.setRolloverEnabled(true);
        toggleCatBut.setToolTipText("Click To Open Categories");
        toggleCatBut.setOpaque(false);
        selectedCatLab = new JLabel("Lookout");
        selectedCatLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        selectedCatLab.setForeground(Color.ORANGE);
        leftPan.add(toggleCatBut);
        leftPan.add(Box.createHorizontalStrut(8));
        leftPan.add(selectedCatLab);
        leftPan.add(Box.createHorizontalStrut(8));
        
        JPanel rightPan = new JPanel();
        connectBut = new JButton("Download");
        connectBut.addActionListener(this);
        connectBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        connectBut.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        connectBut.setBackground(new Color(40,123,26));
        connectBut.setForeground(Color.white);
        logoutBut = new JButton("Logout");
        logoutBut.addActionListener(this);
        logoutBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBut.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        logoutBut.setBackground(Color.black);
        logoutBut.setForeground(Color.orange);
        
        rightPan.add(connectBut);
        rightPan.add(logoutBut);
        
        rightPan.setOpaque(false);
        leftPan.setOpaque(false);
        setBackground(new Color(154, 12, 12));
        setBorder(BorderFactory.createEmptyBorder(5,2,5,2));
        add(leftPan, "West");
        add(rightPan, "East");
        
//        timer = new Timer(10000, new MailActivity());
    }
    
    public void setTitle(String title){
        title = title.trim();
        selectedCatLab.setText(title);
    }
    
    public String getTitle(){
        return selectedCatLab.getText();
    }
    
    @Override
    public void actionPerformed(ActionEvent axnEve){
        Object obj=axnEve.getSource();
        String str=axnEve.getActionCommand();
        if(obj==logoutBut){
            FootPan.setMessage(FootPan.LOGOUT_MESSAGE);
            Home.home.setLogout();
            
        } else if(obj == toggleCatBut){
//            Home.catPan.hidePan();
            if(this.isHidden){
                System.out.println("call show");
                Home.catPan.showPan();
                
            } else {
                System.out.println("call hide");
                Home.catPan.hidePan();
            }
            this.isHidden = !this.isHidden;
            FootPan.setMessage("Category Button Pressed");
        } else if(str.equals("Download")){
            if (Home.mailWork == null) {
                Home.mailWork = new MailActivity(Home.username, Home.password);
            }
            connectBut.setBackground(Color.red);
            connectBut.setText("Disconnect");
//            connectThread = new Thread(() -> {
                Home.mailWork.receiveMail();
//            });
//            timer.start();
        } else if(str.equals("Disconnect")){
            disconnectNetwork();
        }
    }
    
    public void disconnectNetwork(){
//        timer.stop();
//        try{
//        connectThread.wait();
//        } catch(InterruptedException interExc){
//            interExc.printStackTrace();
//        }
        connectBut.setBackground(new Color(40, 123, 26));
        connectBut.setText("Download");
    }
}
