package panels;
import core.*;
/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FootPan extends JPanel implements ActionListener{
    
    public final static String SENT_MESSAGE="Message Sent Successfully";
    public final static String LOGOUT_MESSAGE="Successfully Logged Out";
    public final static String SAVED_MESSAGE="Message SAVED AS DRAFT";
    public final static String OUTBOX_MESSAGE="Message Stored In Outbox";
    public final static String NO_SELECTION_MESSAGE="No Message(s) Selected";
    public JButton composeBut;
    static JLabel msgLab;
    
    public FootPan(){
        super(new BorderLayout());
        
        JPanel centPan = new JPanel();
        msgLab = new JLabel();
        msgLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        msgLab.setForeground(Color.red);
        centPan.add(msgLab);
        
        JPanel rightPan = new JPanel();
        composeBut = new JButton(new ImageIcon(this.getClass().getResource("/images/composeIcon.png")));
        composeBut.addActionListener(this);
        composeBut.setRolloverEnabled(true);
        composeBut.setRolloverIcon(new ImageIcon(this.getClass().getResource("/images/composeIconRoll.png")));
        composeBut.setToolTipText("Compose Mail");
        composeBut.setContentAreaFilled(false);
        composeBut.setBorder(BorderFactory.createEmptyBorder());
        composeBut.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        composeBut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        composeBut.setBackground(new Color(40,123,26));
        composeBut.setForeground(Color.white);
        rightPan.add(composeBut);
        
        centPan.setOpaque(false);
        rightPan.setOpaque(false);
        setOpaque(false);
//        setBackground(Color.darkGray);
        add(centPan, "Center");
        add(rightPan, "East");
    }
    
    public static void setMessage(String msg){
        new Thread(new Runnable(){
            @Override
            public void run(){
                msgLab.setText(msg);
                try{
                    Thread.sleep(1050);
                } catch(InterruptedException interExc){
                    
                }
                msgLab.setText("");
            }
        }).start();
    }
    
    @Override
    public void actionPerformed(ActionEvent axnEve){
        Object obj = axnEve.getSource();
        if(obj==composeBut) {
            if (Home.composeDial != null && Home.composeDial.isShowing()) {

            } else {
                Home.composeDial = new ComposeMailDialog(0);
            }
        }
    }
}
