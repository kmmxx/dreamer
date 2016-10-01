package com.dreamer.pattern;

import java.util.Iterator;
import java.util.Vector;

public class Observerton {

	public Vector<IObserver> vector;

	public Observerton() {
		vector = new Vector<Observerton.IObserver>();
	}

	public synchronized void addObserver(IObserver i) {
		if (i != null) {
			vector.add(i);
		}
	}

	public synchronized void removeObserver(IObserver i) {
		if (i != null) {
			vector.remove(i);
		}
	}

	public synchronized void notifyObserver() {
		Iterator<IObserver> itor = vector.iterator();
		while (itor.hasNext()) {
			IObserver obser = ((IObserver) itor.next());
			if (obser != null)
				obser.update();
		}
	}

	public interface IObserver {
		public void update();
	}
}
