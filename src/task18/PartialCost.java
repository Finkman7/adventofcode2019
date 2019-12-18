package task18;

import java.util.Set;

public class PartialCost {
	public final Coordinate		pos;
	public final Set<Character>	toCollect;
	public final int			steps;

	public PartialCost(Coordinate pos, Set<Character> toCollect, int steps) {
		this.pos = pos;
		this.toCollect = toCollect;
		this.steps = steps;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.pos == null) ? 0 : this.pos.hashCode());
		result = prime * result + this.steps;
		result = prime * result + ((this.toCollect == null) ? 0 : this.toCollect.hashCode());
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
		PartialCost other = (PartialCost) obj;
		if (this.pos == null) {
			if (other.pos != null)
				return false;
		} else if (!this.pos.equals(other.pos))
			return false;
		if (this.steps != other.steps)
			return false;
		if (this.toCollect == null) {
			if (other.toCollect != null)
				return false;
		} else if (!this.toCollect.equals(other.toCollect))
			return false;
		return true;
	}

}
