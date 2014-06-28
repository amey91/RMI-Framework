package test3;

import core.RemoteObjectReference;
import test1.ZipCodeList;

public class NameServerList {
	
	public String city;
    public RemoteObjectReference ror;
    public NameServerList next;

    public NameServerList(String c, RemoteObjectReference r, NameServerList n){
	city=c;
	ror=r;
	next=n;
    }
}
