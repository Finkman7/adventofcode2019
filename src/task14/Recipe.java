package task14;

import java.util.List;
import java.util.stream.Collectors;

public class Recipe {
	public final Quantity		head;
	public final List<Quantity>	reqs;

	public Recipe(Quantity head, List<Quantity> req) {
		this.head = head;
		this.reqs = req;
	}

	public List<String> getIngredients() {
		return this.reqs.stream().map(q -> q.ing).collect(Collectors.toList());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.head == null) ? 0 : this.head.hashCode());
		result = prime * result + ((this.reqs == null) ? 0 : this.reqs.hashCode());
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
		Recipe other = (Recipe) obj;
		if (this.head == null) {
			if (other.head != null)
				return false;
		} else if (!this.head.equals(other.head))
			return false;
		if (this.reqs == null) {
			if (other.reqs != null)
				return false;
		} else if (!this.reqs.equals(other.reqs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return reqs.stream().map(q -> q.toString()).collect(Collectors.joining(", ")) + " => " + head;
	}
}
