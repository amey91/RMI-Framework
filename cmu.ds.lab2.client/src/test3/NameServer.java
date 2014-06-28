package test3;
import core.Remote440;
import core.Remote440Exception;
import core.RemoteObjectReference;

public interface NameServer extends Remote440 {
    public RemoteObjectReference match(String name) throws Remote440Exception;
    public NameServer add(String s, RemoteObjectReference r, NameServer n) throws Remote440Exception;
    public NameServer next() throws Remote440Exception;   
}

