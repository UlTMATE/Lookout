package core;
import model.*;
/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import javax.swing.*;

public class LoginDialog extends JDialog implements ActionListener{
    
    private JTextField usernameTf;
    private JPasswordField passwordPf;
    private JButton loginBut;
    
    public LoginDialog(){
        createAndShowGUI();
    }
    
    private void createAndShowGUI(){
        setTitle("LogIn");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel centPan = new JPanel(new GridBagLayout());
        centPan.setBackground(Color.gray);
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel usernameLab = new JLabel("E-Mail");
        usernameLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        usernameLab.setForeground(Color.green);
        usernameTf = new JTextField("ultmatevaiv@gmail.com", 15);
        usernameTf.addActionListener(this);
        JLabel passwordLab = new JLabel("Password");
        passwordLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        passwordLab.setForeground(Color.green);
        passwordPf = new JPasswordField("bogkzqvbotetmfqa", 15);
        passwordPf.addActionListener(this);
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor=GridBagConstraints.WEST;
        centPan.add(usernameLab,gbc);
        gbc.gridx=1;
        centPan.add(usernameTf, gbc);
        gbc.gridx=0; gbc.gridy=1;
        centPan.add(passwordLab,gbc);
        gbc.gridx=1;
        centPan.add(passwordPf,gbc);
        
        JPanel botPan = new JPanel();
        botPan.setBackground(Color.darkGray);
        loginBut = new JButton("Login");
        loginBut.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        loginBut.setBackground(new Color(94,181,51));
        loginBut.addActionListener(this);
        botPan.add(loginBut);
        
        add(centPan, "Center");
        add(botPan, "South");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent axnEve) {
        Object obj = axnEve.getSource();
        if (obj == loginBut || obj == passwordPf) {
            String username = usernameTf.getText();
            String password = passwordPf.getText();
            Pattern pat = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-_]+\\.[a-zA-Z]{2,4}");
            Matcher mat = pat.matcher(username);
            if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, "Provide Both E-Mail and Password", "ERROR", JOptionPane.WARNING_MESSAGE);
            } else if (!mat.find()) {
                JOptionPane.showMessageDialog(null, "Provide Valid E-Mail Address", "ERROR", JOptionPane.WARNING_MESSAGE);
            } else if (Database.isValidUser(username, password)) {
                Home.home.showLookout();
                Home.username=username;
                Home.password=password;
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "E-Mail Address and Password Mismatch", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        } else if(obj==usernameTf){
            passwordPf.requestFocus();
        }
    }
}
