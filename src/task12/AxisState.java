package task12;

import java.util.Arrays;

public class AxisState {
	private Long[] values;

	public AxisState(Long[] values) {
		this.values = values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.values);
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
		AxisState other = (AxisState) obj;
		if (!Arrays.equals(this.values, other.values))
			return false;
		return true;
	}

}
