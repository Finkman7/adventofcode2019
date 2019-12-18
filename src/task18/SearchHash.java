package task18;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchHash {
	public Character		pos;
	public Set<Character>	collected;

	public SearchHash(List<Character> keyOrder) {
		if (!keyOrder.isEmpty()) {
			this.pos = keyOrder.get(keyOrder.size() - 1);
		}
		this.collected = new HashSet<>(keyOrder);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.collected == null) ? 0 : this.collected.hashCode());
		result = prime * result + ((this.pos == null) ? 0 : this.pos.hashCode());
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
		SearchHash other = (SearchHash) obj;
		if (this.collected == null) {
			if (other.collected != null)
				return false;
		} else if (!this.collected.equals(other.collected))
			return false;
		if (this.pos == null) {
			if (other.pos != null)
				return false;
		} else if (!this.pos.equals(other.pos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SearchHash [pos=" + this.pos + ", collected=" + this.collected + "]";
	}

}
