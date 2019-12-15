package task14;

public class Quantity {
	public long		N;
	public String	ing;

	public Quantity(long n, String ing) {
		this.N = n;
		this.ing = ing;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + this.N);
		result = prime * result + ((this.ing == null) ? 0 : this.ing.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quantity other = (Quantity) obj;
		if (this.N != other.N)
			return false;
		if (this.ing == null) {
			if (other.ing != null)
				return false;
		} else if (!this.ing.equals(other.ing))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.N + " " + this.ing;
	}
}
