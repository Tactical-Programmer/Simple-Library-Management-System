import java.io.Serializable;

public class User implements Serializable{
    
    public String _id;
    public String _name;
    public String _residence;
    public Long _phoneNumber;
    public boolean _hasLoan;
    
    public User(){
        _id = null;
        _name = null;
        _residence = null;
        _phoneNumber = null;
        _hasLoan = false;
    }
    
    public User(String id, String name, String residence, Long phoneNumber){
        this._id = id;
        this._name = name;
        this._residence = residence;
        this._phoneNumber = phoneNumber;
        this._hasLoan = false;
    }
    
    public String GetName(){
        return _name;
    }
    
    @Override
    public String toString(){
        return "\nBI: " + _id + "\nNome do Usuário: " + _name + "\nResidencia do Usuário: " + _residence + "\nNúmero de Telefone: " + _phoneNumber + "\nFez Empréstimo? " + _hasLoan;
    }
}