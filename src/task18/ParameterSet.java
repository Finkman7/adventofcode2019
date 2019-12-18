package task18;

import java.util.Set;

public class ParameterSet {
	Coordinate		c, c_Target;
	Set<Character>	collectedKeys;

	public ParameterSet(Coordinate c, Coordinate c_Target, Set<Character> collectedKeys) {
		this.c = c;
		this.c_Target = c_Target;
		this.collectedKeys = collectedKeys;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.c == null) ? 0 : this.c.hashCode());
		result = prime * result + ((this.c_Target == null) ? 0 : this.c_Target.hashCode());
		result = prime * result + ((this.collectedKeys == null) ? 0 : this.collectedKeys.hashCode());
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
		ParameterSet other = (ParameterSet) obj;
		if (this.c == null) {
			if (other.c != null)
				return false;
		} else if (!this.c.equals(other.c))
			return false;
		if (this.c_Target == null) {
			if (other.c_Target != null)
				return false;
		} else if (!this.c_Target.equals(other.c_Target))
			return false;
		if (this.collectedKeys == null) {
			if (other.collectedKeys != null)
				return false;
		} else if (!this.collectedKeys.equals(other.collectedKeys))
			return false;
		return true;
	}

}
