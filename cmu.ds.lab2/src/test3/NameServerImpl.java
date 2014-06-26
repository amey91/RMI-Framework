package test3;

import core.RemoteObjectReference;

public class NameServerImpl 
    implements NameServer
{
    String serviceName;
    RemoteObjectReference ro;
    NameServer next;

    public NameServerImpl()
    {
	serviceName="";
	ro=null;
	next=null;
    }

    public NameServerImpl(String s, RemoteObjectReference r, NameServer n)
    {
	serviceName=s;
	ro=r;
	next=n;
    }

    public NameServer add(String s, RemoteObjectReference r, NameServer n)
    {
	return new NameServerImpl(s, r, this);
    }

    public RemoteObjectReference match(String name)
    {
	if (name.equals(serviceName))
	    return ro;
	else
	    return null;
    }
    
    public  NameServer next()
    {
	return next;
    }
}

