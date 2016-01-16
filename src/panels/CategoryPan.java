package panels;
import core.*;
/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CategoryPan extends JPanel implements ListSelectionListener{
    
    JList list;
    DefaultListModel listModel;
    private boolean hidden=false;
    
    public CategoryPan(){
        super(new BorderLayout());
        listModel = new DefaultListModel();
        listModel.addElement("  Inbox   ");
        listModel.addElement("  SentMail   ");
        listModel.addElement("  Draft   ");
        listModel.addElement("  Outbox   ");
        listModel.addElement("  Trash   ");
        list = new JList(listModel);
        list.addListSelectionListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCursor(new Cursor(Cursor.HAND_CURSOR));
        list.setFocusable(false);
        list.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        list.setForeground(Color.lightGray);
        list.setSelectionForeground(Color.green);
//        list.setSelectionBackground(new Color(255,164,62));
        list.setSelectionBackground(new Color(52,86,70));
        list.setOpaque(false);
        list.setBackground(new Color(154, 12, 12));
        setBackground(new Color(154, 12, 12));
        add(Box.createVerticalStrut(34), "North");
//        add(Box.createHorizontalStrut(10), "East");
//        add(Box.createHorizontalStrut(10), "West");
        add(list,"West");
//        System.out.println("List width " +this.getSize());
    }
    
    public void hidePan(){
        System.out.println("in side hide");
        new Thread() {
            public void run() {
                Home.home.homeFrame.repaint();
            }
        }.start();
        new Thread(() -> {
            
            int w = Home.catPan.getPreferredSize().width;
            int h = Home.catPan.getPreferredSize().height;
            
                for(int i=0; w > 0; i+=5){
//                    list.setSize(new Dimension(this.getWidth() - i, list.getHeight()));
                    Home.catPan.setPreferredSize(new Dimension(w -= i, h));
                    try{
                        Thread.sleep(50);
                    } catch (InterruptedException interExc) {
                        
                    }
                }
             
            System.out.println("hide");
        }).start();
    }
    
    public void showPan(){
        System.out.println("process - hide with :"+0);
        new Thread(() -> {
             int w = 0;
            int h = 100;
                for(int i=0; i<62; i+=5) {
                    System.out.println("process - hide with :"+i);
                    Home.catPan.setPreferredSize(new Dimension(w += 10, h));
//                    list.setSize(new Dimension(this.getWidth() + 1, list.getHeight()));
                    try{
                        Thread.sleep(50);
                    } catch (InterruptedException interExc) {
                        
                    }
                }
                System.out.println("Completed showing");
            System.out.println("visible");
        }).start();
        new Thread() {
            public void run() {
                Home.home.homeFrame.repaint();
            }
        }.start();
    }
    
    @Override
    public void valueChanged(ListSelectionEvent lsEve){
        String cat = list.getSelectedValue()+"".trim();
        Home.titlePan.setTitle(cat);
        Home.bodyPan.setContent(cat);
    }
}
