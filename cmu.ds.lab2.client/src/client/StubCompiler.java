package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class StubCompiler {
	static File f;
	static FileOutputStream stream;
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		
		if(args.length!=1)
		{		
			log("Usage: java StubCompiler <interfaceName>");
			System.exit(0);
		}

		
 		Class<?> inter = Class.forName(args[0]);//.getClass();
 		
		f = new File(inter.getSimpleName() + "_Stub.java");
		stream = new FileOutputStream(f);
		
 		Method[] methods = inter.getMethods();
		String stubClassName = inter.getSimpleName() + "_Stub";
		
		//TODO compile time checks (should extend Remote440)
		//TODO serial id generation
		
		//imports required
		printToFile(inter.getPackage() + ";", 0);
		printToFile("import core.*;", 0);
		printToFile("import java.lang.reflect.InvocationTargetException;\n", 0);
 		
 		//class declaration
		printToFile("public class " + stubClassName + " extends core.RemoteStub implements " + inter.getName() + " {", 0);

 		//constructor
		printToFile("public " + stubClassName + " (RemoteObjectReference ror) {",1);
		printToFile("super(ror);",2);
		printToFile("}",1);
 		
 		//interface methods
 		for(Method m:methods)
 			printMethod(m, 1);

 		printToFile("}",0);
 		
 		stream.close();
 	}
 	
 	static void printCatchClauses(Class<?>[] exceptionTypes, int tabLevel){
 		//get the target exception from InvocationTargetException
 		printToFile("Throwable targetException = e.getTargetException();", tabLevel);
 		
 		//check if the exception is one of the exception types thrown by the interface
 		for(Class<?> c: exceptionTypes){
 			printToFile("if(targetException instanceof " + c.getName() + ")", tabLevel);
 			printToFile("throw (" + c.getName()+") targetException;", tabLevel+1);
 		}
 		
 		//otherwise throw Remote440Exception
 		printToFile("else", tabLevel);
 		printToFile("throw new Remote440Exception(\"Unknown Exception\");", tabLevel+1);
 	}
 	
 	static void printMethod(Method m, int tabLevel)
 	{
 		Class<?> exceptionTypes[] = m.getExceptionTypes();
		Class<?> argumentTypes[] = m.getParameterTypes();
		Class<?> returnType = m.getReturnType();
		String   methodName = m.getName();
		//TODO parameter annotations, generic
		//TODO compile time checks(parameters/ return value
		//	   should be Remote440Exception, params should be remote/primitive/serializable)

		//create parameter Names
		String[] paramNames = new String[argumentTypes.length];
		for(int i=0;i<argumentTypes.length; i++)
			paramNames[i] = "param" + (new Integer(i)).toString();
		
		// @Override annotation and declarator
		printToFile("",0);
		printToFile("@Override", tabLevel);
		String declarator = getDeclarator(argumentTypes, paramNames, returnType, methodName, exceptionTypes);
		printToFile(declarator, tabLevel);
		
		//calling invoke function in try catch clause
		printToFile("try{", tabLevel + 1);
		String invocationStmt = getInvocationStmt(argumentTypes, paramNames, returnType, methodName);
		printToFile(invocationStmt, tabLevel + 2);
		
		//return result if return type is not void
		if(!returnType.getName().equals("void"))
			printToFile("return result;", tabLevel + 2);
		
		//print appropriate catch clauses
		printToFile("} catch(InvocationTargetException e){", tabLevel + 1);
		printCatchClauses(exceptionTypes, tabLevel + 2);
		printToFile("}", tabLevel+1);
		printToFile("}", tabLevel);
 	}
 	
 	private static String getDeclarator(Class<?>[] argumentTypes,
		String[] paramNames, Class<?> returnType, String methodName,
		Class<?>[] exceptionTypes) {
	
 		//access modifier and method name
		String declarator = "public " + returnType.getName() + " " + methodName + "(";
		
		//method arguments
		for(int i=0;i<argumentTypes.length;i++)
		{
			declarator += argumentTypes[i].getName() + " ";
			declarator += paramNames[i];
			if(i!=argumentTypes.length - 1)
				declarator += ", ";;
		}
		
		//exceptions thrown by method
		declarator += ") throws " + exceptionTypes[0].getName();
		for(int i=1;i<exceptionTypes.length;i++)
			declarator += ", " + exceptionTypes[i].getName();
		
		declarator += "{";
		return declarator;
	}
 	public static String getNonPrimitiveClassName(Class<?> c)
 	{
 		switch(c.getName())
 		{
 		case "byte":
 			return "Byte";
 		case "short":
 			return "Short";
 		case "int":
 			return "Integer";
 		case "long":
 			return "Integer";
 		case "float":
 			return "Float";
 		case "double":
 			return "Double";
 		case "boolean":
 			return "Boolean";
 		case "char":
 			return "Character";
 		default:
 			return c.getName();
 		}
 	}
 	public static String getNonPrimitiveClassType(Class<?> c)
 	{
 		switch(c.getName())
 		{
 		case "byte":
 		case "short":
 		case "int":
 		case "long":
 		case "float":
 		case "double":
 		case "boolean":
 		case "char":
 			return getNonPrimitiveClassName(c) + ".TYPE";
 		default:
 			return c.getName() + ".class";
 		}
 	}
 	private static String getInvocationStmt(Class<?>[] argumentTypes,
 			String[] paramNames, Class<?> returnType, String methodName)
 	{
 		String invocationStmt = "";
 		
 		//store the return value if method returns
 		if(!returnType.getName().equals("void"))
 			invocationStmt += returnType.getName() + " result = (" + returnType.getName() +")";
 		
 		
 		invocationStmt += "invoke(\"" + methodName + "\", new Object[]{";
 		
 		//list of actual arguments
		for(int i=0;i<argumentTypes.length;i++)
		{
			if(!argumentTypes[i].isPrimitive())
				invocationStmt += paramNames[i];
			else
				invocationStmt += "new " + getNonPrimitiveClassName(argumentTypes[i]) + "(" + paramNames[i] + ")";
			if(i!=argumentTypes.length - 1)
				invocationStmt += ", ";
		}
		
		//list of argument types
		invocationStmt+="}, new Class[]{";
		for(int i=0;i<argumentTypes.length;i++)
		{
			invocationStmt += getNonPrimitiveClassType(argumentTypes[i]);
			if(i!=argumentTypes.length - 1)
				invocationStmt += ", ";
		}
		
		
		invocationStmt += "});";
		return invocationStmt;
	}

	static void log(String s)
	{
		System.out.println(s);
	}
 	static void printToFile(String s, int tabs)
 	{
 		String out = "";
 		for(int i=0;i<tabs;i++)
 			out += "\t";
 		out += s +'\n';
 		try {
			stream.write(out.getBytes());
		} catch (IOException e) {
			System.out.println("Remote exception.");
		}
 	}
}
