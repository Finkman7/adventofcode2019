package intCodeComputer;

public class IntCodeComputerEvent {
	public final IntCodeComputerEventType	type;
	public final IntCodeComputer			source;

	public IntCodeComputerEvent(IntCodeComputerEventType type, IntCodeComputer source) {
		this.type = type;
		this.source = source;
	}

	@Override
	public String toString() {
		return this.type + " [" + this.source + "]";
	}
}
