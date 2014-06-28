package test3;
// a client for ZipCodeRList.
// it uses ZipCodeRList as an interface, and test
// all methods by printing all data.

// It reads data from a file containing the service name and city-zip 
// pairs in the following way:
//   city1
//   zip1
//   ...
//   ...
//   end.

import java.io.*;

import core.Naming;
import core.Remote440Exception;
import core.RemoteObjectReference;
import test1.ZipCodeList;

public class NameServerClient { 

	// the main takes three arguments:
	// (0) a host. 
	// (1) a port.
	// (2) a service name.
	// (3) a file name as above. 
	public static void main(String[] args) {
		if(args.length<4){
			System.out.println("Usage: java test3/ZipCodeRListClient <registry_IP> <registry_port> <object_name> <outputfile>");
			System.exit(0);
		}
		
		try{
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String serviceName = args[2];
			BufferedReader in = new BufferedReader(new FileReader(args[3]));


			serviceName = host.toString() + ":" + Integer.toString(port) + "/" + serviceName;

			// get (create) the stub out 
			NameServer rl = (NameServer) Naming.lookup(serviceName);

			// reads the data and make a "local" nameserver list.
			// later this is sent to the server.
			// again no error check!
			NameServerList l = null;
			boolean flag = true;

			int counter = 0;
			while (flag)
			{
				counter++;
				String city = in.readLine();
				String code = in.readLine();
				if (city == null)
					flag = false;
				else{
					RemoteObjectReference ror = new RemoteObjectReference("ip"+counter, counter, code, "interface"+counter);
					l = new NameServerList(city.trim(), ror, l);
				}
			}
			// the final value of l should be the initial head of 
			// the list.

			// we print out the local Nameserverlist.
			System.out.println("This is the original list.");
			NameServerList temp = l;
			int listSize = 0;
			while (temp !=null){
				System.out.println
				("city: "+temp.city+", "+
						"code: "+temp.ror.toString());       
				temp = temp.next;
				listSize++; 
			}

			// test "add".
			System.out.println("testing add.");
			temp = l;
			NameServer rtemp = rl;

			while (temp !=null){
				rl=rl.add(temp.city, temp.ror, rtemp);
				temp = temp.next;
			}
			
			System.out.println("add tested.");
			// rl should contain the initial head of the list.

			// test "find" and "next" by printing all. 
			// This is also the test that "add" performed all right.
			System.out.println("\n This is the remote list, printed using find/next.");
			temp = l;


			while(temp!=null){
				rtemp = rl;
				while (rtemp !=null){
					// here is a test.
					RemoteObjectReference ror = rtemp.match(temp.city);
					if(ror!=null)
						System.out.println("city: "+temp.city+", "+
								"ror: "+ ror.toString());

					rtemp = rtemp.next();
				}
				temp = temp.next;
			}
			in.close();

		}
		catch(IOException | Remote440Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}