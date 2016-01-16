package core;
import panels.*;
import model.*;
//import images.*;
/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Home {
    
    public static Home home;
    public JFrame homeFrame;
    public static JPanel startPan, glassPan;
    public static TitlePan titlePan;
    public static CategoryPan catPan;
    public static FootPan footPan;
    public static BodyPan bodyPan;
    public static MailActivity mailWork;
    public static String username;
    public static String password;
    public static ComposeMailDialog composeDial;
    
    public Home(){
        createGUI();
        new Database();
    }
    
    private void createGUI(){
        homeFrame = new JFrame("Lookout Express");
//        homeFrame.getGlassPane().add(new JPanel());
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
        homeFrame.setSize(scrDim.width-50, scrDim.height-50);
        homeFrame.setLocationRelativeTo(null);
        try{
            for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(info.getName())){
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch(Exception exc){
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exc2){
                exc2.printStackTrace();
            }
        }
        
        showStartPanel();
        
        homeFrame.setVisible(true);
    }
    
    public void showStartPanel(){
        startPan = new JPanel(new GridBagLayout());
        startPan.setBackground(new Color(154, 12, 12));
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        JButton loginBut = new JButton(new ImageIcon(this.getClass().getResource("/images/loginIcon.png")));
        loginBut.setRolloverIcon(new ImageIcon(this.getClass().getResource("/images/loginIconRoll.png")));
        loginBut.setBorder(BorderFactory.createEmptyBorder());
        loginBut.setCursor(handCursor);
        loginBut.setContentAreaFilled(false);
        loginBut.setRolloverEnabled(true);
//        loginBut.setRolloverIcon();
        loginBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                new LoginDialog();
            }
        });
        JButton signupBut = new JButton(new ImageIcon(this.getClass().getResource("/images/signupIcon.png")));
        signupBut.setRolloverIcon(new ImageIcon(this.getClass().getResource("/images/signupIconRoll.png")));
        signupBut.setBorder(BorderFactory.createEmptyBorder());
        signupBut.setCursor(handCursor);
        signupBut.setContentAreaFilled(false);
        signupBut.setRolloverEnabled(true);
        signupBut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent axnEve){
                new SignupDialog();
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,10,5,10);
        gbc.gridwidth=10; gbc.gridheight=10;
        startPan.add(loginBut, gbc);
        gbc.gridx=10;
        startPan.add(signupBut, gbc);
        homeFrame.add(startPan,  "Center");
        homeFrame.setVisible(true);
    }
    
    public void showLookout(){
        titlePan = new TitlePan();
        catPan = new CategoryPan();
        footPan = new FootPan();
        bodyPan = new BodyPan();
        
        homeFrame.remove(startPan);
        homeFrame.add(titlePan,"North");
        homeFrame.add(catPan, "West");
        homeFrame.add(bodyPan, "Center");
        glassPan = (JPanel)homeFrame.getGlassPane();
        glassPan.setVisible(true);
        glassPan.setLayout(new BorderLayout());
        glassPan.add(footPan, "South");
//        homeFrame.add(southPan, "South");
        homeFrame.repaint();
        homeFrame.setVisible(true);
    }
    
    public void setLogout(){
        homeFrame.remove(titlePan);
        homeFrame.remove(catPan);
        homeFrame.remove(footPan);
        homeFrame.remove(glassPan);
        homeFrame.remove(bodyPan);
        homeFrame.getGlassPane().setVisible(false);
        showStartPanel();
    }
    
    public static void main(String args[]){
        home = new Home();
    }
}
