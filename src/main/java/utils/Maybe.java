package utils;

public class Maybe<T> {
	T value;
	
	public Maybe(T val) {
		this.value = val;
	}

	public T unbox() {
		return value;
	}

}
