package example1;

public class Calci implements CalciInterface {
	private int memoryVar = 0;
	
	@Override
	public int add(int a, int b) {
		
		return a+b;
	}

	@Override
	public void setMemory(int b) {
		// TODO Auto-generated method stub
		memoryVar = b;
	}

	@Override
	public int addMemory(int b) {
		// TODO Auto-generated method stub
		return this.memoryVar + b;
		
	}

}
