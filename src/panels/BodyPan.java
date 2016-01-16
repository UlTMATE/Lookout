package panels;
import core.*;
import model.*;
//import model.*;
/**
 *
 * @author UlTMATE
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.sql.*;

public class BodyPan extends JPanel implements ActionListener, MouseListener, ListSelectionListener {
    
    Object rows[][];
    int msgID[];
    JTable table;
    DefaultTableModel tableModel;
    JToolBar toolBar;
    JCheckBox selectAllCB;
    JButton backBut, refreshBut, deleteBut, restoreBut;
    Database db;
//    public ResultSet inboxSet, sentmailSet, draftSet, outboxSet, trashSet, 
    public ResultSet workingSet;
    JScrollPane contentPan;
//    int selectedMails=0;
    
    public BodyPan(){
        createGUI();
    }
    
    public void createGUI(){
        setLayout(new BorderLayout());
        toolBar = new JToolBar("options", JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setOpaque(true);
        toolBar.setBackground(new Color(154, 12, 12));
        toolBar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        selectAllCB = new JCheckBox("Select All");
        selectAllCB.setVisible(false);
        selectAllCB.setForeground(Color.white);
        selectAllCB.addActionListener(this);
        refreshBut = new JButton(new ImageIcon(this.getClass().getResource("/images/reloadIcon.png")));
        refreshBut.setVisible(false);
        refreshBut.setToolTipText("Refresh");
        refreshBut.setForeground(new Color(20,88,49));
        refreshBut.addActionListener(this);
        backBut = new JButton(new ImageIcon(this.getClass().getResource("/images/backIcon.png")));
        backBut.setVisible(false);
        backBut.setToolTipText("Back");
        backBut.setForeground(new Color(20,88,49));
        backBut.addActionListener(this);
        deleteBut = new JButton(new ImageIcon(this.getClass().getResource("/images/trashIcon.png")));
        deleteBut.setToolTipText("Delete");
        deleteBut.setForeground(Color.red);
        deleteBut.setVisible(false);
        deleteBut.addActionListener(this);
        deleteBut.setBackground(Color.red);
        restoreBut = new JButton(new ImageIcon(this.getClass().getResource("/images/restoreIcon.png")));
        restoreBut.setToolTipText("Restore");
        restoreBut.setForeground(Color.blue);
        restoreBut.setVisible(false);
        restoreBut.addActionListener(this);
        toolBar.addSeparator(new Dimension(10,5));
        toolBar.add(selectAllCB);
        toolBar.add(backBut);
        toolBar.add(refreshBut);
        toolBar.add(deleteBut);
        toolBar.add(restoreBut);
        add(toolBar, "North");
        JPanel centPan = new JPanel(new GridBagLayout());
        centPan.setBackground(new Color(52,86,70));
        JLabel label = new JLabel("Select A Category From Left Pane");
        label.setFont(new Font("arial", Font.BOLD, 35));
        label.setForeground(Color.yellow);
        centPan.add(label);
        contentPan = new JScrollPane(centPan);
        contentPan.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(contentPan, "Center");
        contentPan.repaint();
        
        db = new Database();
//        inboxSet = db.getData("SELECT * FROM messages WHERE tag='inbox' ORDER BY msg_id desc");
//        sentmailSet = db.getData("SELECT * FROM messages WHERE tag='sentmail' ORDER BY msg_id desc");
//        draftSet = db.getData("SELECT * FROM messages WHERE tag='draft' ORDER BY msg_id desc");
//        outboxSet = db.getData("SELECT * FROM messages WHERE tag='outbox' ORDER BY msg_id desc");
//        trashSet = db.getData("SELECT * FROM messages,trash WHERE messages.tag='trash' and messages.msg_id=trash.msg_id ORDER BY deleted_at desc");
        
    }
    public void setContent(String cat){
        cat=cat.trim();
        selectAllCB.setVisible(false);
        selectAllCB.setSelected(false);
        deleteBut.setVisible(false);
        restoreBut.setVisible(false);
        refreshBut.setVisible(true);
        Object columns[]=null;
        int count=0;
        switch (cat){
            case "Inbox":
                columns = new Object[]{"", "From", "Date", "Subject", "Content"};
                count = Database.getCount("Inbox");
                workingSet = db.getData("SELECT * FROM messages WHERE tag='inbox' ORDER BY msg_id desc");;
                break;
            case "SentMail":
                columns = new Object[]{"", "To", "Date", "Subject", "Content"};
                count = Database.getCount("Sentmail");
                workingSet = db.getData("SELECT * FROM messages WHERE tag='sentmail' ORDER BY msg_id desc");
                break;
            case "Draft":
                columns = new Object[]{"", "To", "Date", "Subject", "Content"};
                count = Database.getCount("Draft");
                workingSet = db.getData("SELECT * FROM messages WHERE tag='draft' ORDER BY msg_id desc");
                break;
            case "Outbox":
                columns = new Object[]{"", "To", "Date", "Subject", "Content"};
                count = Database.getCount("Outbox");
                workingSet = db.getData("SELECT * FROM messages WHERE tag='outbox' ORDER BY msg_id desc");
                break;
            case "Trash":
//                restoreBut.setVisible(true);
                columns = new Object[]{"", "To/From", "Date", "Subject", "Content"};
                count = Database.getCount("Trash");
                workingSet = db.getData("SELECT * FROM messages,trash WHERE messages.tag='trash' and messages.msg_id=trash.msg_id ORDER BY deleted_at desc");
                break;
            default: System.out.println("in default case");
        }
        if (count > 0) {
            selectAllCB.setVisible(true);
            rows = new Object[count][];
            msgID = new int[count];
            try {
                workingSet.beforeFirst();
                for (int i = 0; i < count && workingSet.next(); i++) {
                    msgID[i] = workingSet.getInt(1);
                    rows[i] = new Object[]{false, workingSet.getString(2), workingSet.getDate(3), workingSet.getString(4), workingSet.getString(5)};
                }
            } catch (SQLException sqlExc) {
                JOptionPane.showMessageDialog(null, sqlExc, "EXCEPTION", JOptionPane.ERROR_MESSAGE);
                sqlExc.printStackTrace();
            }

            tableModel = new MyDefaultTableModel(rows, columns);
            table = new JTable(tableModel);
            table.getSelectionModel().addListSelectionListener(this);
            table.addMouseListener(this);
            table.getTableHeader().setOpaque(true);
            table.getTableHeader().setReorderingAllowed(false);
//            table.getTableHeader().setBackground(Color.blue);
            table.getTableHeader().setForeground(Color.blue);
//        table.setRowSelectionAllowed(false);
//            table.setColumnSelectionAllowed(false);
            table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            table.setRowHeight(20);
            table.setFillsViewportHeight(true);

            TableColumn column = null;
            for (int i = 0; i < 5; i++) {
                column = table.getColumnModel().getColumn(i);
                if (i == 0) {
                    column.setPreferredWidth(6);
                } else if (i == 3) {
                    column.setPreferredWidth(250);
                } else if (i == 4) {
                    column.setPreferredWidth(450);
                } else {
                    column.setPreferredWidth(40);
                }
            }
            table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

            remove(contentPan);
            contentPan = new JScrollPane(table);
            contentPan.setBackground(Color.orange);
            contentPan.setOpaque(true);
            contentPan.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            add(contentPan, "Center");
            Home.home.homeFrame.setVisible(true);
        } else {
            JPanel centPan = new JPanel(new GridBagLayout());
            centPan.setBackground(new Color(52,86,70));
            JLabel label = new JLabel("No Messages In This Category");
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
            label.setForeground(Color.orange);
            centPan.add(label);
            remove(contentPan);
            contentPan = new JScrollPane(centPan);
            contentPan.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            add(contentPan, "Center");
            contentPan.repaint();
        }
    }
    
    public ArrayList getSelectedMessages(){
        ArrayList list = new ArrayList();
        for(int i=0; i<table.getRowCount(); i++){
            if(((Boolean)table.getValueAt(i, 0)) == true){
                list.add(msgID[i]);
            }
        }
        return list;
    }
    
    @Override
    public void actionPerformed(ActionEvent axnEve){
        Object obj = axnEve.getSource();
        
        if(obj == selectAllCB){
            Boolean state;
            if(selectAllCB.isSelected()){
                state = true;
                deleteBut.setVisible(true);
                if(Home.titlePan.getTitle().equals("Trash")){
                    restoreBut.setVisible(true);
                }
            } else {
                state = false;
                deleteBut.setVisible(false);
                if(Home.titlePan.getTitle().equals("Trash")){
                    restoreBut.setVisible(false);
                }
            }
            for(int i=0; i<table.getRowCount(); i++){
                table.setValueAt(state, i, 0);
            }
        } else if (obj==refreshBut || obj==backBut){
            setContent(Home.titlePan.getTitle());
            backBut.setVisible(false);
        } else if(obj == deleteBut){
            ArrayList selectedMessages = getSelectedMessages();
            if(selectedMessages.isEmpty()){
                FootPan.setMessage(FootPan.NO_SELECTION_MESSAGE);
            } else {
                int option = JOptionPane.showConfirmDialog(Home.home.homeFrame, "Are You Sure?", "DELETE", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(option==0){
                    Database.deleteMessages(selectedMessages, Home.titlePan.getTitle());
                    setContent(Home.titlePan.getTitle());
                }
            }
        } else if(obj == restoreBut){
            ArrayList selectedMessages = getSelectedMessages();
            if(selectedMessages.isEmpty()){
                FootPan.setMessage(FootPan.NO_SELECTION_MESSAGE);
            } else {
                int option = JOptionPane.showConfirmDialog(Home.home.homeFrame, "Are You Sure?", "RESTORE", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(option==0){
                    Database.restoreMessages(selectedMessages);
                    setContent(Home.titlePan.getTitle());
                }
            }
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent lsEve) {
        int rowNum = table.getSelectedRow();
        int colNum = table.getSelectedColumn();
//            System.out.println("Reached in else");
        if(colNum > 0) {
            selectAllCB.setVisible(false);
            deleteBut.setVisible(false);
            new MessagePan(rowNum, workingSet);
            backBut.setVisible(true);
            Home.home.homeFrame.setVisible(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ArrayList list = getSelectedMessages();
        if(list.size()==0){
            deleteBut.setVisible(false);
            restoreBut.setVisible(false);
        } else {
            deleteBut.setVisible(true);
            if(Home.titlePan.getTitle().equals("Trash")){
                restoreBut.setVisible(true);
                System.out.println("Here inside");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    class MyDefaultTableModel extends DefaultTableModel {
        
        MyDefaultTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }
        
        @Override
        public Class getColumnClass(int c){
            return getValueAt(0, c).getClass();
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            if(column==0){
                return true;
            }
            return false;
        }
    }
}
