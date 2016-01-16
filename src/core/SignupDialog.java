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

public class SignupDialog extends JDialog implements ActionListener {
    
    private JTextField nameTf, usernameTf;
    private JPasswordField passwordPf;
    private JButton signupBut;
    
    public SignupDialog(){
        createAndShowGUI();
    }
    
    private void createAndShowGUI(){
        setTitle("Signup");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel centPan = new JPanel(new GridBagLayout());
        centPan.setBackground(Color.gray);
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel nameLab = new JLabel("Name");
        nameLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        nameLab.setForeground(new Color(51,204,255));
        nameTf = new JTextField("", 15);
        nameTf.addActionListener(this);
        JLabel usernameLab = new JLabel("E-Mail");
        usernameLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        usernameLab.setForeground(new Color(51,204,255));
        usernameTf = new JTextField("", 15);
        usernameTf.addActionListener(this);
        JLabel passwordLab = new JLabel("Password");
        passwordLab.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        passwordLab.setForeground(new Color(51,204,255));
        passwordPf = new JPasswordField("", 15);
        passwordPf.addActionListener(this);
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor=GridBagConstraints.WEST;
        centPan.add(nameLab, gbc);
        gbc.gridx=1;
        centPan.add(nameTf, gbc);
        gbc.gridx=0; gbc.gridy=1;
        centPan.add(usernameLab,gbc);
        gbc.gridx=1;
        centPan.add(usernameTf, gbc);
        gbc.gridx=0; gbc.gridy=2;
        centPan.add(passwordLab,gbc);
        gbc.gridx=1;
        centPan.add(passwordPf,gbc);
        
        JPanel botPan = new JPanel();
        botPan.setBackground(Color.darkGray);
        signupBut = new JButton("Signup");
        signupBut.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        signupBut.setBackground(new Color(68,129,162));
        signupBut.addActionListener(this);
        botPan.add(signupBut);
        
        add(centPan, "Center");
        add(botPan, "South");
        setSize(300,200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent axnEve) {
        Object obj = axnEve.getSource();
        if (obj == signupBut || obj == passwordPf) {
            String name = nameTf.getText();
            String username = usernameTf.getText();
            String password = passwordPf.getText().replaceAll("\'", "\\\\'");
            Pattern pat = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-_]+\\.[a-zA-Z]{2,4}");
            Matcher mat = pat.matcher(username);
            if (username.equals("") || password.equals("") || name.equals("")) {
                JOptionPane.showMessageDialog(null, "Fill All The Fields");
            } else if (!mat.find()) {
                JOptionPane.showMessageDialog(null, "Provide Valid E-Mail Address");
            } else {
                if(Database.isValidUser(username, password)){
                    JOptionPane.showMessageDialog(null, "User Already Exists");
                } else {
                    Database.createAccount(name, username, password);
                    dispose();
                }
            }
        } else if(obj == nameTf){
            usernameTf.requestFocus();
        } else if(obj == usernameTf){
            passwordPf.requestFocus();
        }
    }
}
