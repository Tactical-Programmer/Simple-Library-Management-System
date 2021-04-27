import java.io.Serializable;
import java.time.LocalDate;

public class Loan implements Serializable{
    
    public int _loanNumber;
    public User _user;
    public Book _book;
    public int _loanDuration;
    public LocalDate _borrowDate, _expectedReturnDate;
    public double _debt;
    
    public Loan(){
        _loanNumber = 0;
        _user = null;
        _book = null;
        _borrowDate = null;
        _expectedReturnDate = null;
        _loanDuration = 14;
        _debt = 0;
    }
    
    public Loan(int loanNumber, User user, Book book, LocalDate borrowDate){
        this._loanNumber = loanNumber;
        this._user = user;
        this._book = book;
        this._loanDuration = 14;
        this._borrowDate = borrowDate;
        this._expectedReturnDate = borrowDate.plusDays(_loanDuration);
        this._debt = 0;
    }
    
    @Override
    public String toString(){
        return "\nNúmero de Empréstimo: " + _loanNumber + "\nNome do Usuário: " + _user.GetName() + "\nNome do Livro: " + _book.GetTitle() + "\nData de Empréstimo: " + _borrowDate +
                "\nData Prevista da Devolução: " + _expectedReturnDate + "\nMulta: " + _debt;
    }
}
