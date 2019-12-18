package task18;

import java.util.Set;

public class ConditionalPath {
	public final Path			shortestPath;
	public final Set<Character>	prereqs;

	public ConditionalPath(Path shortestPath, Set<Character> prereqs) {
		this.shortestPath = shortestPath;
		this.prereqs = prereqs;
	}

	@Override
	public String toString() {
		return this.shortestPath + (this.prereqs.isEmpty() ? "" : " if " + this.prereqs);
	}

}
