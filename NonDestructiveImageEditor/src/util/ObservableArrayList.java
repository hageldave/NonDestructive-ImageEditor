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
		if(success){
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
		}
		return success;
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
		if(success){
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
		}
		return success;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean success = delegateList.addAll(index, c);
		if(success){
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_ADDED);
		}
		return success;
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
		if(success){
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
		}
		return success;
	}

	@Override
	public T remove(int index) {
		T element = delegateList.remove(index);
		this.setChanged();
		this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
		return element;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean success = delegateList.removeAll(c);
		if(success){
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
		}
		return success;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean success = delegateList.retainAll(c);
		if(success){
			this.setChanged();
			this.notifyObservers(ListDataEvent.INTERVAL_REMOVED);
		}
		return success;
	}

	@Override
	public T set(int index, T element) {
		T replaced = delegateList.set(index, element);
		this.setChanged();
		this.notifyObservers(ListDataEvent.CONTENTS_CHANGED);
		return replaced;
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
