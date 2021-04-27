import java.io.Serializable;

public class Book implements Serializable{
    
    public Long _isbn;
    public String _title, _author;
    public int _copiesRemaining;

    public Book(){
        _isbn = null;
        _title = null;
        _author = null;
        _copiesRemaining = 0;
    }

    public Book(Long isbn, String title, String author, int copiesRemaining){
        this._isbn = isbn;
        this._title = title;
        this._author = author;
        this._copiesRemaining = copiesRemaining;
    }
    
    public String GetTitle(){
        return _title;
    }
    
    @Override
    public String toString(){
        return "\nISBN: " + _isbn + "\nTitle: " + _title + "\nAuthor: " + _author + "\nExemplares Restantes: " + _copiesRemaining;
    }
}