package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import javax.swing.event.ListDataEvent;

public class ObservableArrayList<T> extends Observable implements List<T> {
	
	private ArrayList<T> delegateList = new ArrayList<T>();

	@Override
	public boolean add(T e) {
		boolean success = delegateList.add(e);
		try {
			return success;
		} finally {
			if(success){
				this.setChanged();
				this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
			}
		}
	}

	@Override
	public void add(int index, T element) {
		delegateList.add(index, element);
		this.setChanged();
		this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean success = delegateList.addAll(c);
		try {
			return success;
		} finally {
			if(success){
				this.setChanged();
				this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
			}
		}
	}
	
	
	public boolean setElementToIndex(T element, int index){
		int sourceindex = delegateList.indexOf(element);
		if(sourceindex > -1){
			return setElementToIndex(sourceindex, index);
		} else {
			return false;
		}
	}
	
	public boolean setElementToIndex(int sourceIndex, int targetIndex){
		try {
			if(		sourceIndex > -1 && 
					targetIndex > -1 && 
					sourceIndex != targetIndex && 
					sourceIndex < size() &&
					targetIndex < size()){
				T element = delegateList.remove(sourceIndex);
				delegateList.add(targetIndex, element);
				return true;
			} else {
				return false;
			}
		} finally {
			this.setChanged();
			this.notifyObservers(ListDataEvent.CONTENTS_CHANGED);
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean success = delegateList.addAll(index, c);
		try {
			return success;
		} finally {
			if(success){
				this.setChanged();
				this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
			}
		}
	}

	@Override
	public void clear() {
		delegateList.clear();
		this.setChanged();
		this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
	}

	@Override
	public boolean contains(Object o) {
		return delegateList.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return delegateList.containsAll(c);
	}

	@Override
	public T get(int index) {
		return delegateList.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return delegateList.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return delegateList.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return delegateList.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return delegateList.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return delegateList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return delegateList.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		boolean success = delegateList.remove(o);
		try {
			return success;
		} finally {
			if(success){
				this.setChanged();
				this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
			}
		}
	}

	/** return element if index in range, else null */
	@Override
	public T remove(int index) {
		try {
			if (index > -1 && index < size()) {
				T element = delegateList.remove(index);
				return element;
			} else {
				return null;
			}
		} finally {
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean success = delegateList.removeAll(c);
		try {
			return success;
		} finally {
			if(success){
				this.setChanged();
				this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
			}
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean success = delegateList.retainAll(c);
		try {
			return success;
		} finally {
			if(success){
				this.setChanged();
				this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
			}
		}
	}

	/** return replaced element or null if not possible */
	@Override
	public T set(int index, T element) {
		T replaced = null; 
		if(index > -1 && index < size()){
			replaced = delegateList.set(index, element);
		}
		try {
			return replaced;
		} finally {
			this.setChanged();
			this.notifyObservers(ListDataEvent.CONTENTS_CHANGED);
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return delegateList.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		ObservableArrayList<T> sublist = new ObservableArrayList<>();
		sublist.delegateList.addAll(delegateList.subList(fromIndex, toIndex));
		return sublist;
	}

	@Override
	public Object[] toArray() {
		return delegateList.toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		// TODO Auto-generated method stub
		return delegateList.toArray(a);
	}

}
