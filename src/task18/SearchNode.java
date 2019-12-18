package task18;

import java.util.ArrayList;
import java.util.List;

public class SearchNode implements Comparable<SearchNode> {
	public final List<Character>	keyOrder;
	public final int				steps;
	private final SearchHash		searchHash;

	public SearchNode(List<Character> list, int steps) {
		this.keyOrder = list;
		this.steps = steps;
		this.searchHash = new SearchHash(keyOrder);
	}

	public Character getLastKey() {
		return searchHash.pos;
	}

	public boolean contains(Character c) {
		return searchHash.collected.contains(c);
	}

	public SearchHash asSearchHash() {
		return searchHash;
	}

	public SearchNode extend(Character key, int steps) {
		List<Character> extendedKeyOrder = new ArrayList<>(this.keyOrder);
		extendedKeyOrder.add(key);
		return new SearchNode(extendedKeyOrder, this.steps + steps);
	}

	@Override
	public String toString() {
		return this.steps + ":" + this.keyOrder + "\t" + this.asSearchHash().collected;
	}

	@Override
	public int compareTo(SearchNode o) {
		return Integer.compare(steps, o.steps);
	}

}
