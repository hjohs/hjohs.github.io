package com.fmt;

import java.util.Iterator;

public class InvoiceList<T> implements Iterable<Invoice> {
    private Invoice[] elements;
    private int size;
    private static String sortBy = "";

    public InvoiceList() {
        this.elements = (Invoice[]) new Invoice[10];
        this.size = 0;
    }
    
    public String getSortBy() {
    	return this.sortBy;
    }
    
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
		sort();
	}
	
    public void add(Invoice element) {
        if (size == elements.length) {
            resize();
        }
        elements[size++] = element;
        sort();
    }

    public Invoice get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return elements[index];
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Invoice> iterator() {
        return new Iterator<Invoice>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Invoice next() {
                return elements[index++];
            }
        };
    }

    //Implementation of insertion sort
    private void sort() {
        for (int i = 1; i < size; i++) {
            Invoice current = elements[i];
            int j = i - 1;
            while (j >= 0 && compare(elements[j], current) > 0) {
                elements[j + 1] = elements[j];
                j--;
            }
            elements[j + 1] = current;
        }
    }

    // Creates bigger copy of array when list size is exceeded
    private void resize() {
        Invoice[] newElements = (Invoice[]) new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }
    
	public static int compare(Invoice i1, Invoice i2) {
		
		if (sortBy.equals("Total")) {
	    	if (i1.getTotal() < i2.getTotal()) {
	    		return 1;
	    	}
	    	else if (i1.getTotal() == i2.getTotal()) {
	    		return 0;
	    	}
	    	else {
	    		return -1;
	    	}
		} else if (sortBy.equals("Customer")) {
			return i1.getCustomerName(i1.getCustomerCode()).compareTo(i2.getCustomerName(i2.getCustomerCode()));
		} else {
			if (i1.getStoreCode().compareTo(i2.getStoreCode()) < 0) {
				return -1;
			} 
			else if (i1.getStoreCode().compareTo(i2.getStoreCode()) == 0) {
				return i1.getManagerName(i1.getSalesPersonCode()).compareTo(i2.getManagerName(i2.getSalesPersonCode()));
			}
			else {
				return 1;
			}
		}
	}	
}
