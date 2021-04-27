
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Border;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class MainSystem {

    static Validations _validations = new Validations();

    static File _loanFile = new File("loans.fun");
    static File _userFile = new File("users.fun");
    static File _bookFile = new File("books.fun");

    static List<Loan> _loanCollection = new ArrayList<Loan>();
    static List<User> _userCollection = new ArrayList<User>();
    static List<Book> _bookCollection = new ArrayList<Book>();

    private JTable _table;
    private JScrollPane _scrollPane;
    private JFrame _frame;
    private JTextField _idField, _nameField, _phoneField, _residenceField,
            _isbnField, _titleField, _authorField, _copiesRemainingField;
    private JComboBox _userBox, _bookBox, _dateBox, _monthBox, _yearBox;
    private String dates[]
		= { "01", "02", "03", "04", "05",
                    "06", "07", "08", "09", "10",
                    "11", "12", "13", "14", "15",
                    "16", "17", "18", "19", "20",
                    "21", "22", "23", "24", "25",
                    "26", "27", "28", "29", "30", "31" };
	private String months[]
		= { "01", "02", "03", "04",
                    "05", "06", "07", "08",
                    "09", "10", "11", "12" };
	private String years[]
		= { "2019", "2020", "2021" };

    public static void main(String[] args) throws IOException {
        LoadFiles();
        MainSystem main = new MainSystem();
        main.CreateMainMenu();
    }

    private void CreateMainMenu() {
        _frame = new JFrame();
        JPanel _panel = new JPanel();
        JButton _button1, _button2, _button3, _button4;

        _frame.setTitle("Menu Principal!");
        _frame.setSize(750, 520);
        _frame.setLocationRelativeTo(null);
        _frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        _panel.setLayout(new GridLayout(4, 1));

        _button1 = new JButton("Menu de Empréstimos");
        _button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    LoanGUI();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        _panel.add(_button1);

        _button2 = new JButton("Menu de Usuários");
        _button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    UserGUI();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        _panel.add(_button2);

        _button3 = new JButton("Menu da Biblioteca");
        _button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    BookGUI();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        _panel.add(_button3);

        _button4 = new JButton("Exit");
        _button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _frame.dispose();
                Exit();
            }
        });
        _panel.add(_button4);

        _frame.add(_panel);
        _frame.setVisible(true);
    }
    
    public void LoanGUI() throws IOException {
        _frame.dispose();
        _frame = new JFrame();
        _frame.setTitle("Loan Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        _table = new JTable(new LoanJTable());

        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _frame.dispose();
                CreateMainMenu();
            }
        });
        subPanel1.add(backButton);
        panel.add(subPanel1, BorderLayout.NORTH);

        JPanel subPanel2 = new JPanel();
        JButton requestBookButton = new JButton("Request Book");
        requestBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    RequestBook();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JButton returnBookButton = new JButton("Return Book");
        returnBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ReturnBook();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        subPanel2.add(requestBookButton);
        subPanel2.add(returnBookButton);
        panel.add(subPanel2, BorderLayout.SOUTH);

        _scrollPane = new JScrollPane(_table);
        panel.add(_scrollPane, BorderLayout.CENTER);

        _frame.add(panel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(1200, 600);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    class LoanJTable extends AbstractTableModel {

        private String[] columnNames = {"Número de Empréstimo", "Nome do Usuário", "Nome do Livro", "Data de Empréstimo", "Data Prevista da Devolução", "Multa"};
        Object[][] rows = new Object[_loanCollection.size()][columnNames.length];

        public LoanJTable() {
            int count = 0;
            Iterator<Loan> iter = _loanCollection.iterator();
            while (iter.hasNext()) {
                Loan loan = (Loan) iter.next();
                int i = 0;
                rows[count][i++] = loan._loanNumber;
                rows[count][i++] = loan._user._name.toString();
                rows[count][i++] = loan._book._title.toString();
                rows[count][i++] = loan._borrowDate.toString();
                rows[count][i++] = loan._expectedReturnDate.toString();
                if(loan._expectedReturnDate.isBefore(_validations.GetCurrentDate())){
                    loan._debt = ChronoUnit.DAYS.between(loan._expectedReturnDate, _validations.GetCurrentDate()) * 50;
                }
                rows[count][i++] = loan._debt;
                count++;
            }
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public int getRowCount() {
            return rows.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int column) {
            return rows[row][column];
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
    }

    private void RequestBook() throws IOException {
        _frame.dispose();
        _frame = new JFrame();
        _frame.setTitle("Loan Request Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    LoanGUI();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel1.add(backButton);
        panel.add(subPanel1, BorderLayout.NORTH);

        JLabel userLabel = new JLabel("Usuário: ");
        String users[] = new String[_userCollection.size()];
        int count = 0;
        for (User user : _userCollection) {
            users[count] = user._name;
            count++;
        }
        _userBox = new JComboBox(users);
        JLabel bookLabel = new JLabel("Livro: ");
        String books[] = new String[_bookCollection.size()];
        count = 0;
        for (Book book : _bookCollection) {
            books[count] = book._title;
            count++;
        }
        _bookBox = new JComboBox(books);
        JLabel dateLabel = new JLabel("Dara de Empréstimo: ");
        _dateBox = new JComboBox(dates);
        _monthBox = new JComboBox(months);
        _yearBox = new JComboBox(years);
        JPanel subPanel2 = new JPanel(new GridLayout(3, 2));
        subPanel2.add(userLabel);
        subPanel2.add(_userBox);
        subPanel2.add(bookLabel);
        subPanel2.add(_bookBox);
        subPanel2.add(dateLabel);
        JPanel subPanel3 = new JPanel(new GridLayout(1, 3));
        subPanel3.add(_yearBox);
        subPanel3.add(_monthBox);
        subPanel3.add(_dateBox);
        subPanel2.add(subPanel3);
        panel.add(subPanel2, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    CheckLoanRegistry(_userBox.getSelectedIndex(), _bookBox.getSelectedIndex(), _dateBox.getSelectedItem().toString(), _monthBox.getSelectedItem().toString(), _yearBox.getSelectedItem().toString());
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel subPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanel4.add(submitButton);
        panel.add(subPanel4, BorderLayout.SOUTH);

        _frame.add(panel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(800, 600);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    public void CheckLoanRegistry(int userIndex, int bookIndex, String date, String month, String year) throws IOException {
        User user = null;
        Book book = null;
        LocalDate borrowDate = null;
        
        _userBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        _bookBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        if (_userCollection.get(userIndex)._hasLoan) {
            _userBox.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            JOptionPane.showMessageDialog(new JFrame(), "O Usuário escolhido já fez empréstimo!");
            return;
        }else {
            _userCollection.get(userIndex)._hasLoan = true;
            user = _userCollection.get(userIndex);
        }
        if (_bookCollection.get(bookIndex)._copiesRemaining == 0) {
            _bookBox.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            JOptionPane.showMessageDialog(new JFrame(), "O Livro escolhido ja não tem cópias restantes!");
            _userCollection.get(userIndex)._hasLoan = false;
            return;
        }else {
            _bookCollection.get(bookIndex)._copiesRemaining -= 1;
            book = _bookCollection.get(bookIndex);
        }
        
        String _date = date + "-" + month + "-" + year;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        borrowDate = LocalDate.parse(_date, formatter);
        
        int i = 0;
        if(!_loanCollection.isEmpty()){
            Loan tempLoan = _loanCollection.get(_loanCollection.size() - 1);
            i = tempLoan._loanNumber + 1;
        }
        
        if(borrowDate.isAfter(_validations.GetCurrentDate())){
            JOptionPane.showMessageDialog(new JFrame(), "A data de Empréstimo não pode ser maior que a data actual!");
            return;
        }
        
        Loan loan = new Loan(i, user, book, borrowDate);
        _loanCollection.add(loan);
        SaveFiles();

        JOptionPane.showMessageDialog(new JFrame(), "Empréstimo feito com Sucesso!");

        LoanGUI();
    }

    private void ReturnBook() throws IOException {
        int loanNumber = _validations.ValidateInt(JOptionPane.showInputDialog(null, "Insira o Número do Empréstimo!", "Devolução de um Livro", JOptionPane.INFORMATION_MESSAGE), 0, 999999999, "Número de Empréstimo");
        if (loanNumber == -1) {
            return;
        }

        for (Loan loan : _loanCollection) {
            if (loan._loanNumber == loanNumber) {
                _loanCollection.remove(loan);
                for (User user : _userCollection) {
                    if (loan._user._id.equals(user._id)) {
                        user._hasLoan = false;
                        SaveFiles();
                    }
                }
                for (Book book : _bookCollection) {
                    if (loan._book._isbn.equals(book._isbn)) {
                        book._copiesRemaining += 1;
                        SaveFiles();
                    }
                }
                SaveFiles();
                if(loan._debt > 0){
                    JOptionPane.showMessageDialog(new JFrame(), "O Usuário deve pagar uma multa de " + loan._debt + " meticais.");
                }
                JOptionPane.showMessageDialog(new JFrame(), "O Empréstimo do " + loan._user._name + " foi Removido com Sucesso!");
                LoanGUI();
                return;
            }
        }
    }
    
    public void UserGUI() throws IOException {
        _frame.dispose();
        _frame = new JFrame();
        _frame.setTitle("User Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        _table = new JTable(new UserJTable());

        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _frame.dispose();
                CreateMainMenu();
            }
        });
        subPanel1.add(backButton);
        panel.add(subPanel1, BorderLayout.NORTH);

        JPanel subPanel2 = new JPanel();
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    RegisterUser();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JButton removeUserButton = new JButton("Remove User");
        removeUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RemoveUser();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        subPanel2.add(addUserButton);
        subPanel2.add(removeUserButton);
        panel.add(subPanel2, BorderLayout.SOUTH);

        _scrollPane = new JScrollPane(_table);
        panel.add(_scrollPane, BorderLayout.CENTER);

        _frame.add(panel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(800, 600);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    class UserJTable extends AbstractTableModel {

        private String[] columnNames = {"Número de BI", "Nome", "Residência", "Número de Telefone", "Tem Empréstimo?"};
        Object[][] rows = new Object[_userCollection.size()][columnNames.length];

        public UserJTable() {
            int count = 0;
            Iterator<User> iter = _userCollection.iterator();
            while (iter.hasNext()) {
                User user = (User) iter.next();
                int i = 0;
                rows[count][i++] = user._id.toString();
                rows[count][i++] = user._name.toString();
                rows[count][i++] = user._residence.toString();
                rows[count][i++] = user._phoneNumber.toString();
                if (user._hasLoan) {
                    rows[count][i++] = "Sim";
                } else {
                    rows[count][i++] = "Não";
                }
                count++;
            }
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public int getRowCount() {
            return rows.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int column) {
            return rows[row][column];
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
    }

    private void RegisterUser() throws IOException {
        _frame.dispose();
        _frame = new JFrame();
        _frame.setTitle("User Register Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    UserGUI();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel1.add(backButton);
        panel.add(subPanel1, BorderLayout.NORTH);

        JLabel idLabel = new JLabel("Número de BI: ");
        _idField = new JTextField();
        JLabel nameLabel = new JLabel("Nome Completo: ");
        _nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Número de Telefone: ");
        _phoneField = new JTextField();
        JLabel residenceLabel = new JLabel("Residencia: ");
        _residenceField = new JTextField();
        JPanel subPanel2 = new JPanel(new GridLayout(4, 2));
        subPanel2.add(idLabel);
        subPanel2.add(_idField);
        subPanel2.add(nameLabel);
        subPanel2.add(_nameField);
        subPanel2.add(phoneLabel);
        subPanel2.add(_phoneField);
        subPanel2.add(residenceLabel);
        subPanel2.add(_residenceField);
        panel.add(subPanel2, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    CheckUserRegistry(_idField.getText(), _nameField.getText(), _residenceField.getText(), _phoneField.getText());
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel subPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanel3.add(submitButton);
        panel.add(subPanel3, BorderLayout.SOUTH);

        _frame.add(panel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(800, 600);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    public void CheckUserRegistry(String id, String name, String residence, String phoneNumber) throws IOException {
        String _id, _name, _residence;
        Long _phoneNumber;
        Boolean userExists;

        do {
            _idField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            _nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            _phoneField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            _residenceField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            userExists = false;
            _id = _validations.ValidateID(id, (byte) 13);
            if (_id == null) {
                _idField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                return;
            }

            for (User user : _userCollection) {
                if (_id.equals(user._id)) {
                    userExists = true;
                    JOptionPane.showMessageDialog(new JFrame(), "O Número BI inserido ja existe! Use outro Número BI.");
                    _idField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    return;
                }
            }
        } while (userExists == true);

        _name = _validations.ValidateName(name, 3, 800, "Nome Completo");
        if (_name == null) {
            _nameField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            return;
        }
        _phoneNumber = _validations.ValidatePhoneNumber(phoneNumber);
        if (_phoneNumber == null) {
            _phoneField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            return;
        }
        _residence = _validations.ValidateString(residence, 3, 800, "Residência");
        if (_residence == null) {
            _residenceField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            return;
        }

        User user = new User(id, name, residence, _phoneNumber);
        _userCollection.add(user);
        SaveFiles();

        JOptionPane.showMessageDialog(new JFrame(), "Usuário Registrado com Sucesso!");

        UserGUI();
    }

    private void RemoveUser() throws IOException {
        String id = _validations.ValidateID(JOptionPane.showInputDialog(null, "Insira o Número de BI do Usuário!", "Remoção de um Usuário", JOptionPane.INFORMATION_MESSAGE), (byte) 13);
        if (id.isEmpty()) {
            return;
        }

        for (User user : _userCollection) {
            if (id.equals(user._id)) {
                if(user._hasLoan){
                    JOptionPane.showMessageDialog(new JFrame(), "O Usuário escolhido tem um empréstimo, logo, não pode ser removido!");
                    return;
                }
                _userCollection.remove(user);
                SaveFiles();
                JOptionPane.showMessageDialog(new JFrame(), "O Usuário " + user.GetName() + " foi Removido com Sucesso!");
                UserGUI();
                return;
            }
        }

        JOptionPane.showMessageDialog(new JFrame(), "O Usuário Introduzido não existe!");
    }

    public void BookGUI() throws IOException {
        _frame.dispose();
        _frame = new JFrame();
        _frame.setTitle("Book Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        _table = new JTable(new BookJTable());

        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _frame.dispose();
                CreateMainMenu();
            }
        });
        subPanel1.add(backButton);
        panel.add(subPanel1, BorderLayout.NORTH);

        JPanel subPanel2 = new JPanel();
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    AddBook();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JButton removeBookButton = new JButton("Remove Book");
        removeBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RemoveBook();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        subPanel2.add(addBookButton);
        subPanel2.add(removeBookButton);
        panel.add(subPanel2, BorderLayout.SOUTH);

        _scrollPane = new JScrollPane(_table);
        panel.add(_scrollPane, BorderLayout.CENTER);

        _frame.add(panel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(800, 600);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    class BookJTable extends AbstractTableModel {

        private String[] columnNames = {"ISBN", "Título", "Autor", "Número de cópias restantes"};
        Object[][] rows = new Object[_bookCollection.size()][columnNames.length];

        public BookJTable() {
            int count = 0;
            Iterator<Book> iter = _bookCollection.iterator();
            while (iter.hasNext()) {
                Book book = (Book) iter.next();
                int i = 0;
                rows[count][i++] = book._isbn.toString();
                rows[count][i++] = book._title.toString();
                rows[count][i++] = book._author.toString();
                rows[count][i++] = book._copiesRemaining;
                count++;
            }
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public int getRowCount() {
            return rows.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int row, int column) {
            return rows[row][column];
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
    }

    private void AddBook() throws IOException {
        _frame.dispose();
        _frame = new JFrame();
        _frame.setTitle("Book Register Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    _frame.dispose();
                    BookGUI();
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subPanel1.add(backButton);
        panel.add(subPanel1, BorderLayout.NORTH);

        JLabel isbnLabel = new JLabel("ISBN: ");
        _isbnField = new JTextField();
        JLabel titleLabel = new JLabel("Título: ");
        _titleField = new JTextField();
        JLabel authorLabel = new JLabel("Autor: ");
        _authorField = new JTextField();
        JLabel nrCopiesLabel = new JLabel("Número de Cópias: ");
        _copiesRemainingField = new JTextField();
        JPanel subPanel2 = new JPanel(new GridLayout(4, 2));
        subPanel2.add(isbnLabel);
        subPanel2.add(_isbnField);
        subPanel2.add(titleLabel);
        subPanel2.add(_titleField);
        subPanel2.add(authorLabel);
        subPanel2.add(_authorField);
        subPanel2.add(nrCopiesLabel);
        subPanel2.add(_copiesRemainingField);
        panel.add(subPanel2, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    CheckBookRegistry(_isbnField.getText(), _titleField.getText(), _authorField.getText(), _copiesRemainingField.getText());
                } catch (IOException ex) {
                    Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JPanel subPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanel3.add(submitButton);
        panel.add(subPanel3, BorderLayout.SOUTH);

        _frame.add(panel);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setSize(800, 600);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    public void CheckBookRegistry(String isbn, String title, String author, String copiesRemaining) throws IOException {
        Long _isbn;
        String _title, _author;
        int _copiesRemaining;
        Boolean bookExists;

        do {
            _isbnField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            _titleField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            _authorField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            _copiesRemainingField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            bookExists = false;
            _isbn = _validations.ValidateLong(isbn, (byte) 13, "ISBN");
            if (_isbn == null) {
                _isbnField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                return;
            }

            for (Book book : _bookCollection) {
                if (_isbn.equals(book._isbn)) {
                    bookExists = true;
                    JOptionPane.showMessageDialog(new JFrame(), "O ISBN Introduzido ja existe! Use outro ISBN.");
                    _isbnField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    return;
                }
            }
        } while (bookExists == true);

        _title = _validations.ValidateString(title, 3, 800, "Título");
        if (_title == null) {
            _titleField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            return;
        }
        _author = _validations.ValidateName(author, 3, 800, "nome do Autor");
        if (_author == null) {
            _authorField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            return;
        }
        _copiesRemaining = _validations.ValidateInt(copiesRemaining, 0, 99999999, "Número de Cópias");
        if (_copiesRemaining == -1) {
            _copiesRemainingField.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            return;
        }

        Book book = new Book(_isbn, _title, _author, _copiesRemaining);
        _bookCollection.add(book);
        SaveFiles();

        JOptionPane.showMessageDialog(new JFrame(), "Livro Inserido com Sucesso!");

        BookGUI();
    }

    private void RemoveBook() throws IOException {
        Long isbn = _validations.ValidateLong(JOptionPane.showInputDialog(null, "Insira o ISBN do Livro!", "Remoção de um Livro", JOptionPane.INFORMATION_MESSAGE), (byte) 13, "ISBN");
        if (isbn == null) {
            return;
        }

        for (Book book : _bookCollection) {
            if (book._isbn.equals(isbn)) {
                _bookCollection.remove(book);
                SaveFiles();
                JOptionPane.showMessageDialog(new JFrame(), "O Livro " + book.GetTitle() + " foi Removido com Sucesso!");
                BookGUI();
                return;
            }
        }

        JOptionPane.showMessageDialog(new JFrame(), "O Livro Introduzido não existe!");
    }

    public static void LoadFiles() {
        try {
            _bookFile.createNewFile(); // if file already exists will do nothing
            _userFile.createNewFile(); // if file already exists will do nothing
            _loanFile.createNewFile(); // if file already exists will do nothing

            FileInputStream fis = null;
            ObjectInputStream oit = null;

            fis = new FileInputStream(_bookFile);
            oit = new ObjectInputStream(fis);
            _bookCollection = (ArrayList<Book>) oit.readObject();
            oit.close();

            fis = new FileInputStream(_userFile);
            oit = new ObjectInputStream(fis);
            _userCollection = (ArrayList<User>) oit.readObject();
            oit.close();

            fis = new FileInputStream(_loanFile);
            oit = new ObjectInputStream(fis);
            _loanCollection = (ArrayList<Loan>) oit.readObject();
            oit.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            // ... this is fine
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void SaveFiles() {
        try {
            _bookFile.createNewFile(); // if file already exists will do nothing
            _userFile.createNewFile(); // if file already exists will do nothing
            _loanFile.createNewFile(); // if file already exists will do nothing

            FileOutputStream fos = null;
            ObjectOutputStream out = null;

            fos = new FileOutputStream(_bookFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(_bookCollection);
            out.flush();
            out.close();
            fos.close();

            fos = new FileOutputStream(_userFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(_userCollection);
            out.flush();
            out.close();
            fos.close();

            fos = new FileOutputStream(_loanFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(_loanCollection);
            out.flush();
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Exit() {
        System.exit(0);
    }
}

//List Loan by _loanNumber
//List User by _name
//List Book by _title
//List Loans past _returnDate
//Remove a book from library (if a book that was loaned gets removed from the library, i will wait for the user to return it.)
//Comprovante de devolução/empréstimo/pagamento
//While has book, no new books
//50Mtn for each extra day
//Edit Data on User and Book
//List all loans made

//String to LocalDate
//Don't allow dates after current date

//cant remove user/book while has loan
//Translate everything
//make windows not resizeable
//JOptionPane title
//limites de caracteres
//Check erro inesperado message (null or invalid input)